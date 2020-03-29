package com.github.bogdan.services;

import com.github.bogdan.databaseConfiguration.DatabaseConfiguration;
import com.github.bogdan.exceptions.WebException;
import com.github.bogdan.modals.Group;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;

import java.sql.SQLException;

public class GroupService {
    public static boolean isGroupWithSuchNameIsExist(String name) throws SQLException {
        Dao<Group,Integer> groupDao = DaoManager.createDao(DatabaseConfiguration.connectionSource,Group.class);
        for(Group g:groupDao){
            if(g.getGroupName().equals(name)){
                return true;
            }
        }
        return false;
    }
    public static void checkIsGroupWithSuchNameIsExist(String name) throws SQLException {
        if (isGroupWithSuchNameIsExist(name)){
            throw new WebException("Group with such name is already exist",400);
        }
    }
}
