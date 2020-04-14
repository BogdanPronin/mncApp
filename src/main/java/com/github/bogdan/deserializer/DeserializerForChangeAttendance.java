package com.github.bogdan.deserializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.node.NullNode;
import com.github.bogdan.databaseConfiguration.DatabaseConfiguration;
import com.github.bogdan.exceptions.WebException;
import com.github.bogdan.models.Attendance;
import com.github.bogdan.models.Group;
import com.github.bogdan.models.User;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;

import java.io.IOException;
import java.sql.SQLException;

import static com.github.bogdan.services.DeserializerService.*;
import static com.github.bogdan.services.GroupService.checkDoesGroupWithSuchIdExist;
import static com.github.bogdan.services.LocalDateService.checkLocalDateFormat;
import static com.github.bogdan.services.ScheduleService.checkIsThereLessonOnThisDate;
import static com.github.bogdan.services.UserService.checkDoesUserWithSuchIdExists;

public class DeserializerForChangeAttendance extends StdDeserializer<Attendance> {
    public DeserializerForChangeAttendance() {
        super(Attendance.class);
    }

    @Override
    public Attendance deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
        try {
            Dao<Attendance,Integer> attendanceDao = DaoManager.createDao(DatabaseConfiguration.connectionSource,Attendance.class);
            Dao<User, Integer> userDao = DaoManager.createDao(DatabaseConfiguration.connectionSource, User.class);
            Dao<Group,Integer> groupDao = DaoManager.createDao(DatabaseConfiguration.connectionSource,Group.class);

            JsonNode node = jsonParser.getCodec().readTree(jsonParser);
            Attendance attendance = new Attendance();

            int attendanceId = getIntFieldValue(node,"attendanceId");
            Attendance attendanceBase = attendanceDao.queryForId(attendanceId);

            if(attendanceDao.queryForId(attendanceId)==null){
                throw new WebException("Such attendance doesn't exist",400);
            }
            attendance.setId(attendanceId);

            int userId;
            if(node instanceof NullNode){
                attendance.setUser(attendanceBase.getUser());
            }else if(node.get("userId") == null){
                attendance.setUser(attendanceBase.getUser());
            }else if(node.get("userId").asText()==""){
                attendance.setUser(attendanceBase.getUser());
            } else{
                userId = node.get("userId").asInt();
                checkDoesUserWithSuchIdExists(userId);
                attendance.setUser(userDao.queryForId(userId));
            }

            int groupId = attendanceBase.getGroup().getId();
            if(node instanceof NullNode){
                attendance.setGroup(attendanceBase.getGroup());
            }else if(node.get("groupId") == null){
                attendance.setGroup(attendanceBase.getGroup());
            }else if(node.get("groupId").asText()==""){
                attendance.setGroup(attendanceBase.getGroup());
            } else{
                groupId = node.get("groupId").asInt();
                checkDoesGroupWithSuchIdExist(groupId);
                attendance.setGroup(groupDao.queryForId(groupId));
            }

            String date;
            if(node instanceof NullNode){
                attendance.setDate(attendanceBase.getDate());
            }else if(node.get("date") == null){
                attendance.setDate(attendanceBase.getDate());
            }else if(node.get("date").asText()==""){
                attendance.setDate(attendanceBase.getDate());
            } else{
                date = node.get("date").asText();
                checkLocalDateFormat(date);
                checkIsThereLessonOnThisDate(date,groupId);
                attendance.setDate(date);
            }

            boolean isAttends = false;
            if(node instanceof NullNode){
                attendance.setAttends(attendanceBase.isAttends());
            }else if(node.get("isAttends") == null){
                attendance.setAttends(attendanceBase.isAttends());
            }else if(node.get("isAttends").asText()==""){
                attendance.setAttends(attendanceBase.isAttends());
            } else{
                isAttends = node.get("isAttends").asBoolean();
                attendance.setAttends(isAttends);
            }

            boolean isValidReason = true;
            if(isAttends){
                attendance.setValidReason(true);
            }else if(node instanceof NullNode){
                attendance.setValidReason(attendanceBase.isValidReason());
            }else if(node.get("isValidReason") == null){
                attendance.setValidReason(attendanceBase.isValidReason());
            }else if(node.get("isValidReason").asText()==""){
                attendance.setValidReason(attendanceBase.isValidReason());
            } else{
                isValidReason = node.get("isValidReason").asBoolean();
                attendance.setValidReason(isValidReason);
            }
            return attendance;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
