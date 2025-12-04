package com.piattaforme.gestorebeb.model.services;

import com.piattaforme.gestorebeb.model.entities.Guest;
import com.piattaforme.gestorebeb.model.entities.Reservation;
import com.piattaforme.gestorebeb.model.exceptions.conflict.EmailAlreadyExists;
import com.piattaforme.gestorebeb.model.exceptions.notFound.UserNotFoundException;
import com.piattaforme.gestorebeb.model.repositories.GuestRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class GuestService {

    private final GuestRepository guestRepository;
    private final Guest currentGuest;

    public GuestService(GuestRepository guestRepository) {
        this.guestRepository = guestRepository;
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Jwt jwt = (Jwt) authentication.getPrincipal();
        String email = jwt.getClaim("email");
        this.currentGuest = guestRepository.findByEmail(email);
    }

    @Transactional
    public Guest addGuest(Guest guest){
        if(guest!=null && !guestRepository.existsByEmail(guest.getEmail()))
            return guestRepository.save(guest);
        throw new EmailAlreadyExists("Guest already exists");
    }

    @Transactional(readOnly = true)
    public List<Guest> getAllGuests(){
        return guestRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Guest getCurrentGuest() {
        if (currentGuest == null)
            throw new UserNotFoundException("The guest doesn't exist");
        return currentGuest;
    }

    public List<Reservation> getReservations() {
        if (this.currentGuest == null)
            throw new UserNotFoundException("The guest doesn't exist");
        return guestRepository.findReservationById(currentGuest.getId());
    }

}
