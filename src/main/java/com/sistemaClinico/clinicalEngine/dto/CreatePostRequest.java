package com.sistemaClinico.clinicalEngine.dto;

import lombok.Data;

@Data
public class CreatePostRequest {

    private String title;
    private String content;
    private String status ;
}
