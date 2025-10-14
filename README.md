# GestionSpectacle (Back-end)

Service back-end développé en **Spring Boot** pour la gestion de spectacles, réservations et envoi d'e-mails de confirmation.

## À propos
Cette application fournit des **API REST** pour :
- Consulter les spectacles et leurs dates/lieux.
- Créer, modifier et annuler des réservations (avec ou sans compte utilisateur).
- Envoyer des e-mails de confirmation aux clients.

Le projet est conçu pour être intégré avec un front-end ou utilisé comme microservice autonome.

Classe principale : [`GestionSpectacleApplication`](src/main/java/tn/enicarthage/gestionspectacle/GestionSpectacleApplication.java)

---

## Fonctionnalités principales
- Gestion des spectacles et de leurs lieux
- Gestion des utilisateurs et authentification JWT
- Réservations avec validation et notification par email
- Templates d'e-mails personnalisables
- Sécurisation des endpoints via JWT
- Base de données MySQL pour persistance

---

## Structure du projet
- **Controllers** :  
  - `SpectacleController`, `ReservationController`, `UtilisateurController`, `LieuController`, `AuthenticationController`, `EmailController`  
- **DTOs** :  
  - `ReservationDTO`, `SpectacleDateLieuDTO`, `LieuDTO`  
- **Templates emails** : `src/main/resources/templates/email-reservation.html`  
- **Configuration** : `src/main/resources/application.properties`  

---

## Prérequis
- Java 17
- Maven (ou le wrapper fourni)
- Base de données MySQL
- IDE compatible Spring Boot (IntelliJ, VSCode, Eclipse)

---

## Configuration rapide
Les paramètres principaux sont dans `src/main/resources/application.properties` :
- `server.port` — port de l'application (ex. 8081)
- `spring.datasource.*` — URL, utilisateur, mot de passe MySQL
- `jwt.secret` — clé secrète JWT
- `spring.mail.*` — SMTP pour l'envoi d'e-mails
