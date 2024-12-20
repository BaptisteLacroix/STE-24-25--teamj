# 1 Périmètre fonctionnel

## 1.1 Hypothèse de travail

Livraison & temps de préparation:

- Livraison, nous n’avons pas pris en compte le temps de livraison entre le moment où la commande est prête et le moment
  où elle se fait livrer. Donc nous partons du principe qu’il est compris dans le temps de préparation.

Système de paiement:

- Nous avons implémenté une stratégie de paiement qui pour le moment ne fait rien a part faire un affichage. Donc pour
  le moment tous les paiements sont validés automatiquement

## 1.2 Points non implémentés

## 1.3 Fonctionnalités: points forts, points faibles

### 1.3.1 Points forts

### 1.3.2 Points faibles

---

# 2 Architecture et Justification de l'Architecture

## 2.1 Organisation du code

Le projet est organisé en modules pour une meilleure lisibilité et maintenabilité. Chaque module contient des fichiers
pertinents pour une responsabilité claire :

- **Backend** : Structure en microservices, chacun responsable d'une fonctionnalité unique.
    - Exemple :
        - [Gateway](../../backend/gateway) : Point d'entrée pour les requêtes.
        - [RestaurantsService](../../backend/restaurant) : Gestion des restaurants.
        - [GroupOrderService](../../backend/grouporder) : Gestion des commandes groupées.
        - [DatabaseService](../../backend/bdd) : Actions sur la base de données.
        - [Flexible Rest Server](../../backend/flexiblerestserver) : Custom Framework pour les services REST.
- **Frontend** : Application React structurée par composants avec organisation en dossiers par fonctionnalités (e.g.,
  `components`, `model`, `utils`). Les composants sont réutilisables et modulaires.
    - Exemple :
        - [Components](../../frontend/ste-24-25--teamj-frontend/src/modules/components) : Composants réutilisables.
        - [Model](../../frontend/ste-24-25--teamj-frontend/src/modules/model) : Modèles de données.
        - [Utils](../../frontend/ste-24-25--teamj-frontend/src/modules/utils) : Fonctions utilitaires.

Des liens vers les fichiers importants sont inclus pour guider les développeurs :

- Backend : [Lien vers le répertoire](../../backend)
- Frontend : [Lien vers le répertoire](../../frontend/ste-24-25--teamj-frontend)

## 2.2 Décomposition en services

La solution repose sur une architecture basée sur les microservices, avec une nette séparation des responsabilités :

- **Front-End** :
    - Interagit avec l’API via une passerelle (Gateway).
    - Utilise React pour l’interface utilisateur et TypeScript pour la sécurité et la lisibilité du code.
- **Passerelle (Gateway)** :
    - Accessible depuis l’extérieur.
    - Route les requêtes vers les services internes selon la logique d'affaires.
- **Services internes** :
    - `RestaurantsService` : Gère les données et opérations relatives aux restaurants.
    - `GroupOrderService` : Traite les commandes groupées.
    - `DatabaseService` : Accès et modifications des données dans la base de données.

Les services sont éventuellement décomposables en fonction des besoins futurs.

## 2.3 Entités persistantes

Les entités persistantes de l’application incluent :

- **Restaurant** : Informations relatives aux restaurants.
- **GroupOrder** : Détails sur les commandes groupées.
- **Order**: Informations sur les commandes.
- **CampusUser** : Données des utilisateurs.
- **RestaurantManager** : Gestionnaires de restaurants.
- **Menu** : Détails des menus des restaurants.
- **MenuItem** : Éléments individuels des menus.
- **DeliverLocation** : Informations sur les lieux de livraison.
- ...

Chaque entité est modélisée sous forme d’objets et mappée dans la base de données avec des ORM (Object-Relational
Mapping).

## 2.4 Objets de communication (DTO)

Les DTO (Data Transfer Objects) facilitent la communication entre services. Ces objets sont localisés dans chaque
service sous le dossier `dto` :

- Tous les dossiers dto:
    - [Gateway](../../backend/gateway/src/main/java/fr/unice/polytech/equipe/j/dto)
    - [RestaurantsService](../../backend/restaurant/src/main/java/fr/unice/polytech/equipe/j/dto)
    - [GroupOrderService](../../backend/grouporder/src/main/java/fr/unice/polytech/equipe/j/dto)
    - [DatabaseService/Order](../../backend/bdd/src/main/java/fr/unice/polytech/equipe/j/order/dto)
    - [DatabaseService/Restaurant](../../backend/bdd/src/main/java/fr/unice/polytech/equipe/j/restaurant/dto)
    - [DatabaseService/DeliveryLocation](../../backend/bdd/src/main/java/fr/unice/polytech/equipe/j/deliverylocation/dto)
    - [DatabaseService/User](../../backend/bdd/src/main/java/fr/unice/polytech/equipe/j/user/dto)

- Exemple :
    - `bdd/src/main/java/fr/unice/polytech/equipe/j/restaurant/dto/RestaurantDTO.java`
    - `restaurant/src/main/java/fr/unice/polytech/equipe/j/database/dto/MenuItemDTO.java`
    - ...

Ces DTO standardisent les données transmises et permettent une meilleure validation.

## 2.5 APIs utilisées

L’ensemble des APIs est documenté en OpenAPI (fichiers `openapi.yaml`). Les fichiers pertinents sont disponibles ici :

- Gateway API : [Lien vers ](../openapi/gatewayOpenApi.json)`doc/openapi/gatewayOpenApi.json`
- Restaurants API : [Lien vers ](../openapi/restaurantOpenApi.json)`restaurants/openapi.yaml`
- Group Order API : [Lien vers ](../openapi/grouporderOpenApi.json)`group-order/openapi.yaml`
- Database API : [Lien vers ](../openapi/bddOpenApi.json)`database/openapi.yaml`

## 2.6 Interface utilisateur

L’interface utilisateur est conçue avec :

- **ViteJS** pour une compilation rapide.
- **React** et **TypeScript** pour une architecture frontale modulaire et robuste.
- **TailwindCSS** pour un style rapide et réactif.
- **NextUI** pour des composants rapides et réutilisables.

Lien vers les fichiers front-end : [Frontend](../../frontend/ste-24-25--teamj-frontend).

### Captures d’écran

Voici un exemple de l'interface utilisateur déconnecté:

### Page d'accueil

![Page d'accueil](./images/home-page-disconnected.png)

### Filtre par items

![Filtre par items](./images/home-page-disconnected-filter.png)

### Recherche par nom

![Recherche par nom](./images/home-page-disconnected-search.png)

### Affichage du menu d'un restaurant

![Affichage du menu d'un restaurant](./images/home-page-disconnected-menu-items.png)

Voici un exemple de l'interface utilisateur connecté, pour un Individual Order:

### Saisie des détails de livraison

![Saisie des détails de livraison](./images/connected-individual-order-delivery-details.png)

### Ajout d'un item au panier

![Ajout d'un item au panier](./images/connected-individual-order-cart-notification.png)

### Affichage du panier

![Affichage du panier](./images/connected-individual-order-cart-summary.png)

Voici un exemple de l'interface utilisateur connecté, pour un Group Order:

### Création d'une commande groupée

![Création d'une commande groupée](./images/connected-create-group-order.png)

### Affichage du code de la commande groupée

![Affichage du code de la commande groupée](./images/connected-share-group-order-code.png)

### Rejoindre une commande groupée

![Rejoindre une commande groupée](./images/connected-join-group-order.png)

### Affichage du panier pour un utilisateur au sein d'une commande groupée

![Affichage du panier pour un utilisateur au sein d'une commande groupée](./images/connected-group-order-cart-summary.png)

### Validation de la commande groupée, Affichage du récapitulatif de la commande de groupe

![Validation de la commande groupée, Affichage du récapitulatif de la commande de groupe](./images/connected-group-order-before-validation-summary.png)

## 2.7 Cheminement des requêtes

### 2.7.1 Récupération des restaurants

1. **Frontend** : Une requête HTTP GET est initiée vers la passerelle (route `/api/restaurants/all`).
2. **Gateway** : Transfère la requête au service `RestaurantsService`.
3. **RestaurantsService** :
    - Interroge la base de données pour récupérer les restaurants. (route `/api/database/restaurants/all`)
    - Retourne une liste formatée via un `RestaurantDTO`.
4. **Frontend** : Affiche les résultats dans une vue utilisateur.

### 2.7.2 Une étape de la prise de commande

1. **Frontend** : Envoie une requête POST pour créer un group order.
2. **Gateway** : Route la requête vers `GroupOrderService`.
3. **GroupOrderService** :
    - Valide les données reçues.
    - Persiste l’entité `GroupOrder` dans la base de données.
4. **Frontend** : Affiche un message de confirmation.

Un diagramme de séquence illustre ces flux (voir [Diagramme](#diagramme-sequence)).

## 2.8 Optimisations réalisées

### Côté Backend

- **FlexibleRestServer** :
    - Serveur REST personnalisable en Java.
    - Réduction des duplications grâce à l’utilisation d’annotations (@Controller, @Route).
    - Standardisation des réponses HTTP.
    - Gain de temps pour le développement de microservices.

### Côté Frontend

- **ViteJS** : Compilation rapide pour un feedback instantané.
- **React et TypeScript** : Meilleure lisibilité et typage fort.
- **TailwindCSS** : Productivité accrue pour le design.
- **NextUI** : Composants réutilisables pour une interface cohérente.

---

# 3. Qualité des codes et gestion de projets

## **Gestion du projet**

Pour garantir une organisation efficace et structurée, nous avons adopté plusieurs pratiques méthodologiques :

1. **Division des tâches par issues**
    - Chaque tâche était liée à une **issue clairement définie**.
    - Les **User Stories** ont été rédigées pour préciser les besoins et définir les critères d’acceptation.
    - Les issues ont été regroupées et planifiées dans des **milestones**, facilitant ainsi la priorisation des
      objectifs à atteindre.

2. **Organisation des branches**
    - Nous avons suivi un modèle de gestion des branches inspiré des bonnes pratiques, avec les branches principales :
        - `main` : branche de production stable.
        - `dev` : branche de développement intégrant les fonctionnalités validées.
        - Branches spécifiques pour chaque **feature**, nommées de manière explicite, par exemple :
          `feature/<numero-issue>-<nom_de_la_feature>`.
    
![Branching Model](./images/gitflow.png)

3. **Utilisation d’un tableau Kanban**
    - Le tableau Kanban a permis de visualiser l’état d’avancement des tâches en temps réel.
    - Les colonnes représentaient les différentes étapes : **À faire**, **En cours**, **En review**, et **Terminé**.
    - Chaque feature était associée à une issue pour une traçabilité optimale.

4. **Conventions de commit**

- Nous avons imposé une structure standardisée pour les messages de commit afin de garantir la cohérence et la
  traçabilité :
  ```  
  <type de commit>(<scope>): <objet du commit> #<numéro de l’issue>  
  ```  
    - Par exemple : `feat: ajout du formulaire de connexion #12`.

- Les types de commit utilisés incluaient :
    - **feat** : pour les nouvelles fonctionnalités.
    - **fix** : pour la correction de bugs.
    - **docs** : pour les modifications de la documentation.
    - **refactor** : pour les améliorations du code sans modification de fonctionnalité.
    - **style**, **test**, **chore**, **perf**, **ci**, **build**, et **revert** pour des cas spécifiques.

- Afin d'appliquer rigoureusement ces conventions, **nous avons mis en place un hook Git côté client** qui vérifie
  automatiquement chaque message de commit avant qu’il ne soit accepté. Ce hook, nommé `commit-msg`, s’assure que :
    1. Le message de commit respecte le format conventionnel.
    2. Un numéro d’issue est inclus dans le message pour assurer la traçabilité avec les issues du projet.

Voici le contenu du script `commit-msg` utilisé :

```bash  
#!/bin/sh  

# This script enforces conventional commits and checks for an issue number in commit messages.  

COMMIT_MSG_FILE=$1  

# Check for conventional commit format  
if ! grep -E -q '^(feat|fix|chore|refactor|docs|style|test|perf|ci|build|revert)(\([a-zA-Z0-9-]+\))?(!)?: .+' "$COMMIT_MSG_FILE"; then  
    echo "Error: Commit message does not follow conventional commit format."  
    echo "Please use the format: <type>(<scope>): <message>"  
    echo "The scope can only contain: [a-zA-Z], [0-9] and '-'"  
    exit 1  
fi  

# Check for the presence of an issue number  
if ! grep -E -q '#[0-9]+' "$COMMIT_MSG_FILE"; then  
    echo "Error: Commit message does not contain an issue number."  
    echo "Please include an issue number using the format: #[issue_number]"  
    exit 1  
fi  

# If all checks pass, the commit is allowed  
exit 0  
```  

- **Impact du hook** :
    - Grâce à ce hook, nous avons pu automatiser l’application des conventions de commit, réduisant ainsi les erreurs
      humaines.
    - Cela a également renforcé la cohérence dans les messages de commit et a facilité le suivi des tâches liées aux
      issues.

--- 

5. **Gestion des pull requests (PR)**
    - Chaque **pull request** devait être **reviewée par au moins un membre de l’équipe** avant de pouvoir être mergée
      dans la branche `dev`.
    - Cette étape comprenait :
        - Une **vérification manuelle** de la fonctionnalité.
        - Une validation des critères d’acceptation des US.
        - Une **review de code** pour garantir la qualité et la cohérence du code.

#### **Automatisation et assurance qualité**

- **Automatisation des tests** :
    - Nous avons mis en place une suite de tests automatisés (unitaires et/ou d’intégration) pour valider les
      fonctionnalités critiques.
- **Intégration continue (CI)** :
    - Un pipeline d’intégration continue a été configuré pour exécuter les tests à chaque push ou pull request,
      garantissant ainsi la stabilité des branches `dev` et `main`.

#### **Qualité du code**

1. **Respect des standards de codage** :
    - Nous avons adopté un linter pour assurer la cohérence et la lisibilité du code.
    - Les règles de codage étaient documentées et partagées avec l’équipe.

2. **Révision collaborative** :
    - La review des PR a permis de partager les connaissances entre membres, d’identifier d’éventuels bugs, et d’assurer
      une meilleure qualité globale du projet.

3. **Documentation technique** :
    - Une documentation claire accompagnait le code, facilitant sa compréhension et son maintien à long terme.

---

Ce texte synthétise vos efforts sur la gestion de projet et la qualité des codes tout en mettant en avant vos
méthodologies, outils, et pratiques pour assurer un travail collaboratif efficace et une livraison de qualité.

# 4. Rétrospective et Auto-évaluation

## **Product Owner (LACROIX Baptiste)**

J'ai joué un rôle clé dans le bon déroulement de notre projet en :

- **Créant les issues correspondant aux User Stories (US)** : Cela a permis de structurer clairement les tâches et
  d'assurer que chaque élément de la demande du client soit pris en compte.
- **Attribuant des rôles spécifiques à chaque membre de l’équipe** : Cette démarche a facilité la collaboration en
  assignant des responsabilités adaptées à chacun.
- **Élaborant un scénario complet intégrant toutes les US** : Ce scénario détaillé a servi de référence pour garantir
  que toutes les fonctionnalités demandées par le client soient correctement implémentées.

#### **Ce qui a bien été mené**

- Une **vision claire des User Stories** : Les US ont été bien définies, ce qui a permis de donner une direction
  cohérente au projet.
- La **collaboration au sein de l’équipe** : Les rôles assignés ont permis de maintenir une communication efficace entre
  les membres.
- La création d’un **scénario global** qui a guidé le développement.

#### **Leçons apprises et erreurs**

- **Attribution incorrecte des tâches** : Certaines tâches trop complexes ont été assignées à des membres ayant un
  niveau ou une expérience insuffisante.
- **Mauvaise gestion des compétences** : Ne pas avoir bien pris en compte les forces et faiblesses de chaque membre a
  ralenti certaines étapes du projet.

Ces erreurs m'ont permis de comprendre que :

- Une **bonne gestion de projet** repose sur des issues précises, bien découpées et adaptées à la capacité de l’équipe.
- Le Product Owner doit s’assurer de connaître les compétences de chaque membre pour attribuer les tâches de manière
  plus stratégique.

#### **Bilan d'équipe**

L’équipe a bien collaboré dans l’ensemble, malgré quelques ajustements nécessaires dans la gestion des responsabilités
et le découpage des tâches. La communication et la réactivité face aux imprévus ont été des points forts.

#### **Ce que nous aurions fait autrement**

- Découper les tâches encore plus précisément pour éviter de surcharger certains membres ou de les placer dans des
  situations difficiles.
- Instaurer des points réguliers pour mieux évaluer la progression et ajuster les assignations si nécessaire.

#### **Améliorations souhaitées**

- **Affiner la répartition des tâches** : Prendre en compte les niveaux et les compétences des membres de manière plus
  détaillée.
- **Améliorer la clarté des issues** : Les rendre plus complètes et détaillées pour minimiser les incompréhensions.
- **Mieux anticiper les besoins** : Planifier des sessions de formation ou d’accompagnement pour les membres ayant des
  lacunes sur certains aspects du projet.

---
