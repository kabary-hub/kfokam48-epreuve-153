# PresenceKF — Présence et relecture croisée

Application full-stack destinée à gérer les présences d'étudiants par session, le dépôt des liens d'exercices et leur relecture par les pairs.

## Candidat et dépôt

- Auteur du projet : **BOUBACAR SIDDIGHI BALDE**
- Matricule communiqué : **kf48-153**
- Dépôt : <https://github.com/kabary-hub/kfokam48-epreuve-153>

## Objectifs fonctionnels

- Ouvrir une session et produire un code de présence.
- Marquer la présence par code pendant la période de validité.
- Déposer un lien d'exercice et permettre sa relecture croisée.
- Affecter un étudiant présent à chaque exercice, en respectant les règles de capacité et d'anonymat.
- Faire respecter une échéance de session et les règles de clôture.

## Architecture

```
PresenceKF
├── backend/                    # API Spring Boot (Java 17, Maven)
│   ├── pom.xml
│   └── src/main/java/com/presencekf/backend/
│       ├── controller/         # Contrats HTTP (REST)
│       ├── service/            # Logique métier
│       ├── entity/             # Modèles JPA
│       ├── repository/         # Accès données (Spring Data JPA)
│       ├── dto/                # Objets de transfert
│       └── config/
└── frontend/                   # Interface React + TypeScript + Vite
    └── src/
        ├── components/         # Composants React
        ├── services/           # Appels API
        └── hooks/
```

## Technologies

| Partie | Technologie | Usage |
|---|---|---|
| Backend | Java 17+, Spring Boot, Maven | API REST et règles métier |
| Architecture backend | MVC en couches | Séparer transport HTTP, métier et persistance |
| Base de données | PostgreSQL | Stockage relationnel |
| Migrations | Flyway | Schéma versionné |
| Frontend | React + TypeScript + Vite | Trois écrans : formateur, étudiant, relecteur |
| Conteneurs | Docker et Docker Compose | Environnement reproductible |
| Contrat | OpenAPI 3.0, `api/contrat.yaml` | Référence partagée |

## État de l'implémentation

| Ticket | Titre | Branch | PR |
|---|---|---|---|
| M1 | Ouvrir et clôturer une session | `ticket/M1-ouvrir-session` | #28 |
| M2 | Marquer la présence avec un code | `ticket/M2-marquage-presence` | #29 |
| M3 | Ajouter une présence manuellement | `ticket/M3-presence-manuelle` | #30 |
| M4 | Déposer le lien d'un exercice | `ticket/M4-depot-exercice` | #31 |
| M5 | Affecter un relecteur | `ticket/M5-affectation-relecteur` | #32 |
| M6 | Démarrer et rendre une relecture | `ticket/M6-debut-rendre-relecture` | #33 |
| M7 | Modifier une relecture avant clôture | `ticket/M7-modification-note` | #34 |
| M8 | Consulter le tableau et les exercices | `ticket/M8-tableau` | #35 |
| M9 | Consulter sa note et son commentaire | `ticket/M9-consultation-note` | #36 |
| M10 | Consulter les relectures affectées | `ticket/M10-consulter-relectures` | #37 |
| M11 | Collection de règles de gestion | `ticket/M11-regles-gestion` | #38 |
| M12 | Schéma de données et démonstration | `ticket/M12-schema-donnees` | #39 |
| M13 | Auto-clôture de session | `ticket/M13-auto-cloture` | #40 |

## Lancer le projet

### Avec Docker Compose

```bash
docker-compose up --build
```

Cela démarre :
- `backend` (Spring Boot, port 8080)
- `frontend` (Vite, port 5173)
- `postgres` (base de données)

### Manuellement

#### Backend

```bash
cd backend
mvn spring-boot:run
```

#### Frontend

```bash
cd frontend
npm install
npm run dev
```

## Documentation

- Contrat API : `api/contrat.yaml`
- Cahier des charges : `docs/CAHIER_DES_CHARGES.md`
- Diagrammes : `docs/diagrammes/`
- Journal d'analyse : `docs/JOURNAL.md`

## Évolutions possibles

- Authentification et autorisation des rôles
- Parcours de clôture assisté
- Export CSV/PDF
- Notifications
