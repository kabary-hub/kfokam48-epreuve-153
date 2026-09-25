# M12 — Schéma de données et démonstration

## Contexte
Le schéma doit être versionné avec Flyway exactement correspondre aux diagrammes, et les données de démonstration doivent être suffisantes pour vérifier les parcours principaux.

## Critères d'acceptation
- [ ] Les migrations Flyway créent la base, les tables et les contraintes.
- [ ] `ddl-auto=update` est interdit hors tests.
- [ ] Un jeu de données de démonstration est présent et déterministe.
- [ ] Un ou plusieurs étudiants, sessions, présences, exercices, relectures sont visibles.
- [ ] Les données de performance pour le test de charge sont séparées du démo.

## Règles de gestion
- RG20 : Schéma versionné.
- RG21 : Données de démonstration stable.

## Préconditions
- Base vide ou migration initiale.

## Données d'entrée
- Aucune.

## Scénario nominal
1. Une instance vierge s'initialise.
2. Les données dans les colonnes sont visiblement présentes.
3. Le tableau affiche des valeurs réelles.

## Scénarios alternatifs
- Migration déjà appliquée, base corrompue, identifiants manquants.

## Postconditions
- L'environnement est démarrant et vérifiable.

## Critères d'acceptation
- [ ] Testez la correction, l'expiration et la réponse.
- [ ] Vérifiez le retour.

## Definition of Done
- Soumission complète, tests, documentation.
