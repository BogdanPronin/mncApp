package com.github.bogdan.services;

import com.github.bogdan.databaseConfiguration.DatabaseConfiguration;
import com.github.bogdan.exceptions.WebException;
import com.github.bogdan.modals.UserGroup;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;

import java.sql.SQLException;

public class UserGroupService {
    public static void checkIsSuchRecordExists(int userId,int groupId) throws SQLException {
        Dao<UserGroup,Integer> userGroupDao = DaoManager.createDao(DatabaseConfiguration.connectionSource,UserGroup.class);
        for(UserGroup ug: userGroupDao.queryForAll()){
            if(ug.getUser().getId() == userId && ug.getGroup().getId()== groupId){
                throw new WebException("Such record is already exists",400);
            }
        }
    }
}
