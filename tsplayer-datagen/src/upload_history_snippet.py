import datetime
import os
import random
import time

import requests
import tqdm

from src.utils import get_logger

logger = get_logger(__name__, 'DEBUG')

SERIES_NAME = os.environ.get('SERIES_NAME', 'mydata')
STEP_SECONDS = int(os.environ.get('STEP_SECONDS', '1'))
STEPS_COUNT = int(os.environ.get('STEPS_COUNT', '10000000'))
RANDOM_INC_RANGE = (
    int(os.environ.get('RANDOM_INC_RANGE_MIN', '-10')),
    int(os.environ.get('RANDOM_INC_RANGE_MAX', '10'))
)
SERVICE_HOST = os.environ.get('SERVICE_HOST', 'localhost:8080')
ENDPOINT_UPLOAD = f'http://{SERVICE_HOST}/series/{SERIES_NAME}/values'
ENDPOINT_INIT = f'http://{SERVICE_HOST}/series'
BATCH_SIZE = int(os.environ.get('BATCH_SIZE', '10000'))
SLEEP_AFTER_FAIL = int(os.environ.get('SLEEP_AFTER_FAIL', '1'))


def _chunks(lst, n):
    for i in range(0, len(lst), n):
        yield lst[i:i + n]


def _generate_data():
    t_begin = datetime.datetime.now()

    y = 0
    batch = []
    for i in tqdm.tqdm(range(STEPS_COUNT), total=STEPS_COUNT):
        t = (t_begin + datetime.timedelta(seconds=i * STEP_SECONDS)).timestamp()
        y += random.randint(*RANDOM_INC_RANGE)
        batch += [
            {
                'timestamp': int(t),
                'value': y
            }
        ]

        if len(batch) == BATCH_SIZE:
            yield batch
            batch = []

    if len(batch) > 0:
        yield batch


def _init():
    r = requests.post(
        ENDPOINT_INIT,
        json={
            'name': SERIES_NAME
        }
    )
    if not r.ok:
        raise ValueError(r)


def _request(chunk, *, http):
    while True:
        try:
            r = http.post(
                ENDPOINT_UPLOAD,
                json=chunk
            )
            if not r.ok:
                raise ValueError(r)
            return
        except Exception as e:
            logger.exception(e)
            time.sleep(SLEEP_AFTER_FAIL)


def _upload(dataset):
    http = requests.Session()
    for chunk in dataset:
        _request(chunk, http=http)


def main():
    _init()
    _upload(_generate_data())


if __name__ == '__main__':
    main()
