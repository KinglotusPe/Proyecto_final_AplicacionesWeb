# Base44 Dev Environment — BRUTAL FITNESS Gym Management System

## Overview
Spring Boot 3.3.4 + Thymeleaf MVC app for gym management. Serves HTML pages on port 8080 (mapped to host port 3000). Uses MySQL 8 for persistence.

## Architecture
- **Backend**: Java 17, Spring Boot, Spring Data JPA, Spring Security (BCrypt + RBAC), Thymeleaf templates.
- **Frontend (MVC)**: `frontend/templates/` (Thymeleaf HTML), `frontend/css/`, `frontend/js/` — mapped into the backend's classpath via `backend/pom.xml` resource entries.
- **Frontend (Angular SPA)**: `frontend-angular/` — a separate standalone Angular 17 app (NOT run in this dev setup; the MVC app is the primary entry point).
- **Database**: MySQL 8, schema `gym_db`. Seed data via `database/gym_db.sql` (auto-imported on first MySQL init).

## How It Runs (docker-compose.base44.yml)
- `mysql` service: MySQL 8.0 with `database/gym_db.sql` mounted to `/docker-entrypoint-initdb.d/` (runs once on first init). Healthcheck with 40s start period.
- `backend` service: `maven:3.9-eclipse-temurin-17` image, repo bind-mounted at `/app`. Runs `mvn -f /app/backend/pom.xml spring-boot:run`. Maven repo cached in named volume. Port 3000→8080.
- `SPRING_PROFILES_ACTIVE=dev` activates `application-dev.properties` which serves Thymeleaf templates and static resources directly from source (live reload for HTML/CSS/JS).

## Live Reload
- **Templates/CSS/JS**: Live reload works via the `dev` profile (Thymeleaf prefix + static locations point to source files). Changes appear on next request.
- **Java code**: Requires backend restart — run `docker compose -f docker-compose.base44.yml restart backend` then `reload_preview`.

## Default Login Credentials (seeded by CommandLineRunner on startup)
| Role | User | Password |
|------|------|----------|
| Admin | `admin` | `admin123` |
| Recepción | `recepcion` | `recepcion123` |
| Entrenador | `entrenador` | `entrenador123` |
| Cliente | `72345678` | `cliente123` |

## Key Fix: Forwarded Headers
The preview proxy serves over HTTPS but forwards HTTP to the app. Without `server.forward-headers-strategy=framework` (in `application-dev.properties`), Spring Security generates `http://` redirect URLs which browsers block as mixed content. The forward-headers strategy makes Spring respect `X-Forwarded-Proto`/`X-Forwarded-Host` headers and generate correct `https://` redirects.

## Verification
1. `docker compose -f docker-compose.base44.yml up -d --build`
2. Wait for backend to compile and start (~1-2 min on first run for Maven deps).
3. `curl -sf http://localhost:3000/login` should return the login page HTML.
4. External check: `curl -sf -H "Host: external.example.com" http://localhost:3000/login` (Spring Boot binds 0.0.0.0, accepts any host).

## No External Secrets Required
All dependencies (MySQL) run as local compose services. No third-party API keys needed for the MVC app.
