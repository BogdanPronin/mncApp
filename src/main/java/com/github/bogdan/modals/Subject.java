package com.github.bogdan.modals;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "subject")
public class Subject {
    @DatabaseField(generatedId = true)
    private int id;
    @DatabaseField
    private String name;
    public Subject() {
    }

    public Subject(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
