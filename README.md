# Assignment
---
## Env
`Database: Mongo 4.2`
`Java: 1.8`
`Spring Boot 2.2.1.RELEASE`

## Docker
`docker-compose up --build -d`

## App
`./gradlew build`
`./gradlew bootRun`

## Actuator
`http://localhost:8080/actuator`

---

## API
API that receives JSON base64 data.

- *POST* localhost:8080/api/v1/diff/{ID}/left
- *POST* localhost:8080/api/v1/diff/{ID}/right
- *GET* localhost:8080/api/v1/diff/{ID}

### Headers:
```
ContentType: application/json
Accepts: application/json
```

Examples:
```
POST localhost:8080/api/v1/diff/{ID}/left
{
    "data": "ewogICAgZGF0YTogInRoaXMgaXMgYSBqc29uIgp9"
}
```
```
POST localhost:8080/api/v1/diff/1/right
{
    "data": "ewogICAgZGF0YTogInRoaXMgaXMgYSBqc29uIgp9"
}
```

```
GET localhost:8080/api/v1/diff/1
{
    "status": "EQUAL_SIZE",
    "offset": 15,
    "length": 40
}
```
---
Notes:
- status: `EQUAL, NOT_EQUAL_SIZE, EQUAL_SIZE`
- offset: index where it starts differing
- length: string's length