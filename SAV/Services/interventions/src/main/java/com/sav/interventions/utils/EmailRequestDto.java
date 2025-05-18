package com.sav.interventions.utils;

import lombok.Data;

@Data
public class EmailRequestDto {
    private String to;
    private String subject;
    private String text;
    private String attachment; // Base64
    private String filename;

    // Getters et Setters
}

