package com.github.bogdan;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.github.bogdan.databaseConfiguration.DatabaseConfiguration;
import com.github.bogdan.deserializer.DeserializerForChangeGroup;
import com.github.bogdan.modals.Group;
import com.github.bogdan.modals.Subject;
import com.github.bogdan.modals.User;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;

import java.sql.SQLException;
import java.time.LocalDate;

import static com.github.bogdan.services.LocalDateService.checkForOverlappingTime;


public class TestMain {
    public static void main(String[] args){
//        Dao<User, Integer> userDao = DaoManager.createDao(DatabaseConfiguration.connectionSource, User.class);
//        User u = userDao.queryForId(1);
//        u.setRole(Role.ADMIN);
//        userDao.update(u);

    }
}
