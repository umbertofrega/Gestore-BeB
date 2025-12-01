package com.piattaforme.gestorebeb.controller;

import com.piattaforme.gestorebeb.model.entities.Guest;
import com.piattaforme.gestorebeb.model.services.GuestService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/guests")
public class GuestController {

    private final GuestService guestService;

    public GuestController(GuestService guestService) {
        this.guestService = guestService;
    }

    @PostMapping
    public ResponseEntity<Guest> registerGuest(@RequestBody Guest guest) {
        Guest newGuest = guestService.addGuest(guest);
        return new ResponseEntity<>(newGuest, HttpStatus.CREATED);
    }


    @PreAuthorize("hasAnyRole('RECEPTIONIST', 'OWNER')")
    @GetMapping
    public ResponseEntity<List<Guest>> getAllGuests() {
        return ResponseEntity.ok(guestService.getAllGuests());
    }

}