# 🥗 NutriChef — Backend API (Spring Boot 3 + Java 17)

[![Live API Health](https://img.shields.io/badge/API-Health%20Live-brightgreen?style=for-the-badge&logo=spring)](https://ais-pre-5bmzthygksre2hnescz46u-67796372107.asia-southeast1.run.app/api/health)
[![Java](https://img.shields.io/badge/Java-17-orange?style=for-the-badge&logo=openjdk)](https://adoptium.net/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.2.x-green?style=for-the-badge&logo=springboot)](https://spring.io/)

> 🔗 **Live Backend URL:** [https://ais-pre-5bmzthygksre2hnescz46u-67796372107.asia-southeast1.run.app/api](https://ais-pre-5bmzthygksre2hnescz46u-67796372107.asia-southeast1.run.app/api)  
> 🩺 **Health Check Endpoint:** [https://ais-pre-5bmzthygksre2hnescz46u-67796372107.asia-southeast1.run.app/api/health](https://ais-pre-5bmzthygksre2hnescz46u-67796372107.asia-southeast1.run.app/api/health)

NutriChef is an enterprise-grade backend service powering smart food inventory management, recipe recommendation with ingredient matching, and comprehensive nutrition tracking.

Built with **Java 17**, **Spring Boot 3.2.x**, **Spring Security 6**, **Spring Data JPA**, and **MySQL**.

---

## 🚀 Features

- **🔐 Authentication & Security**: Stateless JWT authentication (`OncePerRequestFilter`), BCrypt password encoding (strength 12), and role-isolated access.
- **📦 Smart Inventory Management**: Real-time CRUD for pantry & fridge items, category classification, low-stock flags, and expiration tracking (expiring soon vs. expired).
- **🍳 Intelligent Recipe Recommendation**:
  - Exact & fuzzy ingredient matching algorithms.
  - Match percentage score calculation based on current pantry items.
  - Sorting and prioritization of recipes using ingredients expiring soon to minimize food waste.
  - Dietary filter matching (Vegan, Vegetarian, Gluten-Free, Dairy-Free, Low-Carb, Keto, Paleo, Halal).
- **📊 Nutrition Analytics**: Daily calorie tracking, macronutrient balance breakdown (Protein, Carbs, Fat), meal logging (Breakfast, Lunch, Dinner, Snack), and integration support for Edamam API with demo fallback.
- **🛒 Shopping List Generator**: Automated aggregation of missing recipe ingredients, status toggling, and pantry item synchronisation.
- **🩺 Health & Actuator Endpoint**: `/api/health` providing instant readiness and liveness status.

---

## 🛠️ Tech Stack

- **Language**: Java 17 (LTS)
- **Framework**: Spring Boot 3.2.3
- **Build Tool**: Apache Maven 3.8+
- **Security**: Spring Security 6, JJWT (Java JWT `0.12.5`)
- **Database**: MySQL 8.0 / MySQL Dialect
- **ORM**: Spring Data JPA / Hibernate
- **Validation**: Hibernate Validator / Jakarta Bean Validation
- **Containerization**: Docker (multi-stage build), Docker Compose

---

## 📋 Prerequisites

Before running the backend locally, ensure you have installed:
- [Java 17 JDK](https://adoptium.net/) (`java -version`)
- [Apache Maven](https://maven.apache.org/) (`mvn -version`)
- [MySQL 8.0](https://www.mysql.com/) OR [Docker](https://www.docker.com/)

---

## ⚙️ Environment Configuration

Copy the `.env.example` file to `.env` or set environment variables in your runtime environment:

```bash
cp .env.example .env
```

| Variable | Description | Default (Local) |
| :--- | :--- | :--- |
| `PORT` | HTTP server port | `8080` |
| `DB_URL` | JDBC MySQL connection URL | `jdbc:mysql://localhost:3306/nutrichef_db?createDatabaseIfNotExist=true&useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true` |
| `DB_USER` | MySQL database user | `root` |
| `DB_PASSWORD` | MySQL database password | `password` |
| `JWT_SECRET` | 256-bit secret key (at least 32 chars) | `404E635266556A586E3272357538782F413F4428472B4B6250645367566B5970` |
| `JWT_EXPIRATION_MS` | JWT token validity in milliseconds | `86400000` (24 hours) |
| `CORS_ALLOWED_ORIGINS` | Permitted frontend origins (comma separated) | `http://localhost:3000,http://localhost:5173` |
| `EDAMAM_APP_ID` | *(Optional)* Edamam Nutrition App ID | *(Empty / demo mode fallback)* |
| `EDAMAM_APP_KEY` | *(Optional)* Edamam Nutrition App Key | *(Empty / demo mode fallback)* |

---

## 🏃 Running the Application

### Option A: Running with Maven Locally

1. **Start MySQL database** (create database `nutrichef_db` if not using auto-create).
2. **Build and run**:
   ```bash
   mvn clean spring-boot:run
   ```
3. The server starts on `http://localhost:8080`.
4. Verify by visiting `http://localhost:8080/api/health`.

### Option B: Running with Docker Compose (One-Command Setup)

This starts both MySQL 8.0 and the Spring Boot backend container in an isolated network:

```bash
docker compose up --build -d
```

View logs:
```bash
docker compose logs -f backend
```

Stop services:
```bash
docker compose down
```

---

## 📡 REST API Reference

All endpoints are prefixed with `/api`.

### 🩺 Health & Public
- `GET /api/health` — Service health status (`{"status": "UP"}`)

### 🔐 Authentication (`/api/auth`)
- `POST /api/auth/register` — Create a new user account (`name`, `email`, `password`)
- `POST /api/auth/login` — Authenticate and receive JWT Bearer token
- `GET /api/auth/profile` — Get authenticated user profile (`Bearer <token>`)
- `PUT /api/auth/profile` — Update user profile & dietary preferences (`Bearer <token>`)

### 📦 Food Inventory (`/api/inventory`)
- `GET /api/inventory` — List all inventory items for current user
- `POST /api/inventory` — Add item (`name`, `category`, `quantity`, `unit`, `expirationDate`, `caloriesPerUnit`)
- `PUT /api/inventory/{id}` — Update item
- `DELETE /api/inventory/{id}` — Delete item
- `GET /api/inventory/expiring` — Get items expiring within 3 days
- `GET /api/inventory/expired` — Get items that have expired
- `GET /api/inventory/low-stock` — Get items below low stock threshold

### 🍳 Recipes & Matching (`/api/recipes`)
- `GET /api/recipes` — List recipes with optional search query & category filter
- `GET /api/recipes/{id}` — Recipe detail with ingredients, steps, and nutrition facts
- `GET /api/recipes/recommendations` — Smart recommendations matched against user inventory with match percentage
- `POST /api/recipes/{id}/rate` — Rate a recipe (1–5 stars)

### 📊 Nutrition & Meal Logs (`/api/nutrition`)
- `GET /api/nutrition/summary` — Daily calories and macronutrient breakdown (consumed vs. target)
- `GET /api/nutrition/logs` — List logged meals
- `POST /api/nutrition/logs` — Log a meal (`recipeId` or manual entry, `mealType`, `servings`, `calories`, `date`)
- `DELETE /api/nutrition/logs/{id}` — Remove meal log

### 🛒 Shopping List (`/api/shopping-list`)
- `GET /api/shopping-list` — List shopping list items
- `POST /api/shopping-list` — Add item manually
- `PUT /api/shopping-list/{id}` — Update item / toggle checked status
- `DELETE /api/shopping-list/{id}` — Remove item
- `POST /api/shopping-list/add-missing/{recipeId}` — Auto-add missing ingredients from recipe

---

## 🚢 How to Push This Backend to GitHub (Separate Repository)

Follow these steps in your terminal to create and push this backend as a dedicated repository:

```bash
# 1. Navigate to the backend directory
cd nutrichef/backend

# 2. Initialize git repository
git init -b main

# 3. Add all files and make initial commit
git add .
git commit -m "feat: initial commit for NutriChef Spring Boot backend"

# 4. Create a new repository on GitHub (e.g., nutrichef-backend)
# Using GitHub CLI:
gh repo create nutrichef-backend --public --source=. --remote=origin --push

# OR Using standard git (after creating an empty repo on github.com):
git remote add origin https://github.com/<YOUR_GITHUB_USERNAME>/nutrichef-backend.git
git branch -M main
git push -u origin main
```

---

## 🌐 Production Deployment Guide

### Deploying to Render:
1. Create a **Web Service** on Render and connect your `nutrichef-backend` GitHub repository.
2. Choose **Docker** environment (it automatically detects the included `Dockerfile`) or **Native Java**.
3. In Environment Variables, set:
   - `PORT`: `8080`
   - `DB_URL`: your cloud MySQL connection string (e.g., Aiven MySQL or PlanetScale)
   - `DB_USER`: database username
   - `DB_PASSWORD`: database password
   - `JWT_SECRET`: 64-character hex string
   - `CORS_ALLOWED_ORIGINS`: your frontend URL (e.g. `https://your-app.vercel.app`)
4. Click **Deploy**.

---

## 📄 License

MIT License. Built for modern food inventory and nutrition intelligence.
