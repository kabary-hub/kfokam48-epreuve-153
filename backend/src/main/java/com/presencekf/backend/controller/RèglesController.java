package com.presencekf.backend.controller;

import com.presencekf.backend.dto.RègleDto;
import com.presencekf.backend.service.RèglesService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/règles")
public class RèglesController {

    private final RèglesService règlesService;

    public RèglesController(RèglesService règlesService) {
        this.règlesService = règlesService;
    }

    @GetMapping
    public List<RègleDto> getAllRègles() {
        return règlesService.getAllRègles();
    }

    @GetMapping("/{id}")
    public RègleDto getRègle(@PathVariable Long id) {
        return règlesService.getRègle(id);
    }
}
