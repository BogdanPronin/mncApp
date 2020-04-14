package com.github.bogdan.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.github.bogdan.models.Group;
import com.github.bogdan.models.User;

import java.io.IOException;
import java.sql.SQLException;

import static com.github.bogdan.services.UserGroupService.*;

public class UserGetSerializer extends StdSerializer<User> {
    public UserGetSerializer() {
        super(User.class);
    }

    protected UserGetSerializer(JavaType type) {
        super(type);
    }

    protected UserGetSerializer(Class<?> t, boolean dummy) {
        super(t, dummy);
    }

    protected UserGetSerializer(StdSerializer<?> src) {
        super(src);
    }

    @Override
    public void serialize(User user, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeStartObject();
        jsonGenerator.writeStringField("fname",user.getFname());
        jsonGenerator.writeStringField("lname",user.getLname());
        jsonGenerator.writeStringField("phone",user.getPhone());
        jsonGenerator.writeStringField("email",user.getEmail());
        jsonGenerator.writeStringField("login",user.getLogin());
        jsonGenerator.writeStringField("role",user.getRole().toString());
        jsonGenerator.writeStringField("date of register",user.getDateOfRegister());
        jsonGenerator.writeArrayFieldStart("groups");

        try {
            for(Group g : getUsersGroups(user)){
                jsonGenerator.writeObject(g);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        jsonGenerator.writeEndArray();
        jsonGenerator.writeEndObject();
    }
}
