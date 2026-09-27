package com.presencekf.backend.controller;

import com.presencekf.backend.dto.SessionCreateDto;
import com.presencekf.backend.dto.SessionResponseDto;
import com.presencekf.backend.entity.Session;
import com.presencekf.backend.service.SessionService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 * Contrôleur REST pour les sessions de cours.
 *
 * M1 / EF2 : POST /api/sessions avec corps {titre, promotionId} → 201
 * {id, code, ouvertureAt, expirationAt}.
 *
 * Respecte B2 (contrat api/contrat.yaml) et B3 (aucune requête base
 * dans le contrôleur : tout passe par SessionService).
 */
@RestController
@RequestMapping("/api/sessions")
public class SessionController {

    private final SessionService sessionService;

    public SessionController(SessionService sessionService) {
        this.sessionService = sessionService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public SessionResponseDto ouvrir(@Valid @RequestBody SessionCreateDto dto) {
        Session session = sessionService.ouvrirSession(dto.getTitre(), dto.getPromotionId());
        return new SessionResponseDto(
                session.getId(),
                session.getCode(),
                session.getOuvertureAt(),
                session.getExpirationAt()
        );
    }
}
