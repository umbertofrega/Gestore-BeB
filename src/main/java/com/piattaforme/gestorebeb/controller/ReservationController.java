package com.piattaforme.gestorebeb.controller;

import com.piattaforme.gestorebeb.model.entities.Reservation;
import com.piattaforme.gestorebeb.model.enums.PaymentStatus;
import com.piattaforme.gestorebeb.model.exceptions.ReservationCancellationDeadlineException;
import com.piattaforme.gestorebeb.model.exceptions.ReservationNotFoundException;
import com.piattaforme.gestorebeb.model.exceptions.RoomNotFoundException;
import com.piattaforme.gestorebeb.model.exceptions.RoomOccupiedException;
import com.piattaforme.gestorebeb.model.services.ReservationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/reservations")
public class ReservationController {

    private final ReservationService reservationService;

    public ReservationController(ReservationService reservationService){
        this.reservationService=reservationService;
    }

    //Create
    @PostMapping
    public ResponseEntity<?> addReservation(@RequestBody Reservation newReservation){
        try{
            Reservation reservation = reservationService.reserveRoom(newReservation);
            return new ResponseEntity<>(reservation, HttpStatus.CREATED);
        } catch(RoomOccupiedException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "The room is reserved");
        }
    }

    //Read
    @PreAuthorize("hasRole('Admin')")
    @GetMapping
    public ResponseEntity<?> getAllReservations(){
        return new ResponseEntity<>(reservationService.getAll(), HttpStatus.OK);
    }

    @GetMapping("/{reservationId}")
    public ResponseEntity<?> getReservation(@PathVariable int reservationId){
        Reservation reservation;
        try {
           reservation = reservationService.findById(reservationId);
        } catch (ReservationNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Reservation not found");
        }
        return new ResponseEntity<>(reservation, HttpStatus.OK);
    }

    //Update
    @PreAuthorize("hasRole('Admin')")
    @PutMapping("/{reservationId}")
    public ResponseEntity<?> changeStatus(@PathVariable int reservationId, PaymentStatus status){
        Reservation reservation;
        try {
            reservation = reservationService.changePaymentStatus(reservationId,status);
        } catch (ReservationNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Reservation not found");
        }
        return new ResponseEntity<>(reservation, HttpStatus.OK);
    }

    //Delete
    @PreAuthorize("hasRole('Admin')")
    @DeleteMapping("/{reservationId}")
    public ResponseEntity<?> deleteReservation(@PathVariable int reservationId) {
        Reservation reservation;
        try {
            reservation = reservationService.deleteReservation(reservationId);
        } catch (RoomNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Reservation not found");
        } catch (ReservationCancellationDeadlineException t){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "The time to revoke the reservation has finished");
        }
        return new ResponseEntity<>(reservation, HttpStatus.OK);
    }

}
