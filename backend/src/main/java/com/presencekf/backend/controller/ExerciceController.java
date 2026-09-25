package com.presencekf.backend.controller;

import com.presencekf.backend.dto.ExerciceDto;
import com.presencekf.backend.service.ExerciceService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/sessions/{sessionId}/exercices")
public class ExerciceController {

    private final ExerciceService exerciceService;

    public ExerciceController(ExerciceService exerciceService) {
        this.exerciceService = exerciceService;
    }

    @PostMapping
    public ExerciceDto deposerExercice(@PathVariable Long sessionId,
                                       @RequestParam String lien,
                                       @RequestParam String etudiant) {
        return exerciceService.deposerExercice(sessionId, lien, etudiant);
    }

    @GetMapping("/{id}")
    public ExerciceDto getExercice(@PathVariable Long sessionId,
                                   @PathVariable Long id) {
        return exerciceService.getExercice(sessionId, id);
    }
}
