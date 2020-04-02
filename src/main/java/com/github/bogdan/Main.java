package com.github.bogdan;

import com.github.bogdan.controllers.*;
import com.github.bogdan.databaseConfiguration.DatabaseConfiguration;
import com.github.bogdan.exceptions.MyException;
import com.github.bogdan.exceptions.WebException;
import com.github.bogdan.modals.*;
import com.google.i18n.phonenumbers.NumberParseException;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import io.javalin.Javalin;
import io.javalin.core.JavalinConfig;

import java.sql.SQLException;
import java.time.format.DateTimeParseException;

public class Main {
    public static void main(String[] args) throws SQLException {
        Javalin app = Javalin
                .create();
        app.config = new JavalinConfig().enableDevLogging();
        app.start(22867);
        Dao<User, Integer> userDao = DaoManager.createDao(DatabaseConfiguration.connectionSource, User.class);
        Dao<Subject,Integer> subjectDao = DaoManager.createDao(DatabaseConfiguration.connectionSource,Subject.class);
        Dao<Schedule,Integer> scheduleDao = DaoManager.createDao(DatabaseConfiguration.connectionSource,Schedule.class);
        Dao<Group,Integer> groupDao = DaoManager.createDao(DatabaseConfiguration.connectionSource,Group.class);
        Dao<UserGroup,Integer> userGroupDao = DaoManager.createDao(DatabaseConfiguration.connectionSource,UserGroup.class);
        Dao<Attendance,Integer> attendanceDao = DaoManager.createDao(DatabaseConfiguration.connectionSource,Attendance.class);

        app.post("/users", ctx -> UserController.add(ctx, userDao));
        app.patch("/users",ctx->UserController.changeUser(ctx,userDao));
        app.delete("/users/:id",ctx-> UserController.delete(ctx,userDao));
        app.get("/users/:id",ctx -> UserController.getUser(ctx,userDao));
        app.get("/users",ctx -> UserController.getUsers(ctx,userDao));


        app.post("/subjects", ctx -> SubjectController.add(ctx, subjectDao));
        app.delete("/subjects/:id",ctx -> SubjectController.delete(ctx,subjectDao));
        app.get("/subjects",ctx-> SubjectController.get(ctx,subjectDao));
        app.get("/subjects/:id",ctx-> SubjectController.getById(ctx,subjectDao));
        app.patch("/subjects/:id",ctx-> SubjectController.getById(ctx,subjectDao));

        app.post("/schedule", ctx -> ScheduleController.add(ctx, scheduleDao));
        app.delete("/schedule/:id",ctx -> ScheduleController.delete(ctx,scheduleDao));
        app.get("/schedule",ctx-> ScheduleController.get(ctx,scheduleDao));
        app.get("/schedule/:id",ctx-> ScheduleController.getById(ctx,scheduleDao));
        app.patch("/schedule",ctx -> ScheduleController.change(ctx,scheduleDao));

        app.post("/groups",ctx-> GroupController.add(ctx, groupDao));
        app.delete("/groups/:id",ctx -> GroupController.delete(ctx,groupDao));
        app.get("/groups",ctx-> GroupController.get(ctx,groupDao));
        app.get("/groups/:id",ctx-> GroupController.getById(ctx,groupDao));
        app.patch("/groups",ctx-> GroupController.change(ctx,groupDao));

        app.post("/userGroup",ctx -> UserGroupController.add(ctx,userGroupDao));
        app.delete("/userGroup",ctx -> UserGroupController.deleteUserFromGroup(ctx,userGroupDao));
        app.delete("/userGroup/:id",ctx -> UserGroupController.deleteRecord(ctx,userGroupDao));
        app.get("/userGroup/:id",ctx-> UserGroupController.getById(ctx,userGroupDao));
        app.get("/userGroup",ctx-> UserGroupController.get(ctx,userGroupDao));

        app.post("/attendance",ctx-> AttendanceController.add(ctx,attendanceDao));
        app.get("/attendance",ctx-> AttendanceController.get(ctx,attendanceDao));
        app.get("/attendance/:id",ctx-> AttendanceController.getById(ctx,attendanceDao));
        app.patch("/attendance/:id",ctx-> AttendanceController.change(ctx,attendanceDao));
        app.delete("/attendance/:id",ctx-> AttendanceController.delete(ctx,attendanceDao));

        app.exception(MyException.class, (e, ctx) -> {
            ctx.result(e.getMessage());
            e.printStackTrace();
        });
        app.exception(WebException.class, (e, ctx) -> {
            ctx.result(e.getMessage());
            ctx.status(e.getStatus());
            e.printStackTrace();
        });
        app.exception(DateTimeParseException.class,(e,ctx)->{
            ctx.status(400);
            ctx.result("Wrong date format, correct date format should be YYYY-MM-DD"+'\n'+e.getMessage());
            e.printStackTrace();
        });
//        app.exception(NumberParseException.class,(e,ctx)->{
//            ctx.status(400);
//            ctx.result("FFFFF");
//            e.printStackTrace();
//        });
    }
}
