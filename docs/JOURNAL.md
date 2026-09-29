# Journal de bord — KF48-153

> **Note sur les dates.** Le sujet situe l'épreuve au 25 septembre 2026. Ce journal et les commits Git reflètent les dates réelles de travail (27 septembre 2026 et jours suivants). Aucune date n'a été antidatée : la cohérence entre l'historique Git et ce journal est vérifiable par `git log --date=short`.

> À mettre à jour à la fin de chaque étape, en indiquant les faits réels, le temps passé et la manière dont les réponses IA ont été vérifiées. Aucune entrée ne doit être prétendue à l’avance.

## Étape 0 — Intégration et alignement

**Fait :**
- Dossier fourni analysé (sujet, CLIENT.md, contrat, modèles).
- Dépôt public `kfokam48-epreuve-153` cloné localement, vide, avec uniquement `README.md`.
- Décisions métier documentées : Q10 sur Q15, auto-clôture de session, durée globale à usage unique, anonymat des deux sens, modification de la note unique, contenu de blocage.

**Bloqué :**
- Aucune instruction non vérifiable ne doit remplacer une décision de l’utilisateur.
- `gh` n’est pas installé : les issues GitHub ne peuvent pas être créées de façon automatique depuis ce dépôt. C’est une contrainte technique à signaler.

**IA :**
- Requête de clarification : choix de la route de configuration de durée et de la valeur de blocage Q4 (réponse : utilisation `PUT /api/sessions/configuration` et `400 TROP_TENTATIVES`).
- Réponses enregistrées pour l’alignement et les livrables de l’analyse.

## Étape 1 — Analyse et conception

**Fait :**
- Cahier des charges complet en 10 sections : contexte, acteurs, périmètre, EF1-EF11, ENF1-ENF5, RG1-RG16, zones d'ombre, contraintes techniques (B1-B6, F1-F3), livrables, démarche prévue et Definition of Done. EF4 a été alignée sur les statuts retournés après tentative d'assignation.
- Quatre diagrammes Mermaid dans `docs/diagrammes/` : D1 cas d'utilisation, D2 modèle de données (`relecteurId` nullable), D3 séquence de présence avec les résultats HTTP documentés, D4 états-transitions avec une note précisant que `DEPOSE` est transitoire.
- Contrat `api/contrat.yaml` consolidé : les cinq opérations imposées et leurs codes ont été vérifiés ; les extensions retenues sont justifiées (Q1, Q10, Q12, Q13, Q14) et les erreurs utilisent `{code, message}`.
- `docs/BACKLOG.md` réécrit en 13 tickets (8 Must, 5 Should), avec critères vérifiables et références EF/RG. La création ou la mise à jour des issues GitHub reste à faire.
- Cette entrée complète l'analyse après les commits de consolidation sur `main` ; le jalon `[JALON] analyse` n'est pas encore posé.

**Bloqué :**
- Environ 12 min, estimation rétrospective, sur la contradiction Q10/Q15. Q10 a été retenue : Q11 décrit le suivi concret des relectures avant clôture, tandis que Q15 formule une intention générale. La décision est documentée en section 7.1 et RG9 du CDC ; D4 représente la modification de note par `RELUE → RELUE`.
- Environ 8 min, estimation rétrospective, sur le cas Q7 + Q12 sans candidat pour relire. Décision : `relecteurId` nullable et statut `EN_ATTENTE_SANS_RELECTEUR`, documentés en section 7.2 du CDC, EF6, RG6, D2 et D4.
- Environ 5 min, estimation rétrospective, sur le statut renvoyé par `POST /api/exercices`. La réponse contient `EN_ATTENTE` ou `EN_ATTENTE_SANS_RELECTEUR` après la tentative d'assignation ; `DEPOSE` est transitoire et non renvoyé au client. Cette décision est alignée dans EF4, M4/M5 et la note de D4.
- L'historique comporte déjà des commits de code sur les branches `ticket/M1` à `ticket/M13` avant la pose prévue du jalon d'analyse. Par exemple, le sommet de `ticket/M13-auto-cloture` est `e5d2fd8`, daté du 25 septembre 2026. Au moment de la rédaction, le 27 septembre 2026, `main` est à `c9ec2a5` et ne contient pas encore `[JALON] analyse`. L'ordre réel n'est donc pas « analyse jalonnée avant tout code » ; il est consigné ici sans antidater ni réécrire l'historique.
- Les issues GitHub sont à créer ou mettre à jour ; elles ne sont pas déclarées comme créées par cette entrée.

**IA :**
- Freebuff m'a aidé à auditer et mettre en cohérence le cahier des charges, les diagrammes, le contrat et le backlog ; les décisions métier ont été confirmées par moi.
- Vérifications effectuées :
  - Relecture des documents concernés et inspection des diffs avant commit ; les changements récents du contrat, du backlog, d'EF4 et de D4 étaient limités au fichier convenu pour chacun.
  - Le YAML du contrat a été parsé ; les cinq opérations, leurs codes, les termes interdits et le schéma d'erreur partagé ont été contrôlés.
  - Le backlog a été contrôlé pour ses 8 Must, 5 Should et l'absence de RG17-RG26, de la route de configuration de durée et d'auto-clôture.
  - Les sources des diagrammes Mermaid ont été relues. Leur rendu dans un navigateur n'a pas été vérifié.
- Le message de commit demandé pour cette entrée comporte la date du 25 septembre 2026, mais cette entrée est rédigée et committée le 27 septembre 2026 ; cette date de titre ne prétend pas être la date réelle de rédaction.

## Étape 2 — Première version

**Fait :** Backend M1 (#15) livré : entité `Session` conforme à D2 (`5b91678`), DTO `SessionCreateDto`/`SessionResponseDto` (`9a6d14e`), service avec génération d’un code de 6 caractères et expiration à +15 min (RG1, `e7611fd`), contrôleur `POST /api/sessions` → 201 (EF2, B2, `fd43926`). Ajout de la gestion globale B4 (`bdfd510`) et des migrations Flyway V1/V2 avec `ddl-auto=none` (B5, `3e4e6c1`). Le repository a été nettoyé (`5311e7f`). Les tests couvrent RG1, le contrat MVC et l’endpoint en contexte Spring Boot sur H2 : 7 tests passent. Wrapper Maven ajouté (B1, `3ad0f66`). Frontend M1 : couche API dédiée, hook `useAsync`, formulaire titre/promotion avec états de chargement et d’erreur; `npm run build` réussit (`df49579`). Choix Flyway et wrapper documentés dans le CDC (`0d69fdb`).

**Bloqué :** Deux problèmes rencontrés puis résolus : (1) les coordonnées Flyway PostgreSQL sans version explicite n’étaient pas gérées par Spring Boot 3.2.0; choix retenu après vérification du build : Flyway 9 avec `flyway-core` seul (`3870e5f`). Le commit intermédiaire `22d7082` a ensuite été corrigé. (2) Surefire ne détectait pas le suffixe `IT`; le test Spring Boot a été renommé `SessionControllerSpringBootTest` afin que `mvn test` exécute toute la suite (`f3269c5`). Durées perdues non mesurées.

**IA :** Freebuff a présenté les options de dépendance Flyway; j’ai retenu Flyway 9 avec `flyway-core` seul après l’échec Maven sur les modules PostgreSQL sans version, puis validé le choix par `./mvnw test` (7 tests) et `./mvnw clean compile`. La détection Surefire a été vérifiée en lançant la suite complète après renommage. Les changements ont été contrôlés par rapport au contrat API et au CDC.

### M2 — Marquer la présence (issue #16)

**Fait :** Entités `Etudiant` et `Presence` mappées sur V1, DTO d’entrée et de sortie conformes au contrat, repository avec recherche par session/étudiant (RG15), service et endpoint `POST /api/presences` → 201. RG1/RG2 (expiration), RG15 (unicité) et RG3 (5 codes inconnus → blocage de 2 minutes) sont implémentés ; le compteur RG3 est en mémoire et s’applique à l’étudiant, car le contrat n’identifie pas la session quand le code est inconnu. Le gestionnaire d’erreurs renvoie 429 `{code, message}` pour `TROP_TENTATIVES`. Tests ajoutés : 5 unitaires et 3 d’intégration pour M2 ; la suite complète compte 15 tests et passe. Frontend : couche API dédiée, formulaire de présence avec états de chargement/erreur et routes React Router `/formateur`, `/etudiant`, `/relecteur`. `/relecteur` est un écran placeholder ; la relecture n’est pas implémentée dans M2. `npm run build` passe.

**Bloqué :** Le premier test d’intégration nominal échouait (500) car les méthodes partageaient le code de session `CODE01`, provoquant plusieurs résultats pour la recherche par code. Résolu en supprimant présences et sessions avant chaque test. La règle RG3 est approchée au niveau étudiant pour les codes inconnus : le corps `{code, etudiantId}` ne permet pas d’identifier la session visée si le code est invalide. Les compteurs sont volatils et sont réinitialisés au redémarrage de l’application.

**IA :** Freebuff a aidé à implémenter le flux API, le formulaire et le blocage RG3. Les règles ont été vérifiées avec les tests unitaires et d’intégration, notamment le scénario de cinq erreurs puis une réponse 429, ainsi que la suite complète `./mvnw test` (15 tests). Le build frontend a été vérifié par `npm run build`.

### M3 — Ajouter une présence manuellement (issue #17)

**Fait :** DTO `PresenceFormateurCreateDto` (`sessionId`, `etudiantId`), `SessionInconnueException` et réponse 404 `SESSION_INCONNUE`. `PresenceService.enregistrerPresenceFormateur` vérifie l’existence de la session et l’unicité, puis enregistre la présence avec `source=FORMATEUR` (EF3, RG13, Q14). L’endpoint `POST /api/presences/formateur` retourne 201 avec le DTO contractuel. Trois tests unitaires et trois tests d’intégration couvrent le service et l’endpoint ; la suite compte 21 tests. Le formulaire React d’ajout manuel est intégré à `/formateur`, avec affichage de la source renvoyée et états de chargement/erreur (F2, F3). `npm run build` passe.

**Bloqué :** Aucun blocage significatif. La sous-étape B.1 (repository) était un NO-OP : `findBySessionIdAndEtudiantId` existait déjà depuis M2. Le CDC documentait déjà Q14/RG13 et la source `FORMATEUR` ; aucune modification supplémentaire n’a été nécessaire.

**IA :** Freebuff a aidé à implémenter la méthode de service, le contrôleur, les tests et le formulaire. La source `FORMATEUR`, le statut 201 et les erreurs session inconnue/validation ont été vérifiés par les tests unitaires et Spring Boot. Les vérifications finales ont été faites avec `./mvnw clean test` (21 tests) et `npm run build`.

### M4 — Déposer un exercice (issue #18)

**Fait :** Entité `Exercice` refondue pour V1 (`relecteurId` nullable, unicité RG16) ; nouveaux DTO `ExerciceCreateDto` et `ExerciceResponseDto` ; repository avec recherches par étudiant/session et par session. `ExerciceService` valide les URL http(s), vérifie la clôture de la session et l’unicité, puis crée le dépôt avec statut `EN_ATTENTE` (EF4, RG16). `POST /api/exercices` répond 201 avec `{id, statut}` ; les erreurs de lien invalide renvoient 400 `LIEN_INVALIDE`, les doublons 409 `EXERCICE_DEJA_DEPOSE`. Cinq tests unitaires et quatre tests d’intégration M4 ; la suite totale compte 30 tests. Formulaire React de dépôt intégré à `/etudiant`, avec API dédiée et états de chargement/erreur (F2, F3). `npm run build` passe.

**Bloqué :** L’entité `Exercice`, le repository et un ancien `ExerciceDto` étaient présents sur `main`, mais l’entité et le DTO utilisaient un modèle antérieur à V1. L’entité a été refondue ; l’ancien DTO non utilisé a été laissé en place. Le repository a été complété avec les méthodes nécessaires à RG16 et à l’assignation M5.

**IA :** Freebuff a aidé à refondre le mapping JPA et à implémenter la validation URI. Vérifications : liens sans schéma et schémas autres que HTTP(S) refusés ; session inconnue, dépôt dupliqué, réponse 201 et statut initial `EN_ATTENTE` couverts par les tests. `./mvnw clean test` passe (30 tests) et `npm run build` réussit.

### M5 — Assigner un relecteur automatiquement (issue #19)

**Fait :** `AssignationRelecteurService` choisit aléatoirement avec `SecureRandom` parmi les étudiants présents. L’auteur est exclu (RG4), ainsi que les étudiants ayant déjà un exercice à relire dans cette session (RG17, unicité implicite du rôle de relecteur). `ExerciceService.deposer()` effectue l’assignation lors du dépôt : avec candidat, `relecteurId` est renseigné et le statut vaut `EN_ATTENTE` ; sans candidat, `relecteurId = null` et le statut vaut `EN_ATTENTE_SANS_RELECTEUR` (Q7+Q12). Tests unitaires couvrant absence de présents, candidats tous occupés, un candidat, plusieurs candidats et exclusions ; les deux statuts sont couverts en intégration. La suite backend compte 37 tests. Frontend : feuille CSS responsive commune (layout, cartes, champs, boutons, alertes) et messages explicites pour les deux statuts dans le formulaire de dépôt.

**Bloqué :** Aucun blocage majeur. Une assertion de test pour plusieurs candidats a nécessité une correction de compilation (`a9bbf31`), puis la suite complète a passé.

**IA :** Freebuff a aidé à implémenter l’assignation, les tests et le style global. Vérifié que l’auteur et les candidats déjà occupés sont exclus, qu’un statut dédié est renvoyé en l’absence de candidat, et que les deux résultats sont présentés lisiblement côté frontend.

### M6 — Rendre une relecture (issue #20)

**Fait :** Entité `Relecture` conforme à V1 (unicité sur `exerciceId`), DTO d’entrée avec validation de note 0–20 et DTO de réponse. `RelectureService.rendre()` vérifie l’existence de l’exercice, l’affectation, l’auto-relecture (RG4), la double soumission (RG9) et la note (RG8), puis marque la relecture et l’exercice `RELUE`. `POST /api/relectures/{id}` utilise `{id}` comme identifiant d’exercice et renvoie 200. Les erreurs exposent les codes prévus : `NOTE_INVALIDE` (400), `AUTO_RELECTURE` (403), `RELECTURE_DEJA_RENDUE` (409). Six tests unitaires et quatre tests d’intégration couvrent le service et l’endpoint ; la suite totale compte 47 tests. Le formulaire React de relecture est disponible dans l’espace `/relecteur`.

**Bloqué :** Aucun blocage majeur. Les tests d’intégration ont mis en évidence que la validation `@Valid` renvoyait initialement `VALIDATION_ERROR` pour une note hors limites ; le mapping a été aligné sur `NOTE_INVALIDE` et vérifié en intégration.

**IA :** Freebuff a aidé à implémenter le parcours et les tests. Vérifié que l’identifiant de route désigne l’exercice, que le succès est HTTP 200 et que les erreurs de note, auto-relecture et double rendu utilisent les statuts/codes contractuels. `./mvnw clean test` et `npm run build` ont été exécutés.

### M7 — Consulter le tableau (issue #22)

**Fait :** Entité `Promotion` et `PromotionRepository` mappés sur V1, `TableauDto` conforme au contrat et `PromotionInconnueException` mappée en 404 `PROMOTION_INCONNUE`. Repositories étendus pour compter présences et exercices par étudiant, compter les relectures à faire et charger les notes associées aux exercices. `TableauService` agrège une ligne par étudiant : présences, dépôts, moyenne des notes reçues (calculée côté API conformément à F3, `null` en l’absence de notes), et relectures en attente. `GET /api/tableau?promotionId=X` renvoie 200. Quatre tests unitaires et trois tests d’intégration couvrent l’agrégation, la moyenne nulle, la promotion inconnue et le paramètre manquant ; la suite compte 54 tests. Frontend : API `tableauApi` et tableau responsive à cinq colonnes intégré à `/formateur`; la moyenne fournie par l’API est seulement formatée à deux décimales, jamais recalculée.

**Bloqué :** Aucun blocage majeur. En intégration, Jackson sérialise la moyenne nulle comme `moyenne: null`; le test vérifie explicitement cette représentation.

**IA :** Freebuff a aidé à réaliser l’agrégation, les tests et l’affichage. Vérifié que le tableau ne contient que les étudiants de la promotion, que l’erreur promotion inconnue renvoie 404 `PROMOTION_INCONNUE`, et que le frontend consomme la moyenne de l’API sans recalculer (F3). `./mvnw clean test` et `npm run build` ont été exécutés.

### M8 — Clôturer une session manuellement (issue #41)

**Fait :** `SessionDetailDto`, `SessionDejaClotureeException` (409) et `SessionService.cloturer()` ajoutés. `POST /api/sessions/{id}/cloture` renvoie 200 avec les détails de la session; les erreurs sont 404 `SESSION_INCONNUE` et 409 `SESSION_DEJA_CLOTUREE`. Après clôture, le dépôt est refusé avec le code dédié `SESSION_CLOTUREE` (409), distinct de `EXERCICE_DEJA_DEPOSE`, et `RelectureService` refuse les nouvelles relectures/modifications (RG11, RG14). Trois tests d’intégration dédiés couvrent l’endpoint de clôture; les tests unitaires et MVC couvrent service, erreurs et réponse. La suite backend compte 66 tests et passe. Frontend : `cloturerSession` et `SessionClotureForm` ajoutés à `/formateur`, avec confirmation avant action et affichage des états succès/erreur; `npm run build` passe.

**Bloqué :** Aucun blocage. L’audit a relevé que `EXERCICE_DEJA_DEPOSE` était sémantiquement incorrect pour un dépôt après clôture; une exception et un code spécifiques `SESSION_CLOTUREE` ont été ajoutés et testés.

**IA :** Freebuff a aidé à réaliser le service, l’endpoint, les protections métier, les tests et le formulaire. Vérifié la clôture manuelle (Q12), les réponses HTTP 200/404/409, le refus de dépôt et de relecture après clôture, ainsi que l’absence de régression avec `./mvnw clean test` (66 tests) et `npm run build`.

## Étape 3 — Enveloppe

### Bug #52 — Présences simultanées non reproduites

**Fait :** Issue #52 créée pour documenter le symptôme du client ("2 étudiants tapent le code en même temps, 1 seul apparaît"). Le scénario de concurrence (2 puis 10 threads simultanés) n'a pas permis de reproduire la race condition. Investigation : PresenceService n'avait ni @Transactional ni capture de DataIntegrityViolationException — vulnérabilité réelle même sans reproduction exacte. Branche fix/race-condition-presence avec 2 commits : test de robustesse (échoue avant, passe après) + fix (transaction + saveAndFlush + traduction en DejaPresentException).

**Bloqué :** Le symptôme exact n'a pas été reproduit. Décision : traiter la vulnérabilité identifiée (bonne pratique Spring) plutôt que de prétendre avoir reproduit un bug inexistant dans le scénario testé.

**IA :** Freebuff a proposé plusieurs itérations de test de concurrence, toutes vertes. J'ai décidé d'arrêter la recherche de reproduction et de corriger la vulnérabilité documentée.

### Changement #53 — Double relecture

**Fait :** Issue #53 créée. Impact : 1 relecteur → 2 relecteurs par exercice, note = moyenne des 2, provisoire si 1 seule relecture. LOT A : CDC mis à jour (RG5 remplacée, RG18 ajoutée, contradictions tranchées), diagrammes D2 et D4 corrigés. LOT B : contrat api/contrat.yaml (+ champ provisoire) et migration V2 (UNIQUE exercice_id retirée, UNIQUE (exercice_id, relecteur_id) ajoutée, relecteur2_id sur exercices). LOT C : backend complet. LOT D : frontend (badge provisoire dans le tableau, relecteurId optionnel dans le formulaire). 74 tests passent.

**Bloqué :** 3 corrections en cascade : typo RELECTEUR_NON_ASSIGNE, import manquant eq() dans les tests, stub manquant sessionRepository. Toutes corrigées manuellement.

**IA :** Freebuff a implémenté le backend et le frontend. Vérifié que la moyenne est calculée côté API (F3), que les 2 relecteurs sont distincts (RG18), et que le provisoire est correctement propagé.

### Sacrifice de périmètre

**Décision :** Le changement #53 est un Must tardif. Pour l'absorber dans le temps imparti, on sacrifie les 5 tickets Should (S1-S5) : S1, S2, S3, S4, S5. Ces tickets restent ouverts comme "Could / Won't" pour cette épreuve. Un périmètre réduit et assumé vaut mieux qu'un périmètre annoncé et non tenu.


## Étape 4 — Version finale

**Fait :**

**Bloqué :**

**IA :**

## Étape 5 — Épreuve Git

**Fait :**

**Bloqué :**

**IA :**

## Étape 6 — Soumission

**Fait :**

**Ce que je referais autrement avec plus de temps :**

