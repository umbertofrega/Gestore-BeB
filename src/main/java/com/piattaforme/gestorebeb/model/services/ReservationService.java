package com.piattaforme.gestorebeb.model.services;


import com.piattaforme.gestorebeb.model.entities.Reservation;
import com.piattaforme.gestorebeb.model.repositories.ReservationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ReservationService {

    private final ReservationRepository reservationRepository;

    public ReservationService(ReservationRepository reservationService) {
        this.reservationRepository = reservationService;
    }

    @Transactional(readOnly = true)
    public List<Reservation> getAll(){
        return reservationRepository.findAll();
    }
}
