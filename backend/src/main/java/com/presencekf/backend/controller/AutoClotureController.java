package com.presencekf.backend.controller;

import com.presencekf.backend.dto.AutoClotureDto;
import com.presencekf.backend.service.AutoClotureService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/sessions/{id}/auto-cloture")
public class AutoClotureController {

    private final AutoClotureService autoClotureService;

    public AutoClotureController(AutoClotureService autoClotureService) {
        this.autoClotureService = autoClotureService;
    }

    @PutMapping
    public AutoClotureDto autoClôturer(@PathVariable Long id) {
        return autoClotureService.autoClôturer(id);
    }

    @GetMapping
    public AutoClotureDto getStatus(@PathVariable Long id) {
        return autoClotureService.getStatus(id);
    }
}
