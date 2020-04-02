package com.github.bogdan.services;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.NullNode;
import com.github.bogdan.exceptions.WebException;
import java.time.LocalDate;
import static com.github.bogdan.services.LocalDateService.checkLocalDateFormat;

public class DeserializerService {
    //проверяет на null или пустое поле. Если все хорошо возвращает String, иначе кидает ошибку
    public static String getStringFieldValue(JsonNode node, String field){
        checkForExplicitlyNullField(node.get(field),"Necessary field \""+field+ "\" can't be null");
        if(node.get(field) == null){
            throw new WebException("Necessary field \""+field+ "\" can't be null",400);
        }else if(node.get(field).asText()==""){
            throw new WebException("Necessary field \""+field+ "\" can't be null",400);
        } else return node.get(field).asText();
    }
    public static String getNullableStringFielValue(JsonNode node, String field){
        if(node instanceof NullNode){
            return null;
        }else if(node.get(field) == null){
            return null;
        }else if(node.get(field).asText()==""){
            return null;
        } else return node.get(field).asText();
    }
    public static int getNullableIntFielValue(JsonNode node, String field){
        if(node instanceof NullNode){
            return -1;
        }else if(node.get(field) == null){
            return -1;
        }else if(node.get(field).asText()==""){
            return -1;
        } else return node.get(field).asInt();
    }

    //проверяет на пустое поле. Если все хорошо возвращает int, иначе кидает ошибку
    public static int getIntFieldValue(JsonNode node, String field){
        checkForExplicitlyNullField(node.get(field),"Necessary field \""+field+ "\" can't be null");
        if(node.get(field) == null){
            throw new WebException("Necessary field \""+field+ "\" can't be null",400);
        }else if(node.get(field).asText()==""){
            throw new WebException("Necessary field \""+field+ "\" can't be null",400);
        }else return node.get(field).asInt();
    }
    //проверяет на пустое поле. Если все хорошо возвращает boolean, иначе кидает ошибку
    public static boolean getBooleanFieldValue(JsonNode node,String field){
        checkForExplicitlyNullField(node.get(field),"Necessary field \""+field+ "\" can't be null");
        if(node.get(field) == null){
            throw new WebException("Necessary field \""+field+ "\" can't be null",400);
        }else if(node.get(field).asText()==""){
            throw new WebException("Necessary field \""+field+ "\" can't be null",400);
        } else return node.get(field).asBoolean();
    }
    //проверяет на null или пустое поле.
    // Если все хорошо проверяет на правильность введения даты и возвращает String, иначе кидает ошибку
    public static String getDateFieldValue(JsonNode node, String field){
        if(node.get(field) == null){
            LocalDate localDate = LocalDate.now();
            return localDate.toString();
        }else if(node.get(field).asText()==""){
            LocalDate localDate = LocalDate.now();
            return localDate.toString();
        } else {
            checkLocalDateFormat(node.get(field).asText());
            return node.get(field).asText();
        }
    }

    public static void checkForExplicitlyNullField(JsonNode node, String exceptionMessage){
        if (node instanceof NullNode) {
            throw new WebException(exceptionMessage,400);
        }
    }
}
