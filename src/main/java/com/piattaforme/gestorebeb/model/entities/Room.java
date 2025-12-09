package com.piattaforme.gestorebeb.model.entities;


import com.piattaforme.gestorebeb.model.enums.RoomState;
import com.piattaforme.gestorebeb.model.enums.RoomType;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name="rooms")
public class Room {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name="number")
    private int number;

    @Enumerated(EnumType.STRING)
    @Column(name="type")
    private RoomType type;

    @Column(name="name")
    private String name;

    @Column(name="price")
    private double price;

    @Column(name = "max_guests")
    private int maxGuests;

    @Enumerated(EnumType.STRING)
    @Column(name="state")
    private RoomState state;

    @Column(name = "description")
    private String description;
}
