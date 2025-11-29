package com.piattaforme.gestorebeb.controller;

import com.piattaforme.gestorebeb.model.entities.Reservation;
import com.piattaforme.gestorebeb.model.enums.PaymentStatus;
import com.piattaforme.gestorebeb.model.services.ReservationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/reservations")
public class ReservationController {

    private final ReservationService reservationService;

    public ReservationController(ReservationService reservationService){
        this.reservationService=reservationService;
    }

    //Create
    @PreAuthorize("hasRole('RECEPTIONIST') or hasRole('OWNER')")
    @PostMapping
    public ResponseEntity<?> addReservation(@RequestBody Reservation newReservation){
        Reservation reservation = reservationService.reserveRoom(newReservation);
        return new ResponseEntity<>(reservation, HttpStatus.CREATED);
    }

    //Read
    @GetMapping
    public ResponseEntity<?> getAllReservations(){
        return new ResponseEntity<>(reservationService.getAll(), HttpStatus.OK);
    }

    @GetMapping("/{reservationId}")
    public ResponseEntity<?> getReservation(@PathVariable int reservationId){
        Reservation reservation = reservationService.findById(reservationId);
        return new ResponseEntity<>(reservation, HttpStatus.OK);
    }

    //Update
    @PreAuthorize("hasRole('RECEPTIONIST') or hasRole('OWNER')")
    @PutMapping("/{reservationId}")
    public ResponseEntity<?> changeStatus(@PathVariable int reservationId, PaymentStatus status){
        Reservation reservation = reservationService.changePaymentStatus(reservationId, status);
        return new ResponseEntity<>(reservation, HttpStatus.OK);
    }

    //Delete
    @PreAuthorize("hasRole('RECEPTIONIST') or hasRole('OWNER')")
    @DeleteMapping("/{reservationId}")
    public ResponseEntity<?> deleteReservation(@PathVariable int reservationId) {
        Reservation reservation = reservationService.deleteReservation(reservationId);
        return new ResponseEntity<>(reservation, HttpStatus.OK);
    }

}
