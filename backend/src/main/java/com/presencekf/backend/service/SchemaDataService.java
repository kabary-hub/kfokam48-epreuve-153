package com.presencekf.backend.service;

import com.presencekf.backend.dto.SchemaDataDto;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SchemaDataService {

    public SchemaDataDto getSchemaData() {
        List<TableSchema> tables = new ArrayList<>();
        tables.add(new TableSchema("session", Arrays.asList(
            new Column("id", "BIGINT", "PRIMARY KEY"),
            new Column("code", "VARCHAR(50)", "UNIQUE"),
            new Column("debut", "TIMESTAMP"),
            new Column("fin", "TIMESTAMP"),
            new Column("ouverte", "BOOLEAN")
        )));
        tables.add(new TableSchema("presence", Arrays.asList(
            new Column("id", "BIGINT", "PRIMARY KEY"),
            new Column("etudiant", "VARCHAR(100)"),
            new Column("code", "VARCHAR(50)"),
            new Column("timestamp", "TIMESTAMP"),
            new Column("session_id", "BIGINT", "FK -> session.id")
        )));
        tables.add(new TableSchema("exercice", Arrays.asList(
            new Column("id", "BIGINT", "PRIMARY KEY"),
            new Column("lien", "VARCHAR(500)"),
            new Column("etudiant", "VARCHAR(100)"),
            new Column("session_id", "BIGINT", "FK -> session.id")
        )));
        tables.add(new TableSchema("relecteur", Arrays.asList(
            new Column("id", "BIGINT", "PRIMARY KEY"),
            new Column("etudiant", "VARCHAR(100)"),
            new Column("exercice_id", "BIGINT", "FK -> exercice.id")
        )));
        tables.add(new TableSchema("relecture", Arrays.asList(
            new Column("id", "BIGINT", "PRIMARY KEY"),
            new Column("exercice_id", "BIGINT", "FK -> exercice.id"),
            new Column("terminee", "BOOLEAN")
        )));
        return new SchemaDataDto(tables);
    }
}
