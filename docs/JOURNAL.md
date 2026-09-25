# Journal de bord — KF48-153

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

## Étape 1 — Analyse

**Fait :**
- `docs/CAHIER_DES_CHARGES.md` (10 sections, EF/RG/RQ, zones d’ombre, contraintes, définition de Done).
- `docs/BACKLOG.md` (découpage de tickets avec modèles et critères d'acceptation).
- `docs/diagrammes/D1` (cas d'utilisation), `D2` (modèle de données), `D3` (séquence présence), `D4` (états de l'exercice).
- `api/contrat.yaml` complété et figé : 5 opérations imposées, extensions de durée, auto-clôture, relecture anonyme, modification de note seule.
- `README.md` cible avec architecture, patterns, Git flow et TDD.
- `SOUMISSION.md` prérempli pour la soumission finale.
- `.gitignore` et `docs/JOURNAL.md` posés.

**Bloqué :**
- Impossible de créer des issues GitHub (CLI `gh` absente et erreur de commande). À résoudre côté collaborateur.

**IA :**
- M'a demandé de confirmer la route de durée, le blocage Q4 et la version d'analyse. Les réponses ont été intégrées et vérifiées par relecture croisée du sujet, du contrôle et du contrat.

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
