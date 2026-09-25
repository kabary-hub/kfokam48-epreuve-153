# M2 — Marquer la présence avec un code

## Contexte
L'étudiant doit pouvoir s'inscrire en présence à l'aide d'un code fourni par le formateur. Le système doit valider le code, la session et l'état de l'étudiant.

## Critères d'acceptation
- [ ] Le code inconnu renvoie une erreur claire.
- [ ] Un étudiant déjà présent est refusé.
- [ ] Le code expiré est refusé avec une explication.
- [ ] La présence est liée à la session et à l'étudiant avec la bonne source.

## Règles de gestion
- RG1 : Le code expire 15 minutes après l'ouverture.
- RG2 : Une présence est unique par étudiant et par session.
- RG3 : La présence après expiration est refusée.
- RG4 : Le blocage après cinq tentatives est appliqué.

## Préconditions
- Code de session connu.
- Identifiant de l'étudiant valide.
- Session cochée.

## Données d'entrée et contraintes
- code : chaîne fournie par l'utilisateur.
- etudiantId : identifiant de l'étudiant.

## Règles de validation transversales
- Les champs obligatoires sont vérifiés.
- Code susceptible de correspondre au format attendu.
- Nombre de tentatives dépassées est bloqué.

## Scénario nominal
1. L'étudiant soumet le code et son identifiant.
2. Le serveur valide la session et la présence.
3. La réponse indique le statut avec identifiants.

## Scénarios alternatifs
- Code incorrect.
- Code expiré.
- Déjà présent.

## Postconditions
- La présence est confirmée ou refusée.
- L'état de l'étudiant est mis à jour.

## Critères d'acceptation
- [ ] Testez la correction, l'expiration et le cas déjà présent.
- [ ] Les réponses doivent être cohérentes.

## Definition of Done
- Soumission complète, tests, documentation.
