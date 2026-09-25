package com.presencekf.backend.controller;

import com.presencekf.backend.dto.RelectureModificationDto;
import com.presencekf.backend.service.RelectureModificationService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/relectures/{id}/modification")
public class RelectureModificationController {

    private final RelectureModificationService relectureModificationService;

    public RelectureModificationController(RelectureModificationService relectureModificationService) {
        this.relectureModificationService = relectureModificationService;
    }

    @PutMapping
    public RelectureModificationDto modifierRelecture(@PathVariable Long id,
                                                      @RequestParam String note,
                                                      @RequestParam String commentaire) {
        return relectureModificationService.modifierRelecture(id, note, commentaire);
    }
}
