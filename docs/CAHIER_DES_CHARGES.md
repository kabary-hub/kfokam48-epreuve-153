# Cahier des charges — PresenceKF

**Auteur :** BOUBACAR SIDDIGHI BALDE · **Matricule communiqué :** `kf48-153`  
**Version :** 1 · **Date :** 25 septembre 2026  
**Frontend choisi :** React, pour réaliser les trois interfaces demandées avec un framework autorisé et une application monopage distincte de l’API Spring Boot.

> Le candidat a communiqué le numéro `153`, puis a précisé la forme `kf48-153`. Le dépôt existant est `kabary-hub/kfokam48-epreuve-153`. Reporter exactement le matricule officiel demandé par la plateforme dans la soumission finale.

## 1. Contexte et objectif

PrésenceKF répond au besoin de la formation KFOKAM48 de suivre les activités de cours au sein de chaque promotion. Le formateur ouvre une session, partage un code de présence valable quinze minutes et peut enregistrer une présence manuellement si un étudiant rencontre un problème. Les étudiants déposent le lien de leur exercice avant la clôture de la session. Le système attribue chaque exercice à relire à un étudiant présent admissible et conserve la note ainsi que le commentaire remis. Le formateur consulte les présences, les dépôts et les relectures en attente dans un tableau récapitulatif. Il clôt la session manuellement ; aucune clôture automatique n’est prévue (Q12). L’application vise à remplacer un suivi dispersé par des informations centralisées et vérifiables, dans un prototype pédagogique sans authentification.

## 2. Acteurs et rôles

| Acteur | Ce qu'il peut faire | Ce qu'il ne peut pas faire |
|---|---|---|
| Formateur | Ouvrir une session, obtenir un code, clôturer la session (Q12), ajouter une présence manuellement marquée FORMATEUR (Q14), consulter le tableau d'une promotion (Q16) | Marquer une présence sans que la source soit visible, relire un exercice, modifier une note de relecture |
| Étudiant | Marquer sa présence avec un code (Q2), déposer le lien de son exercice (Q12), remplacer le lien avant relecture (Q13), consulter sa note et son commentaire sans voir le nom du relecteur (Q8) | Relire son propre exercice (Q5), voir le nom de son relecteur (Q8), marquer sa présence après expiration du code (Q3) |
| Relecteur | Rendre une note entière de 0 à 20 avec commentaire (Q9), modifier sa note tant que la session n'est pas clôturée (Q10) | Relire son propre exercice (Q5), relire plus d'un exercice par session (Q6), modifier son commentaire après la première soumission (décision Q10 > Q15) |

Le relecteur est un étudiant dans un état particulier (assigné à un exercice). Cette décision évite une entité redondante et simplifie le modèle de données : la table Relecture porte relecteurId → Etudiant.

## 3. Périmètre

**Inclus dans cette version :**

- Gestion des sessions : ouverture par le formateur, génération d'un code à 6 caractères, expiration à +15 minutes, clôture manuelle (Q2, Q12).
- Marquage de présence par code (étudiant, Q2) et ajout manuel par le formateur avec source=FORMATEUR visible (Q14).
- Dépôt du lien d'exercice par l'étudiant, avec possibilité de remplacement tant que la relecture n'a pas commencé (Q12, Q13).
- Assignation automatique d'un relecteur parmi les étudiants présents à la session, hors auteur, sans double affectation (Q6, Q7).
- Relecture : note entière de 0 à 20 avec commentaire, modifiable tant que la session n'est pas clôturée (Q9, Q10).
- Consultation de sa note et de son commentaire par l'étudiant relu, sans révélation de l'identité du relecteur (Q8).
- Tableau récapitulatif par promotion pour le formateur : présences, exercices déposés, moyenne, relectures en attente (Q16).
- Données de démonstration chargées au démarrage (promotion, formateur, étudiants, session exemple).

**Explicitement exclu de cette version :**

- Authentification et mots de passe (Q1) : l'identité est portée par un identifiant transmis en clair (formateurId, etudiantId).
- Gestion CRUD des promotions, étudiants et formateurs : ces référentiels sont supposés préexister et sont chargés par les données de démonstration.
- Clôture automatique de session : le sujet impose une clôture manuelle par le formateur (Q12). Toute mention d'auto-clôture est retirée du CDC, des diagrammes, du contrat et du code.
- Upload de fichiers : on ne stocke qu'un lien vers l'exercice (Q13).
- Notifications par email, push ou SMS.
- Historique complet des modifications de relecture : seule la dernière version est conservée (le commentaire reste immuable après la 1re soumission).
- Application mobile native (le frontend React+Vite est responsive, suffisant pour Q16 et ENF1).
- Import/export de masse, statistiques avancées, tableaux de bord multi-promotions.
- Sécurité de production (rôles Spring Security, JWT, HTTPS) : hors périmètre pour l'épreuve, documenté en section 7.

**Justification du périmètre :**
Ce découpage garde les 5 opérations imposées par le contrat d'API et leurs extensions directement issues des 16 questions client. Tout ce qui n'est pas explicitement demandé (auth, CRUD, notifications, upload, auto-clôture) est exclu pour concentrer l'effort sur la conformité B1-B6, F1-F3 et le barème.

## 4. Exigences fonctionnelles

| Réf | Exigence | Critère d'acceptation | Priorité |
|---|---|---|---|
| EF1 | L'étudiant marque sa présence avec un code | Quand je saisis un code valide et non expiré, ma présence apparaît dans le tableau du formateur | Must |
| EF2 | Le formateur ouvre une session et obtient un code | Quand je crée une session avec {titre, promotionId}, je reçois 201 {id, code, ouvertureAt, expirationAt} avec expirationAt = ouvertureAt + 15 min | Must |
| EF3 | Le formateur peut ajouter une présence manuellement | Quand j'ajoute une présence pour un étudiant de la promotion, elle est enregistrée avec source = FORMATEUR et visible dans le tableau | Must |
| EF4 | L'étudiant dépose le lien de son exercice | Quand je dépose un URI valide, l'exercice est créé avec statut = DEPOSE | Must |
| EF5 | L'étudiant peut remplacer le lien de son exercice | Quand je remplace le lien avant toute relecture commencée, le nouveau lien est pris en compte | Should |
| EF6 | Le système assigne un relecteur à chaque exercice déposé | Quand un exercice est déposé, un relecteur est choisi au hasard parmi les étudiants présents hors auteur ; si aucun candidat, relecteurId = null et statut = EN_ATTENTE_SANS_RELECTEUR | Must |
| EF7 | Le relecteur rend une note et un commentaire | Quand je soumets une note entière entre 0 et 20, la relecture passe au statut RELUE et l'étudiant relu peut voir la note | Must |
| EF8 | Le relecteur peut modifier sa note avant clôture | Quand je modifie ma note tant que la session n'est pas clôturée, la nouvelle valeur remplace l'ancienne ; le commentaire initial reste immuable | Should |
| EF9 | Le formateur consulte le tableau d'une promotion | Quand je demande GET /api/tableau?promotionId=X, j'obtiens pour chaque étudiant : présences, exercices déposés, moyenne, relectures en attente | Must |
| EF10 | L'étudiant relu consulte sa note et son commentaire | Quand je consulte mon exercice relu, je vois la note et le commentaire mais pas le nom ni l'id du relecteur | Should |
| EF11 | Le formateur clôture une session manuellement | Quand je clôture la session, plus aucun dépôt ni modification de relecture n'est possible ; les relectures deviennent définitives | Must |

Répartition par priorité :
- Must (8) : EF1, EF2, EF3, EF4, EF6, EF7, EF9, EF11
- Should (3) : EF5, EF8, EF10
- Could : aucune exigence fonctionnelle supplémentaire dans cette version (les extensions éventuelles sont documentées en section 7).

## 5. Exigences non fonctionnelles

| Réf | Exigence | Comment on la vérifie |
|---|---|---|
| ENF1 | L'interface de marquage de présence est utilisable sur un téléphone (viewport 375 px minimum) | Test manuel sur viewport 375 px : saisie du code possible sans zoom, bouton visible et cliquable, aucun débordement horizontal |
| ENF2 | Le tableau du formateur répond en moins de 2 secondes pour une promotion de 60 étudiants sur 10 sessions | Test de charge simple : 60 étudiants, 10 sessions, 600 présences et 200 exercices en base ; mesure du temps de réponse de GET /api/tableau?promotionId=X (moyenne sur 10 appels < 2 s) |
| ENF3 | Le code de présence est un identifiant à 6 caractères alphanumériques non devinable, et le blocage après 5 erreurs limite le brute force | Vérification : le code est généré aléatoirement (SecureRandom ou équivalent) ; après 5 erreurs consécutives, l'étudiant est bloqué 2 minutes (RG3) et reçoit 400 TROP_TENTATIVES |
| ENF4 | Le schéma de base de données est versionné par Flyway ou Liquibase ; ddl-auto=update est interdit hors tests | Vérification : présence de src/main/resources/db/migration/V1__init.sql ; absence de spring.jpa.hibernate.ddl-auto=update dans application.properties (hors profil test) ; la table flyway_schema_history existe après démarrage |
| ENF5 | Toutes les erreurs de l'API respectent le format {code, message} sans jamais exposer de stack trace | Vérification : appel de chaque endpoint en erreur (400, 403, 404, 409, 410) et lecture de la réponse ; aucune trace Java, aucun corps vide, aucun message par défaut de Spring |

Justification :
- ENF1 répond à Q3 et Q16 (usage mobile réel des étudiants).
- ENF2 fixe une cible mesurable pour le tableau (Q16).
- ENF3 relie la sécurité du code à Q2 et Q4.
- ENF4 impose la contrainte B5 du sujet.
- ENF5 impose la contrainte B4 et le format d'erreur du contrat.

## 6. Règles de gestion

| Réf | Règle | Source |
|---|---|---|
| RG1 | Un code de présence expire 15 minutes après l'ouverture de la session | Q2 |
| RG2 | Un étudiant ne peut pas marquer sa présence après l'expiration du code | Q3 |
| RG3 | Après 5 tentatives de code erronées consécutives, l'étudiant est bloqué pendant 2 minutes | Q4 |
| RG4 | Un étudiant ne peut jamais relire son propre exercice | Q5 |
| RG5 | Un exercice a exactement un seul relecteur | Q6 |
| RG6 | Le relecteur est choisi au hasard parmi les étudiants présents à la session (hors auteur) | Q7 |
| RG7 | L'étudiant relu voit la note et le commentaire, mais jamais le nom ni l'identifiant du relecteur | Q8 |
| RG8 | La note est un entier compris entre 0 et 20 inclus | Q9 |
| RG9 | Une relecture rendue est modifiable tant que la session n'est pas clôturée (Q10 l'emporte sur Q15) ; seul le commentaire initial reste immuable | Q10 > Q15 |
| RG10 | Un exercice sans relecture rendue reste au statut EN_ATTENTE et apparaît comme tel dans le tableau | Q11 |
| RG11 | Un exercice peut être déposé jusqu'à la clôture manuelle de la session par le formateur | Q12 |
| RG12 | Le lien d'un exercice est remplaçable tant qu'aucune relecture n'a été rendue (statut EN_ATTENTE) | Q13 |
| RG13 | Une présence ajoutée manuellement par le formateur porte source = FORMATEUR et est distinguable dans le tableau | Q14 |
| RG14 | Après clôture de la session par le formateur, une relecture rendue est définitive et non modifiable | Q15 corrigée par Q10 |
| RG15 | Un étudiant ne peut être présent qu'une seule fois par session (unicité) | implicite (déduit de Q2) |
| RG16 | Un étudiant ne peut déposer qu'un seul exercice par session (unicité) | implicite (déduit de Q4) |

Décision Q10 > Q15 :
Q10 indique qu'une relecture est modifiable tant que la session n'est pas clôturée. Q15 affirme qu'une note envoyée est définitive. Ces deux réponses se contredisent. Q10 l'emporte car : (1) Q11 décrit un usage concret du formateur qui implique un état intermédiaire avant clôture, (2) Q10 décrit un mécanisme conditionné et précis, (3) Q15 formule une intention générale (« c'est plus honnête ») sans mécanisme. Conséquence : seule la note est modifiable avant clôture ; le commentaire initial reste immuable.

Unicité implicite (RG15, RG16) :
Le client ne formule pas explicitement ces deux règles, mais elles découlent du modèle : une présence par étudiant et par session (sinon le tableau est faussé), et un exercice par étudiant et par session (sinon l'assignation d'un relecteur devient ambiguë). Ces règles sont documentées ici comme hypothèses raisonnables.

Clôture manuelle (RG11, RG14) :
La clôture de session est une action manuelle du formateur (Q12). Aucune clôture automatique n'est prévue. Les relectures deviennent définitives après clôture (RG14) ; les dépôts sont refusés après clôture (RG11).

## 7. Zones d'ombre, hypothèses et contradictions

### 7.1 — Contradictions relevées et tranchées

| Réponses en conflit | Ce que j'ai choisi | Pourquoi |
|---|---|---|
| Q10 (« relecteur peut corriger tant que la session n'est pas clôturée ») vs Q15 (« la note est définitive une fois envoyée ») | Q10 l'emporte : la note reste modifiable tant que la session n'est pas clôturée ; seul le commentaire initial est immuable | Q10 décrit un mécanisme concret et conditionné, cohérent avec Q11 (l'existence d'un état « en attente » avant clôture). Q15 formule une intention générale (« c'est plus honnête ») sans mécanisme associé. La règle RG9 formalise cette décision. |

### 7.2 — Trous comblés par hypothèse raisonnable

| Point non tranché par le client | Hypothèse retenue | Conséquence sur le modèle / l'API |
|---|---|---|
| Q7 (« relecteur choisi parmi les étudiants présents ») + Q12 (« dépôt possible jusqu'à clôture ») : que se passe-t-il si un exercice est déposé alors qu'aucun étudiant présent n'est disponible pour le relire (session à 1 étudiant, ou tous déjà relecteurs) ? | À la soumission, on choisit un relecteur parmi les étudiants présents (Presence existante pour la session), hors auteur, n'ayant pas déjà un exercice à relire. Si aucun candidat → relecteurId = null, statut = EN_ATTENTE_SANS_RELECTEUR, l'exercice reste visible dans le tableau (Q11) | Champ Exercice.relecteurId NULLABLE ; statut EN_ATTENTE_SANS_RELECTEUR ajouté ; EF6 et RG6 précisent ce cas |
| Q1 (« pas de mot de passe, l'étudiant choisit son nom dans une liste ») + Q14 (« le formateur peut ajouter une présence manuellement ») : comment identifier le formateur sans authentification ? | Pas d'authentification. Le formateurId (et l'etudiantId) sont transmis en clair dans les requêtes, ou sélectionnés dans une liste côté frontend. C'est une simplification assumée pour l'épreuve | Aucun filtre de sécurité ; à documenter comme limite du périmètre en section 3 ; l'en-tête X-Formateur-Id peut être utilisé mais n'apporte aucune garantie de sécurité |
| Q3 (« pas de présence après la fin de session ») + Q12 (« dépôt possible jusqu'à clôture ») : que signifie « fin de session » ? | On distingue expirationAt (= ouvertureAt + 15 min, fin du code de présence) et clotureAt (= clôture manuelle par le formateur). La présence est impossible après expirationAt ; le dépôt d'exercice est possible jusqu'à clotureAt | Deux champs distincts sur Session : expirationAt (dérivé) et clotureAt (nullable, rempli à la clôture) ; RG11 et RG14 s'y réfèrent |
| Q13 (« remplacer le lien tant que personne n'a commencé à le relire ») : que signifie « commencé » ? | Un exercice est remplaçable tant que son statut est EN_ATTENTE (aucune note saisie). Dès qu'une note est rendue (statut RELUE), le lien est figé | Champ Exercice.statut utilisé comme verrou ; EF5 et RG12 formalisent cette condition |
| Q16 (« voir combien d'exercices il a déposés, la moyenne des notes reçues, les relectures en attente ») : la moyenne inclut-elle les exercices non relus ? | La moyenne est calculée uniquement sur les exercices RELUS. Si aucun exercice n'a été relu, la moyenne est null (et non 0) | GET /api/tableau renvoie moyenne = null si aucun exercice relu ; EF9 précise le calcul |
| Q4 (« 5 erreurs → bloqué 2 minutes ») : le blocage est-il par étudiant, par session, ou global ? | Le blocage est par (etudiantId, sessionId) : 5 tentatives erronées consécutives sur une même session bloquent l'étudiant 2 minutes pour cette session uniquement | Ajout d'un compteur de tentatives et d'un timestamp de blocage côté service ; EF1 et RG3 s'y réfèrent |

### 7.3 — Décisions structurantes

- Le relecteur n'est pas une entité séparée : c'est un étudiant dans un état particulier (assigné à une relecture). Conséquence : table Relecture avec relecteurId → Etudiant, pas d'entité Relecteur.
- Le code de présence est généré aléatoirement (SecureRandom, 6 caractères alphanumériques). Aucune liste prédéfinie.
- Aucune clôture automatique : la session est clôturée manuellement par le formateur (RG11, RG14, Q12).
- Toutes les erreurs respectent le format {code, message} imposé par le contrat d'API, sans exception.

Toutes les hypothèses ci-dessus sont documentées comme décisions assumées du candidat, conformément à la règle du sujet : « S'il te manque une information, décide à la place du client et écris-le dans la section 7 de ton cahier des charges. »

## 8. Contraintes techniques

### 8.1 — Contraintes imposées par le sujet (backend)

| Réf | Contrainte imposée | Engagement (cible) | État actuel |
|---|---|---|---|
| B1 | Java 17 ou plus, Maven, wrapper `mvnw` commité | Backend Java 17+ avec Maven et wrappers `mvnw`, `mvnw.cmd` committés dans `backend/` | Partiel — `backend/pom.xml` local indique Java 17, mais il n'est pas suivi par Git ; wrappers absents et aucun backend dans `origin/main` |
| B2 | Le contrat `api/contrat.yaml` est respecté à la lettre : chemins, verbes, codes de statut, format d'erreur | Tester en intégration les cinq opérations imposées et garantir le format `{code, message}` par le gestionnaire global d'erreurs | Partiel — le contrat est suivi dans `origin/main`, mais aucune implémentation backend n'y est suivie ; le contrôleur local non suivi utilise des routes et paramètres incompatibles |
| B3 | Séparation contrôleur / service / repository ; aucune requête base dans un contrôleur, aucune entité JPA exposée en JSON — passage par des DTO | Séparer les packages `controller`, `service`, `repository`, `entity`, `dto` et retourner uniquement des DTO | Partiel — ces packages et quelques classes existent localement mais ne sont pas suivis ; la séparation et l'absence d'entités exposées ne sont pas vérifiées pour l'ensemble de l'API |
| B4 | Validation des entrées et gestion centralisée des erreurs (`@RestControllerAdvice`) ; aucune stack trace renvoyée au client | Valider les DTO d'entrée et ajouter un gestionnaire global qui retourne `{code, message}` sans stack trace | À faire (Phase 2) — aucun gestionnaire global ni mécanisme complet de validation n'a été trouvé |
| B5 | Schéma versionné par Flyway ou Liquibase, migrations commitées ; `ddl-auto=update` interdit hors tests | Ajouter une migration Flyway initiale et désactiver `ddl-auto=update` hors tests | Partiel — le `pom.xml` local déclare Flyway, mais aucune migration n'a été trouvée ; la configuration locale utilise `spring.jpa.hibernate.ddl-auto=update` ; rien de cela n'est suivi sur `origin/main` |
| B6 | Un test unitaire d'une règle métier réelle et un test d'intégration d'un endpoint, exécutables sans base locale personnelle | Ajouter un test unitaire, un test d'intégration et vérifier leur exécution sur une base de test reproductible avec `mvnw test` | À faire (Phase 2) — aucun test backend ni wrapper Maven n'a été trouvé |

### 8.2 — Contraintes imposées par le sujet (frontend)

| Réf | Contrainte imposée | Engagement (cible) | État actuel |
|---|---|---|---|
| F1 | Framework déclaré et justifié en une ligne dans le README ; build fonctionnel | Utiliser React 18, Vite et TypeScript, justifier React en une ligne dans le README et vérifier `npm run build` | Partiel — le prototype local React/Vite a réussi `npm run build`, mais ses fichiers ne sont pas suivis ; le README suivi ne contient pas encore la justification demandée |
| F2 | Trois écrans : formateur (ouvrir une session, voir le tableau), étudiant (marquer sa présence, déposer son exercice), relecteur (faire une relecture) | Fournir les parcours formateur, étudiant et relecteur, accessibles depuis l'interface React | À faire (Phase 2) — seul un formulaire d'ouverture de session est présent dans le frontend local non suivi ; écrans étudiant et relecteur absents |
| F3 | Couche API dédiée, états de chargement et d'erreur, aucune règle métier dupliquée ; la moyenne affichée vient de l'API | Centraliser les appels API, afficher les états de chargement/erreur et afficher la moyenne renvoyée par l'API sans la recalculer | Partiel — un service Axios local existe pour les sessions seulement et le formulaire affiche une erreur ; la couche API complète et les états de chargement manquent ; ces fichiers ne sont pas suivis |

### 8.3 — Contraintes personnelles (choix du candidat)

| Domaine | Choix | Justification |
|---|---|---|
| Base de données | PostgreSQL 16 via Docker Compose | Base relationnelle adaptée aux liens entre sessions, présences, exercices et relectures ; environnement de démonstration reproductible |
| Migrations | Flyway | Versionner le schéma avec des migrations additives, sans réécrire les migrations déjà commitées |
| Tests backend | JUnit 5, Mockito et Spring Boot Test ; H2 en profil test | Couvrir une règle métier et une intégration d'endpoint sans dépendre d'une base locale personnelle |
| Tests frontend | Vitest, si le temps le permet | Vérifier les parcours et les états d'interface sans en faire une dépendance au démarrage de l'application |
| Build frontend | Vite | Outil de build choisi pour le frontend React |
| Démarrage | `docker compose up` à la racine, avec une alternative documentée en trois commandes maximum | Limiter les étapes nécessaires au correcteur et satisfaire l'exigence de démarrage reproductible |
| Données de démonstration | Données déterministes chargées au démarrage par un composant d'initialisation ou une migration dédiée | Permettre de vérifier les parcours sans créer toutes les données manuellement |
| Gestion des erreurs | `@RestControllerAdvice` global, format `{code, message}` | Respecter B4 et le format uniforme imposé par le contrat d'API |
| Sécurité | Pas d'authentification ni de mots de passe | Simplification conforme à Q1 ; limite du prototype documentée dans les sections 3 et 7 |

### 8.4 — Contraintes de qualité

- Aucun secret commité (`.env`, mots de passe, tokens) ; `.gitignore` exclut les fichiers locaux concernés.
- Aucun fichier généré ne doit être commité : `.gitignore` exclut déjà `target/`, `node_modules/` et `dist/` ; ajouter `.vite/` si cet artefact est produit par l'outillage retenu.
- `main` reste sain ; chaque changement fonctionnel est intégré par une PR après vérification du build et des tests disponibles.
- Commits atomiques avec des messages explicites au format `type(scope): description (EFx, RGx)` lorsque pertinent.
- Chaque commit de code cite les références `EFx`/`RGx` applicables et inclut `Closes #N` lorsqu'il termine une issue.

Les colonnes « État actuel » reflètent l'état du dépôt au 25 septembre 2026, avant la Phase 2 (implémentation). La conformité finale sera vérifiée par les tests B6 et le build F1 à l'issue de la Phase 4.

## 9. Livrables

### 9.1 — Documents d'analyse (étape 1, avant tout code)

| Livrable | Emplacement | Statut attendu |
|---|---|---|
| Cahier des charges (10 sections, EF/RG numérotés) | `docs/CAHIER_DES_CHARGES.md` | Rédigé, tenu à jour après l'étape 3 |
| Diagramme D1 — cas d'utilisation | `docs/diagrammes/D1-cas-utilisation.md` | Mermaid, versionné |
| Diagramme D2 — modèle de données | `docs/diagrammes/D2-modele-donnees.md` | Mermaid, cohérent avec les migrations Flyway |
| Diagramme D3 — séquence « marquer sa présence » | `docs/diagrammes/D3-sequence-presence.md` | Mermaid, codes HTTP conformes au contrat |
| Diagramme D4 — états-transitions d'un exercice (bonus) | `docs/diagrammes/D4-etats-exercice.md` | Mermaid, statuts cohérents avec D2 |
| Backlog en issues GitHub | `github.com/kabary-hub/kfokam48-epreuve-153/issues` | 12-13 issues, critères, priorités, EFx/RGx |
| Contrat d'API complété et figé | `api/contrat.yaml` | 5 opérations imposées + extensions justifiées |
| Journal de bord (1 entrée par étape) | `docs/JOURNAL.md` | Tenu à jour à chaque étape |

### 9.2 — Code backend (étape 2)

| Livrable | Emplacement | Contrainte |
|---|---|---|
| Projet Spring Boot (Java 17, Maven) | `backend/` | B1 |
| Wrapper Maven | `backend/mvnw`, `backend/mvnw.cmd` | B1 |
| Migrations Flyway | `backend/src/main/resources/db/migration/V1__init.sql` | B5 |
| Contrôleurs REST | `backend/src/main/java/.../controller/` | B3, B2 |
| Services métier | `backend/src/main/java/.../service/` | B3 |
| Repositories JPA | `backend/src/main/java/.../repository/` | B3 |
| Entités JPA | `backend/src/main/java/.../entity/` | B3 |
| DTO (entrée/sortie) | `backend/src/main/java/.../dto/` | B3 |
| Gestion centralisée des erreurs | `backend/src/main/java/.../exception/GlobalExceptionHandler.java` | B4 |
| Test unitaire règle métier | `backend/src/test/java/.../SessionServiceTest.java` | B6 |
| Test d'intégration endpoint | `backend/src/test/java/.../PresenceControllerIT.java` | B6 |
| Configuration application | `backend/src/main/resources/application.properties` (+ `application-test.properties`) | B5 |

### 9.3 — Code frontend (étape 2)

| Livrable | Emplacement | Contrainte |
|---|---|---|
| Projet React + Vite + TypeScript | `frontend/` | F1 |
| Écran formateur (ouvrir session, tableau) | `frontend/src/pages/FormateurPage.tsx` | F2 |
| Écran étudiant (présence, dépôt) | `frontend/src/pages/EtudiantPage.tsx` | F2 |
| Écran relecteur (relecture) | `frontend/src/pages/RelecteurPage.tsx` | F2 |
| Couche API dédiée | `frontend/src/api/` | F3 |
| Hooks de chargement / erreur | `frontend/src/hooks/useAsync.ts` | F3 |
| Build de production | `npm run build` → `frontend/dist/` | F1 |

### 9.4 — Infrastructure et démarrage

| Livrable | Emplacement | Contrainte |
|---|---|---|
| `docker-compose.yml` | Racine du dépôt | Démarrage en 1 commande |
| Dockerfile backend | `backend/Dockerfile` | Démarrage en 1 commande |
| Dockerfile frontend | `frontend/Dockerfile` | Démarrage en 1 commande |
| Données de démonstration | `CommandLineRunner` ou Flyway `afterMigrate` | Application non vide pour le correcteur |
| README d'installation testé | `README.md` | Testé depuis un clone vierge |

### 9.5 — Livrables de fin d'épreuve

| Livrable | Emplacement | Contrainte |
|---|---|---|
| `CHANGELOG.md` | Racine du dépôt | Cohérent avec l'historique Git |
| `SOUMISSION.md` | Racine du dépôt | Hash complets (40 caractères) |
| Dépôt Git-lab (étape 5) | `github.com/kabary-hub/kfokam48-gitlab-153` | Public, séparé du projet |
| Journal final | `docs/JOURNAL.md` | Étapes 1 à 6 renseignées |

« Ces livrables couvrent les quatre piliers du barème : analyse (documents d'étape 1), produit (code backend et frontend), conduite du changement (mises à jour après enveloppe) et traçabilité (journal, CHANGELOG, SOUMISSION). »

## 10. Démarche prévue

1. **Analyse, sans code :** valider cette spécification, compléter les quatre diagrammes, compléter/figer l’API, créer le backlog d’issues, poser `.gitignore`, préparer le journal. Puis commit dédié `[JALON] analyse`, poussé avant le premier commit de code.
2. **v0.1 :** implémenter uniquement les tickets Must, une branche par ticket et une PR par branche, tickets liés/fermés par commits ; intégrer Flyway dès la première tranche de code, ajouter les tests et données de démonstration. Commit/push `[JALON] v0.1` après intégration des Must.
3. **Enveloppe :** seulement après `v0.1` poussé, ouvrir `./enveloppe`. Créer d’abord une issue, reproduire le problème, établir les changements BD/API/UI requis, migrer sans modifier les migrations précédentes, ré-prioriser et séparer correctif et évolution en branches/PR ; mettre à jour CDC et diagrammes dans un commit documentaire explicite.
4. **Version finale :** livrer les priorités finales, documenter ce qui reste, préparer CHANGELOG/README/backlog et tester depuis un clone vierge. Selon le choix du candidat, le jalon `[JALON] v1.0` sera créé avant les derniers documents finaux ; le commit final à soumettre sera le dernier hash après tous les livrables.
5. **Épreuve Git :** travailler exclusivement dans le dépôt distinct cloné depuis `git-lab.bundle`, résoudre les cinq situations de son README, publier toutes les branches dans le second dépôt ; la réécriture de l’historique ne concerne que cette épreuve.
6. **Soumission :** renseigner identité, centre, liens publics, hash complet de 40 caractères, frontend et commandes ; vérifier l’accès privé, les hashes et l’état Git, puis téléverser avant 18h00. Ne plus pousser après la sélection du hash déclaré.

**Definition of Done — un ticket est terminé quand :**
- ses critères d’acceptation sont vérifiés et sa règle `RGx` est citée dans le commit/test pertinent ;
- le code suit les couches prévues et le contrat, et les tests correspondants passent ;
- la PR est liée à l’issue, fusionnée dans `main`, et le commit ferme l’issue ;
- la documentation impactée est mise à jour et `main` reste démarrable.

## Journal des révisions

| Version | Quand | Ce qui a changé et pourquoi |
|---|---|---|
| 1 | 25 septembre 2026 | Première spécification bâtie à partir du sujet, des annexes, du contrat imposé et des décisions confirmées par le candidat. |
| 2 | 25 septembre 2026 | Précise l’auto-clôture paramétrable, le réglage global à usage unique, la relecture anonyme dans les deux sens et la modification de la note seule ; décisions confirmées par le candidat. |
