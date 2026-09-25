# M8 — Consulter le tableau et les exercices d'une session

## Contexte
Le formateur consulte un tableau récapitulatif, détaillé par étudiant, avec les présences et les exercices déposés.

## Critères d'acceptation
- [ ] Le tableau retourne les étudiants, les dépôts et les présences.
- [ ] Chaque étudiant a des statistiques claires.
- [ ] La présence manuelle est distinguée.
- [ ] L'exercice est visible avec son statut.

## Règles de gestion
- RG17 : Tableaux par session.
- RG18 : Les statistiques sont calculées sur l'état actuel.

## Préconditions
- Promotion ou session sélectionnée.
- Identifiant formateur.

## Données d'entrée et contraintes
- sessionId ou promotionId : identifiants.
- Date : optionnelle.

## Règles de validation transversales
- Champs requis.
- Formaturs valides.

## Scénario nominal
1. Le formateur demande le tableau.
2. Le serveur renvoie les stats.
3. Le frontend affiche le résultat.

## Scénarios alternatifs
- Session introuvable.
- Données vides.

## Postconditions
- Le tableau est affiché.

## Critères d'acceptation
- [ ] Testez la correction, l'expiration et la réponse.
- [ ] Vérifiez la performance.

## Definition of Done
- Soumission complète, tests, documentation.
