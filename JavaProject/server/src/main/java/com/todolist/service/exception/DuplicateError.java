package com.todolist.service.exception;

import lombok.Data;

@Data
public class DuplicateError extends RuntimeException {

    public DuplicateError(String message){
        super(message);
    }
}
