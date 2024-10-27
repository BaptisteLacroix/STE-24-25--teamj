# SopiaTech Eats-Team-24-25

---

## Team Members:

- [Baptiste Lacroix](https://github.com/BaptisteLacroix) - PO
- [Tom Toupence](https://github.com/tom-toupence) - QA
- [Antoine Maïstre-Rice](https://github.com/Antoine-MR) - AL
- [Abderrahmen Tlili ](https://github.com/AbdouTlili) - OPS

---

## Doc

Rapport : [TeamJ-renduD1.pdf](./doc/TeamJ-renduD1.pdf)<br>
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
  Example: `feature/R2-group-order-validation`
- **Bugfix Branches**:  
  `bugfix/<issue-number>-<short-description>`  
  Example: `bugfix/42-fix-group-order-validation`

### Commit Message Convention

- **Commit Message Format**:  
  `<type>(<scope>): <subject> #<issue-number>`  
  Example: `feat(orders): add group order validation feature. #4`

---

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
   

   
