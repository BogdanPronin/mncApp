package com.github.bogdan.exceptions;

public class SQLRefactoredException extends RuntimeException {
    public SQLRefactoredException(String message) {
        super(refactoredMessage(message));
    }
    public static String refactoredMessage(String message){
        if(message.contains("UNIQUE")){
            String s ="";
            for(int i = message.indexOf(":")+2; i < message.length()-1;i++){
                s+=message.charAt(i);
            }
            return s;
        }
        return "another SQLException";
    }
}
