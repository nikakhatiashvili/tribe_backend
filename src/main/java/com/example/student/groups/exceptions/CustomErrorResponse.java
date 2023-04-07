package com.example.student.groups.exceptions;


public class CustomErrorResponse {
    private final int status;
    private final String message;

    public CustomErrorResponse(int status, String message) {
        this.status = status;
        this.message = message;
    }

    public int getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }
}



