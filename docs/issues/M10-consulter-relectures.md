# M10 — Consulter les relectures affectées

## Contexte
Un étudiant auteur doit voir les relectures qui lui ont été faites, avec leurs statuts, sans identifier le relecteur.

## Critères d'acceptation
- [ ] La liste est filtrée par l'étudiant concerné.
- [ ] Chaque élément montre le statut et les données utiles.
- [ ] Aucun identifiant de relecteur n'est renvoyé.
- [ ] Les erreurs sont gérées.

## Règles de gestion
- RG20 : L'information d'identité du relecteur est masquée.
- RG21 : Les données sont accessibles uniquement à l'auteur.

## Préconditions
- Données d'exercice et d'étudiant.
- Accès autorisé.

## Données d'entrée et contraintes
- id : identifiant de l'exercice.
- etudiantId : identifiant de l'auteur.

## Règles de validation transversales
- Sécurité des identifiants.
- Règles métiers.

## Scénario nominal
1. L'étudiant demande ses relectures.
2. Le serveur renvoie le résultat.
3. L'interface affiche la liste.

## Scénarios alternatifs
- Aucune réaction.
- Session fermée.

## Postconditions
- La liste est affichée.

## Critères d'acceptation
- [ ] Testez la correction, l'expiration et la réponse.
- [ ] Vérifiez le retour.

## Definition of Done
- Soumission complète, tests, documentation.
