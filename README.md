# MatchMe

This repository contains the beginnings of the **MatchMe** co-founder matching application. The project will include a Spring Boot backend and a React frontend. The backend currently contains a minimal setup so development can start.

## Backend

To run the backend locally:

```bash
cd backend
mvn spring-boot:run
```

The backend exposes a simple health endpoint at `/`.
You can register and log in using JSON requests:

```bash
curl -X POST -H "Content-Type: application/json" -d '{"email":"test@example.com","password":"pass"}' http://localhost:8080/register
curl -X POST -H "Content-Type: application/json" -d '{"email":"test@example.com","password":"pass"}' http://localhost:8080/login
```
