package com.github.bogdan.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.github.bogdan.models.User;

import java.io.IOException;

public class UserForGroupSerializer extends StdSerializer<User> {

    public UserForGroupSerializer() { super(User.class); }

    @Override
    public void serialize(User user, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeStartObject();
        jsonGenerator.writeStringField("fname",user.getFname());
        jsonGenerator.writeStringField("lname",user.getLname());
        jsonGenerator.writeStringField("phone",user.getPhone());
        jsonGenerator.writeStringField("email",user.getEmail());
        jsonGenerator.writeStringField("login",user.getLogin());
        jsonGenerator.writeEndObject();
    }
}
