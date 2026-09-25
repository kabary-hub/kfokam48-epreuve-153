package com.presencekf.backend.controller;

import com.presencekf.backend.dto.SchemaDataDto;
import com.presencekf.backend.service.SchemaDataService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/schema-data")
public class SchemaDataController {

    private final SchemaDataService schemaDataService;

    public SchemaDataController(SchemaDataService schemaDataService) {
        this.schemaDataService = schemaDataService;
    }

    @GetMapping
    public SchemaDataDto getSchemaData() {
        return schemaDataService.getSchemaData();
    }
}
