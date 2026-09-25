# PresenceKF — présence et relecture croisée

Application full-stack destinée à gérer les présences d’étudiants par session, le dépôt des liens d’exercices et leur relecture par les pairs. Le formateur dispose d’un tableau par promotion ; les étudiants consultent leurs dépôts et les relecteurs leurs affectations.

> **État du dépôt :** la conception et les documents sont en cours. Le code Spring Boot, React et Docker n’est pas encore installé. Les versions de commandes ci-dessous décrivent la cible retenue et devront être exécutées, corrigées si nécessaire, puis validées depuis un clone vierge avant la livraison.

## Candidat et dépôt

- Auteur du projet : **BOUBACAR SIDDIGHI BALDE**
- Matricule communiqué : **kf48-153** (vérifier la forme officielle dans le document de soumission)
- Dépôt : <https://github.com/kabary-hub/kfokam48-epreuve-153>

## Objectifs fonctionnels

- Ouvrir une session et produire un code de présence.
- Marquer la présence par code pendant la période de validité.
- Déposer un lien d’exercice et permettre sa relecture croisée.
- Affecter un étudiant présent à chaque exercice, en respectant les règles de capacité et d’anonymat définies dans le cahier des charges.
- Produire côté API les agrégats du tableau (dont la moyenne) et les données de détail par session.
- Configurer une durée de session globale à usage unique avant l’ouverture (15 à 480 minutes, sans défaut), puis clôturer automatiquement à l’échéance. Le code de présence expire toujours à +15 minutes.

Les exigences détaillées, les contradictions du client et les hypothèses retenues sont dans le [cahier des charges](docs/CAHIER_DES_CHARGES.md). Le backlog candidat est dans [`docs/BACKLOG.md`](docs/BACKLOG.md). Les propositions du backlog doivent être créées en issues avant le jalon d’analyse.

## Technologies visées

| Partie | Technologie | Usage |
|---|---|---|
| Backend | Java 17+, Spring Boot, Maven + `mvnw` | API REST et règles métier |
| Architecture backend | MVC en couches : contrôleurs, services, repositories | Séparer transport HTTP, métier et persistance |
| Base de données | PostgreSQL | Stockage relationnel |
| Migrations | Flyway | Schéma versionné ; ne jamais modifier une migration déjà appliquée |
| Frontend | React + TypeScript + Vite | Trois espaces : formateur, étudiant, relecteur |
| Tests frontend | Vitest (choix cible) | Tests unitaires de composants et logique d’affichage |
| Conteneurs | Docker et Docker Compose | Environnement reproductible pour l’API, le frontend et PostgreSQL |
| Contrat | OpenAPI 3.0, `api/contrat.yaml` | Référence partagée par l’API, les tests et le frontend |

Les outils indiqués comme cible ne sont pas réputés installés tant que leurs fichiers et dépendances ne sont pas présents dans le dépôt. Ne pas générer de code avant le jalon `[JALON] analyse`.

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

- **Controller :** traduit HTTP en appels de service, valide le format des entrées et retourne les DTO/statuts du contrat. Aucune règle métier complexe ni requête SQL/JPA dans un contrôleur.
- **Service :** applique les règles `RG-*`, contrôle les transitions, coordonne une transaction lorsque plusieurs données changent.
- **Repository :** seule couche qui interroge la base ; les repositories restent centrés sur la persistance.
- **DTO :** les entités JPA ne sont jamais exposées directement dans les réponses JSON.
- **Gestion d’erreurs :** un `@RestControllerAdvice` traduit les erreurs de validation et métier en `{ "code": "…", "message": "…" }`. Aucun détail interne ni stack trace ne part au client.
- **Migrations :** Flyway définit le schéma. Une évolution de structure crée une nouvelle migration numérotée et versionnée ; une migration déjà partagée n’est jamais éditée pour « corriger » le passé.

### Frontend : organisation par fonctionnalités

Organisation cible, à ajuster sans multiplier les abstractions :

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

Les composants affichent et collectent les actions ; l’API reste la source de vérité métier. Les états de chargement, succès, absence de données et erreur réseau sont explicites. Le frontend ne recalcule ni la moyenne ni les critères d’assignation.

## Patterns attendus et anti-patterns interdits

### À privilégier

- **MVC en couches** côté Spring : controller → service → repository.
- **DTO explicites** et validation à la frontière HTTP.
- **Services transactionnels** pour les opérations atomiques (présence unique, affectation, clôture, relecture).
- **Contraintes SQL** pour les invariants d’unicité ; validations métier complémentaires dans les services.
- **Migrations Flyway additives** et données de démonstration reproductibles.
- **Client API React centralisé**, composants focalisés, affichage piloté par les réponses serveur.
- **Tests lisibles par règle** (`RG-*`) et scénarios d’acceptation vérifiables.
- **Configuration externalisée** pour les ports, accès de base et options d’environnement ; `.env` local ignoré, variables d’exemple sans secret.
- **Diffs petits et commits atomiques** associés à une issue et à une règle ou exigence.

### À éviter / proscrire

- SQL, repository ou logique métier dans les contrôleurs ; entité JPA sérialisée directement.
- Contrôleur « fourre-tout », service monolithique ou abstractions génériques sans besoin réel.
- Validation métier dupliquée dans React, calcul de moyenne côté navigateur, ou règles d’affectation implémentées dans l’UI.
- `fetch` dispersé, erreurs avalées, promesses sans gestion d’échec, états de chargement absents.
- `ddl-auto=update` hors tests, modification d’une ancienne migration Flyway, données de production/secrets committés.
- Code généré (`target/`, `node_modules/`, `dist/`) dans Git.
- Dépendances ajoutées sans vérifier qu’elles sont nécessaires et compatibles avec les versions retenues.
- Réécriture forcée de l’historique du dépôt projet, commits regroupant plusieurs tickets sans justification ou commit directement sur `main` pour une fonctionnalité.

## Règles métier à vérifier avant tout changement

La référence normative est le cahier des charges et le contrat. Points qui ne doivent pas être supposés :

- Q10 et Q15 sont contradictoires. La décision candidate inscrite actuellement est : le relecteur affecté peut modifier sa note avant la fin/clôture ; le POST initial reste conforme et la modification utilise une opération d’extension.
- Le code de présence expire après 15 minutes ; cette fenêtre est indépendante de la durée totale de session.
- La durée est réglée via `PUT /api/sessions/configuration`, avant la création, et globalement à usage unique : 15 à 480 minutes, obligatoire, sans valeur par défaut. La dernière configuration non consommée remplace la précédente ; le prochain `POST /api/sessions` consomme la valeur. Une concurrence d’ouvertures peut faire échouer l’une avec `409 DUREE_SESSION_REQUISE`.
- La session est clôturée automatiquement par un planificateur à l’échéance, sans action manuelle. Après un arrêt serveur, la première requête doit rattraper les échéances passées avant toute mutation.
- Cette auto-clôture s’écarte explicitement de Q12, qui prévoit une clôture par le formateur, et du parcours de clôture manuelle indiqué par le sujet. L’écart a été choisi par le candidat et ne doit pas être présenté comme une exigence client.
- Le candidat a choisi une relecture anonyme : le relecteur ne reçoit pas l’identité de l’auteur ; l’auteur ne reçoit pas l’identité du relecteur.
- La première soumission de relecture contient une note et un commentaire. Avant clôture, le relecteur affecté peut modifier uniquement la note ; le commentaire reste inchangé.
- Une attente sans relecteur reste visible ; toute nouvelle présence peut déclencher une nouvelle tentative d’affectation, selon le cahier des charges.
- L’absence d’authentification est une simplification de démonstration, pas une sécurité de production.

Si une nouvelle information contredit l’un de ces points, **ne pas arbitrer silencieusement** : signaler la contradiction, poser une question au candidat et attendre sa décision avant de modifier le cahier des charges, le modèle ou l’API.

## Git flow obligatoire pour le dépôt projet

Le dépôt `main` doit rester intégrable et sain. Une fonctionnalité ou correction se réalise sur une branche dédiée, publiée, puis fusionnée dans `main` par pull request GitHub (équivalent de merge request).

### Nommage des branches

Une branche par issue/ticket :

```text
feat/EF1-marquer-presence
feat/EF2-ouvrir-session
fix/RG4-blocage-tentatives
chore/issue-12-tests-api
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

Créer ensuite une PR vers `main`, liée à l’issue. La description contient les critères vérifiés et `Closes #<numéro>`. Attendre les contrôles et fusionner la PR ; supprimer la branche distante après fusion si GitHub le propose. Reprendre les tickets suivants depuis un `main` à jour.

### Commits

- Commits petits, atomiques et explicites : `feat(EF1): … (RG1)`, `fix(RG4): …`, `test(EF1): …`, `docs: …`.
- Une seule préoccupation par commit ; citer issue, exigence et règle métier quand pertinent.
- **Aucune signature cryptographique GPG/SSH** : commits Git ordinaires avec l’identité Git réelle du candidat. Ne pas falsifier l’auteur ou le committer.
- Pousser chaque lot documentaire ou ticket terminé ; ne pas attendre la fin pour publier.
- Ne jamais utiliser `--force` sur le dépôt du projet. Le cas éventuel de réécriture concerne uniquement le dépôt séparé de l’épreuve Git.

### Jalons d’examen

Les messages doivent être exactement :

```text
[JALON] analyse
[JALON] v0.1
[JALON] v1.0
```

Ils sont des commits vides distincts, poussés sur `main`, dans cet ordre. `[JALON] analyse` ne se crée qu’après CDC, diagrammes, backlog en issues et contrat figé ; il doit précéder le premier commit de code. `[JALON] v0.1` suit l’intégration des Must et précède l’ouverture de l’enveloppe. Respecter l’ordre final explicitement retenu dans la section 10 du CDC ; le hash de soumission est celui du dernier commit réellement livré.

## Méthodologie obligatoire avant toute tâche (humain ou agent IA)

1. **Lire avant d’agir :** l’issue, les critères d’acceptation, les règles `RG-*`, `docs/CAHIER_DES_CHARGES.md`, `CLIENT.md` fourni et `api/contrat.yaml` ; vérifier aussi l’architecture et l’état Git actuels.
2. **Comparer les attentes :** identifier les consignes applicables, les livrables, les fichiers touchés et les effets sur contrat, modèle, migration, UI, tests et documentation.
3. **Détecter les incertitudes :** ne jamais inventer une réponse ni « choisir la plus probable ». Lister les contradictions/trous, décrire leurs conséquences et demander une décision explicite au candidat avant de changer un comportement ou un contrat.
4. **Planifier :** proposer un plan bref, ordonné, avec les tests de régression et le commit/branche attendus. Ne commencer qu’une fois les points bloquants compris.
5. **Appliquer le TDD :** écrire un test d’acceptation/règle qui échoue (RED), implémenter le changement minimal (GREEN), refactoriser sans changer le comportement, puis relancer les tests pertinents et la suite disponible.
6. **Contrôler :** examiner le diff complet, l’API et le statut Git ; vérifier qu’aucun fichier généré, secret, nom de fournisseur IA ou attribution publicitaire n’a été ajouté. Mettre à jour les documents qui sont devenus faux.
7. **Livrer :** ticket par ticket, branche dédiée, commits ordinaires non signés cryptographiquement, push, PR vers `main` et merge après validation. Mettre à jour le journal d’étape avec des faits exacts et la manière dont les résultats IA ont été vérifiés, sans ajouter de marque ou de traces publicitaires.

**Bloquant :** si l’information nécessaire n’est pas vérifiable dans les documents, le code, les tests ou les consignes confirmées par le candidat, poser la question ; ne pas conclure par supposition.

## TDD et commandes de vérification

Chaque règle métier modifiée doit avoir un test pertinent avant son implémentation. Les cas négatifs (validation, expiration, conflit, clôture, accès interdit) sont testés avec les statuts et enveloppes d’erreur exacts du contrat.

Commandes cibles après création des modules — à confirmer en exécutant et à maintenir selon les scripts réels :

```bash
# Backend : tests unitaires et d’intégration du module Maven
cd backend && ./mvnw test

# Backend : vérifications et packaging
cd backend && ./mvnw verify

# Frontend : installer les versions figées depuis le lockfile
cd frontend && npm ci

# Frontend : tests Vitest en mode CI (script npm "test" à configurer)
npm run test -- --run

# Frontend : build de production
npm run build

# Démarrage reproductible de la démonstration
docker compose up --build
```

Les commandes Maven/frontend sont des **commandes prévues**, pas encore validées tant que les fichiers correspondants n’existent pas. Avant la remise, vérifier que les tests d’intégration tournent sur un poste vierge sans dépendre d’une base locale personnelle, que le build passe et que le README a été suivi depuis un clone vierge. Documenter toute commande corrigée après exécution réelle.

## Configuration et données

- PostgreSQL local est destiné au développement/démonstration uniquement.
- Les secrets et fichiers locaux restent hors Git (`.env`, `application-local.properties`). Ajouter des noms de variables sans valeurs secrètes dans un éventuel `.env.example`.
- Les données initiales doivent être déterministes, minimales et non sensibles ; elles permettent au correcteur de vérifier les trois rôles sans ouvrir une application vide.
- Le serveur ne doit jamais renvoyer stack traces, SQL ou données privées dans ses réponses d’erreur.

## Dossiers du dépôt

```text
api/contrat.yaml           Contrat OpenAPI de référence
backend/                   API Spring Boot (à créer après l’analyse)
frontend/                  Interface React (à créer après l’analyse)
docs/                      CDC, backlog, journal, diagrammes
  diagrammes/
.gitignore                 Exclusions Java, Node, IDE et secrets
README.md                  Architecture, règles et procédures
SOUMISSION.md              Brouillon de soumission, finalisé à l’étape 6
```

## Livrables de l’épreuve

Le dépôt doit contenir le cahier des charges en dix sections, les trois diagrammes obligatoires (et le diagramme bonus s’il est conservé), les issues GitHub vérifiables, le contrat figé avant le code, les migrations versionnées, les tests, l’application et ses données de démo, le journal tenu au fil des étapes, le changelog et le README testé depuis un clone vierge. Le fichier `SOUMISSION.md` final doit être téléversé sur la plateforme avant 18h00 avec les deux dépôts publics et les hashes complets déclarés.
