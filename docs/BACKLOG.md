# Backlog PresenceKF — Épreuve finale KFOKAM48

13 tickets répartis en 8 Must et 5 Should. Chaque ticket correspond à une issue GitHub avec critères d'acceptation vérifiables, priorité et références EFx/RGx.

## Must — livrés en v0.1 (jalon [JALON] v0.1)

### M1 — Ouvrir une session et obtenir un code de présence
- Références : EF2, RG1
- Priorité : Must
- Critères d'acceptation :
  - [ ] Quand je POST /api/sessions avec {titre, promotionId}, je reçois 201 {id, code, ouvertureAt, expirationAt}.
  - [ ] Le code est à 6 caractères alphanumériques (pattern ^[A-Z0-9]{6}$).
  - [ ] expirationAt = ouvertureAt + 15 minutes (RG1).
  - [ ] Si titre ou promotionId manque → 400 {code, message}.

### M2 — Marquer sa présence avec un code
- Références : EF1, RG1, RG2, RG3, RG15
- Priorité : Must
- Critères d'acceptation :
  - [ ] Quand je POST /api/presences avec {code, etudiantId}, je reçois 201 {id, sessionId, etudiantId, source: "ETUDIANT"}.
  - [ ] Code inconnu → 400 CODE_INCONNU.
  - [ ] Code expiré → 410 CODE_EXPIRE.
  - [ ] Étudiant déjà présent → 409 DEJA_PRESENT.
  - [ ] Après 5 erreurs consécutives → blocage 2 min (RG3).

### M3 — Ajouter une présence manuellement (formateur)
- Références : EF3, RG13
- Priorité : Must
- Critères d'acceptation :
  - [ ] Quand je POST /api/presences/formateur, la présence est créée avec source = "FORMATEUR".
  - [ ] La présence apparaît dans le tableau avec la distinction FORMATEUR.

### M4 — Déposer un exercice
- Références : EF4, RG16
- Priorité : Must
- Critères d'acceptation :
  - [ ] Quand je POST /api/exercices avec {sessionId, etudiantId, lien}, je reçois 201 {id, statut}.
  - [ ] Le statut retourné après la tentative d'assignation vaut EN_ATTENTE si un relecteur est trouvé, ou EN_ATTENTE_SANS_RELECTEUR si aucun candidat n'est disponible ; DEPOSE est un état transitoire non renvoyé au client.
  - [ ] Lien invalide → 400 LIEN_INVALIDE.
  - [ ] Déjà déposé pour cette session → 409 EXERCICE_DEJA_DEPOSE.

### M5 — Assigner un relecteur automatiquement
- Références : EF6, RG4, RG5, RG6
- Priorité : Must
- Critères d'acceptation :
  - [ ] L'assignation est tentée pendant le POST /api/exercices, parmi les étudiants présents à la session, hors auteur.
  - [ ] Un étudiant ne peut être relecteur que d'un seul exercice par session.
  - [ ] Si un candidat est disponible, le statut retourné est EN_ATTENTE ; sinon, relecteurId = null et le statut retourné est EN_ATTENTE_SANS_RELECTEUR.

### M6 — Rendre une relecture
- Références : EF7, RG8, RG9
- Priorité : Must
- Critères d'acceptation :
  - [ ] Quand je POST /api/relectures/{id} avec {note, commentaire}, je reçois 200.
  - [ ] Note entière de 0 à 20 → sinon 400 NOTE_INVALIDE.
  - [ ] Auto-relecture → 403 AUTO_RELECTURE.
  - [ ] Relecture déjà rendue → 409 RELECTURE_DEJA_RENDUE.

### M7 — Consulter le tableau d'une promotion
- Références : EF9, Q16
- Priorité : Must
- Critères d'acceptation :
  - [ ] Quand je GET /api/tableau?promotionId=X, je reçois 200 avec un tableau [{etudiantId, nom, presences, exercicesDeposes, moyenne, relecturesEnAttente}].
  - [ ] promotionId inconnu → 404 PROMOTION_INCONNUE.
  - [ ] La moyenne est calculée côté API (jamais côté frontend).

### M8 — Clôturer une session manuellement
- Références : EF11, RG11, RG14
- Priorité : Must
- Critères d'acceptation :
  - [ ] Quand je POST /api/sessions/{id}/cloture, la session passe à CLOTUREE et clotureAt est rempli.
  - [ ] Après clôture, tout dépôt ou modification de relecture est refusé.
  - [ ] Les relectures rendues deviennent définitives (RG14).

## Should — livrés en v1.0

### S1 — Remplacer le lien d'un exercice
- Références : EF5, RG12
- Priorité : Should
- Critères d'acceptation :
  - [ ] Quand je PUT /api/exercices/{id} avec un nouveau lien, il est remplacé si la relecture n'a pas commencé.
  - [ ] Après relecture (statut RELUE), le remplacement est refusé.

### S2 — Modifier sa relecture avant clôture
- Références : EF8, RG9, Q10 > Q15
- Priorité : Should
- Critères d'acceptation :
  - [ ] Quand je PUT /api/relectures/{id} avec une nouvelle note, elle remplace l'ancienne tant que la session n'est pas clôturée.
  - [ ] Le commentaire initial reste immuable.

### S3 — Consulter sa note et son commentaire
- Références : EF10, RG7, Q8
- Priorité : Should
- Critères d'acceptation :
  - [ ] Quand je GET /api/exercices/{id}, je vois ma note et mon commentaire si l'exercice est RELUE.
  - [ ] Le nom et l'id du relecteur ne sont jamais renvoyés.

### S4 — Lister les étudiants d'une promotion
- Références : Q1 (sélecteur)
- Priorité : Should
- Critères d'acceptation :
  - [ ] Quand je GET /api/etudiants?promotionId=X, je reçois la liste [{id, nom}] pour le sélecteur frontend.
  - [ ] Aucune authentification (Q1).

### S5 — Lister les sessions d'une promotion
- Références : EF9, Q16
- Priorité : Should
- Critères d'acceptation :
  - [ ] Quand je GET /api/sessions?promotionId=X, je reçois la liste des sessions avec leurs statuts (OUVERTE, CLOTUREE).

## Could / Won't

- Could : historique des relectures, notifications email/push, import CSV.
- Won't : authentification, CRUD promotions/étudiants/formateurs, upload de fichiers, application mobile native, statistiques avancées.

## Politique Git

- Une branche par ticket : feat/Mx-<slug> ou fix/RGx-<slug>.
- Une PR par branche, liée à l'issue.
- Closes #N dans le commit final de la PR.
- main toujours sain après chaque merge.
