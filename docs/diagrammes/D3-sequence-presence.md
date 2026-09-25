# D3 — Séquence : marquer sa présence

```mermaid
sequenceDiagram
    actor E as Étudiant
    participant UI as Frontend React
    participant API as API Spring
    participant S as PresenceService
    participant DB as PostgreSQL

    E->>UI: choisit son identité et saisit le code
    UI->>API: POST /api/presences\n(code, etudiantId) + headers navigateur
    API->>S: enregistrerPresence(code, etudiantId, navigateurId)
    S->>DB: rechercher session du code
    alt code inconnu
        DB-->>S: aucune session
        S-->>API: CodeInconnu
        API-->>UI: 400 {code: CODE_INCONNU, message}
    else session trouvée, code expiré
        DB-->>S: session.expirationAt <= maintenant
        S-->>API: CodeExpire
        API-->>UI: 410 {code: CODE_EXPIRE, message}
    else étudiant déjà présent
        S->>DB: vérifier unicité sessionId + etudiantId
        DB-->>S: présence existante
        S-->>API: DejaPresent
        API-->>UI: 409 {code: DEJA_PRESENT, message}
    else cinquième erreur de saisie dans la session navigateur
        S->>S: incrémenter le compteur de la session navigateur
        alt 5e erreur puis encore erreur
            S-->>API: TentativesBloquees
            API-->>UI: 400 {code: TROP_TENTATIVES, message}
        else 5e erreur et demande suivante valide
            S->>DB: réinitialiser la session navigateur
            S->>API: validation normale
        end
    else cas nominal
        S->>DB: insérer présence(source=ETUDIANT)
        DB-->>S: présence créée
        S-->>API: présence DTO
        API-->>UI: 201 {id, sessionId, etudiantId, source}
        UI-->>E: confirme la présence
    end
```

## Conformité du contrat

- Succès : `201`.
- Erreurs contractuelles : code inconnu `400`, déjà présent `409`, code expiré `410`.
- L’extension du candidat utilise toujours `400` pour la cinquième tentative, avec `code: TROP_TENTATIVES`.
- Le schéma d’erreur, `{ "code": "…" , "message": "…" }`, est imposé pour toutes les erreurs, sans exception, comme indiqué dans le sujet.

## Portée du compteur Q4

La décision du candidat est explicite : toute erreur serving (code inconnu, code expiré, présence déjà enregistrée) incrémente le compteur. Une fois la cinquième erreur atteinte, le reste des tentatives de cette session navigateur est bloqué deux minutes.
