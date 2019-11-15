# Assignment

#Env
`Database: Mongo 4.2`
`Java: 1.8`
`Spring Boot 2.2.1.RELEASE`

#Docker
`docker-compose up -d`

#Actuator
http://localhost:8080/actuator

#API
```
POST localhost:8080/api/v1/diff/1/left
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
"data": "ewogICAgZGF0YTogInRoaXMgaXMgYSBqc29uIgp9"
}
```