package com.github.bogdan.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.github.bogdan.databaseConfiguration.DatabaseConfiguration;
import com.github.bogdan.deserializer.DeserializerForDeleteUserFromGroup;
import com.github.bogdan.deserializer.UserGroupDeserializer;
import com.github.bogdan.exceptions.WebException;
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
import static com.github.bogdan.services.UserGroupService.checkDoesSuchRecordExist;
import static com.github.bogdan.services.UserGroupService.getUserGroup;
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
            checkDoesSuchRecordExist(ug.getUser().getId(),ug.getGroup().getId());
            userGroupDao.create(ug);
            created(ctx);
        }else youAreNotAdmin(ctx);
    }
    public static void get(Context ctx,Dao<UserGroup,Integer> userGroupDao){}
    public static void getById(Context ctx,Dao<UserGroup,Integer> userGroupDao){}
    public static void change(Context ctx,Dao<UserGroup,Integer> userGroupDao){}
    public static void deleteUserFromGroup(Context ctx,Dao<UserGroup,Integer> userGroupDao) throws SQLException, JsonProcessingException {
        String login = ctx.basicAuthCredentials().getUsername();
        String password = ctx.basicAuthCredentials().getPassword();
        checkAuthorization(login,password,ctx);
        if(getUserByLogin(login).getRole()==Role.ADMIN){
            SimpleModule simpleModule = new SimpleModule();
            simpleModule.addDeserializer(UserGroup.class, new DeserializerForDeleteUserFromGroup());
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.registerModule(simpleModule);

            UserGroup ug = objectMapper.readValue(ctx.body(),UserGroup.class);
//            if(getUserGroup(ug.getUser().getId(),ug.getGroup().getId()).getDateOfDrop()!=null){
//                throw new WebException("This user has already been removed from this group",400);
//            }
            ug.setId(getUserGroup(ug.getUser().getId(),ug.getGroup().getId()).getId());
            userGroupDao.update(ug);
            deleted(ctx);
        }else youAreNotAdmin(ctx);
    }


}
