package com.presencekf.backend.controller;

import com.presencekf.backend.dto.RelectureDto;
import com.presencekf.backend.service.RelectureService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/relectures")
public class RelectureController {

    private final RelectureService relectureService;

    public RelectureController(RelectureService relectureService) {
        this.relectureService = relectureService;
    }

    @PostMapping
    public RelectureDto demarrerRelecture(@RequestParam Long exerciceId) {
        return relectureService.demarrerRelecture(exerciceId);
    }

    @PutMapping("/{id}")
    public RelectureDto rendreRelecture(@PathVariable Long id) {
        return relectureService.rendreRelecture(id);
    }
}
