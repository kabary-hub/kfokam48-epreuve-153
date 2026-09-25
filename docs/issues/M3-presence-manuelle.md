# M3 — Ajouter une présence manuellement

## Contexte
Un étudiant peut parfois ne pas pouvoir utiliser un code. Un formateur doit donc pouvoir ajouter une présence manuelle, chiffrée et vérifiable.

## Critères d'acceptation
- [ ] La présence ajoutée par un formateur est visible dans le tableau.
- [ ] Le statut et la source précisent que c'est un ajout manuel.
- [ ] La présence manuelle ne peut pas être supprimée ou modifiée sauf par le formateur.

## Règles de gestion
- RG5 : La présence manuelle est émise par formateur.
- RG6 : La présence manuelle respecte la même fenêtre d'expiration.

## Préconditions
- L'identifiant du formateur est confirment.
- La session et l'étudiant sont vérifiés.

## Données d'entrée et contraintes
- sessionId : identifiant de la session.
- etudiantId : identifiant de l'étudiant.

## Règles de validation transversales
- Les identifiants sont requis.
- Les types d'identifiants doivent être valides.

## Scénario nominal
1. Le formateur envoie une demande de présence manuelle.
2. Le serveur vérifie la session et l'étudiant.
3. La réponse est enregistrée et visible.

## Scénarios alternatifs
- Identifiants manquants.
- Session fermée.

## Postconditions
- La présence est enregistrée et visible.

## Critères d'acceptation
- [ ] Testez la résistance, la correction et les messages d'erreur.
- [ ] Vérifiez la source dans le tableau.

## Definition of Done
- Soumission complète, tests, documentation.
