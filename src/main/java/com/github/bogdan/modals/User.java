package com.github.bogdan.modals;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "user")
public class User {
    @DatabaseField(generatedId = true)
    private int id;
    @DatabaseField
    private String fname;
    @DatabaseField
    private String lname;
    @DatabaseField(unique = true)
    private String phone;
    @DatabaseField(unique = true)
    private String email;
    @DatabaseField
    private String dateOfRegister;
    @DatabaseField(unique = true)
    private String login;
    @DatabaseField
    private String password;
    @DatabaseField
    private Role role;

    public User() {
    }

    public User(int id,String fname, String lname, String phone, String email, String login, String password, Role role) {
        this.id = id;
        this.fname = fname;
        this.lname = lname;
        this.phone = phone;
        this.email = email;
        this.login = login;
        this.password = password;
        this.role = role;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getLname() {
        return lname;
    }

    public void setLname(String lname) {
        this.lname = lname;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDateOfRegister() {
        return dateOfRegister;
    }

    public void setDateOfRegister(String dateOfRegister) {
        this.dateOfRegister = dateOfRegister;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }
}
