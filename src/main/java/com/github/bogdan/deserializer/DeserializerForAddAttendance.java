package com.github.bogdan.deserializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.github.bogdan.databaseConfiguration.DatabaseConfiguration;
import com.github.bogdan.exceptions.WebException;
import com.github.bogdan.modals.Attendance;
import com.github.bogdan.modals.Group;
import com.github.bogdan.modals.User;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;

import static com.github.bogdan.services.DeserializerService.*;
import static com.github.bogdan.services.LocalDateService.checkLocalDateFormat;
import static com.github.bogdan.services.ScheduleService.checkIsThereLessonOnThisDate;
import static com.github.bogdan.services.UserGroupService.*;

public class DeserializerForAddAttendance extends StdDeserializer<Attendance> {
    public DeserializerForAddAttendance() {
        super(Attendance.class);
    }

    @Override
    public Attendance deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
        try {
            JsonNode node = jsonParser.getCodec().readTree(jsonParser);
            int userId = getIntFieldValue(node,"userId");
            Dao<User,Integer> userDao = DaoManager.createDao(DatabaseConfiguration.connectionSource,User.class);
            if(userDao.queryForId(userId)==null){
                throw new WebException("Such user isn't exist",400);
            }

            int groupId = getIntFieldValue(node,"groupId");
            Dao<Group,Integer> groupDao = DaoManager.createDao(DatabaseConfiguration.connectionSource,Group.class);
            if(groupDao.queryForId(groupId)==null) {
                throw new WebException("Such group isn't exist", 400);
            }

            checkDoesUserInThisGroup(userId,groupId);

            String date = getDateFieldValue(node,"date");

            checkLocalDateFormat(date);
            checkIsThereLessonOnThisDate(date,groupId);


            boolean isAttends = getBooleanFieldValue(node,"isAttends");

            boolean isValidReason;
            if(!isAttends) {
                isValidReason = getBooleanFieldValue(node, "isValidReason");
            }else isValidReason = true;

            return new Attendance(userDao.queryForId(userId),groupDao.queryForId(groupId),date,isAttends,isValidReason);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
