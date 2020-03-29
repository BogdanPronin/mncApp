package com.github.bogdan.services;


import com.github.bogdan.exceptions.MyException;
import io.javalin.http.Context;

public class ContextService {
    public static void authorizationFailed(Context ctx){
        ctx.result("Authorization failed");
        ctx.status(401);
    }
    public static void youAreNotAdmin(Context  ctx){
        ctx.result("You aren't admin");
        ctx.status(403);
    }
    public static void created(Context ctx){
        ctx.status(200);
        ctx.result("Created");
    }
    public static void updated(Context ctx){
        ctx.status(200);
        ctx.result("Updated");
    }
    public static void deleted(Context ctx){
        ctx.status(200);
        ctx.result("Deleted");
    }
    public static void basicAuthIsEmpty(Context ctx){
        if (!ctx.basicAuthCredentialsExist()){
            throw new MyException("Basic auth is empty");
        }
    }
    public static void bodyRequestIsEmpty(Context ctx){
        ctx.status(401);
        throw new MyException("Request body is empty");
    }
}
