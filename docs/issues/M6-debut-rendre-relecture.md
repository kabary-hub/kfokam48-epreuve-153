# M6 — Démarrer et rendre une relecture

## Contexte
Le relecteur assigné doit pouvoir démarrer une relecture et y rendre une note et un commentaire. Le système doit bloquer les lectures inchangées ou invalides.

## Critères d'acceptation
- [ ] Une note entre 0 et 20 est acceptée.
- [ ] Une note hors bornes est refusée.
- [ ] L'auto-relecture est refusée.
- [ ] Une relecture déjà rendue est refusée.
- [ ] La réponse indique le statut de la relecture.

## Règles de gestion
- RG12 : La note est un entier de 0 à 20.
- RG13 : Un relecteur ne peut relire son propre exercice.
- RG14 : Une relecture doit être démarrée avant de l'envoyer.

## Préconditions
- Session ouverte.
- Le relecteur est affecté à cet exercice.

## Données d'entrée et contraintes
- id : identifiant de la relecture.
- note : entier de 0 à 20.
- commentaire : texte.

## Règles de validation transversales
- Fonction sur les types.
- Choix de format acceptés.
- Dates vérifiées.

## Scénario nominal
1. Le relecteur rend une note et un commentaire.
2. Le système valide et enregistre.
3. La réponse confirme la réaction.

## Scénarios alternatifs
- Note invalide.
- Réaction déjà faite.

## Postconditions
- La relecture est enregistrée.

## Critères d'acceptation
- [ ] Testez la correction, l'expiration et la réponse.
- [ ] Vérifiez le retour.

## Definition of Done
- Soumission complète, tests, documentation.
