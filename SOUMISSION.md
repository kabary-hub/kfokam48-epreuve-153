# SOUMISSION — PrésenceKF

## Candidat

- **Nom :** BOUBACAR SIDDIGHI BALDE
- **Matricule :** kf48-153
- **Campus :** Yaoundé
- **Dépôt GitHub :** <https://github.com/kabary-hub/kfokam48-epreuve-153>

## Résumé

Application full-stack (Spring Boot + React) pour la gestion des présences d'étudiants par session, le dépôt des liens d'exercices et leur relecture croisée par les pairs. L'application met en œuvre un système de présence par code (15 minutes de validité), un tableau de bord par promotion, et un cycle de relecture croisée avec clôture automatique.

## Architecture

```
PrésenceKF
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
| Base de données | PostgreSQL | Stockage relationnel |
| Migrations | Flyway | Schéma versionné |
| Frontend | React + TypeScript + Vite | Frontend utilisateur |
| Conteneurs | Docker et Docker Compose | Environnement reproductible |

## Objectifs fonctionnels couverts

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

## Documentation

- Cahier des charges : `docs/CAHIER_DES_CHARGES.md`
- Contrat API : `api/contrat.yaml`
- Diagrammes : `docs/diagrammes/`
- Journal d'analyse : `docs/JOURNAL.md`
- README : `README.md`

## Lancer le projet

### Avec Docker Compose

```bash
docker-compose up --build
```

### Manuellement

```bash
# Backend
cd backend
mvn spring-boot:run

# Frontend
cd frontend
npm install
npm run dev
```

## Évolutions futures

- Authentification et autorisation des rôles (formateur, étudiant, relecteur)
- Parcours de clôture assisté
- Export CSV/PDF
- Notifications
