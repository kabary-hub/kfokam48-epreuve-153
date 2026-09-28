package com.presencekf.backend.controller;

import com.presencekf.backend.dto.PresenceCreateDto;
import com.presencekf.backend.dto.PresenceFormateurCreateDto;
import com.presencekf.backend.dto.PresenceResponseDto;
import com.presencekf.backend.entity.Presence;
import com.presencekf.backend.service.PresenceService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 * Contrôleur REST pour les présences.
 *
 * M2 / EF1 : POST /api/presences avec corps {code, etudiantId} → 201
 * {id, sessionId, etudiantId, source}.
 *
 * Respecte B2 (contrat api/contrat.yaml) et B3 (aucune requête base
 * dans le contrôleur : tout passe par PresenceService).
 */
@RestController
@RequestMapping("/api/presences")
public class PresenceController {

    private final PresenceService presenceService;

    public PresenceController(PresenceService presenceService) {
        this.presenceService = presenceService;
    }

    @PostMapping("/formateur")
    @ResponseStatus(HttpStatus.CREATED)
    public PresenceResponseDto ajouterParFormateur(
            @Valid @RequestBody PresenceFormateurCreateDto dto) {
        Presence presence = presenceService.enregistrerPresenceFormateur(
                dto.getSessionId(), dto.getEtudiantId());
        return new PresenceResponseDto(
                presence.getId(),
                presence.getSessionId(),
                presence.getEtudiantId(),
                presence.getSource()
        );
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PresenceResponseDto marquer(@Valid @RequestBody PresenceCreateDto dto) {
        Presence presence = presenceService.enregistrerPresence(
                dto.getCode(), dto.getEtudiantId());
        return new PresenceResponseDto(
                presence.getId(),
                presence.getSessionId(),
                presence.getEtudiantId(),
                presence.getSource()
        );
    }
}
