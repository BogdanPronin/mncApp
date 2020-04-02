package com.github.bogdan.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.github.bogdan.modals.Role;
import com.github.bogdan.modals.Subject;
import com.j256.ormlite.dao.Dao;
import io.javalin.http.Context;

import java.sql.SQLException;

import static com.github.bogdan.services.AuthService.authorization;
import static com.github.bogdan.services.ContextService.*;
import static com.github.bogdan.services.SubjectService.checkDoesSubjectWithSuchIdExists;
import static com.github.bogdan.services.SubjectService.checkDoesSubjectWithSuchNameExists;
import static com.github.bogdan.services.UserService.getUserByLogin;

public class SubjectController {
    static ObjectMapper objectMapper = new ObjectMapper();
    public static void add(Context ctx, Dao<Subject,Integer> subjectDao) throws SQLException, JsonProcessingException {
        if(authorization(ctx.basicAuthCredentials().getUsername(),ctx.basicAuthCredentials().getPassword())){
            if(getUserByLogin(ctx.basicAuthCredentials().getUsername()).getRole()== Role.ADMIN){
                Subject s = objectMapper.readValue(ctx.body(),Subject.class);
                checkDoesSubjectWithSuchNameExists(s.getName());
                subjectDao.create(s);
                created(ctx);
            }else youAreNotAdmin(ctx);
        }else authorizationFailed(ctx);
    }
    public static void delete(Context ctx, Dao<Subject,Integer> subjectDao) throws SQLException {
        if(authorization(ctx.basicAuthCredentials().getUsername(),ctx.basicAuthCredentials().getPassword())){
            if(getUserByLogin(ctx.basicAuthCredentials().getUsername()).getRole()== Role.ADMIN){
                int id = Integer.parseInt(ctx.pathParam("id"));
                checkDoesSubjectWithSuchIdExists(id);
                subjectDao.delete(subjectDao.queryForId(id));
                deleted(ctx);
            }else youAreNotAdmin(ctx);
        }else authorizationFailed(ctx);
    }
    public static void get(Context ctx, Dao<Subject,Integer> subjectDao) throws SQLException, JsonProcessingException {
        if(authorization(ctx.basicAuthCredentials().getUsername(),ctx.basicAuthCredentials().getPassword())){
            if(getUserByLogin(ctx.basicAuthCredentials().getUsername()).getRole()== Role.ADMIN){
                ctx.result(objectMapper.writeValueAsString(subjectDao.queryForAll()));
                ctx.status(200);
            }else youAreNotAdmin(ctx);
        }else authorizationFailed(ctx);
    }
    public static void getById(Context ctx, Dao<Subject,Integer> subjectDao) throws SQLException, JsonProcessingException {
        if(authorization(ctx.basicAuthCredentials().getUsername(),ctx.basicAuthCredentials().getPassword())){
            if(getUserByLogin(ctx.basicAuthCredentials().getUsername()).getRole()== Role.ADMIN){
                int id = Integer.parseInt(ctx.pathParam("id"));
                checkDoesSubjectWithSuchIdExists(id);
                ctx.result(objectMapper.writeValueAsString(subjectDao.queryForId(id)));
                ctx.status(200);
            }else youAreNotAdmin(ctx);
        }else authorizationFailed(ctx);
    }
    public static void change(Context ctx, Dao<Subject,Integer> subjectDao){}
}
