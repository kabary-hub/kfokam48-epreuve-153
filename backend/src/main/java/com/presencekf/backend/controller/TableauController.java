package com.presencekf.backend.controller;

import com.presencekf.backend.dto.TableauDto;
import com.presencekf.backend.service.TableauService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/sessions/tableau")
public class TableauController {

    private final TableauService tableauService;

    public TableauController(TableauService tableauService) {
        this.tableauService = tableauService;
    }

    @GetMapping
    public List<TableauDto> getTableau() {
        return tableauService.getTableau();
    }

    @GetMapping("/{code}")
    public TableauDto getTableauBySession(@PathVariable String code) {
        return tableauService.getTableauBySession(code);
    }
}
