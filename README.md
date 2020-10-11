[![Maintainability](https://api.codeclimate.com/v1/badges/f3bb0d15d028021493df/maintainability)](https://codeclimate.com/github/mrk-andreev/demo-tsplayer/maintainability)

# demo-tsplayer

- **tsplayer-application** ui+backend (svg + ws + quarkus + graalvm + redistimeseries)
- **tsplayer-datagen** python script for uploading random data

Use `tsplayer-deploy/docker-compose/docker-compose.yaml` for quick start (port 8080). 

```
version: '3'

services:
  app:
    image: markandreev/demo-tsplayer:latest
    depends_on:
      - "redis"
    ports:
      - "8080:8080"
    environment:
      REDIS_HOST: redis
      REDIS_PORT: 6379
  redis:
    image: "redislabs/redistimeseries:1.4.3"
  datagen:
    image: "markandreev/demo-tsplayer-datagen:latest"
    depends_on:
      - "app"
    environment:
      SERVICE_HOST: app:8080
      STEPS_COUNT: 10000000
```

[![Video](tsplayer-assets/player-example.gif)](https://www.youtube.com/watch?v=cIc6v9Pygyg)


# Architecture
![](tsplayer-assets/preview.png)

# Data player
![](tsplayer-assets/preview-player.png)

# Protocol

![](tsplayer-assets/preview-protocol.gif)

Request (json)
```json
{
  "requestId": "<uuid>",
  "key": "<dataset name>",
  "from": "<min timestamp>",
  "to": "<max timestamp>",
  "aggregation": "<AVG, SUM, MIN, MAX, RANGE, COUNT, FIRST, LAST>"
}
```
Response (binary, avro)
```json
{
  "requestId": "<from request>",
  "isSuccess": "<no errors>",
  "errorMessage": "<if not isSuccess than display error message>",
  "values": [
    {
      "timestamp": "<timestamp>",
      "value": "<double>"
    } 
  ]
}
```
