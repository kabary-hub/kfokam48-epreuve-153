# Cahier des charges — PresenceKF

**Auteur :** BOUBACAR SIDDIGHI BALDE · **Matricule communiqué :** `kf48-153`  
**Version :** 1 · **Date :** 25 septembre 2026  
**Frontend choisi :** React, pour réaliser les trois interfaces demandées avec un framework autorisé et une application monopage distincte de l’API Spring Boot.

> Le candidat a communiqué le numéro `153`, puis a précisé la forme `kf48-153`. Le dépôt existant est `kabary-hub/kfokam48-epreuve-153`. Reporter exactement le matricule officiel demandé par la plateforme dans la soumission finale.

## 1. Contexte et objectif

La direction de la formation KFOKAM48 veut gérer les présences et la relecture croisée des exercices pour ses sessions de cours. Le parcours doit permettre à un formateur de paramétrer la durée et d’ouvrir une session, à un étudiant de signaler sa présence pendant la fenêtre prévue et de déposer un lien d’exercice, puis au système d’affecter des relectures entre étudiants présents. La session se clôt automatiquement à son échéance configurée selon la décision d’extension prise par le candidat. Le formateur doit consulter des indicateurs par promotion, tandis que le relecteur traite les exercices qui lui sont affectés. L’application vise à remplacer un suivi manuel difficile à consolider par des données consultables et cohérentes. Le rendu visuel n’est pas un objectif de notation ; la conformité au contrat, la démarche, les tests et l’historique Git sont prioritaires.

## 2. Acteurs et rôles

| Acteur | Ce qu’il peut faire | Ce qu’il ne peut pas faire |
|---|---|---|
| Formateur | Paramétrer la durée de la prochaine session, ouvrir une session, obtenir son code de présence, ajouter manuellement une présence dans la fenêtre d’acceptation, consulter le tableau et les détails de sa promotion | Clôturer manuellement une session (non prévu par l’extension retenue), soumettre ou modifier une relecture en tant qu’étudiant ; agir sans que son identité d’acteur soit indiquée dans le prototype |
| Étudiant | Se sélectionner dans la liste, marquer sa présence avec un code non expiré, déposer un lien avant clôture, consulter sa note/commentaire quand la relecture existe | Marquer sa présence après expiration, déposer après clôture, consulter l’identité du relecteur, relire son propre exercice |
| Relecteur | Rôle tenu par un étudiant affecté : commencer la relecture, rendre une note et un commentaire, modifier sa relecture avant clôture (décision Q10) | Relire son propre exercice, commencer une relecture non affectée, relire plus d’un exercice par session selon l’hypothèse retenue |

Le relecteur n’est pas une entité/identité distincte : c’est un étudiant désigné sur une affectation de relecture.

## 3. Périmètre

**Inclus dans la version livrée :**
- Ouverture des sessions avec une durée de fermeture automatique paramétrée entre 15 et 480 minutes ; génération d’un code à six caractères alphanumériques, choix de projet. Aucune clôture manuelle n’est prévue selon la décision du candidat.
- Présence avec code, expiration après 15 minutes, unicité par étudiant/session, gestion de cinq erreurs puis blocage de deux minutes par session navigateur, et ajout manuel visible comme `FORMATEUR`.
- Dépôt d’un lien d’exercice par étudiant présent, jusqu’à clôture de la session.
- Affectation aléatoire à la soumission parmi les étudiants présents admissibles, en excluant l’auteur ; attente visible si aucun candidat n’est disponible.
- Relecture démarrable explicitement, note entière 0–20, commentaire, affichage sans nom du relecteur et modification jusqu’à clôture.
- Tableau par promotion avec agrégats et présence détaillée par session ; API de consultation des exercices et affectations.
- Démonstration, migrations versionnées, tests et documentation de démarrage.

**Explicitement exclu :**
- Authentification, gestion sécurisée des comptes, autorisations de production et identité vérifiée ; les identifiants d’acteur transmis par en-têtes sont uniquement un mécanisme de démonstration.
- CRUD des promotions, étudiants et formateurs : référentiels préchargés en données de démonstration.
- Notifications, téléversement de fichiers (seul un lien est conservé), application mobile native, historique/audit de toutes les versions de notes.
- Garantie de sécurité anti-devinette distribuée entre plusieurs instances ou contre le changement d’identité/session navigateur.

## 4. Exigences fonctionnelles

| Réf | Exigence | Critère d’acceptation vérifiable | Priorité |
|---|---|---|---|
| EF1 | Marquer sa présence avec un code | Étant étudiant, si je donne un code existant et non expiré, l’API répond `201` et la présence apparaît dans le tableau | Must |
| EF2 | Configurer puis ouvrir une session | Le formateur configure la durée globale (15–480 min), puis ouvre une session avec titre et promotion ; le POST retourne `201` au format imposé, utilise le réglage en attente, et fixe l’expiration du code à +15 min | Must |
| EF3 | Ajouter une présence manuellement | Si le formateur ajoute la présence durant la fenêtre de 15 minutes, elle apparaît avec source `FORMATEUR` | Must |
| EF4 | Déposer le lien d’exercice | Si un étudiant présent soumet un URI valide avant clôture, l’API répond `201` et l’exercice est créé | Must |
| EF5 | Remplacer le lien d’exercice | Si l’exercice n’a pas été démarré en relecture et la session n’est pas clôturée, le remplacement réussit ; après démarrage il est refusé | Should |
| EF6 | Affecter un relecteur | Au dépôt, le système tire au hasard un étudiant présent autre que l’auteur et sans affectation dans cette session ; sinon l’exercice est signalé en attente sans relecteur | Must |
| EF7 | Démarrer une relecture affectée | Si le relecteur assigné demande explicitement le démarrage, l’état passe à `EN_COURS` et le remplacement du lien est interdit | Must |
| EF8 | Rendre une relecture | Si le relecteur affecté rend une note entière de 0 à 20 et un commentaire, l’API enregistre la relecture ; auto-relecture refusée `403` | Must |
| EF9 | Modifier la note avant clôture | Si la session n’est pas clôturée, le relecteur affecté peut remplacer uniquement la note ; le commentaire reste inchangé ; après clôture, toute modification est refusée | Must, décision Q10 précisée par le candidat |
| EF10 | Consulter le tableau formateur | Le tableau retourne pour chaque étudiant les compteurs, moyenne calculée par l’API et présence de chaque session de la promotion | Must |
| EF11 | Clôturer automatiquement une session à l’échéance configurée | À la date d’échéance, le serveur clôture la session ; les mutations sont refusées. Après redémarrage, la première requête vérifie et rattrape les échéances dépassées | Must, extension assumée contre Q12 |
| EF12 | Consulter sa note/commentaire | L’étudiant auteur peut voir sa note et son commentaire si rendus, sans l’identité du relecteur | Must |
| EF13 | Voir les relectures affectées anonymement | Un étudiant consulte les liens et états des relectures qui lui sont affectées, sans nom ni ID de l’auteur | Must, décision candidat |
| EF14 | Gérer les codes incorrects | À la cinquième erreur dans une session navigateur, le serveur bloque les tentatives de cette session pendant deux minutes | Must |
| EF15 | Réaffecter les attentes lorsque c’est possible | Lorsqu’une nouvelle présence est créée, les exercices en attente peuvent être attribués aléatoirement à un candidat admissible devenu disponible | Must |

## 5. Exigences non fonctionnelles

| Réf | Exigence | Vérification |
|---|---|---|
| ENF1 | L’écran présence est utilisable sur téléphone | Vérification manuelle à 375 px de largeur, sans débordement horizontal |
| ENF2 | Le tableau répond en moins de 2 secondes pour 60 étudiants et 10 sessions | Test de performance reproductible sur données de démonstration/fixture ; mesure documentée |
| ENF3 | Le code est aléatoire, limité dans le temps et non exposé après sa fenêtre d’usage | Tests d’expiration et de génération ; le code n’est pas réutilisé dans des données triviales |
| ENF4 | Le schéma est versionné | Démarrage vierge exécute les migrations Flyway ; aucune configuration `ddl-auto=update` hors tests |
| ENF5 | Toute erreur HTTP renvoie exactement un objet JSON `{code, message}` sans stack trace | Tests de validation, erreurs métier, 404 et erreur serveur contrôlée |
| ENF6 | Le projet est reproductible sur un poste vierge | Démarrage par `docker compose up` ou au plus trois commandes documentées et testées |
| ENF7 | Les interfaces affichent chargement et erreurs API | Vérification manuelle des états de chargement, succès et erreur |
| ENF8 | Aucune moyenne n’est recalculée par le frontend | Vérification du code frontend : il affiche seulement la moyenne retournée par l’API |

## 6. Règles de gestion

| Réf | Règle | Source |
|---|---|---|
| RG1 | Le code expire 15 minutes après l’ouverture de la session | Q2 |
| RG2 | Une présence par étudiant et par session au maximum | Implicite au besoin ; unicité technique nécessaire |
| RG3 | L’étudiant ne peut pas marquer sa présence après expiration du code | Q3, interprété avec Q2 |
| RG4 | Après cinq erreurs de code, la session navigateur est bloquée deux minutes à partir de la cinquième erreur | Q4 + choix candidat |
| RG5 | Une présence manuelle porte la source `FORMATEUR` ; une présence par code porte `ETUDIANT` | Q14 + contrat |
| RG6 | Un exercice ne peut avoir qu’un seul relecteur affecté | Q6 |
| RG7 | L’auteur ne peut jamais être son propre relecteur | Q5 |
| RG8 | Le relecteur est tiré au hasard parmi les étudiants présents à cette session, hors auteur | Q7 |
| RG9 | Le relecteur peut rendre une note entière comprise entre 0 et 20 | Q9 |
| RG10 | L’auteur voit sa note et son commentaire, mais pas le nom du relecteur | Q8 |
| RG11 | Une relecture non démarrée/non rendue reste en attente et doit être visible au formateur | Q11 |
| RG12 | Un étudiant peut déposer après la fin du code de présence, mais seulement jusqu’à clôture | Q12 ; présence exigée comme décision projet |
| RG13 | Le lien ne peut être remplacé après le démarrage explicite de la relecture | Q13 + décision candidat sur le démarrage explicite |
| RG14 | Le relecteur affecté peut modifier uniquement la note tant que la session n’est pas clôturée ; le commentaire initial reste inchangé et la relecture est figée après clôture | Q10 choisi contre Q15 ; portée précisée par le candidat |
| RG15 | La clôture automatique interdit ensuite les dépôts et toutes les mutations liées à la session | Décision candidat, extension de Q12/EF11 |
| RG16 | La présence manuelle est soumise à la même fenêtre de 15 minutes que le code | Choix candidat ; hypothèse conservatrice |
| RG17 | Un étudiant ne reçoit au maximum qu’un exercice à relire par session | Hypothèse candidat, non exprimée par Q6/Q7 |
| RG18 | Si aucun relecteur n’est disponible au dépôt, l’exercice reste sans relecteur ; chaque nouvelle présence déclenche une tentative d’affectation des attentes | Trou Q7 + choix candidat |
| RG19 | Le code comporte six caractères alphanumériques | Choix candidat, non imposé dans les pièces |
| RG20 | La moyenne est calculée par l’API, arrondie à deux décimales ; elle vaut `null` sans note reçue | Q16 + choix candidat et contrat |
| RG21 | Pour s’identifier dans le prototype, les interfaces utilisent des sélecteurs et transmettent les IDs d’acteur par en-têtes ; cela ne constitue pas une authentification | Q1 + choix candidat |
| RG22 | La durée de session est un réglage global à usage unique, obligatoire avant ouverture, entre 15 et 480 minutes, sans défaut ; une nouvelle valeur remplace toute valeur en attente | Décision candidat |
| RG23 | À l’échéance, un planificateur clôt la session ; si le serveur était arrêté, toute requête après redémarrage clôt les sessions échues avant d’autoriser une mutation | Décision candidat |
| RG24 | La relecture est anonyme dans les deux sens : ni l’auteur n’identifie le relecteur ni le relecteur l’auteur | Décision candidat ; Q8 ne précisait que le premier sens |
| RG25 | Le relecteur affecté peut modifier uniquement la note jusqu’à clôture ; le commentaire reste celui du premier envoi | Décision candidat clarifiant Q10 |
| RG26 | Le POST d’ouverture sans durée globale préconfigurée renvoie `409 DUREE_SESSION_REQUISE` | Décision candidat ; extension au contrat initial |

### Statuts proposés

- Session : `OUVERTE`, `CLOTUREE`. `expirationAt` gouverne la fenêtre du code à +15 min ; `cloturePrevueAt` est calculée à partir de la durée configurée ; un planificateur ferme la session à l’échéance. L’échéance est figée à la création.
- Exercice/relecture : `EN_ATTENTE_SANS_RELECTEUR`, `EN_ATTENTE`, `EN_COURS`, `RELUE`. Une session clôturée verrouille les modifications sans nécessairement changer le statut affiché.
- Source de présence : `ETUDIANT`, `FORMATEUR`.

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
