package com.github.bogdan.models;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.Objects;

@DatabaseTable(tableName = "user_group")
public class UserGroup {
    @DatabaseField(generatedId = true)
    private int id;
    @DatabaseField(foreign = true,foreignAutoRefresh = true,foreignAutoCreate = false)
    private User user;
    @DatabaseField(foreign = true,foreignAutoRefresh = true,foreignAutoCreate = false)
    private Group group;
    @DatabaseField
    private String dateOfEnrollment;
    @DatabaseField
    private String dateOfDrop;

    public UserGroup() {
    }

    public UserGroup(User user, Group group, String dateOfEnrollment, String dateOfDrop) {

        this.user = user;
        this.group = group;
        this.dateOfEnrollment = dateOfEnrollment;
        this.dateOfDrop = dateOfDrop;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

    public String getDateOfEnrollment() {
        return dateOfEnrollment;
    }

    public void setDateOfEnrollment(String dateOfEnrollment) {
        this.dateOfEnrollment = dateOfEnrollment;
    }

    public String getDateOfDrop() {
        return dateOfDrop;
    }

    public void setDateOfDrop(String dateOfDrop) {
        this.dateOfDrop = dateOfDrop;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserGroup userGroup = (UserGroup) o;
        return id == userGroup.id &&
                Objects.equals(user, userGroup.user) &&
                Objects.equals(group, userGroup.group) &&
                Objects.equals(dateOfEnrollment, userGroup.dateOfEnrollment) &&
                Objects.equals(dateOfDrop, userGroup.dateOfDrop);
    }

    @Override
    public String toString() {
        return "UserGroup{" +
                "id=" + id +
                ", user=" + user +
                ", group=" + group +
                ", dateOfEnrollment='" + dateOfEnrollment + '\'' +
                ", dateOfDrop='" + dateOfDrop + '\'' +
                '}';
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, user, group, dateOfEnrollment, dateOfDrop);
    }
}
