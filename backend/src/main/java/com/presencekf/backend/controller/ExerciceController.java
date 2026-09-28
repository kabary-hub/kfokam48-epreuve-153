package com.presencekf.backend.controller;

import com.presencekf.backend.dto.ExerciceCreateDto;
import com.presencekf.backend.dto.ExerciceResponseDto;
import com.presencekf.backend.entity.Exercice;
import com.presencekf.backend.service.ExerciceService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 * Contrôleur REST pour les exercices.
 *
 * M4 / EF4 : POST /api/exercices {sessionId, etudiantId, lien} → 201
 * {id, statut}.
 *
 * B2 : conformité au contrat api/contrat.yaml.
 * B3 : aucune requête base dans le contrôleur.
 */
@RestController
@RequestMapping("/api/exercices")
public class ExerciceController {

    private final ExerciceService exerciceService;

    public ExerciceController(ExerciceService exerciceService) {
        this.exerciceService = exerciceService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ExerciceResponseDto deposer(@Valid @RequestBody ExerciceCreateDto dto) {
        Exercice exercice = exerciceService.deposer(
                dto.getSessionId(), dto.getEtudiantId(), dto.getLien());
        return new ExerciceResponseDto(exercice.getId(), exercice.getStatut());
    }
}
