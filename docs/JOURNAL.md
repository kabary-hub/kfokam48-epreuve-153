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

**Fait :**

**Bloqué :**

**IA :**

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
