# FitAI — Styliste IA & catalogue mode

![Java](https://img.shields.io/badge/Java-21-orange?style=for-the-badge&logo=openjdk)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.3.4-brightgreen?style=for-the-badge&logo=springboot)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-16-blue?style=for-the-badge&logo=postgresql)
![Docker](https://img.shields.io/badge/Docker-Enabled-2496ED?style=for-the-badge&logo=docker)

**FitAI** est une API de mode : catalogue de vêtements + (ensuite) un **styliste IA** qui propose des tenues selon l’occasion, le budget et la météo, à partir de **vrais produits** en base.

> Projet étudiant, écrit from scratch.

---



## Ce qui marche aujourd’hui

- Authentification JWT (inscription, login, refresh, logout)
- Catalogue : catégories, produits, tailles, images
- Lecture publique du catalogue (filtres + pagination)
- Création / modification / archivage **admin**
- Données de démo au premier démarrage (profil `dev`)
- Swagger, PostgreSQL via Docker, tests unitaires auth + catalogue



## Prochaines étapes

1. Styliste IA (`POST /ai/stylist`) branché sur le catalogue
2. Frontend (login + liste produits + chat styliste)
3. Panier / commandes

---



## Stack


| Couche       | Techno                                           |
| ------------ | ------------------------------------------------ |
| API          | Java 21, Spring Boot 3.3, Spring Security 6, JWT |
| Données      | PostgreSQL 16, Spring Data JPA                   |
| Docs         | OpenAPI 3 / Swagger UI                           |
| Infra locale | Docker Compose                                   |


---



## Démarrage



### Prérequis

- Java 21
- Maven 3.9+
- [Docker Desktop](https://www.docker.com/products/docker-desktop/)



### 1. Base de données

```bash
docker compose up -d postgres pgadmin
```



### 2. Backend

```bash
cd backend
mvn spring-boot:run
```


| Service | URL                                                                                 |
| ------- | ----------------------------------------------------------------------------------- |
| API     | `http://localhost:8080/api/v1`                                                      |
| Swagger | `http://localhost:8080/api/v1/swagger-ui.html`                                      |
| Health  | `http://localhost:8080/api/v1/health`                                               |
| PgAdmin | `http://localhost:5050` ([admin@fitai.dev](mailto:admin@fitai.dev) / adminpassword) |




### Compte admin de démo (créé au 1er lancement)

- Username : `admin`
- Email : `admin@fitai.dev`
- Mot de passe : `Admin1234!`

---



## API catalogue (extraits)

```http
GET  /api/v1/categories
GET  /api/v1/products?q=jean&clothingCategory=BOTTOM&page=0&size=10
GET  /api/v1/products/{id}
GET  /api/v1/sizes

POST /api/v1/categories     # admin
POST /api/v1/products       # admin
PUT  /api/v1/products/{id}  # admin
DELETE /api/v1/products/{id}  # admin (archive, active=false)
```

Dans Swagger : Authorize → `Bearer <accessToken>` après `/auth/login`.

---



## Architecture

```text
backend/src/main/java/com/fitai/
├── config/       # Security, CORS, OpenAPI, JPA
├── controller/   # REST
├── dto/          # Requests / Responses
├── entity/       # Modèle JPA
├── repository/
├── security/     # JWT
└── service/
```

Package Java : `com.fitai`.

---



## GitHub

```text
https://github.com/MabWalid/FitAI
```

