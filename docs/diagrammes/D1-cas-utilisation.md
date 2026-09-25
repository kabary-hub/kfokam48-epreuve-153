# D1 — Diagramme de cas d’utilisation

```mermaid
flowchart LR
    F[Formateur]
    E[Étudiant]
    R[Relecteur]
    SYS((Système PresenceKF))

    F --> UC1([Ouvrir une session])
    F --> UC2([Paramétrer la durée de la prochaine session])
    F --> UC3([Clôturer manuellement si nécessaire])
    F --> UC4([Consulter le tableau de promotion])

    E --> UC5([Choisir son identité dans la liste])
    E --> UC6([Marquer sa présence avec un code])
    E --> UC7([Déposer un lien d'exercice])
    E --> UC8([Consulter sa note et son commentaire])

    R --> UC9([Consulter ses affectations anonymisées])
    R --> UC10([Démarrer une relecture])
    R --> UC11([Rendre une note et un commentaire])
    R --> UC12([Modifier la note selon l'échéance])

    UC6 -. vérifie .-> UC13([Valider code et fenêtre de 15 min])
    UC7 -. déclenche .-> UC14([Assigner un pair présent admissible])
    UC14 -. absence de candidat .-> UC15([Mettre en attente sans relecteur])
    UC10 -. verrouille le remplacement .-> UC16([Exercice en cours de relecture])
    UC12 -. bloque après clôture .-> UC17([Opération refusée])
```

**Note métier :** le rôle de formateur inclut la modification de la durée globale, mais pas la clôture manuelle par défaut. La relecture est appliquée par l’étudiant affecté et anonymisé, sans que l’auteur soit visible du relecteur. Les étudiants ne peuvent relire que leur exercice, jamais le leur.
