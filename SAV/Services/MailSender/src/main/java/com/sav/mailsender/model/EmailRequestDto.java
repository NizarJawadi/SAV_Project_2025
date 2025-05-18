package com.sav.mailsender.model;

import lombok.Data;

@Data
public class EmailRequestDto {
    private String to;
    private String subject;
    private String text;
    private String filename;
    private String attachment; // Base64-encoded string
}
