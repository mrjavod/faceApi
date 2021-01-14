package com.example.faceApi.models;

import lombok.Data;

import java.util.List;

@Data
public class Person {

    private int id;
    private String name;
    private List<String> imgBase64;
}
