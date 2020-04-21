package com.github.bogdan;


import com.github.bogdan.databaseConfiguration.DatabaseConfiguration;
import com.github.bogdan.models.User;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;

import java.sql.SQLException;
import java.util.ArrayList;

import static com.github.bogdan.services.PaginationService.getPage;


public class TestMain {
    public static void main(String[] args) throws SQLException {
        Dao<User, Integer> userDao = DaoManager.createDao(DatabaseConfiguration.connectionSource, User.class);
        ArrayList<Object> userArrayList = new ArrayList<>();
        userArrayList.addAll(userDao.queryForAll());

        System.out.println(getPage(userDao,1,1));

//        User u = userDao.queryForId(1);
//        u.setRole(Role.ADMIN);
//        userDao.update(u);


    }
}
