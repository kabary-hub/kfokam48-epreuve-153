# M4 — Déposer le lien d'un exercice

## Contexte
Un étudiant soumet un lien vers son exercice pour une session. Le lien doit être validé, stocké et visible dans le tableau de suivi.

## Critères d'acceptation
- [ ] Lien invalide est refusé.
- [ ] Un exercice déjà déposé est refusé avec une erreur claire.
- [ ] L'exercice est rattaché à la session et à l'étudiant.
- [ ] Le lien est stocké sans autre traitement métier.

## Règles de gestion
- RG7 : Un étudiant ne peut déposer qu'un seul exercice par session.
- RG8 : Le lien est stocké tel quel, sans modification.

## Préconditions
- Session ouverte.
- Étudiant présent.

## Données d'entrée et contraintes
- sessionId : session de dépôt.
- etudiantId : étudiant responsable.
- lien : URL valide et accessible en lecture.

## Règles de validation transversales
- Requête JSON correcte.
- Champs requis.
- Lien doit respecter l'URI attendu.

## Scénario nominal
1. L'étudiant soumet une demande avec un lien.
2. Le serveur valide.
3. La réponse indique l'exercice créé.

## Scénarios alternatifs
- Lien invalide.
- Exercice déjà présent.
- Session fermée.

## Postconditions
- L'exercice est visible dans le tableau.

## Critères d'acceptation
- [ ] Testez la correction, l'expiration et la réponse.
- [ ] Vérifiez que le lien est bien stocké.

## Definition of Done
- Soumission complète, tests, documentation.
