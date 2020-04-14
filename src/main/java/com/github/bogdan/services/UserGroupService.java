package com.github.bogdan.services;

import com.github.bogdan.databaseConfiguration.DatabaseConfiguration;
import com.github.bogdan.exceptions.WebException;
import com.github.bogdan.models.Group;
import com.github.bogdan.models.User;
import com.github.bogdan.models.UserGroup;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;

import java.sql.SQLException;
import java.util.ArrayList;

public class UserGroupService {
    public static void checkDoesSuchRecordExist(int userId, int groupId) throws SQLException {
        Dao<UserGroup,Integer> userGroupDao = DaoManager.createDao(DatabaseConfiguration.connectionSource,UserGroup.class);
        for(UserGroup ug: userGroupDao.queryForAll()){
            if(ug.getUser().getId() == userId && ug.getGroup().getId()== groupId && ug.getDateOfDrop()==null){
                throw new WebException("Such record already exists",400);
            }
        }
    }

    public static void checkDoesSuchRecordExist(int id) throws SQLException {
        Dao<UserGroup,Integer> userGroupDao = DaoManager.createDao(DatabaseConfiguration.connectionSource,UserGroup.class);
        if(userGroupDao.queryForId(id)==null){
            throw new WebException("Such record doesn't exist",400);
        }
    }
    //проверка: состоит ли данный user в данной группе
    public static void checkDoesUserInThisGroup(int userId,int groupId) throws SQLException {
        Dao<UserGroup,Integer> userGroupDao = DaoManager.createDao(DatabaseConfiguration.connectionSource,UserGroup.class);
        boolean userInGroup = false;
        boolean userWasInGroup = false;
        for(UserGroup ug: userGroupDao.queryForAll()){
            if(ug.getUser().getId() == userId && ug.getGroup().getId()== groupId){
                if(ug.getDateOfDrop()!=null){
                    userWasInGroup = true;
                }else userInGroup=true;
            }
        }
        if(userWasInGroup){
            throw new WebException("User left this group",400);
        }
        if(!userInGroup){
            throw new WebException("User isn't in this group",400);
        }
    }
    public static UserGroup getUserGroup(int userId,int groupId) throws SQLException {
        Dao<UserGroup,Integer> userGroupDao = DaoManager.createDao(DatabaseConfiguration.connectionSource,UserGroup.class);
        for(UserGroup ug: userGroupDao.queryForAll()){
            if(ug.getUser().getId() == userId && ug.getGroup().getId()== groupId && ug.getDateOfDrop()==null){
                return ug;
            }
        }
        return null;
    }
    //возвращает лист групп, в которых состоит user
    public static ArrayList<Group> getUsersGroups(User user) throws SQLException {
        Dao<UserGroup,Integer> userGroupDao = DaoManager.createDao(DatabaseConfiguration.connectionSource,UserGroup.class);
        ArrayList<Group> groupArrayList = new ArrayList<>();
        for(UserGroup ug:userGroupDao.queryForAll()){
            if(user.getId() == ug.getUser().getId()){
                groupArrayList.add(ug.getGroup());
            }
        }
        return groupArrayList;
    }
    //возвращает лист user'ов, состоящих в этой группе
    public static ArrayList<User> getGroupsUsers(Group group) throws SQLException {
        Dao<UserGroup,Integer> userGroupDao = DaoManager.createDao(DatabaseConfiguration.connectionSource,UserGroup.class);
        ArrayList<User> userArrayList = new ArrayList<>();
        for(UserGroup ug:userGroupDao.queryForAll()){
            if(group.getId() == ug.getGroup().getId() && ug.getDateOfDrop()==null){
                userArrayList.add(ug.getUser());
            }
        }
        return userArrayList;
    }


}
