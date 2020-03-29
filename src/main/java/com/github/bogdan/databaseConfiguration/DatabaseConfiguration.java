package com.github.bogdan.databaseConfiguration;

import com.github.bogdan.modals.*;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;

public class DatabaseConfiguration {
    public static ConnectionSource connectionSource;
    static {
        try{
            connectionSource = new JdbcConnectionSource("jdbc:sqlite:/Users/bogdan/Desktop/mncDB.db");
            TableUtils.createTableIfNotExists(connectionSource, User.class);
            TableUtils.createTableIfNotExists(connectionSource, Subject.class);
            TableUtils.createTableIfNotExists(connectionSource, Group.class);
            TableUtils.createTableIfNotExists(connectionSource, Schedule.class);
            TableUtils.createTableIfNotExists(connectionSource, UserGroup.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
