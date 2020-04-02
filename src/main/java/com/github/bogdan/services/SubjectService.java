package com.github.bogdan.services;

import com.github.bogdan.databaseConfiguration.DatabaseConfiguration;
import com.github.bogdan.exceptions.WebException;
import com.github.bogdan.modals.Subject;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;

import java.sql.SQLException;


public class SubjectService {
    public static boolean doesSubjectWithSuchNameExists(String name) throws SQLException {
        Dao<Subject,Integer> subjectDao  = DaoManager.createDao(DatabaseConfiguration.connectionSource,Subject.class);
        for(Subject s:subjectDao){
            if(s.getName().equals(name)){
                return true;
            }
        }
        return false;
    }
    public static void checkDoesSubjectWithSuchNameExists(String name) throws SQLException {
        if(doesSubjectWithSuchNameExists(name)){
            throw new WebException("This subject already exists",400);
        }
    }
    public static void checkDoesSubjectWithSuchIdExists(int id) throws SQLException {
        Dao<Subject,Integer> subjectDao  = DaoManager.createDao(DatabaseConfiguration.connectionSource,Subject.class);
        if(subjectDao.queryForId(id)==null){
            throw new WebException("Subject with such id doesn't exist",400);
        }
    }
}
