package com.github.bogdan.services;

import com.github.bogdan.databaseConfiguration.DatabaseConfiguration;
import com.github.bogdan.exceptions.WebException;
import com.github.bogdan.modals.Attendance;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;

import java.sql.SQLException;

public class AttendanceService {
    public static void checkUniqueAttendance(Attendance attendance) throws SQLException {
        Dao<Attendance,Integer> attendanceDao = DaoManager.createDao(DatabaseConfiguration.connectionSource,Attendance.class);
        for(Attendance a:attendanceDao.queryForAll()){
            if(a.getGroup().getId()==attendance.getGroup().getId()
                    && a.getUser().getId()==attendance.getUser().getId()
                    && a.getDate().equals(attendance.getDate())){
                throw new WebException("Such attendance's record is already exist",400);
            }
        }
    }
    public static void checkDoesSuchAttendanceExist(int id) throws SQLException {
        Dao<Attendance,Integer> attendanceDao = DaoManager.createDao(DatabaseConfiguration.connectionSource,Attendance.class);
        if(attendanceDao.queryForId(id)==null){
            throw new WebException("Attendance with such id isn't exist",400);
        }
    }
}
