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

## Étape 3 — Enveloppe

**Fait :**

**Bloqué :**

**IA :**

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
