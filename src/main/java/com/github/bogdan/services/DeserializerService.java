package com.github.bogdan.services;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.NullNode;
import com.github.bogdan.exceptions.WebException;

public class DeserializerService {
    public static String getFieldValue(JsonNode node, String field){
        checkForExplicitlyNullField(node.get(field),"Necessary field \""+field+ "\" can't be null");
        if(node.get(field) == null){
            throw new WebException("Necessary field \""+field+ "\" can't be null",400);
        }else if(node.get(field).asText()==""){
            throw new WebException("Necessary field \""+field+ "\" can't be null",400);
        } else return node.get(field).asText();
    }
    public static int getIntFieldValue(JsonNode node, String field){
        checkForExplicitlyNullField(node.get(field),"Necessary field \""+field+ "\" can't be null");
        if(node.get(field) == null){
            throw new WebException("Necessary field \""+field+ "\" can't be null",400);
        }else if(node.get(field).asText()==""){
            throw new WebException("Necessary field \""+field+ "\" can't be null",400);
        }else return node.get(field).asInt();
    }
    public static boolean getBooleanFieldValue(JsonNode node,String field){
        checkForExplicitlyNullField(node.get(field),"Necessary field \""+field+ "\" can't be null");
        if(node.get(field) == null){
            throw new WebException("Necessary field \""+field+ "\" can't be null",400);
        }else if(node.get(field).asText()==""){
            throw new WebException("Necessary field \""+field+ "\" can't be null",400);
        } else return node.get(field).asBoolean();
    }
    private static void checkForExplicitlyNullField(JsonNode node, String exceptionMessage){
        if (node instanceof NullNode) {
            throw new WebException(exceptionMessage,400);
        }
    }
}
