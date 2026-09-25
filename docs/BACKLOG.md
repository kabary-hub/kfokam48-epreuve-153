# Backlog proposé — PresenceKF

> Les tickets doivent être créés comme issues GitHub avant le jalon `[JALON] analyse`. Ce fichier décrit le résultat métier sous forme structurée. Les numéros `#` sont invisibles ici ; en production, il faut mettre le numéro de l’issue réelle dans chaque commit et PR.
>
> La politique actuelle : un ticket par branche, un commit par branche, `Closes #N` dans le commit, fuseau GitHub (PR) vers `main`. La résolution des problèmes métiers complique les demandes de réponse, mais c'est toujours acceptable.

## Must — étape 2, v0.1

### M1 — Configurer la durée globale et ouvrir une session
**Références :** EF2, EF11 · RG22, RG23, RG26  
**Critères d'acceptation :**
- [ ] `PUT /api/sessions/configuration` accepte `dureeMinutes` de 15 à 480 et renvoie `200` avec la durée.
- [ ] Une nouvelle configuration remplace la précédente avant consommation.
- [ ] `POST /api/sessions` retourne `201` avec les champs contractuels si un réglage existe.
- [ ] Sans réglage, `POST /api/sessions` retourne `409 DUREE_SESSION_REQUISE`.
- [ ] Le code de la session expire 15 minutes après ouverture, quelle que soit la durée de session.
- [ ] À l'échéance, la session se clôt automatiquement ; après un redémarrage, la première requête mutante vérifie et applique la clôture restante.

### M2 — Marquer sa présence avec un code
**Références :** EF1, EF14 · RG1–RG4, RG19  
**Critères d'acceptation :**
- [ ] Code encore valide et étudiant absent → `201`, `source=ETUDIANT`.
- [ ] Code inconnu → `400 CODE_INCONNU`.
- [ ] Code expiré → `410 CODE_EXPIRE`.
- [ ] Double présence → `409 DEJA_PRESENT`.
- [ ] Cinquième erreur de saisie dans une session navigateur et toutes les tentatives ultérieures de même session → bloquées 2 minutes, réponse `400 TROP_TENTATIVES`.
- [ ] Toute réponse d'erreur utilise `{code, message}` ; aucune stack trace.

### M3 — Ajouter une présence manuellement
**Références :** EF3 · RG5, RG16  
**Critères d'acceptation :**
- [ ] Le formateur peut ajouter la présence pour un étudiant de la promotion dans la fenêtre de 15 minutes.
- [ ] L'enregistrement crée un `source=FORMATEUR`; le tableau et les autres lecteurs affichés font la distinction.

### M4 — Déposer un lien d'exercice
**Références :** EF4 · RG12  
**Critères d'acceptation :**
- [ ] Un étudiant présent (et dans la fenêtre ou après la fenêtre jusqu'à la clôture) peut déposer un URI valide.
- [ ] Lien invalide → `400 LIEN_INVALIDE`.
- [ ] Déjà déposé pour la session → `409 EXERCICE_DEJA_DEPOSE`.

### M5 — Affecter un relecteur et gérer l'absence de candidat
**Références :** EF6, EF15 · RG6–RG8, RG17, RG18  
**Critères d'acceptation :**
- [ ] À la soumission, le système choisit au hasard un étudiant présent autre que l'auteur et sans affectation dans cette session.
- [ ] Si aucun candidat n'existe, l'exercice reste `EN_ATTENTE_SANS_RELECTEUR` et apparaît comme tel dans le tableau.
- [ ] Une nouvelle présence peut déclencher une nouvelle tentative d'affectation.
- [ ] Un relecteur ne peut recevoir qu'un exercice par session ; l'auteur n'est pas son propre relecteur.

### M6 — Démarrer et rendre une relecture
**Références :** EF7, EF8 · RG7, RG9–RG11, RG13, RG24  
**Critères d'acceptation :**
- [ ] Seul l'étudiant affecté peut démarrer explicitement la relecture ; le statut devient `EN_COURS`.
- [ ] L'auto-relecture est refusée en `403 AUTO_RELECTURE`.
- [ ] Note entière de 0 à 20 avec commentaire ; note invalide → `400 NOTE_INVALIDE`.
- [ ] Soumission déjà rendue → `409 RELECTURE_DEJA_RENDUE`.
- [ ] Après redémarrage ou autre tentative, le relecteur peut conserver son état courant sans modification de la note initiale du commentaire.

### M7 — Modifier la note avant l'échéance de session
**Références :** EF9 · RG14, RG25  
**Critères d'acceptation :**
- [ ] Une fois rendue, la note peut être remplacée tant que la session n'est pas clôturée.
- [ ] Après clôture, l'opération échoue sans changer les données.
- [ ] Le commentaire initial reste inchangé après la première soumission et les modifications de note.

### M8 — Consulter le tableau et les exercices d'une session
**Références :** EF10 · RG11, RG20  
**Critères d'acceptation :**
- [ ] Chaque ligne garde les champs contractuels initiaux.
- [ ] `presences` est le total et `presencesParSession` contient aussi les absences explicites.
- [ ] La moyenne est calculée côté API, arrondie à deux décimales, `null` si aucune note.
- [ ] `exercicesSansRelecteur` identifie clairement les devoirs non traités.

### M9 — Consulter sa note et son commentaire
**Références :** EF12 · RG10, RG24  
**Critères d'acceptation :**
- [ ] L'auteur consulte la note et le commentaire s'ils existent.
- [ ] Aucun identifiant ou nom de relecteur n'est présent dans la réponse API.

### M10 — Consulter les relectures anonymisées
**Références :** EF13 · RG24  
**Critères d'acceptance :**
- [ ] L'étudiant voit ses exercices relecteurs avec leur statut.
- [ ] Le nom et l'ID de l'auteur sont absents de la réponse.
- [ ] Au besoin, l'interface montre une indication neutre et ne divulgue pas l'auteur.

### M11 — Prendre en charge les références d'identité et le sélecteur
**Références :** EF2, EF10 · ENF6  
**Critères d'acceptation :**
- [ ] L'API exporte les promotions, les étudiants et les sessions d'une promotion.
- [ ] L'interface frontend ne code pas en dur les IDs métier de démo.
- [ ] Les identifiants sont transmis par en-tête à des fins de démonstration, sans établir un contenu de sécurité.

### M12 — Versionner le schéma et fournir une démonstration démarrable
**Références :** ENF4, ENF6 · B5, F1–F3  
**Critères d'acceptation :**
- [ ] Les migrations Flyway construisent une base vide ; `ddl-auto=update` est absent des tests.
- [ ] Les données de démonstration sont minimales et déterministes.

### M13 — Aligner la clôture automatique
**Références :** EF11 · RG15, RG23  
**Critères d'acceptation :**
- [ ] À l'échéance, la session redevient `CLOTUREE`.
- [ ] Toutes les opérations de mutation reçoivent une réponse d'erreur et ne modifient rien après la clôture.
- [ ] Le correcteur peut simuler un redémarrage ou un planificateur absent et vérifier que la première requête mutante applique la clôture.

## Should — après le socle Must

### S1 — Remplacer un lien avant le démarrage de la relecture
**Références :** EF5 · RG13  
**Critères d'acceptation :** lien modifiable uniquement en `EN_ATTENTE`, immuable après démarrage ou clôture.

### S2 — Améliorer la lecture du tableau sur mobile
**Références :** ENF1  
**Critères d'acceptation :** l'interface reste lisible à 375 px sans perte de données et sans débordement horizontal.

## Could / Won't

- **Could :** historique des modifications, notifications, importation des référentiels.
- **Won't pour l'épreuve :** comptes sécurisés, upload de fichiers, application native, administration CRUD des référentiels, clôture manuelle, sécurité de production.

## Modèle de ticket

### Modèle « CRÉATION / SOUMISSION »

```markdown
## Contexte
Pourquoi cet objet métier doit exister ; quel besoin de l'énoncé ou de l'usage.

## Critères d'acceptation
- [ ] Quand ... alors ...
- [ ] Pas d'interprétation : chaque critère doit être vérifiable par un test ou une observation.

## Règles de gestion applicables
- RGx : ...
- RGy : ...

## Préconditions
- Rôle/identité : ...
- État de l'objet : ...
- Données existantes : ...
- Permissions : ...

## Données d'entrée et contraintes
- Champ : type/format, obligatoire, longueurs, valeurs autorisées, dépendances.

## Règles de validation transversales
- Champs obligatoires, formats, nettoyage, casse, caractères interdits, messages d'erreur.

## Scénario nominal
1. L'acteur accède au formulaire ou à l'action.
2. Il déclenche l'envoi.
3. Le système valide les données.
4. Le système consomme ou persiste les données.
5. Le système confirme et affiche le résultat.

## Scénarios alternatifs
- Échec de la validation : cause, contrôle, message, comportement, données conservées.
- Objet déjà existant : refus 409, message, conservation des données.
- Règles métier bloquantes : conditionnelle, blocage, retour.
- Session clôturée : comportement refusé.
- Réseau/erreur serveur : message, pas de modification persistée.
- Données incohérentes : rejet et message explicite.

## Postconditions
- État final, données modifiées, visibilité, navigation.

## Critères d'acceptation
- [ ] Vérifiables, mesurables, non interprétables.

## Definition of Done
- Critères de la PR, test, message de commit, mise à jour du CDC/contract, `main` démarrable.
```

### Modèle « MODIFICATION »

```markdown
## Contexte
Pourquoi cette modification est nécessaire ; dans quels cas elle intervient.

## Critères d'acceptation
- [ ] Quand ... alors ...
- [ ] Mesurables et vérifiables.

## Règles de gestion applicables
- RGx : ...

## Préconditions
- Rôle, état de l'objet, données existantes, permissions.

## Données modifiables
- Champ : modifiable, type, contrainte, message d'erreur, sensibilité.

## Scénario nominal
1. ...

## Scénarios alternatifs
- ...
```

### Modèle « CONSEIL CONTRE L’ANALYSE SILENCIEUSE »

```markdown
## Regles generales
- Ne jamais supposer un comportement sans source écrite.
- Ne jamais transformer un objet contrat imposé sans l'exiger.
- Si la documentation client, le sujet et l'API sont violés, soit le corriger, soit ouvrir une question au chef.
- Tout changement invalide un autre livre.
```

## Notes de coordination Git

- Chaque ticket crée une branche `feat/...`, une PR, un commit `Closes #N`.
- Les commits métier doivent citer le ticket, l'exigence `EFx` et la règle `RGx` si applicable.
- `main` est toujours intégrable et ne reçoit pas de force.
