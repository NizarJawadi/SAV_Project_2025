package com.sav.springchatbot.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "conversations")
public class Conversation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;
    private String question;
    @Lob
    @Column(columnDefinition = "TEXT")  // This sets the column type to TEXT in the database
    private String response;

    private LocalDateTime createdAt = LocalDateTime.now();
}