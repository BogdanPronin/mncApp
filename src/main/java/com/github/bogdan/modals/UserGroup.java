package com.github.bogdan.modals;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

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
}
