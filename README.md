# Zest India - Product Management API

A RESTful API for managing products, built with Spring Boot.

## Tech Stack

- Java 17
- Spring Boot 3.5.11
- Spring Data JPA + PostgreSQL
- Spring Security + JWT
- Swagger/OpenAPI
- Docker

## How to Run

### Using Docker Compose (easiest)

```bash
docker-compose up --build
```

API will be available at http://localhost:8080

### Running locally

1. Make sure PostgreSQL is running and create a database called `zest_products`
2. Update credentials in `src/main/resources/application.properties` if needed
3. Run:

```bash
mvn spring-boot:run
```

## Default Users

When the app starts, two users are created automatically:

| Username | Password | Role       |
|----------|----------|------------|
| admin    | admin123 | ROLE_ADMIN |
| user     | user123  | ROLE_USER  |

Admins can create, update, and delete products. Regular users can only view.

## API Documentation

Swagger UI: http://localhost:8080/swagger-ui.html

## Authentication Flow

1. POST `/api/v1/auth/login` with username and password
2. Copy the `accessToken` from the response
3. Add to requests as: `Authorization: Bearer <token>`
4. When access token expires, use `/api/v1/auth/refresh` with your refresh token

## Endpoints

| Method | Endpoint                        | Auth    | Description          |
|--------|---------------------------------|---------|----------------------|
| POST   | /api/v1/auth/login              | No      | Login                |
| POST   | /api/v1/auth/refresh            | No      | Refresh token        |
| POST   | /api/v1/auth/register           | No      | Register user        |
| GET    | /api/v1/products                | Yes     | Get all products     |
| GET    | /api/v1/products/{id}           | Yes     | Get product by ID    |
| POST   | /api/v1/products                | Admin   | Create product       |
| PUT    | /api/v1/products/{id}           | Admin   | Update product       |
| DELETE | /api/v1/products/{id}           | Admin   | Delete product       |
| GET    | /api/v1/products/{id}/items     | Yes     | Get items of product |
| POST   | /api/v1/products/{id}/items     | Admin   | Add item to product  |

## Running Tests

```bash
mvn test
```

Tests use H2 in-memory database so no setup needed.

## Project Structure

```
src/
├── main/java/com/zest/products/
│   ├── config/          # Security, Swagger, Data init
│   ├── controller/      # REST controllers
│   ├── dto/             # Request/Response objects
│   ├── entity/          # JPA entities
│   ├── exception/       # Exception handling
│   ├── repository/      # Spring Data repositories
│   ├── security/        # JWT filter and utilities
│   └── service/         # Business logic
└── test/
    └── java/com/zest/products/
        ├── controller/  # Controller tests
        └── service/     # Service unit tests
```
