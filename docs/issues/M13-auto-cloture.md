# M13 — Auto-cloture de session

## Contexte
La session doit se fermer automatiquement à l'échéance de la durée configurée, et le système doit gérer le cas d'arrêt puis de nouveau démarrage.

## Critères d'acceptation
- [ ] À `cloturePrevueAt`, la session devient `CLOTUREE`.
- [ ] Toute mutation retourne une erreur et modifie rien.
- [ ] Après un redémarrage, une première demande de mutation vérifie et applique la clôture.
- [ ] Le contrôle est sécurisé par un contrôle transactionnel, pas seulement un planificateur en arrière-plan.

## Règles de gestion
- RG22 : Auto-clôture à l'échéance.
- RG23 : Rattrapage possible après redémarrage.

## Préconditions
- Session ouverte avec échéance future.

## Données d'entrée
- Aucune pour le dépôt, une demande de mutation déclenche le contrôle de rattrapage.

## Scénario nominal
1. La date de l'échéance est atteinte.
2. La session est refermée.
3. Les demandes mutantes sont rejetées.

## Scénarios alternatifs
- Planificateur absent, redémarrage, retroaction de migration.

## Postconditions
- La session est close de façon sûre.

## Critères d'acceptation
- [ ] Testez la correction, l'expiration et la réponse.
- [ ] Vérifiez le retour.

## Definition of Done
- Soumission complète, tests, documentation.
