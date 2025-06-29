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

After logging in, include the returned token in the `Authorization` header:

```bash
curl -H "Authorization: Bearer <token>" http://localhost:8080/me
curl -X PUT -H "Authorization: Bearer <token>" -H "Content-Type: application/json" \
     -d '{"location":"NYC","industry":"AI","skills":"Java","startupStage":"idea","lookingFor":"Designer"}' \
     http://localhost:8080/me/profile
```

To fetch recommended users, call:

```bash
curl -H "Authorization: Bearer <token>" http://localhost:8080/recommendations
```
The recommendation list ignores users you are already connected to or have pending
connection requests with.

To send a connection request and list your connections:

```bash
curl -X POST -H "Authorization: Bearer <token>" -H "Content-Type: application/json" \
     -d '{"targetId":2}' http://localhost:8080/connections
curl -H "Authorization: Bearer <token>" http://localhost:8080/connections
```

To view incoming requests and accept one:

```bash
curl -H "Authorization: Bearer <token>" http://localhost:8080/connections/requests
curl -X PUT -H "Authorization: Bearer <token>" \
     http://localhost:8080/connections/1/accept
```

### Real-time chat

After connecting with another user you can exchange messages over WebSocket. Connect to `/ws` using SockJS/STOMP and send messages to `/app/chat/{recipientId}`. Incoming messages are delivered on `/user/queue/messages`.

You can also fetch recent messages with a user via:

```bash
curl -H "Authorization: Bearer <token>" http://localhost:8080/chats/2
```

## Frontend

To run the React frontend (using Vite):

```bash
cd frontend
npm install
npm start
```

This starts the dev server on http://localhost:3000 where you can see the placeholder UI.

## Docker

You can run the entire stack using Docker Compose. Build and start all services
with:

```bash
docker-compose up --build
```

The React app will be available on <http://localhost:3000> and the Spring Boot
API on <http://localhost:8080>.
