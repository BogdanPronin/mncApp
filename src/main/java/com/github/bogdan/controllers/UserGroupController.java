package com.github.bogdan.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.github.bogdan.deserializer.DeserializerForDeleteUserFromGroup;
import com.github.bogdan.deserializer.UserGroupDeserializer;
import com.github.bogdan.modals.Role;
import com.github.bogdan.modals.User;
import com.github.bogdan.modals.UserGroup;
import com.github.bogdan.serializer.UserForGroupSerializer;
import com.j256.ormlite.dao.Dao;
import io.javalin.http.Context;
import java.sql.SQLException;

import static com.github.bogdan.services.AuthService.*;
import static com.github.bogdan.services.ContextService.*;
import static com.github.bogdan.services.UserGroupService.checkDoesSuchRecordExist;
import static com.github.bogdan.services.UserGroupService.getUserGroup;
import static com.github.bogdan.services.UserService.*;

public class UserGroupController {
    public static void add(Context ctx,Dao<UserGroup,Integer> userGroupDao) throws SQLException, JsonProcessingException {
        checkDoesBasicAuthIsEmpty(ctx);
        String login = ctx.basicAuthCredentials().getUsername();
        String password = ctx.basicAuthCredentials().getPassword();
        checkAuthorization(login,password,ctx);
        if(getUserByLogin(login).getRole()==Role.ADMIN){
            checkDoesRequestBodyIsEmpty(ctx);
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
    public static void get(Context ctx,Dao<UserGroup,Integer> userGroupDao) throws SQLException, JsonProcessingException {
        checkDoesBasicAuthIsEmpty(ctx);
        String login = ctx.basicAuthCredentials().getUsername();
        String password = ctx.basicAuthCredentials().getPassword();
        checkAuthorization(login,password,ctx);
        if(getUserByLogin(login).getRole()==Role.ADMIN){
            SimpleModule simpleModule = new SimpleModule();
            simpleModule.addSerializer(User.class, new UserForGroupSerializer());
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.registerModule(simpleModule);

            ctx.result(objectMapper.writeValueAsString(userGroupDao.queryForAll()));
            ctx.status(200);
        }else youAreNotAdmin(ctx);
    }
    public static void getById(Context ctx,Dao<UserGroup,Integer> userGroupDao) throws SQLException, JsonProcessingException {
        checkDoesBasicAuthIsEmpty(ctx);
        String login = ctx.basicAuthCredentials().getUsername();
        String password = ctx.basicAuthCredentials().getPassword();
        checkAuthorization(login,password,ctx);
        if(getUserByLogin(login).getRole()==Role.ADMIN){
            SimpleModule simpleModule = new SimpleModule();
            simpleModule.addSerializer(User.class, new UserForGroupSerializer());
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.registerModule(simpleModule);

            int id = Integer.parseInt(ctx.pathParam("id"));
            checkDoesSuchRecordExist(id);
            ctx.result(objectMapper.writeValueAsString(userGroupDao.queryForId(id)));
            ctx.status(200);
        }else youAreNotAdmin(ctx);
    }
    public static void change(Context ctx,Dao<UserGroup,Integer> userGroupDao){}
    public static void deleteUserFromGroup(Context ctx,Dao<UserGroup,Integer> userGroupDao) throws SQLException, JsonProcessingException {
        checkDoesBasicAuthIsEmpty(ctx);
        String login = ctx.basicAuthCredentials().getUsername();
        String password = ctx.basicAuthCredentials().getPassword();
        checkAuthorization(login,password,ctx);
        if(getUserByLogin(login).getRole()==Role.ADMIN){
            checkDoesRequestBodyIsEmpty(ctx);
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
    public static void deleteRecord(Context ctx,Dao<UserGroup,Integer> userGroupDao) throws SQLException {
        checkDoesBasicAuthIsEmpty(ctx);
        String login = ctx.basicAuthCredentials().getUsername();
        String password = ctx.basicAuthCredentials().getPassword();
        checkAuthorization(login,password,ctx);
        if(getUserByLogin(login).getRole()==Role.ADMIN){
            int id = Integer.parseInt(ctx.pathParam("id"));
            checkDoesSuchRecordExist(id);
            userGroupDao.deleteById(id);
            deleted(ctx);
        }else youAreNotAdmin(ctx);
    }

}
