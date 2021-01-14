package com.example.faceApi.models;

import lombok.Data;

@Data
public class Response {

    private int status;
    private String message;
    private Object result;

    public Response(int status, String message, Object result) {
        this.status = status;
        this.message = message;
        this.result = result;
    }
}
