# TaskWave — Real-Time Collaborative Project Management Platform

Jira/Trello-style boards: teams, boards, tasks with status/priority, comments,
and a live activity feed pushed over WebSocket (STOMP) the instant anything
changes — no polling required.

## Stack

- Spring Boot 3 / Java 21, Spring WebSocket (STOMP over `/ws`)
- PostgreSQL — teams, members, boards, tasks
- MongoDB — comments, activity timeline (also the source of the WebSocket broadcast)

## Real-time model

Every task creation, status change, and comment calls `ActivityService.publish()`,
which does two things atomically from the caller's point of view: persists an
`ActivityEvent` to MongoDB *and* pushes it to `/topic/boards/{boardId}` over
STOMP. Any client subscribed to that topic gets the update immediately.

## Run

```
docker-compose up -d
mvn spring-boot:run
```

Swagger UI: `http://localhost:8150/swagger-ui.html`
WebSocket endpoint: `ws://localhost:8150/ws` (STOMP, subscribe to `/topic/boards/{boardId}`)

## Flow

1. `POST /api/v1/teams`, `POST /api/v1/teams/{id}/members`
2. `POST /api/v1/teams/{teamId}/boards`
3. `POST /api/v1/boards/{boardId}/tasks`
4. `PATCH /api/v1/tasks/{id}/status?status=IN_PROGRESS` — broadcasts live
5. `POST /api/v1/tasks/{id}/comments` — broadcasts live
6. `GET /api/v1/teams/{teamId}/boards/{boardId}/activity` — full history (same
   events a live-subscribed client would have already received)
