# D2 — Modèle de données

**Entités** : Promotion, Etudiant, Formateur, Session, Presence, Exercice, Relecture.

**Cohérence** : ce diagramme définit les tables visées par la migration Flyway `V1__init.sql`. Chaque classe correspond à une table et chaque attribut à une colonne.

```mermaid
classDiagram
    class Promotion {
        +Long id PK
        +String nom
    }

    class Etudiant {
        +Long id PK
        +String nom
        +Long promotionId FK
    }

    class Formateur {
        +Long id PK
        +String nom
    }

    class Session {
        +Long id PK
        +String titre
        +String code
        +LocalDateTime ouvertureAt
        +LocalDateTime expirationAt
        +LocalDateTime clotureAt
        +Long promotionId FK
        +Long formateurId FK
    }

    class Presence {
        +Long id PK
        +Long sessionId FK
        +Long etudiantId FK
        +String source
        +LocalDateTime marqueAt
    }

    class Exercice {
        +Long id PK
        +Long sessionId FK
        +Long etudiantId FK
        +String lien
        +String statut
        +Long relecteurId FK nullable
        +LocalDateTime deposeAt
    }

    class Relecture {
        +Long id PK
        +Long exerciceId FK
        +Long relecteurId FK
        +Integer note
        +String commentaire
        +String statut
        +LocalDateTime rendueAt
    }

    Promotion "1" --> "*" Etudiant : contient
    Promotion "1" --> "*" Session : organise
    Formateur "1" --> "*" Session : ouvre
    Session "1" --> "*" Presence : enregistre
    Session "1" --> "*" Exercice : recoit
    Etudiant "1" --> "*" Presence : marque
    Etudiant "1" --> "*" Exercice : depose
    Etudiant "1" --> "*" Relecture : rend
    Exercice "1" --> "0..1" Relecture : est_relu_par
```

`Exercice.relecteurId` est nullable : si aucun étudiant présent admissible n'est disponible pour relire l'exercice, il n'y a pas encore de relecteur affecté et l'exercice reste visible avec le statut `EN_ATTENTE_SANS_RELECTEUR` (Q7, Q11, Q12).
