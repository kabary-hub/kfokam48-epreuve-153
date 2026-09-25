package com.presencekf.backend.dto;

public class Column {
    private String name;
    private String type;
    private String constraints;

    public Column() {}
    public Column(String name, String type, String constraints) {
        this.name = name;
        this.type = type;
        this.constraints = constraints;
    }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public String getConstraints() { return constraints; }
    public void setConstraints(String constraints) { this.constraints = constraints; }
}
