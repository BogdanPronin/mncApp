package com.github.bogdan.models;

import com.j256.ormlite.field.DatabaseField;

import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Group group = (Group) o;
        return id == group.id &&
                Objects.equals(groupName, group.groupName) &&
                Objects.equals(subject, group.subject) &&
                Objects.equals(dateOfTheCreation, group.dateOfTheCreation);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, groupName, subject, dateOfTheCreation);
    }
}
