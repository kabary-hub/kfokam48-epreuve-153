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

## 7. Zones d’ombre, hypothèses et contradictions

| Point | Réponse client ou trou | Décision retenue | Pourquoi / conséquence |
|---|---|---|---|
| Modification après envoi | Q10 autorise jusqu’à clôture ; Q15 dit définitive dès validation | Q10 prévaut | Q10 définit une borne métier explicite ; Q15 la contredit. La modification est exposée par une opération dédiée distincte du POST initial, sans altérer le POST imposé. |
| Assignation et aucun candidat | Q7 ne dit pas quand assigner et ne couvre pas les sessions avec zéro candidat | Tirage au dépôt ; état en attente sans relecteur si nécessaire | Simple à comprendre et visible. Une nouvelle présence déclenche une tentative de réaffectation. |
| Nombre d’exercices par relecteur | Q6 impose un seul relecteur par exercice, pas un seul exercice par relecteur | Limite d’un exercice à relire par étudiant et session | Choix candidat pour répartir la charge ; à distinguer explicitement d’une exigence client. |
| Début de relecture | Q13 interdit le remplacement dès que la relecture « commence » sans définir le signal | Action explicite `demarrer` avant consultation du lien | Évite qu’un GET produise un effet de bord caché. L’API passe à `EN_COURS`. |
| Réaffectation des exercices sans relecteur | Non défini | Chaque nouvelle présence déclenche le traitement des attentes | Évite de laisser une attente bloquée quand un candidat apparaît ensuite. |
| Dépôt sans présence | Q12 permet le dépôt jusqu’à clôture ; Q7 réserve les relecteurs aux présents | Présence requise pour déposer | Hypothèse métier destinée à préserver le parcours de cours. |
| Présence manuelle après expiration | Q14 autorise l’ajout manuel sans en préciser la fenêtre | Même fenêtre de 15 minutes | Règle conservatrice ; empêche que l’ajout manuel contourne la fermeture de présence. |
| Q3 « fin de session » vs Q12 « clôture » | Formulations ambiguës | Fin du code = `expirationAt`; fin des dépôts = clôture automatique à `cloturePrevueAt` | Deux instants distincts ; l’auto-clôture est une extension choisie, contrairement au texte Q12 qui attribue la clôture au formateur. |
| Identité et absence de mot de passe | Q1 exclut les mots de passe étudiants ; le contrat ne porte pas toutes les identités | Sélecteur dans le frontend et IDs d’acteur par en-têtes dédiés | Démonstration uniquement, sans authentification ni sécurité de production. |
| Portée de l’anti-erreur | Q4 ne définit pas l’identité du blocage | Session navigateur identifiée par un ID temporaire transmis par le client ; blocage côté API à la cinquième erreur | Compromis prototype ; contournable par nouvelle session/identité et non adapté à une mise en production. |
| Statut du blocage | Q4 ne spécifie pas le statut HTTP après cinq erreurs | Répondre `400` avec code `TROP_TENTATIVES` | Choix candidat ; distingué d’un code inconnu par le champ stable `code`. |
| Durée de session vs API imposée | Le POST imposé ne prend pas la durée en entrée | `PUT /api/sessions/configuration` règle une durée globale à usage unique ; la dernière valeur remplace l’ancienne ; le POST consomme le réglage | Préserve les champs imposés au succès, mais ajoute une route et le statut d’extension `409 DUREE_SESSION_REQUISE` en l’absence de configuration. Le réglage global peut être écrasé par un autre formateur avant consommation. |
| Arrêt du serveur pendant l’échéance | Un job planifié peut ne pas tourner si l’application est arrêtée | Au premier accès après redémarrage, rattraper et persister toute clôture dont l’échéance est passée | Requiert une vérification transactionnelle avant les opérations mutantes en plus du planificateur. |
| Anonymat du relecteur | Q8 ne dit pas si le relecteur voit l’auteur | Masquer l’auteur au relecteur et le relecteur à l’auteur | Décision candidat, à appliquer aux DTO et aux listes, pas seulement à l’interface. |
| Format du code | Aucune longueur/jeu de caractères spécifié | Six caractères alphanumériques | Choix explicite du candidat ; format et validation doivent être cohérents dans contrat, modèle et tests. |
| Tableau Q16 | Q16 veut une présence à chaque session, l’API imposée ne donne qu’un total | Conserver `presences` et ajouter `presencesParSession`, une entrée par session avec présent/source | Extension du contrat nécessaire ; les absences sont explicites (`present=false`, `source=null`). |
| Identification des exercices en attente | Q11 réclame de les voir, mais le tableau agrégé ne contient pas leur liste | Compteur `exercicesSansRelecteur` par étudiant auteur, plus liste d’affectations destinée au relecteur | Rend visibles les besoins sans faire recalculer les règles métier dans le frontend. |
| Format du matricule | Le candidat communique « 153 », puis précise `kf48-153` ; le modèle d’exemple montre un format avec centre | Conserver `kf48-153` comme valeur communiquée ; vérifier qu’elle correspond au format de la plateforme | Ne pas inventer le suffixe de centre. Le nom du dépôt fourni reste `kfokam48-epreuve-153`. |
| Commit préalable | LISEZ-MOI propose `[JALON] depart` pour tester le push ; le sujet ne le compte pas parmi les trois jalons évalués | Faire le test tel que demandé, puis les jalons évalués dans l’ordre | Le commit de départ ne remplace jamais `[JALON] analyse`, `[JALON] v0.1` ou `[JALON] v1.0`. |
| Ordre du jalon v1.0 | Le sujet présente le jalon puis les livrables finaux | Suivre le choix du candidat : jalon avant CHANGELOG/README/backlog final | Le hash de soumission sera celui du dernier commit réel après ces documents, pas nécessairement le commit jalon. Risque de lisibilité/acceptation de « v1.0 » à assumer. |
| Référentiels de démonstration | Sujet demande « quelques » données ; aucune volumétrie exacte | Jeu de démo réduit avec promotion, étudiants, formateur, session et exemples de présence/exercices/relectures | Éviter une app vide ; les 60 étudiants relèvent du test de performance, pas forcément de la démo. |
| Réglage global concurrent | Une durée en attente est globale et peut être réglée par un autre formateur | La dernière valeur écrase l’ancienne et sera consommée par la prochaine ouverture, quel que soit le formateur | Limitation de prototype connue ; sans authentification, l’isolation entre formateurs est impossible dans cette conception. |

### Contradiction Q10 / Q15 : décision détaillée

Q10 autorise explicitement une correction jusqu’à la clôture de la session, alors que Q15 affirme que toute validation est définitive. La décision est de privilégier Q10 ; Q15 reste consignée comme contradiction. Le relecteur affecté peut modifier uniquement la note après son premier envoi ; le commentaire reste celui du premier envoi. Le contrat initial conserve le `POST /api/relectures/{id}` et son `409` si une deuxième soumission initiale est faite ; une route `PUT` distincte met à jour la note seule avant échéance. Cela préserve l’opération imposée sans la détourner.

## 8. Contraintes techniques

### Imposées par le sujet
- **B1 :** Java 17 ou plus, Spring Boot, Maven et wrapper `mvnw` commité.
- **B2 :** respecter à la lettre les cinq opérations initiales : chemins, verbes, statuts, champs et format d’erreur.
- **B3 :** contrôleur → service → repository ; aucune requête BD depuis un contrôleur ; DTO, jamais d’entité JPA exposée.
- **B4 :** validation des entrées et `@RestControllerAdvice` global ; toute erreur renvoie `{code, message}` sans stack trace.
- **B5 :** schéma versionné avec Flyway ou Liquibase ; migrations commitées ; `ddl-auto=update` interdit hors tests.
- **B6 :** au moins un test unitaire d’une règle métier réelle et un test d’intégration d’un endpoint, exécutables sans base locale personnelle.
- **F1 :** React, Angular ou Next.js déclaré/justifié dans README et build fonctionnel.
- **F2 :** écrans formateur, étudiant et relecteur.
- **F3 :** couche API dédiée, états chargement/erreur, aucune moyenne recalculée côté frontend.
- Démarrage en `docker compose up` ou trois commandes maximum et données de démonstration.

### Choix de conception retenus
- Frontend : React.
- Données relationnelles : PostgreSQL ; migrations : Flyway.
- Frontend : React, TypeScript, Vite ; tests UI prévus avec Vitest.
- Orchestration locale par Docker Compose ; versions précises des outils et dépendances à figer lors de l’étape technique, sans produire de code avant le jalon d’analyse.
- La configuration globale de durée est stockée comme réglage en attente ; elle est consommée atomiquement à l’ouverture pour éviter qu’une valeur soit appliquée à plusieurs sessions.
- Tests : JUnit/Spring Boot Test côté backend ; stratégie frontend à choisir selon l’outil retenu et justifier dans le README.
- Les cinq opérations obligatoires ne seront pas renommées ni détournées. Les opérations supplémentaires seront documentées dans `api/contrat.yaml`.

## 9. Livrables

- `docs/CAHIER_DES_CHARGES.md` (ce document).
- `docs/diagrammes/D1-cas-utilisation.md`.
- `docs/diagrammes/D2-modele-donnees.md`.
- `docs/diagrammes/D3-sequence-presence.md`.
- `docs/diagrammes/D4-etats-exercice.md` (bonus +3).
- `docs/BACKLOG.md` : propositions de tickets à créer comme issues GitHub ; les issues elles-mêmes doivent être créées sur le dépôt.
- `api/contrat.yaml` complété et figé avant le premier commit de code.
- `.gitignore` Java/Node posé avant le premier commit de code.
- Backend Spring Boot, frontend React, Flyway, données de démo, tests et `docker-compose.yml` aux étapes prévues.
- `docs/JOURNAL.md`, tenu à chaque étape ; `CHANGELOG.md`, `README.md` testé, `SOUMISSION.md` final à téléverser sur la plateforme.
- Deux dépôts publics au total : projet `kfokam48-epreuve-153` et dépôt Git séparé de l’épreuve `kfokam48-gitlab-153`.

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
