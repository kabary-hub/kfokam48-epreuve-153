package com.presencekf.backend.dto;

import java.util.List;

public class SchemaDataDto {
    private List<TableSchema> tables;

    public SchemaDataDto() {}
    public SchemaDataDto(List<TableSchema> tables) {
        this.tables = tables;
    }

    public List<TableSchema> getTables() { return tables; }
    public void setTables(List<TableSchema> tables) { this.tables = tables; }
}
