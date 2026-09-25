# M7 — Modifier une relecture avant clôture

## Contexte
Un relecteur peut corriger sa note tant que la session est ouverte. Le service doit autoriser la modification sans altérer le statut existant.

## Critères d'acceptation
- [ ] La note est modifiée tant que la session est ouverte.
- [ ] Une tentative de modification après clôture est refusée.
- [ ] Le commentaire peut être mis à jour si autorisé.
- [ ] La réponse indique la réaction modifiée.

## Règles de gestion
- RG15 : Modification autorisée jusqu'à clôture de la session.
- RG16 : En cas de modification, l'état est conservé.

## Préconditions
- Session ouverte.
- L'utilisateur a le droit de modifier la réaction.

## Données d'entrée et contraintes
- id : identifiant de la relecture.
- note : entier de 0 à 20.
- commentaire : nouveau texte.

## Règles de validation transversales
- Validation day formats.
- Identifiants.
- Espace de noms pour les enregistrements.

## Scénario nominal
1. Le relecteur envoie une nouvelle note.
2. Le serveur valide et enregistre.
3. La réponse confirme.

## Scénarios alternatifs
- Session clôturée.
- Réaction introuvable.

## Postconditions
- La réaction est mise à jour.

## Critères d'acceptation
- [ ] Testez la correction, l'expiration et la réponse.
- [ ] Vérifiez le retour.

## Definition of Done
- Soumission complète, tests, documentation.
