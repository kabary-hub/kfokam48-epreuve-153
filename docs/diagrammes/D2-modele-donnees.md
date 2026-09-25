# D2 — Modèle de données

> Modèle conceptuel de la première version. Il sert de référence aux migrations. Tout changement impose une nouvelle migration versionnée ; il n'est jamais fait de réécriture d'une migration déjà partagée.

```mermaid
erDiagram
    PROMOTION ||--o{ ETUDIANT : regroupe
    PROMOTION ||--o{ SESSION : accueille
    FORMATEUR ||--o{ SESSION : ouvre
    FORMATEUR ||--o{ CONFIGURATION_GLOBALE : règle
    SESSION ||--o{ PRESENCE : enregistre
    ETUDIANT ||--o{ PRESENCE : possede
    SESSION ||--o{ EXERCICE : contient
    ETUDIANT ||--o{ EXERCICE : depose
    EXERCICE ||--o| RELECTURE : recoit
    ETUDIANT ||--o{ RELECTURE : est_relecteur

    PROMOTION {
        bigint id PK
        string nom
    }
    FORMATEUR {
        bigint id PK
        string nom
    }
    CONFIGURATION_GLOBALE {
        bigint id PK
        int duree_minutes
        datetime modifiee_at
    }
    ETUDIANT {
        bigint id PK
        bigint promotion_id FK
        string nom
    }
    SESSION {
        bigint id PK
        bigint promotion_id FK
        bigint formateur_id FK
        string titre
        string code UK
        datetime ouverture_at
        datetime expiration_at
        int duree_minutes
        datetime cloture_prevue_at
        datetime cloture_at "nullable"
        string statut
    }
    PRESENCE {
        bigint id PK
        bigint session_id FK
        bigint etudiant_id FK
        datetime creee_at
        string source
    }
    EXERCICE {
        bigint id PK
        bigint session_id FK
        bigint etudiant_id FK
        string lien
        datetime depose_at
        string statut
    }
    RELECTURE {
        bigint id PK
        bigint exercice_id FK_UK
        bigint relecteur_id FK
        datetime demarree_at "nullable"
        datetime rendue_at "nullable"
        int note "nullable, 0..20"
        string commentaire "nullable avant rendu"
    }
```

## Contraintes relationnelles

- `CONFIGURATION_GLOBALE` : une ligne, valeur dans `15..480`. La configuration n'a pas de valeur par défaut. Quand aucun réglage n'existe, une ouverture de session refuse avec `409 DUREE_SESSION_REQUISE`.
- `PRESENCE` : unicité (`session_id`, `etudiant_id`), `source` limité à `ETUDIANT` ou `FORMATEUR`. La présence manuelle doit être traitée dans la fenêtre de 15 minutes du code.
- `EXERCICE` : unicité (`session_id`, `etudiant_id`).
- `RELECTURE` : une ligne unique par exercice, contenant l'affectation du relecteur. L'auteur ne peut pas être son propre relecteur, ni un étudiant être assigné à un autre exercice de la session.
- `SESSION` : `expiration_at = ouverture_at + 15 minutes`; `cloture_prevue_at = ouverture_at + duree_minutes`, avec `15 <= duree_minutes <= 480`. L'échéance est figée à l'ouverture ; la clôture automatique peut être rattrapée par le service après un arrêt serveur.
- Les présences et les dépôts doivent respecter l'état `OUVERTE` de la session ; après clôture, toute modification est refusée sans modifier l'état métier.
- Les relectures anonymisées ne renvoient ni le nom ni l'identifiant de l'auteur ; l'auteur ne voit jamais l'identité du relecteur.
- La modification de note (route `PUT`) s'autorise uniquement sur la note avant échéance ; le commentaire reste celui du premier envoi.
