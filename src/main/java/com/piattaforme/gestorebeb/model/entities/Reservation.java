package com.piattaforme.gestorebeb.model.entities;


import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Data
@Entity
@Table(
        name = "reservations",
        uniqueConstraints = @UniqueConstraint(
                columnNames = {"room_number", "guest_id", "checkin", "checkout"}
        )
)
public class Reservation {
    @Id
    @Column(name="id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name="room_number")
    private Room room;

    @ManyToOne
    @JoinColumn(name="guest_id")
    private Guest guest;

    @Column(name="checkin")
    private LocalDate checkin;

    @Column(name="checkout")
    private LocalDate checkout;

    @Column(name="price")
    private double price;

    @Column(name="created_at")
    private LocalDate createdAt;
}
