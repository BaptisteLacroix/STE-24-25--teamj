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

## Project Management

We have used GitHub Projects to manage our project. You can find the project
board [here](https://github.com/orgs/PNS-Conception/projects/69).
To ensure consistency in our commit messages, we follow the Conventional Commits standard.
We use pre-commit hooks to enforce this convention.
To enable the hooks, the developer must copy the files from the [hooks](./hooks) directory to the `.git/hooks`
directory.

### Hooks

- **commit-msg**:  
  This hook checks if the commit message follows the Conventional Commits standard.

```shell
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


### Branch Naming Convention

- **Feature Branches**:  
  `feature/<usecase-number>-<short-description>`  
  Example: `feature/R2-group-orderDTO-validation`
- **Bugfix Branches**:  
  `bugfix/<issue-number>-<short-description>`  
  Example: `bugfix/42-fix-group-orderDTO-validation`

### Commit Message Convention

- **Commit Message Format**:  
  `<type>(<scope>): <subject> #<issue-number>`  
  Example: `feat(orders): add group orderDTO validation feature. #4`

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
    - **Order/**: Manages orderDTO processing, including orderDTO creation, updates, and tracking.
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
As a registered user, I want to validate the group orderDTO at the end of my individual orderDTO and, if necessary, specify a
compatible delivery time so that the group orderDTO can be finalized and closed.

**Feature Demonstration**:  
The behavior of this user story is demonstrated
in [ValidateGroupOrder.feature](./src/test/resources/features/orders/ValidateGroupOrder.feature). The scenarios include
validation of a group orderDTO, setting delivery times, and handling incompatible delivery times.


<!-- ## Ce que fait votre projet


### Principales User stories
Vous mettez en évidence les principales user stories de votre projet.
Chaque user story doit être décrite par 
   - son identifiant en tant que issue github (#), 
   - sa forme classique (As a… I want to… In orderDTO to…) (pour faciliter la lecture)
   - Le nom du fichier feature Cucumber et le nom des scénarios qui servent de tests d’acceptation pour la story.
   Les contenus détaillés sont dans l'issue elle-même. -->
   

   
