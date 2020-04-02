package com.github.bogdan.deserializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.github.bogdan.modals.Role;
import com.github.bogdan.modals.User;
import com.google.i18n.phonenumbers.NumberParseException;
import org.mindrot.jbcrypt.BCrypt;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;

import static com.github.bogdan.services.DeserializerService.*;
import static com.github.bogdan.services.UserService.*;

public class DeserializerForAddUser extends StdDeserializer<User> {
    public DeserializerForAddUser() {
        super(User.class);
    }

    @Override
    public User deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
        try {
            JsonNode node = jsonParser.getCodec().readTree(jsonParser);
            User u = new User();

            String fname = getStringFieldValue(node,"fname");
            u.setFname(fname);

            String lname = getStringFieldValue(node,"lname");
            u.setLname(lname);

            String phone = getStringFieldValue(node,"phone");
            u.setPhone(phone);
            validatePhone(u);
            doesPhoneAlreadyInUse(u);

            String email = getStringFieldValue(node,"email");
            u.setEmail(email);
            validateEmail(u);
            doesEmailAlreadyInUse(u);

            LocalDate localDate = LocalDate.now();
            u.setDateOfRegister(localDate.toString());

            u.setRole(Role.USER);

            String password = getStringFieldValue(node,"password");
            String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt(12));
            u.setPassword(hashedPassword);

            String login = getStringFieldValue(node,"login");
            u.setLogin(login);
            doesLoginAlreadyInUse(u);

            return u;

        } catch (SQLException | NumberParseException e) {
            e.printStackTrace();
        }
        return null;
    }
}
