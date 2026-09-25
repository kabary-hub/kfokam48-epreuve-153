package com.presencekf.backend.dto;

import java.util.List;

public class TableSchema {
    private String name;
    private List<Column> columns;

    public TableSchema() {}
    public TableSchema(String name, List<Column> columns) {
        this.name = name;
        this.columns = columns;
    }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public List<Column> getColumns() { return columns; }
    public void setColumns(List<Column> columns) { this.columns = columns; }
}
