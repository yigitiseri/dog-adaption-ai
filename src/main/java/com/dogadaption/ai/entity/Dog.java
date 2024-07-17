package com.dogadaption.ai.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

import java.util.UUID;

@Entity
@Data
public class Dog {
    @Id
    private UUID id;

    private String name;

    private String description;
}
