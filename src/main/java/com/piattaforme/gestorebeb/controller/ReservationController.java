package com.piattaforme.gestorebeb.controller;

import com.piattaforme.gestorebeb.model.entities.Reservation;
import com.piattaforme.gestorebeb.model.exceptions.ReservationNotFoundException;
import com.piattaforme.gestorebeb.model.exceptions.RoomOccupiedException;
import com.piattaforme.gestorebeb.model.services.ReservationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
public class ReservationController {

    private final ReservationService reservationService;

    public ReservationController(ReservationService reservationService){
        this.reservationService=reservationService;
    }

    @GetMapping("/{reservationId}")
    public ResponseEntity<?> getReservation(@PathVariable int reservationId){
        try {
           Reservation reservation = reservationService.findById(reservationId);
            return new ResponseEntity<>(reservation, HttpStatus.FOUND);
        } catch (ReservationNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Prenotazione non trovata");
        }
    }

    @PostMapping("/addReservation")
    public ResponseEntity<?> addReservation(@RequestBody Reservation newReservation){
        try{
             reservationService.reserveRoom(newReservation);
             return new ResponseEntity<>(newReservation, HttpStatus.CREATED);
        } catch(RoomOccupiedException e){
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Camera già occupata");
        }
    }

}
