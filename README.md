# FitAI — Styliste IA & E-Commerce Mode

![Java](https://img.shields.io/badge/Java-21-orange?style=for-the-badge&logo=openjdk)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.3.4-brightgreen?style=for-the-badge&logo=springboot)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-16-blue?style=for-the-badge&logo=postgresql)
![React](https://img.shields.io/badge/React-18-61DAFB?style=for-the-badge&logo=react)
![TailwindCSS](https://img.shields.io/badge/Tailwind_CSS-4.0-38B2AC?style=for-the-badge&logo=tailwind-css)
![Docker](https://img.shields.io/badge/Docker-Enabled-2496ED?style=for-the-badge&logo=docker)

**FitAI** est une plateforme e-commerce complète intégrant un **Styliste Personnel propulsé par l'IA**. L'application permet aux utilisateurs d'explorer un catalogue de vêtements et de générer des tenues complètes basées sur l'occasion, la météo et leur budget.

> Projet Full-Stack (Backend + Frontend) développé pour illustrer des compétences avancées en architecture logicielle, intégration d'API et design UI/UX.

---

## ✨ Fonctionnalités Principales

- **Styliste IA** : Génération de tenues complètes (Haut, Bas, Chaussures, Accessoires) selon des critères précis (Météo, Occasion, Budget).
- **Catalogue E-Commerce** : Navigation fluide dans les produits avec images, prix et catégories.
- **Système de Panier (Cart)** : Ajout d'articles à l'unité ou de tenues entières générées par l'IA, gestion des quantités et calcul du total.
- **Authentification Sécurisée** : Inscription et connexion via JWT (JSON Web Tokens) et Spring Security.
- **Design Premium** : Interface utilisateur moderne, épurée et responsive réalisée avec React et Tailwind CSS.

---

## 🛠️ Stack Technique

| Couche       | Technologie                                           |
| ------------ | ----------------------------------------------------- |
| **Frontend** | React 18, TypeScript, Vite, Tailwind CSS v4, Zustand  |
| **Backend**  | Java 21, Spring Boot 3.3, Spring Security 6, JWT      |
| **Base de données** | PostgreSQL 16, Spring Data JPA, Hibernate      |
| **Infra**    | Docker, Docker Compose                                |

---

## 🚀 Comment tester le projet localement ?

### Prérequis
- **Java 21** et **Maven** installés.
- **Node.js** (v18+) et **npm** installés.
- **Docker** (pour la base de données PostgreSQL).

### Étape 1 : Lancer la Base de Données
Ouvrez un terminal à la racine du projet et lancez Docker :
```bash
docker compose up -d postgres
```

### Étape 2 : Lancer le Backend (Spring Boot)
Ouvrez un terminal, allez dans le dossier `backend` et démarrez le serveur :
```bash
cd backend
mvn spring-boot:run
```
*Le backend sera accessible sur `http://localhost:8080`. (Des données de démonstration sont automatiquement injectées dans la base de données au démarrage).*

### Étape 3 : Lancer le Frontend (React)
Ouvrez un **nouveau terminal**, allez dans le dossier `frontend` et démarrez l'interface :
```bash
cd frontend
npm install
npm run dev
```
*Le frontend sera accessible sur `http://localhost:5173` (ou 5174).*

🎉 **Vous pouvez maintenant ouvrir votre navigateur et tester l'application !**

---

## 📸 Aperçu de l'Architecture

```text
FitAI/
├── backend/                  # API Spring Boot
│   ├── src/main/java/.../
│   │   ├── config/           # Sécurité, CORS, Swagger
│   │   ├── controller/       # Endpoints REST (Produits, Styliste, Panier, Auth)
│   │   ├── dto/              # Objets de transfert de données
│   │   ├── entity/           # Modèles de base de données (JPA)
│   │   ├── repository/       # Interfaces Spring Data JPA
│   │   └── service/          # Logique métier (Algorithme IA, gestion du panier)
│   └── pom.xml
│
├── frontend/                 # Interface React / Vite
│   ├── src/
│   │   ├── components/       # Composants réutilisables (CartDrawer, Navbar)
│   │   ├── pages/            # Vues principales (Home, Catalog, Stylist, Login)
│   │   └── store/            # Gestionnaire d'état global (Zustand pour le panier)
│   └── package.json
│
└── docker-compose.yml        # Configuration de la base de données
```

---

## 👤 Auteur
Développé par **Walid Mabrouk**.
Retrouvez-moi sur [GitHub](https://github.com/MabWalid) ou sur LinkedIn.