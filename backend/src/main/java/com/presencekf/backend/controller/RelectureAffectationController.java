package com.presencekf.backend.controller;

import com.presencekf.backend.dto.RelectureAffectationDto;
import com.presencekf.backend.service.RelectureAffectationService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/sessions/{sessionId}/relectures-affectees")
public class RelectureAffectationController {

    private final RelectureAffectationService relectureAffectationService;

    public RelectureAffectationController(RelectureAffectationService relectureAffectationService) {
        this.relectureAffectationService = relectureAffectationService;
    }

    @GetMapping
    public List<RelectureAffectationDto> getRelecturesAffectees(@PathVariable Long sessionId) {
        return relectureAffectationService.getRelecturesAffectees(sessionId);
    }
}
