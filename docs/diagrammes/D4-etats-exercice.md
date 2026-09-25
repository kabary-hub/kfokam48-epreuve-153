# D4 — États-transitions du cycle de vie d’un exercice

```mermaid
stateDiagram-v2
    [*] --> EN_ATTENTE_SANS_RELECTEUR: dépôt / aucun candidat
    [*] --> EN_ATTENTE: dépôt / candidat assigné

    EN_ATTENTE_SANS_RELECTEUR --> EN_ATTENTE: nouvelle présence / candidat assigné
    EN_ATTENTE_SANS_RELECTEUR --> EN_ATTENTE_SANS_RELECTEUR: nouvelle présence / toujours aucun candidat

    EN_ATTENTE --> EN_COURS: action démarrer par le relecteur affecté
    EN_ATTENTE --> EN_ATTENTE: remplacer le lien / session non clôturée
    EN_COURS --> RELUE: rendre note entière 0..20 + commentaire
    RELUE --> RELUE: modifier note/commentaire / session non clôturée

    EN_ATTENTE_SANS_RELECTEUR --> SESSION_CLOTUREE: clôture
    EN_ATTENTE --> SESSION_CLOTUREE: clôture
    EN_COURS --> SESSION_CLOTUREE: clôture
    RELUE --> SESSION_CLOTUREE: clôture

    state SESSION_CLOTUREE {
        [*] --> VERROUILLE
    }
```

**Invariants :** un exercice n’a qu’un relecteur ; l’auteur ne peut pas être relecteur ; le remplacement du lien n’est permis qu’en attente ; modification et démarrage sont interdits après clôture. La clôture verrouille les mutations sans effacer l’état métier antérieur.
