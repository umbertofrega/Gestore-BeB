package com.piattaforme.gestorebeb.model.services;


import com.piattaforme.gestorebeb.model.entities.Reservation;
import com.piattaforme.gestorebeb.model.enums.PaymentStatus;
import com.piattaforme.gestorebeb.model.exceptions.ReservationCancellationDeadlineException;
import com.piattaforme.gestorebeb.model.exceptions.ReservationNotFoundException;
import com.piattaforme.gestorebeb.model.exceptions.RoomOccupiedException;
import com.piattaforme.gestorebeb.model.repositories.ReservationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
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

    @Transactional(readOnly = true)
    public Reservation findById(int id) {
        Reservation newReservation = reservationRepository.getById(id);
        if(newReservation == null) {
            throw new ReservationNotFoundException("The reservation doesn't exist");
        }
        return newReservation;
    }

    @Transactional
    public Reservation reserveRoom(Reservation newReservation) {
        if(newReservation.getRoom() == null)
            throw new IllegalArgumentException("Room is null");

        if(isRoomOccupied(newReservation))
            throw new RoomOccupiedException("The room is already used in that period");
        return reservationRepository.save(newReservation);
    }

    private boolean isRoomOccupied(Reservation newReservation) {
        LocalDate newCheckIn = newReservation.getCheckin();
        LocalDate newCheckOut = newReservation.getCheckout();

        List<Reservation> existingReservations = reservationRepository.findByRoom(newReservation.getRoom());

        for (Reservation existingRes : existingReservations) {
            LocalDate existingCheckIn = existingRes.getCheckin();
            LocalDate existingCheckOut = existingRes.getCheckout();
            /*
             Controlla se il checkin è prima di un checkout che già c'è e poi vede se il checkout
            è dopo del checkin, se queste cose sono vere le prenotazioni si sovrappongono
            */
            if (newCheckIn.isBefore(existingCheckOut) && newCheckOut.isAfter(existingCheckIn)) {
                return true;
            }
        }

        return false;
    }

    @Transactional
    public Reservation changePaymentStatus(int reservationId, PaymentStatus status){
        if(reservationRepository.existsById(reservationId)){
            Reservation res = reservationRepository.getById(reservationId);
            res.setPaymentStatus(status);
            return res;
        }
        throw new ReservationNotFoundException("The reservation doesn't exist");
    }

    @Transactional
    public Reservation deleteReservation(int reservationId){

        if(reservationRepository.existsById(reservationId)) {
            Reservation reservation = reservationRepository.getById(reservationId);
            LocalDate deadline = reservation.getCheckin().minusWeeks(1);

            if(LocalDate.now().isAfter(deadline))
                throw new ReservationCancellationDeadlineException("The time has passed");

            return reservationRepository.deleteById(reservationId);
        }
        else throw new ReservationNotFoundException("The reservation doesn't exist");
    }

}
