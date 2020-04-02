package com.github.bogdan.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.github.bogdan.deserializer.DeserializerForAddGroup;
import com.github.bogdan.deserializer.DeserializerForChangeGroup;
import com.github.bogdan.modals.Group;
import com.github.bogdan.modals.Role;
import com.github.bogdan.modals.Schedule;
import com.github.bogdan.modals.User;
import com.github.bogdan.serializer.GroupGetSerializer;
import com.github.bogdan.serializer.ScheduleForGroupSerializer;
import com.github.bogdan.serializer.UserForGroupSerializer;
import com.j256.ormlite.dao.Dao;
import io.javalin.http.Context;

import java.sql.SQLException;

import static com.github.bogdan.services.AuthService.*;
import static com.github.bogdan.services.ContextService.*;
import static com.github.bogdan.services.GroupService.checkDoesGroupWithSuchIdExist;
import static com.github.bogdan.services.GroupService.checkDoesGroupWithSuchNameExist;
import static com.github.bogdan.services.LocalDateService.checkLocalDateFormat;
import static com.github.bogdan.services.UserService.*;

public class GroupController {
    public static void add(Context ctx, Dao<Group,Integer> groupDao) throws SQLException, JsonProcessingException {
        checkDoesBasicAuthIsEmpty(ctx);
        if(authorization(ctx.basicAuthCredentials().getUsername(),ctx.basicAuthCredentials().getPassword())){
            if(getUserByLogin(ctx.basicAuthCredentials().getUsername()).getRole()== Role.ADMIN){
                checkDoesRequestBodyIsEmpty(ctx);
                String body = ctx.body();
                SimpleModule simpleModule = new SimpleModule();
                simpleModule.addDeserializer(Group.class,new DeserializerForAddGroup());
                ObjectMapper objectMapperForAddGroup = new ObjectMapper();
                objectMapperForAddGroup.registerModule(simpleModule);

                Group g = objectMapperForAddGroup.readValue(body,Group.class);
                checkLocalDateFormat(g.getDateOfTheCreation());
                checkDoesGroupWithSuchNameExist(g.getGroupName());
                groupDao.create(g);
                created(ctx);
            }else youAreNotAdmin(ctx);
        }else authorizationFailed(ctx);
    }
    public static void delete(Context ctx, Dao<Group,Integer> groupDao) throws SQLException {
        checkDoesBasicAuthIsEmpty(ctx);
        if(authorization(ctx.basicAuthCredentials().getUsername(),ctx.basicAuthCredentials().getPassword())){
            if(getUserByLogin(ctx.basicAuthCredentials().getUsername()).getRole()== Role.ADMIN){
                int id = Integer.parseInt(ctx.pathParam("id"));
                checkDoesGroupWithSuchIdExist(id);
                groupDao.delete(groupDao.queryForId(id));
                deleted(ctx);
            }else youAreNotAdmin(ctx);
        }else authorizationFailed(ctx);
    }
    public static void get(Context ctx, Dao<Group,Integer> groupDao) throws SQLException, JsonProcessingException {
        checkDoesBasicAuthIsEmpty(ctx);
        String login = ctx.basicAuthCredentials().getUsername();
        String password = ctx.basicAuthCredentials().getPassword();
        if(authorization(login,password)){
            SimpleModule simpleModule = new SimpleModule();
            simpleModule.addSerializer(Group.class, new GroupGetSerializer());
            simpleModule.addSerializer(User.class, new UserForGroupSerializer());
            simpleModule.addSerializer(Schedule.class,new ScheduleForGroupSerializer());
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.registerModule(simpleModule);

            String serialized = objectMapper.writeValueAsString(groupDao.queryForAll());
            ctx.result(serialized);
            ctx.status(200);
        }
    }
    public static void getById(Context ctx, Dao<Group,Integer> groupDao) throws SQLException, JsonProcessingException {
        checkDoesBasicAuthIsEmpty(ctx);
        String login = ctx.basicAuthCredentials().getUsername();
        String password = ctx.basicAuthCredentials().getPassword();
        if(authorization(login,password)){
            int id = Integer.parseInt(ctx.pathParam("id"));
            checkDoesGroupWithSuchIdExist(id);
            SimpleModule simpleModule = new SimpleModule();
            simpleModule.addSerializer(Group.class, new GroupGetSerializer());
            simpleModule.addSerializer(User.class, new UserForGroupSerializer());
            simpleModule.addSerializer(Schedule.class,new ScheduleForGroupSerializer());
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.registerModule(simpleModule);

            String serialized = objectMapper.writeValueAsString(groupDao.queryForId(id));
            ctx.result(serialized);
            ctx.status(200);
        }
    }
    public static void change(Context ctx, Dao<Group,Integer> groupDao) throws SQLException, JsonProcessingException {
        checkDoesBasicAuthIsEmpty(ctx);
        if(authorization(ctx.basicAuthCredentials().getUsername(),ctx.basicAuthCredentials().getPassword())){
            if(getUserByLogin(ctx.basicAuthCredentials().getUsername()).getRole()== Role.ADMIN){
                checkDoesRequestBodyIsEmpty(ctx);
                String body = ctx.body();
                SimpleModule simpleModule = new SimpleModule();
                simpleModule.addDeserializer(Group.class,new DeserializerForChangeGroup());
                ObjectMapper objectMapperForAddGroup = new ObjectMapper();
                objectMapperForAddGroup.registerModule(simpleModule);

                Group g = objectMapperForAddGroup.readValue(body,Group.class);
                groupDao.update(g);
                updated(ctx);
            }else youAreNotAdmin(ctx);
        }else authorizationFailed(ctx);
    }
}
