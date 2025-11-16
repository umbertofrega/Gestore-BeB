package com.piattaforme.gestorebeb.entities;


import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name="rooms")
public class Room {
    @Id
    @Column(name="number")
    private int number;

    @Column(name="type")
    private String type;

    @Column(name="name")
    private String name;

    @Column(name="price")
    private double price;

    @Column(name="size")
    private int size;

    private String description;
}
