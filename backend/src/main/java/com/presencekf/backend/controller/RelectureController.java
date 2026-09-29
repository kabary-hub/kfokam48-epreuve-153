package com.presencekf.backend.controller;

import com.presencekf.backend.dto.RelectureCreateDto;
import com.presencekf.backend.dto.RelectureResponseDto;
import com.presencekf.backend.entity.Relecture;
import com.presencekf.backend.service.RelectureService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Contrôleur REST pour les relectures.
 *
 * M6 / EF7 : POST /api/relectures/{id}, où {id} est l'identifiant de
 * l'EXERCICE (conforme au contrat api/contrat.yaml). Réponse 200, pas 201.
 */
@RestController
@RequestMapping("/api/relectures")
public class RelectureController {

    private final RelectureService relectureService;

    public RelectureController(RelectureService relectureService) {
        this.relectureService = relectureService;
    }

    @PostMapping("/{id}")
    public ResponseEntity<RelectureResponseDto> rendre(
            @PathVariable Long id,
            @RequestParam Long relecteurId,
            @Valid @RequestBody RelectureCreateDto dto) {
        Relecture relecture = relectureService.rendre(
                id, relecteurId, dto.getNote(), dto.getCommentaire());
        return ResponseEntity.ok(new RelectureResponseDto(
                relecture.getId(),
                relecture.getExerciceId(),
                relecture.getStatut()
        ));
    }
}
