# M9 — Consulter sa note et son commentaire

## Contexte
L'étudiant auteur doit consulter sa note, son commentaire et le statut de sa relecture sans révéler l'identité du relecteur.

## Critères d'acceptation
- [ ] L'auteur voit sa note et son commentaire.
- [ ] L'identité du relecteur est masquée.
- [ ] L'état de la relecture est visible.
- [ ] Une erreur claire est renvoyée si la relecture n'existe pas.

## Règles de gestion
- RG19 : L'identité du relecteur est masquée.
- RG20 : La réponse est limitée à l'auteur de l'exercice.

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
1. L'étudiant demande sa note.
2. Le serveur renvoie les données.
3. L'interface affiche la note.

## Scénarios alternatifs
- Aucune réaction.
- Session fermée.

## Postconditions
- Les données sont affichées.

## Critères d'acceptation
- [ ] Testez la correction, l'expiration et la réponse.
- [ ] Vérifiez le retour.

## Definition of Done
- Soumission complète, tests, documentation.
