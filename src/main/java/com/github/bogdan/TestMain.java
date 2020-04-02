package com.github.bogdan;

import com.github.bogdan.databaseConfiguration.DatabaseConfiguration;
import com.github.bogdan.modals.Subject;
import com.github.bogdan.modals.User;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;

import java.sql.SQLException;
import java.time.LocalDate;

import static com.github.bogdan.services.LocalDateService.checkForOverlappingTime;


public class TestMain {
    public static void main(String[] args) throws SQLException {
//        Dao<User, Integer> userDao = DaoManager.createDao(DatabaseConfiguration.connectionSource, User.class);
//        User u = userDao.queryForId(1);
//        u.setRole(Role.ADMIN);
//        userDao.update(u);
        LocalDate localDate = LocalDate.now();
        System.out.println(localDate.getDayOfWeek());
    }
}
