# SopiaTech Eats-Team-24-25

![Status_badge](https://img.shields.io/badge/status-complet-brightgreen)
![Version_badge](https://img.shields.io/badge/version-2.0.0-blue)

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

**Rendu Final D2** : [README.md](./doc/report/TeamJ-RenduD2.md)


---

# Comment tester et lancer le projet

## Exécution des Tests

**Tests Backend** :
   - Accédez au répertoire `./backend`.
   - Exécutez les tests avec Maven :
     ```bash
     mvn test
     ```
   - Les résultats des tests seront affichés dans la console.


## Lancer le Frontend

1. Accédez au répertoire `./frontend/ste-24-25--teamj-frontend`.
2. Installez les dépendances nécessaires :
   ```bash
   npm i
   ```
3. Démarrez le serveur de développement :
   ```bash
   npm run dev
   ```
4. Accédez à l'application via votre navigateur à l'adresse indiquée dans la console (par défaut : `http://localhost:5173`).

## Lancer les Services Backend

1. Lancer votre IDE et ouvrir 4 terminaux à la racine du projet.
2. Lancer sur chaque terminal une commande parmis les 4 ci-dessous:
   - **BDD** : `mvn -pl backend\bdd exec:java`
   - **GroupOrder** : `mvn -pl backend\grouporder exec:java`
   - **Restaurant** : `mvn -pl backend\restaurant exec:java`
   - **Gateway** : `mvn -pl backend\gateway exec:java`

Une fois tous les services lancés, ils seront accessibles via leurs points d'entrée et pourront communiquer avec le frontend.

**Remarque :** Essayez de vider vider le localstorage pour se déconnecter si le bouton logout n'a pas fonctionné. Les utilisateurs connctés sont enregsitrés dans le localstorage donc pour 2 utilisateurs différents il faut 2 navigateurs (onglet normal + onglet privé). De plus, parfois il est nécessaire de relancer le service bdd pour clear toutes les anciennes données et recommencer proprement une démonstration.

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

---

### FlexibleRestServer

**But** : Fournit un serveur REST dynamique pour la gestion flexible des API.

**Classes clés** :
- `FlexibleRestServer` : Logique principale du serveur.
- Utilitaires comme `HttpResponse` et `ResponseUtils` pour la gestion des réponses HTTP.

**Dépendances** :
- Dépendances minimales axées sur la fonctionnalité du serveur REST de base de Java.

---

### Gateway

**But** : Sert de point d'entrée, gérant et redirigeant les requêtes vers d'autres microservices.

**Classes clés** :
- Contrôleurs pour le traitement et la redirection des requêtes (`GatewayController`).
- DTO pour l'échange de données entre le client et les services (`OrderDTO`, `MenuItemDTO`).

**Dépendances** :
- FlexibleRestServer.

---

### GroupOrder

**But** : Gère les commandes groupées, y compris le traitement et la coordination des commandes partagées entre utilisateurs.

**Classes clés** :
- Logique principale : `GroupOrder`, `GroupOrderProxy`.
- Contrôleur : `GroupOrderController`.
- DTO et utilitaires pour la gestion des échanges de données et leur formatage.

**Dépendances** :
- BDD pour l'accès aux données.
- FlexibleRestServer.

---

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

---

## Frontend

Le frontend est dans le répertoire `./frontend`.

Le frontend est construit avec React, TypeScript et Vite, offrant un environnement de développement moderne et rapide. Il intègre Tailwind CSS pour des interfaces utilisateurs responsive.


----


### Référence à une histoire utilisateur
**User Story**: [#48](https://github.com/PNS-Conception/STE-24-25--teamj/issues/48)  
**Titre**: GroupOrder Micro-service

### Gestion de projet
le lien vers notre kanban  : [kanban Équipe J](https://github.com/orgs/PNS-Conception/projects/69/views/1)

### Le lien vers le rapport détaillé

**Rendu Final D2** : [README.md](./doc/report/TeamJ-RenduD2.md)

