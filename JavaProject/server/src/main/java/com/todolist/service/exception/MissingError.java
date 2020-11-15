package com.todolist.service.exception;

import lombok.Data;

@Data
public class MissingError extends RuntimeException {

    public MissingError(String message){
        super(message);
    }
}
