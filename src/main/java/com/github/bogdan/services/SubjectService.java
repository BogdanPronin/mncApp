package com.github.bogdan.services;

import com.github.bogdan.databaseConfiguration.DatabaseConfiguration;
import com.github.bogdan.exceptions.WebException;
import com.github.bogdan.modals.Subject;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;

import java.sql.SQLException;


public class SubjectService {
    public static boolean isSubjectWithSuchNameIsExist(String name) throws SQLException {
        Dao<Subject,Integer> subjectDao  = DaoManager.createDao(DatabaseConfiguration.connectionSource,Subject.class);
        for(Subject s:subjectDao){
            if(s.getName().equals(name)){
                return true;
            }
        }
        return false;
    }
    public static void checkIsSubjectWithSuchNameIsExist(String name) throws SQLException {
        if(isSubjectWithSuchNameIsExist(name)){
            throw new WebException("This subject is already exist",400);
        }
    }

}
