package com.example.student.groups.exceptions;

public class OkResponse {
    private String message;

    public OkResponse(String message) {

        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}