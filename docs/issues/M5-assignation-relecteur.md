# M5 — Assigner un relecteur

## Contexte
Lors du dépôt, le système doit attribuer automatiquement un relecteur parmi les étudiants présents. Si aucun candidat n'est disponible, l'exercice reste en attente.

## Critères d'acceptation
- [ ] Le relecteur est sélectionné au hasard parmi les étudiants présents.
- [ ] L'auteur du dépôt n'est jamais son propre relecteur.
- [ ] Chaque relecteur ne peut recevoir qu'un exercice par session.
- [ ] L'exercice en attente reste visible si aucun candidat.

## Règles de gestion
- RG9 : Un seul relecteur par exercice.
- RG10 : Le relecteur est tiré au hasard parmi les étudiants présents non auteurs.
- RG11 : L'attribution est visible dans le tableau.

## Préconditions
- Session ouverte.
- Au moins un étudiant présent.
- Exercice déposé.

## Données d'entrée et contraintes
- sessionId : session liée.
- etudiantId : auteur de l'exercice.

## Règles de validation transversales
- Sécurité des identifiants.
- Règles métiers vérifiées.

## Scénario nominal
1. L'exercice est déposé.
2. Le système sélectionne un relecteur.
3. La réponse indique la réassignation ou l'attente.

## Scénarios alternatifs
- Aucun candidat.
- Dépôt après échéance.

## Postconditions
- La réaffectation ou l'attente est enregistrée.

## Critères d'acceptation
- [ ] Testez la correction, l'expiration et la réponse.
- [ ] Vérifiez l'impossibilité d'assigner un auteur.

## Definition of Done
- Soumission complète, tests, documentation.
