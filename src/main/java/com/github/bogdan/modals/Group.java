package com.github.bogdan.modals;

import com.j256.ormlite.field.DatabaseField;

public class Group {
    @DatabaseField(generatedId = true)
    private int id;
    @DatabaseField(unique = true)
    private String groupName;
    @DatabaseField(foreign = true,foreignAutoRefresh = true,foreignAutoCreate = false)
    private Subject subject;
    @DatabaseField
    private String dateOfTheCreation;

    public String getDateOfTheCreation() {
        return dateOfTheCreation;
    }

    public void setDateOfTheCreation(String dateOfTheCreation) {
        this.dateOfTheCreation = dateOfTheCreation;
    }

    public Group() {
    }

    public Group(String groupName, Subject subject, String dateOfTheCreation) {
        this.groupName = groupName;
        this.subject = subject;
        this.dateOfTheCreation = dateOfTheCreation;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public Subject getSubject() {
        return subject;
    }

    public void setSubject(Subject subject) {
        this.subject = subject;
    }

    @Override
    public String toString() {
        return "Group{" +
                "id=" + id +
                ", groupName='" + groupName + '\'' +
                ", subject=" + subject +
                ", dateOfTheCreation='" + dateOfTheCreation + '\'' +
                '}';
    }
}
