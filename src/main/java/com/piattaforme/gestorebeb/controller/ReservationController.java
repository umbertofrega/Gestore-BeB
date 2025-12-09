package com.piattaforme.gestorebeb.controller;

import com.piattaforme.gestorebeb.model.entities.Reservation;
import com.piattaforme.gestorebeb.model.enums.PaymentStatus;
import com.piattaforme.gestorebeb.model.services.EmailService;
import com.piattaforme.gestorebeb.model.services.ReservationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reservations")
public class ReservationController {

    private final ReservationService reservationService;
    private final EmailService emailService;

    public ReservationController(ReservationService reservationService, EmailService emailService) {
        this.reservationService=reservationService;
        this.emailService = emailService;
    }

    //Create
    @PostMapping
    public ResponseEntity<?> addReservation(@RequestBody Reservation newReservation){
        Reservation reservation = reservationService.reserveRoom(newReservation);
        emailService.sendReservationConfirmation(newReservation);
        return new ResponseEntity<>(reservation, HttpStatus.CREATED);
    }

    //Read
    @PreAuthorize("hasRole('RECEPTIONIST') or hasRole('OWNER')")
    @GetMapping
    public ResponseEntity<?> getAllReservations(){
        return new ResponseEntity<>(reservationService.getAll(), HttpStatus.OK);
    }

    @GetMapping("/{reservationId}")
    public ResponseEntity<?> getReservation(@PathVariable int reservationId){
        Reservation reservation = reservationService.findById(reservationId);
        return new ResponseEntity<>(reservation, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('RECEPTIONIST') or hasRole('OWNER')")
    @GetMapping("/search")
    public ResponseEntity<?> getOnlyPending() {
        List<Reservation> reservations = reservationService.searchReservationAdvanced();
        return new ResponseEntity<>(reservations, HttpStatus.OK);
    }

    //Update
    @PreAuthorize("hasRole('RECEPTIONIST') or hasRole('OWNER')")
    @PutMapping("/{reservationId}")
    public ResponseEntity<?> updateStatus(@PathVariable int reservationId) {
        Reservation reservation = reservationService.changePaymentStatus(reservationId, PaymentStatus.PAID);
        return new ResponseEntity<>(reservation, HttpStatus.OK);
    }

    //Delete
    @DeleteMapping("/{reservationId}")
    public ResponseEntity<?> deleteReservation(@PathVariable int reservationId) {
        Reservation reservation = reservationService.deleteReservation(reservationId);
        emailService.sendCancellationConfirmation(reservation);
        return new ResponseEntity<>(reservation, HttpStatus.OK);
    }

}
