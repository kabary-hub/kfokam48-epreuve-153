# CHANGELOG — PresenceKF

Toutes les modifications notables du projet, alignées sur l'historique Git.

## [1.0.0] — Version finale (30 septembre 2026)

### Must (8 tickets)
- **M1** — Ouvrir une session et obtenir un code de présence (EF2, RG1)
- **M2** — Marquer la présence avec un code (EF1, RG1, RG3, RG15)
- **M3** — Ajouter une présence manuellement (EF3, RG13, Q14)
- **M4** — Déposer le lien d'un exercice (EF4, RG16)
- **M5** — Assigner un relecteur automatiquement (EF6, RG4, RG5, RG6, RG17)
- **M6** — Rendre une relecture (EF7, RG4, RG8, RG9)
- **M7** — Consulter le tableau d'une promotion (EF9, Q16)
- **M8** — Clôturer une session manuellement (EF11, RG11, RG14)

### Conduite du changement (étape 3)
- **Bug #52** — Transaction et capture de DataIntegrityViolationException
  dans PresenceService (vulnérabilité identifiée, symptôme non reproduit).
- **Changement #53** — Double relecture : 2 relecteurs par exercice,
  note = moyenne, provisoire si 1 seule relecture. Migration V2.

### Sacrifice de périmètre
- Les 5 tickets Should (S1-S5) sont sacrifiés pour absorber le
  changement #53. Documenté dans docs/JOURNAL.md (étape 3).

### Non livré
- S1 — Remplacer le lien d'un exercice
- S2 — Modifier sa relecture avant clôture
- S3 — Consulter sa note et son commentaire
- S4 — Lister les étudiants d'une promotion
- S5 — Lister les sessions d'une promotion

## [0.1.0] — Première version (28 septembre 2026)

- Mise en place du backend Spring Boot, frontend React + Vite.
- Analyse complète : CDC (10 sections), 4 diagrammes Mermaid, contrat
  api/contrat.yaml, backlog 13 tickets, journal étape 1.
- Jalons Git : `[JALON] analyse`, `[JALON] v0.1`.
