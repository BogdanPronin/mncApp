package com.github.bogdan.modals;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "attendance")
public class Attendance {
    @DatabaseField(generatedId = true)
    private int id;
    @DatabaseField(foreign = true,foreignAutoRefresh = true,foreignAutoCreate = false)
    private User user;
    @DatabaseField(foreign = true,foreignAutoRefresh = true,foreignAutoCreate = false)
    private Group group;
    @DatabaseField
    private String date;
    @DatabaseField
    private boolean isAttends;

    public Attendance() {
    }

    public Attendance(User user, Group group, String date, boolean isAttends) {
        this.user = user;
        this.group = group;
        this.date = date;
        this.isAttends = isAttends;
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

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public boolean isAttends() {
        return isAttends;
    }

    public void setAttends(boolean attends) {
        isAttends = attends;
    }
}
