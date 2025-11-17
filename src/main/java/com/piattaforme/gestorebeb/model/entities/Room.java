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
    @Column(name="number")
    private int number;

    @Enumerated(EnumType.STRING)
    @Column(name="type")
    private RoomType type;

    @Column(name="name")
    private String name;

    @Column(name="price")
    private double price;

    @Column(name="size")
    private int size;

    @Enumerated(EnumType.STRING)
    @Column(name="state")
    private RoomState state;

    private String description;
}
