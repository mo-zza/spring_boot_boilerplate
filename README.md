# Spring Boot Boilerplate

## Description

This is a boilerplate for Spring Boot projects. It includes a basic configuration for a Spring Boot project, with a
basic controller and a basic service. It also includes a basic configuration for API Docs and a basic configuration for
a Dockerfile.

## Spec

- Java 17
- Spring Boot 3.1.3
- Gradle 8.2.1
- JPA & QueryDSL
- H2 Database - for local profile
- PostgreSQL - for production and development profile
- Docker
- Spring REST Docs
- Spring Security, JWT
- Spring Actuator

## Environment variable

| Name                             | Description                      | Default |
|----------------------------------|----------------------------------|---------|
| SERVER_PORT                      | application port                 | 8080    |
| APPLICATION_VERSION              | application version              | 0.0.1   |
| ACTIVE_PROFILE                   | spring active profile            | default |
| DATABASE_URL                     | database url                     |         |
| DATABASE_USERNAME                | database username                |         |
| DATABASE_PASSWORD                | database password                |         |
| SECURITY_USER_NAME               | security user name               |         |
| SECURITY_USER_PASSWORD           | security user password           |         |
| JWT_SECRET_KEY                   | jwt secret key                   |         |
| JWT_ACCESS_TOKEN_VALIDITY_HOURS  | jwt access token validity hours  |         |
| JWT_REFRESH_TOKEN_VALIDITY_HOURS | jwt refresh token validity hours |         |

## How to use

### 1. Clone this repository

```bash
git clone
```

### 2. Change the project name

- Change the project name in `settings.gradle`
- Change the project name in `build.gradle`

### 3. build & run

```bash
./gradlew build
./gradlew bootRun
```

### 4. Check the API Docs

- Open `http://localhost:8080/docs/index.html` in your browser