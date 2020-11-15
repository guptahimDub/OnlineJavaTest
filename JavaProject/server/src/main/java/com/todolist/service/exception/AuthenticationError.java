package com.todolist.service.exception;

import lombok.Data;

@Data
public class AuthenticationError extends RuntimeException{

    public AuthenticationError(String message){
        super(message);
    }
}
