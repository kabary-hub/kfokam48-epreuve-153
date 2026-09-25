# M1 — Ouvrir et clôturer une session de cours

## Contexte
Le formateur doit pouvoir ouvrir une session de cours avec un code de présence unique. Le service doit valider la demande, produire un identifiant et conserver l'état de la session jusqu'à ce qu'elle soit fermée.

## Critères d'acceptation
- [ ] Une demande valide d'ouverture renvoie un identifiant, un code, une date d'ouverture et une date d'expiration.
- [ ] La date d'expiration est calculée à partir du début de la session et respecte les règles de gestion.
- [ ] Les sessions existantes avec la même promotion ne sont pas remplacées de manière accidentelle.
- [ ] L'erreur est signalée clairement si les données sont invalides.

## Règles de gestion
- RG1 : Le code expire 15 minutes après l'ouverture.
- RG2 : Une session est liée à une promotion et à un formateur identifié.
- RG3 : La soumission de présence est refusée si le code est expiré.

## Préconditions
- Promotion valide.
- Formateur identifié dans le contexte de la demande.
- Données de démonstration disponibles.

## Données d'entrée et contraintes
- Titre de la session.
- Promotion associée.
- Format de code généré aléatoirement.
- Longueur minimale et maximale des codes.

## Règles de validation transversales
- Champs obligatoires vides sont refusés.
- Format de code respecté.
- Codes invalides renvoient un message textuel explicite.

## Scénario nominal
1. Le formateur demande une nouvelle session.
2. Le système valide la promotion et génère un code.
3. Le contrôleur renvoie un identifiant, un code, une date de début et une date de fin.
4. La session est affichée et utilisable.

## Scénarios alternatifs
- Données manquantes.
- Promotion inconnue.
- Code invalide.

## Postconditions
- La session est ouverte et utilisable.
- L'état est enregistré et visible.

## Critères d'acceptation
- [ ] Testez la restauration, les erreurs et les données.
- [ ] Les requêtes doivent être cohérentes et sécurisées.

## Definition of Done
- La soumission est faite, testée, validée et mise à jour.
