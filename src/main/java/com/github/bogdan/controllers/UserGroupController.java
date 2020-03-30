package com.github.bogdan.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.github.bogdan.databaseConfiguration.DatabaseConfiguration;
import com.github.bogdan.deserializer.UserGroupDeserializer;
import com.github.bogdan.modals.Group;
import com.github.bogdan.modals.Role;
import com.github.bogdan.modals.User;
import com.github.bogdan.modals.UserGroup;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import io.javalin.http.Context;

import java.sql.SQLException;
import java.util.ArrayList;

import static com.github.bogdan.services.AuthService.*;
import static com.github.bogdan.services.ContextService.*;
import static com.github.bogdan.services.UserGroupService.checkIsSuchRecordExists;
import static com.github.bogdan.services.UserService.*;

public class UserGroupController {
    public static void add(Context ctx,Dao<UserGroup,Integer> userGroupDao) throws SQLException, JsonProcessingException {
        String login = ctx.basicAuthCredentials().getUsername();
        String password = ctx.basicAuthCredentials().getPassword();
        checkAuthorization(login,password,ctx);
        if(getUserByLogin(login).getRole()==Role.ADMIN){
            SimpleModule simpleModule = new SimpleModule();
            simpleModule.addDeserializer(UserGroup.class, new UserGroupDeserializer());
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.registerModule(simpleModule);

            UserGroup ug = objectMapper.readValue(ctx.body(),UserGroup.class);
            checkIsSuchRecordExists(ug.getUser().getId(),ug.getGroup().getId());
            userGroupDao.create(ug);
            created(ctx);
        }else youAreNotAdmin(ctx);
    }
    public static void get(Context ctx,Dao<UserGroup,Integer> userGroupDao){}
    public static void getById(Context ctx,Dao<UserGroup,Integer> userGroupDao){}
    public static void change(Context ctx,Dao<UserGroup,Integer> userGroupDao){}
    public static void delete(Context ctx,Dao<UserGroup,Integer> userGroupDao){}

    public static ArrayList<Group> getUsersGroups(User user) throws SQLException {
        Dao<UserGroup,Integer> userGroupDao = DaoManager.createDao(DatabaseConfiguration.connectionSource,UserGroup.class);
        ArrayList<Group> groupArrayList = new ArrayList<>();
        for(UserGroup ug:userGroupDao.queryForAll()){
            if(user.getId() == ug.getUser().getId()){
                groupArrayList.add(ug.getGroup());
            }
        }
        return groupArrayList;
    }
    public static ArrayList<User> getGroupsUsers(Group group) throws SQLException {
        Dao<UserGroup,Integer> userGroupDao = DaoManager.createDao(DatabaseConfiguration.connectionSource,UserGroup.class);
        ArrayList<User> userArrayList = new ArrayList<>();
        for(UserGroup ug:userGroupDao.queryForAll()){
            if(group.getId() == ug.getGroup().getId()){
                userArrayList.add(ug.getUser());
            }
        }
        return userArrayList;
    }
}
