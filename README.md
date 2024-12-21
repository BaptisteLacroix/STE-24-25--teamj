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

**Rendu Final D2** : [README.md](./doc/README.md)


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




### Branch Naming Convention

- **Feature Branches**:  
  `feature/<usecase-number>-<short-description>`  
  Example: `feature/R2-group-order-validation`
- **Bugfix Branches**:  
  `bugfix/<issue-number>-<short-description>`  
  Example: `bugfix/42-fix-group-order-validation`

### Commit Message Convention

- **Commit Message Format**:  
  `<type>(<scope>): <subject> #<issue-number>`  
  Example: `feat(orders): add group order validation feature. #4`

---

### Project Structure
This part provides an overview of the project structure, designed to ensure clear organization and easy maintenance.


- **.git/**: Contains version control data managed by Git, tracking changes to the codebase over time.
- **.github/**: Contains GitHub-specific configurations, including templates for issues and workflows for continuous integration.
- **.idea/**: Stores configuration files specific to the IntelliJ IDEA development environment.
- **doc/**: Holds technical documentation, project reports, and other related resources.
- **src/**: The core source code of the application, organized into functional packages.
  - **main/**: Primary application code, organized by core functionalities.
    - **Restaurant/**: Contains modules for managing restaurantEntity data and functionality, including restaurantEntity creation, menuEntity management, and related operations.
    - **Slot/**: Handles scheduling and time slots, supporting features such as reservation management and availability tracking.
    - **Order/**: Manages order processing, including order creation, updates, and tracking.
    - **Payment/**: Implements payment processing features, such as payment initiation, validation, and transaction management.
    - **Delivery/**: Contains modules related to delivery management.
  - **test/**: Contains testing files, definitions, and resources to validate application features, ensuring reliable and functional code.
- **pom.xml**: The Project Object Model file for Maven. It manages project dependencies, plugins, and build configurations, essential for compiling and packaging the application.
- **README.md**: Provides an overview of the project, including setup instructions and usage guidelines.
- **LICENSE**: Details the licensing terms governing the project.
- **target/**: Directory for build outputs, such as compiled code and packaged artifacts.

### User Story Reference

**User Story ID**: [#25](https://github.com/PNS-Conception/STE-24-25--teamj/issues/25)  
**Title**: Validation of group orders by members

**Description**:  
As a registered user, I want to validate the group order at the end of my individual order and, if necessary, specify a
compatible delivery time so that the group order can be finalized and closed.

**Feature Demonstration**:  
The behavior of this user story is demonstrated
in [ValidateGroupOrder.feature](./src/test/resources/features/orders/ValidateGroupOrder.feature). The scenarios include
validation of a group order, setting delivery times, and handling incompatible delivery times.


<!-- ## Ce que fait votre projet


### Principales User stories
Vous mettez en évidence les principales user stories de votre projet.
Chaque user story doit être décrite par 
   - son identifiant en tant que issue github (#), 
   - sa forme classique (As a… I want to… In order to…) (pour faciliter la lecture)
   - Le nom du fichier feature Cucumber et le nom des scénarios qui servent de tests d’acceptation pour la story.
   Les contenus détaillés sont dans l'issue elle-même. -->
   

   
