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

### User Story Reference

**User Story ID**: [#25](https://github.com/PNS-Conception/STE-24-25--teamj/issues/25)  
**Title**: Validation of group orders by members

**Description**:  
As a registered user, I want to validate the group order at the end of my individual order and, if necessary, specify a compatible delivery time so that the group order can be finalized and closed.

**Feature Demonstration**:  
The behavior of this user story is demonstrated in [ValidateGroupOrder.feature](./src/test/resources/features/orders/ValidateGroupOrder.feature). The scenarios include validation of a group order, setting delivery times, and handling incompatible delivery times.


<!-- ## Ce que fait votre projet


### Principales User stories
Vous mettez en évidence les principales user stories de votre projet.
Chaque user story doit être décrite par 
   - son identifiant en tant que issue github (#), 
   - sa forme classique (As a… I want to… In order to…) (pour faciliter la lecture)
   - Le nom du fichier feature Cucumber et le nom des scénarios qui servent de tests d’acceptation pour la story.
   Les contenus détaillés sont dans l'issue elle-même. -->
   

   
