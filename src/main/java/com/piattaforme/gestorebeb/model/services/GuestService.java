package com.piattaforme.gestorebeb.model.services;

import com.piattaforme.gestorebeb.model.entities.Guest;
import com.piattaforme.gestorebeb.model.repositories.GuestRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class GuestService {

    private final GuestRepository guestRepository;

    public GuestService(GuestRepository guestRepository) {
        this.guestRepository = guestRepository;
    }

    @Transactional
    public Guest addGuest(Guest guest){
        if(guest!=null && !guestRepository.existsByEmail(guest.getEmail()))
            return guestRepository.save(guest);

        throw new IllegalArgumentException("Guest already exists");
    }

    @Transactional(readOnly = true)
    public List<Guest> getAllGuests(){
        return guestRepository.findAll();
    }
}
