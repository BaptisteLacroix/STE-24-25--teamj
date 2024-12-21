# SopiaTech Eats-Team-24-25

![Status_badge](https://img.shields.io/badge/status-complet-brightgreen)
![Version_badge](https://img.shields.io/badge/version-1.0.0-blue)

---

## Team Members and Roles

- **PO (Product Owner)**: [Baptiste Lacroix](https://github.com/BaptisteLacroix) - Defines the product vision
- **Architect**: [Antoine Maïstre-Rice](https://github.com/Antoine-MR) - Designs the system architecture
- **QA (Quality Assurance)**: [Tom Toupence](https://github.com/tom-toupence) - Oversees quality assurance and testing
- **Ops (Operations)**: [Abderrahmen Tlili](https://github.com/AbdouTlili) - Manages PRs,infrastructure, deployment and automation


---

## Doc

Rapport : [TeamJ-renduD1.pdf](./doc/report/TeamJ-renduD1.pdf)<br>
Conception : [README.md](./doc/README.md)

**Rendu Final D2** : [README.md](./doc/report/README.md)


---

## Installation & Launch Instructions

1. **Requirements:**
    - Java JDK 21
    - Maven (version 3.6.3 or higher)
    - Git

2. **Setup**:
    - Clone the repository:
      ```bash
      git clone https://github.com/PNS-Conception/STE-24-25--teamj.git
      cd STE-24-25--teamj
      ```
    - Install dependencies with Maven:
      ```bash
      mvn clean install
      ```

3. **Testing**:
    - Run tests:
      ```bash
      mvn clean test
      ```

---

## Structure du projet

Notre projet est organisé en deux répertoires principaux :

- `./backend` : Contient tous les services backend.
- `./frontend` : Contient l'application frontend.

Les services mentionnés ci-dessous se trouvent dans le répertoire `./backend`.

### BDD

**But** : Sert de couche de données, gérant les interactions avec la base de données pour les entités comme les restaurants, les commandes, les utilisateurs et les lieux de livraison.

**Classes clés** :
- Gestion de la base de données : `HibernateUtil`, `DeliveryLocationDatabaseSeeder`, `RestaurantDatabaseSeeder`.
- Définition des entités et DAO pour les commandes, les restaurants et les utilisateurs.

**Dépendances** :
- Hibernate pour l'ORM.
- Jackson pour le traitement JSON.
- FlexibleRestServer pour la communication avec le serveur.

### FlexibleRestServer

**But** : Fournit un serveur REST dynamique pour la gestion flexible des API.

**Classes clés** :
- `FlexibleRestServer` : Logique principale du serveur.
- Utilitaires comme `HttpResponse` et `ResponseUtils` pour la gestion des réponses HTTP.

**Dépendances** :
- Dépendances minimales axées sur la fonctionnalité du serveur REST de base de Java.

### Gateway

**But** : Sert de point d'entrée, gérant et redirigeant les requêtes vers d'autres microservices.

**Classes clés** :
- Contrôleurs pour le traitement et la redirection des requêtes (`GatewayController`).
- DTO pour l'échange de données entre le client et les services (`OrderDTO`, `MenuItemDTO`).

**Dépendances** :
- FlexibleRestServer.

### GroupOrder

**But** : Gère les commandes groupées, y compris le traitement et la coordination des commandes partagées entre utilisateurs.

**Classes clés** :
- Logique principale : `GroupOrder`, `GroupOrderProxy`.
- Contrôleur : `GroupOrderController`.
- DTO et utilitaires pour la gestion des échanges de données et leur formatage.

**Dépendances** :
- BDD pour l'accès aux données.
- FlexibleRestServer.

### Restaurant

**But** : Gère les opérations des restaurants telles que la gestion des menus, les données des restaurants et le traitement des commandes.

**Classes clés** :
- Logique principale : `RestaurantServiceManager`, `IRestaurant`.
- Contrôleurs et DTO pour gérer les opérations des restaurants et des menus.
- Stratégies de tarification des commandes : `FreeItemFotNItemsOrderPriceStrategy`, `KPercentForNOrderPriceStrategy`.

**Dépendances** :
- Utilisation de DTO.
- FlexibleRestServer.
- BDD.

### Transaction

**But** : Gère le traitement des paiements avec plusieurs méthodes de paiement (par exemple, PayPal, carte de crédit).

**Classes clés** :
- `PaymentController` : Gère les requêtes de paiement.
- `PaymentProcessorFactory` : Choisit dynamiquement le processeur de paiement (par exemple, PayPal, Paylib, carte de crédit).
- DTO pour le traitement des paiements : `PaymentRequestDTO`, `PaymentResultDTO`.

**Dépendances** :
- Hibernate pour l'intégration de la base de données.
- FlexibleRestServer pour la communication REST.

## Frontend

Le frontend est dans le répertoire `./frontend`.

Le frontend est construit avec React, TypeScript et Vite, offrant un environnement de développement moderne et rapide. Il intègre Tailwind CSS pour des interfaces utilisateurs réactives.


----


### Référence à une histoire utilisateur
**User Story**: [#48](https://github.com/PNS-Conception/STE-24-25--teamj/issues/48)  
**Titre**: GroupOrder Micro-service

### Référence à une histoire utilisateur
le lien vers notre kanban  : [kanban Équipe J](https://github.com/orgs/PNS-Conception/projects/69/views/1)

### Le lien vers le rapport détaillé

**Rendu Final D2** : [README.md](./doc/report/README.md)

