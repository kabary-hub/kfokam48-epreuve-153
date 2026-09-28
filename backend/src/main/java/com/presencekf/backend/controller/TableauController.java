package com.presencekf.backend.controller;

import com.presencekf.backend.dto.TableauDto;
import com.presencekf.backend.service.TableauService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Contrôleur du tableau récapitulatif du formateur (M7, EF9).
 * GET /api/tableau?promotionId=X → 200 [TableauDto] / 404 PROMOTION_INCONNUE.
 */
@RestController
@RequestMapping("/api/tableau")
public class TableauController {

    private final TableauService tableauService;

    public TableauController(TableauService tableauService) {
        this.tableauService = tableauService;
    }

    @GetMapping
    public List<TableauDto> tableau(@RequestParam Long promotionId) {
        return tableauService.construire(promotionId);
    }
}
