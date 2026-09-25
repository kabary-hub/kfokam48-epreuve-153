# PresenceKF — présence et relecture croisée

Application full-stack destinée à gérer les présences d’étudiants par session, le dépôt des liens d’exercices et leur relecture par les pairs. Le formateur dispose d’un tableau par promotion ; les étudiants consultent leurs dépôts et les relecteurs leurs affectations.

## Candidat et dépôt

- Auteur du projet : **BOUBACAR SIDDIGHI BALDE**
- Matricule communiqué : **kf48-153**
- Dépôt : <https://github.com/kabary-hub/kfokam48-epreuve-153>

## Objectifs fonctionnels

- Ouvrir une session et produire un code de présence.
- Marquer la présence par code pendant la période de validité.
- Déposer un lien d’exercice et permettre sa relecture croisée.
- Affecter un étudiant présent à chaque exercice, en respectant les règles de capacité et d’anonymat définies dans le cahier des charges.
- Faire respecter une échéance de session paramétrable et les règles de clôture documentées dans `docs/CAHIER_DES_CHARGES.md` et `api/contrat.yaml`.

## Technologies visées

| Partie | Technologie | Usage |
|---|---|---|
| Backend | Java 17+, Spring Boot, Maven + `mvnw` | API REST et règles métier |
| Architecture backend | MVC en couches : contrôleurs, services, repositories | Séparer transport HTTP, métier et persistance |
| Base de données | PostgreSQL | Stockage relationnel |
| Migrations | Flyway | Schéma versionné |
| Frontend | React + TypeScript + Vite | Trois écrans : formateur, étudiant, relecteur |
| Tests frontend | Vitest | Tests unitaires et logique d’affichage |
| Conteneurs | Docker et Docker Compose | Environnement reproductible |
| Contrat | OpenAPI 3.0, `api/contrat.yaml` | Référence partagée par l’API, les tests et le frontend |

## Architecture cible

### Vue d’ensemble

```text
Navigateur
  └── React + TypeScript (Vite)
       └── src/api/ : client HTTP, DTO et conversion des erreurs
             │ HTTP / JSON conforme à api/contrat.yaml
             ▼
  Spring Boot — contrôleur REST (MVC)
       └── service métier (validation des règles et transactions)
             └── repository (accès aux données)
                   └── PostgreSQL
                        └── migrations Flyway

Docker Compose orchestre les services nécessaires au développement et à la démonstration.
```

### Backend : MVC en couches

- **Controller :** traduit HTTP en appels de service, valide le format des entrées et retourne les DTO/statuts du contrat.
- **Service :** applique les règles `RG-*`, contrôle les transitions, coordonne les transactions.
- **Repository :** seule couche qui interroge la base.
- **DTO :** les entités JPA ne sont jamais exposées directement dans les réponses JSON.
- **Gestion d’erreurs :** un `@RestControllerAdvice` traduit les erreurs de validation et métier en `{ "code": "…", "message": "…" }`.
- **Migrations :** Flyway définit le schéma. Une évolution de structure crée une nouvelle migration numérotée et versionnée.

### Frontend : organisation par fonctionnalités

```text
frontend/src/
  api/          client HTTP centralisé, contrats/types et erreurs
  app/          démarrage, routes et disposition générale
  components/   composants de présentation partagés
  features/
    formateur/  ouverture, tableau, clôture/échéance
    etudiant/   sélection, présence, dépôt, consultation de note
    relecteur/  affectations, démarrage et soumission de relecture
  hooks/        hooks réutilisables si une logique d’interface le justifie
  types/        types partagés lorsque nécessaire
```

Les composants affichent et collectent les actions ; l’API reste la source de vérité métier. Les états de chargement, succès, absence de données et erreur réseau sont explicites.

## Patterns attendus et anti-patterns interdits

### À privilégier

- **MVC en couches** côté Spring.
- **DTO explicites** et validation à la frontière HTTP.
- **Services transactionnels** pour les opérations atomiques.
- **Contraintes SQL** pour les invariants d’unicité ; validations métier complémentaires dans les services.
- **Migrations Flyway additives** et données de démonstration reproductibles.
- **Client API React centralisé**, composants focalisés, affichage piloté par les réponses serveur.
- **Tests lisibles par règle** (`RG-*`) et scénarios d’acceptation vérifiables.

### À éviter / proscrire

- SQL, repository ou logique métier dans les contrôleurs ; entité JPA sérialisée directement.
- Contrôleur « fourre-tout », service monolithique ou abstractions génériques sans besoin réel.
- Validation métier dupliquée dans React, calcul de moyenne côté navigateur, règles d’affectation dans l’UI.
- `fetch` dispersé, erreurs avalées, promesses sans gestion d’échec, états de chargement absents.
- `ddl-auto=update` hors tests, modification d’une ancienne migration Flyway, données de production/secrets committés.
- Code généré (`target/`, `node_modules/`, `dist/`) dans Git.
- Réécriture forcée de l’historique du dépôt projet, commits regroupant plusieurs tickets sans justification.

## Règles métier à vérifier avant tout changement

- Q10 et Q15 sont contradictoires. La décision candidate est : le relecteur affecté peut modifier sa note jusqu’à la fin/clôture ; le POST initial reste conforme et la modification utilise une opération d’extension.
- Code de présence : expiration après **15 minutes**, indépendamment de la durée totale de session.
- Durée de session configurable avant l’ouverture, 15–480 minutes, sans défaut. Nouvelle valeur remplace l’ancienne ; `POST /api/sessions` consomme le réglage et renvoie `409 DUREE_SESSION_REQUISE` si absent.
- Clôture automatique à l’échéance. Après redémarrage, la première requête mutante vérifie et applique la clôture.
- Relecture anonyme dans les deux sens : l’auteur ne voit pas le relecteur, et le relecteur ne voit pas l’auteur.
- Première soumission contient une note et un commentaire. Avant clôture, le relecteur peut modifier uniquement la note ; le commentaire reste inchangé.
- Une attente sans relecteur reste visible ; une nouvelle présence peut déclencher une nouvelle tentative d’affectation.
- L’absence d’authentification est une simplification de démonstration, pas une sécurité de production.
- Si une nouvelle information contredit l’un de ces points, ne pas arbitrer silencieusement : poser la question avant de modifier un contrat ou un modèle.

## Git flow obligatoire pour le dépôt projet

Le dépôt `main` doit rester intégrable. Une fonctionnalité ou correction se réalise sur une branche dédiée, publiée, puis fusionnée dans `main` par PR (équivalent de merge request).

### Nommage des branches

Une branche par ticket :

```text
feat/EF1-marquer-presence
feat/EF2-ouvrir-session
fix/RG4-blocage-tentatives
docs/issue-13-readme
```

### Cycle de travail

```bash
git switch main
git pull --ff-only origin main
git switch -c feat/EF1-marquer-presence
# écrire un test qui échoue, implémenter le minimum, faire passer les tests
# revoir le diff et le statut

git add <fichiers-du-ticket>
git -c commit.gpgsign=false commit -m "feat(EF1): marquer la présence avec un code (RG1, RG2)"
git push -u origin feat/EF1-marquer-presence
```

Créer une PR vers `main`, liée à l’issue (`Closes #N`). Attendre les contrôles, fusionner, supprimer la branche distante si proposé.

### Commits

- Commits petits, atomiques et explicites : `feat(EF1): … (RG1)`, `fix(RG4): …`, `test(EF1): …`, `docs: …`.
- Une seule préoccupation par commit ; citer issue, exigence et règle métier quand pertinent.
- **Aucune signature cryptographique GPG/SSH** : commits Git ordinaires avec l’identité Git réelle du candidat.
- Pousser chaque lot documentaire ou ticket terminé ; ne pas attendre la fin pour publier.
- Ne jamais utiliser `--force` sur le dépôt du projet.

### Jalons d’examen

Les messages doivent être exactement :

```text
[JALON] analyse
[JALON] v0.1
[JALON] v1.0
```

Ils sont des commits vides distincts, poussés sur `main`, dans cet ordre. `[JALON] analyse` ne se crée qu’après CDC, diagrammes, backlog en issues et contrat figé ; il doit précéder le premier commit de code. `[JALON] v0.1` suit l’intégration des Must. `[JALON] v1.0` précède les livrables finaux, selon l’ordre choisi par le candidat.

## Méthodologie obligatoire avant toute tâche (humain ou agent IA)

1. **Lire avant d’agir :** la ou les issues, les critères d’acceptation, les règles `RG-*`, `docs/CAHIER_DES_CHARGES.md`, `CLIENT.md` et `api/contrat.yaml`.
2. **Comparer les attentes :** identifier les consignes applicables, les livrables, les fichiers touchés et les effets sur le contrat, le modèle, la migration, l’UI, les tests et la documentation.
3. **Détecter les incertitudes :** ne jamais inventer de réponse. Lister les contradictions/trous, décrire leurs conséquences, et demander une décision explicite.
4. **Planifier :** proposer un plan bref, ordonné, avec tests de régression et commit/branche attendus.
5. **Appliquer le TDD :** écrire d’abord un test qui échoue (`RED`), implémenter le minimum (`GREEN`), puis refactoriser (`REFACTOR`).
6. **Contrôler :** examiner le diff complet, l’API et le statut Git ; vérifier qu’aucun fichier généré, secret, nom de fournisseur IA ou attribution publicitaire n’a été ajouté.
7. **Livrer :** ticket par ticket, branche dédiée, commits ordinaires non signés cryptographiquement, push, PR et merge après validation. Mettre à jour le journal d’étape avec des faits exacts et la manière dont les réponses IA ont été vérifiées.

**Bloquant :** si l’information nécessaire n’est pas vérifiable dans les documents, le code, les tests ou les consignes confirmées, poser la question ; ne pas conclure par supposition.

## TDD et commandes de vérification

### Backend

```bash
cd backend
./mvnw test
./mvnw verify
```

### Frontend

```bash
cd frontend
npm ci
npm run test -- --run
npm run build
```

### Environnement de démonstration

```bash
docker compose up --build
```

> Les commandes Maven/frontend sont des commandes cibles : elles seront validées une fois les fichiers réellement présents.

## Configuration et données

- PostgreSQL local est destiné au développement/démonstration uniquement.
- Secrets et fichiers locaux restent hors Git (`.env`, `application-local.properties`).
- Données initiales : déterministes, minimales, non sensibles.
- Le serveur ne renvoie jamais stack traces, SQL ou données privées dans les erreurs.

## Dossiers du dépôt

```text
api/contrat.yaml           Contrat OpenAPI de référence
backend/                   API Spring Boot (à créer après l’analyse)
frontend/                  Interface React (à créer après l’analyse)
docs/                      CDC, backlog, journal, diagrammes, issues
  diagrammes/
  issues/                  Templates et intentions d’issues GitHub
.gitignore                 Exclusions Java, Node, IDE et secrets
README.md                  Architecture, règles et procédures
SOUMISSION.md              Brouillon de soumission, finalisé à l’étape 6
```

## Livrables de l’épreuve

Le dépôt doit contenir le cahier des charges en dix sections, les trois diagrammes obligatoires (et le diagramme bonus s’il est conservé), les issues GitHub vérifiables, le contrat figé avant le code, les migrations versionnées, les tests, l’application et ses données de démo, le journal tenu au fil des étapes, le changelog et le README testé depuis un clone vierge. Le fichier `SOUMISSION.md` final doit être téléversé sur la plateforme avant 18h00 avec les deux dépôts publics et les hashes complets déclarés.
