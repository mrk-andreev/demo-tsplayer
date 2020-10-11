(() => {
    const {Type} = require('avsc');
    const {Observable} = require('rxjs');
    const {v4: uuidv4} = require('uuid');

    const FETCH_TIMING = {
        begin: 0,
        end: 0,
    };
    const REQUEST_ANCHOR = {
        requestId: '',
    };

    /**
     * Binary data deserializer
     */
    const AVRO_SCHEMA = {
        "type": "record",
        "name": "DataResponse",
        "namespace": "name.mrkandreev.avro",
        "fields": [{
            "name": "requestId",
            "type": "string"
        }, {
            "name": "values",
            "type": {
                "type": "array",
                "items": {
                    "type": "record",
                    "name": "DataResponseValue",
                    "fields": [{
                        "name": "timestamp",
                        "type": "long"
                    }, {
                        "name": "value",
                        "type": "double"
                    }]
                },
                "java-class": "java.util.List"
            }
        }]
    };
    const type = Type.forSchema(AVRO_SCHEMA);
    const avroDecode = (buf) => {
        return type.fromBuffer(new Buffer(buf));
    };

    /**
     * Data layer
     */

    const getProtocol = () => location.protocol === 'https:' ? 'wss:' : 'ws:';
    const WS_ENDPOINT = `${getProtocol()}//${location.host}/ws`;
    const META_DATA_ENDPOINT = (key) => `/series/${key}`;
    const WS_RECONNECT_TIMEOUT = 1000;
    const WS_WAIT_CONNECTION_TIMEOUT = 100;
    const META_RETRY_TIMEOUT = 500;
    const POINTS_LIMIT = 1000;

    let wsConnection = null;
    const connectWs = (subscriber) => {
        wsConnection = new WebSocket(WS_ENDPOINT);
        wsConnection.binaryType = 'arraybuffer';
        wsConnection.onmessage = (m) => {
            const data = avroDecode(m.data);

            if (data.requestId === REQUEST_ANCHOR.requestId) {
                FETCH_TIMING.end = new Date().getTime();
                console.log(`Fetched by ${FETCH_TIMING.end - FETCH_TIMING.begin}ms`);
                subscriber.next(data.values);
            }
        }
        wsConnection.onerror = (e) => {
            console.error(e);
        };
        wsConnection.onclose = () => {
            console.log(`Reconnect after ${WS_RECONNECT_TIMEOUT} ms`);
            setTimeout(() => {
                connectWs(subscriber);
            }, WS_RECONNECT_TIMEOUT);
        }
    };
    const wsResponseListener = new Observable(subscriber => {
        connectWs(subscriber);
    });
    wsResponseListener.subscribe();

    const requestDataSlice = (requestId, key, minX, maxX) => {
        if (wsConnection === null || wsConnection.readyState !== 1) {
            setTimeout(() => {
                requestDataSlice(requestId, key, minX, maxX);
            }, WS_WAIT_CONNECTION_TIMEOUT);
        } else {
            const timeBucket = Math.max(Math.round((maxX - minX) / POINTS_LIMIT), 1);

            FETCH_TIMING.begin = new Date().getTime();
            wsConnection.send(
                JSON.stringify({
                    requestId: requestId,
                    key: key,
                    from: minX,
                    to: maxX,
                    aggregation: 'AVG',
                    timeBucket: timeBucket
                })
            );
        }
    };

    const fetchMeta = (key) => {
        const fetchMetaRequest = (resolve) => {
            fetch(META_DATA_ENDPOINT(key))
                .then(res => res.json())
                .then(res => resolve(res))
                .catch((e) => {
                    console.error(e);
                    console.log(`Retry after ${META_RETRY_TIMEOUT} ms`);
                    setTimeout(() => {
                        fetchMetaRequest(resolve);
                    }, META_RETRY_TIMEOUT);
                });
        }

        return new Promise(resolve => {
            fetchMetaRequest(resolve);
        });
    };

    /**
     * Presentation layer
     */
    const KEY = 'mydata';
    const EPS = 0.0001;
    const X_NORMALIZATION = 400;
    const Y_NORMALIZATION = 100;
    const PLAYER_SCALES = [
        {
            label: '1m',
            value: 60
        },
        {
            label: '1h',
            value: 60 * 60
        },
        {
            label: '6h',
            value: 6 * 60 * 60
        },
        {
            label: '1d',
            value: 24 * 60 * 60
        },
        {
            label: '1w',
            value: 7 * 24 * 60 * 60
        },
        {
            label: '30d',
            value: 30 * 24 * 60 * 60
        }
    ];

    window.addEventListener('load', () => {
        const $ = {
            playerCurrentTime: document.getElementById('playerCurrentTime'),
            player: document.getElementById('playerSlider'),
            playerScale: document.getElementById('playerScale'),
            draw: document.getElementById('drawArea'),
            minXPlaceholder: document.getElementById('minXPlaceholder'),
            maxXPlaceholder: document.getElementById('maxXPlaceholder'),
            minYPlaceholder: document.getElementById('minYPlaceholder'),
            maxYPlaceholder: document.getElementById('maxYPlaceholder'),
        };

        PLAYER_SCALES.forEach(scale => {
            const $el = document.createElement('option');
            $el.value = scale.value;
            $el.label = scale.label;

            $.playerScale.append($el);
        });

        const chart = {
            globalMinX: 0,
            globalMaxX: 0,

            minX: 0,
            maxX: 0,

            minY: 0,
            maxY: 0,

            time: 0,
            scale: 60,
        };

        const requestData = () => {
            const requestId = uuidv4();
            REQUEST_ANCHOR.requestId = requestId;
            requestDataSlice(requestId, KEY, chart.time - chart.scale, chart.time);
        }

        const chartSetMinX = (x) => {
            chart.minX = x;
            $.minXPlaceholder.innerText = new Date(chart.minX * 1000).toISOString();
        }
        const chartSetMaxX = (x) => {
            chart.maxX = x;
            $.maxXPlaceholder.innerText = new Date(chart.maxX * 1000).toISOString();
        }
        const chartSetMinY = (y) => {
            chart.minY = Math.round(y);
            $.minYPlaceholder.innerText = chart.minY;
        }
        const chartSetMaxY = (y) => {
            chart.maxY = Math.round(y);
            $.maxYPlaceholder.innerText = chart.maxY;
        }
        const setChartScale = (scale) => {
            chart.scale = scale;
            requestData();
        }
        const setChartTime = (time) => {
            chart.time = time;
            requestData();
            $.player.setAttribute('value', time);
        }
        const setGlobalMinTime = (time) => {
            chart.globalMinX = time;
            $.player.setAttribute('min', chart.globalMinX);
        }
        const setGlobalMaxTime = (time) => {
            chart.globalMaxX = time;
            $.player.setAttribute('max', chart.globalMaxX);
        }

        $.player.addEventListener('change', () => {
            setChartTime($.player.value);
        });

        $.playerScale.addEventListener('change', () => {
            setChartScale($.playerScale.value);
        });

        const transformPointsToSvgFormat = (points) => {
            return points
                .map(point => [
                    (point.timestamp - chart.minX) / (chart.maxX - chart.minX + EPS) * X_NORMALIZATION,
                    (point.value - chart.minY) / (chart.maxY - chart.minY + EPS) * Y_NORMALIZATION
                ])
                .map(point => `${point[0]},${point[1]}`)
                .join(' ');
        };

        const drawFrame = (points) => {
            chartSetMinX(Math.min(...points.map(point => point.timestamp)));
            chartSetMaxX(Math.max(...points.map(point => point.timestamp)));
            chartSetMinY(Math.min(...points.map(point => point.value)));
            chartSetMaxY(Math.max(...points.map(point => point.value)));

            $.playerCurrentTime.innerText = new Date(chart.maxX * 1000).toISOString();

            $.draw.innerHTML = '';
            const $lineCurve = document
                .createElementNS('http://www.w3.org/2000/svg', 'polyline');
            $lineCurve.setAttribute('vector-effect', 'non-scaling-stroke');
            $lineCurve.setAttribute('points', transformPointsToSvgFormat(points));
            $lineCurve.setAttribute('stroke', 'black');
            $lineCurve.setAttribute('stroke-width', '1');
            $lineCurve.setAttribute('fill', 'none');

            $.draw.append($lineCurve);
        };

        /**
         * Main
         */
        wsResponseListener.subscribe(points => drawFrame(points));
        fetchMeta(KEY)
            .then(meta => {
                setGlobalMinTime(meta.minTimestamp);
                setGlobalMaxTime(meta.maxTimestamp);
                setChartTime(meta.maxTimestamp);
            });
    });
})();
