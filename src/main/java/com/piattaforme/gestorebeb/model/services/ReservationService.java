package com.piattaforme.gestorebeb.model.services;


import com.piattaforme.gestorebeb.model.entities.Reservation;
import com.piattaforme.gestorebeb.model.enums.PaymentStatus;
import com.piattaforme.gestorebeb.model.exceptions.conflict.ReservationDatesMismatch;
import com.piattaforme.gestorebeb.model.exceptions.conflict.RoomOccupiedException;
import com.piattaforme.gestorebeb.model.exceptions.forbidden.ReservationCancellationDeadlineException;
import com.piattaforme.gestorebeb.model.exceptions.notFound.ReservationNotFoundException;
import com.piattaforme.gestorebeb.model.repositories.ReservationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Comparator;
import java.util.List;

@Service
public class ReservationService {

    private final ReservationRepository reservationRepository;

    public ReservationService(ReservationRepository reservationService) {
        this.reservationRepository = reservationService;
    }

    @Transactional(readOnly = true)
    public List<Reservation> getAll(){
        return sortByMostRecent(reservationRepository.findAll());
    }

    @Transactional(readOnly = true)
    public Reservation findById(int id) {
        Reservation newReservation = reservationRepository.findById(id);
        if(newReservation == null) {
            throw new ReservationNotFoundException("The reservation doesn't exist");
        }
        return newReservation;
    }

    @Transactional
    public Reservation reserveRoom(Reservation newReservation) {
        LocalDate checkin = newReservation.getCheckin();
        LocalDate checkout = newReservation.getCheckout();

        if(newReservation.getRoom() == null)
            throw new IllegalArgumentException("Room is null");
        if (checkin.equals(checkout) || !checkout.isAfter(checkin))
            throw new ReservationDatesMismatch("You can't use these dates");

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
        Reservation reservation = reservationRepository.findById(reservationId);
        if (reservation != null) {
            reservation.setPaymentStatus(status);
            reservationRepository.save(reservation);
            return reservation;
        }
        throw new ReservationNotFoundException("The reservation doesn't exist");
    }

    @Transactional
    public Reservation deleteReservation(int reservationId){
        Reservation reservation = reservationRepository.findById(reservationId);
        if (reservation != null) {
            LocalDate deadline = reservation.getCheckin().minusWeeks(1);

            if(LocalDate.now().isAfter(deadline))
                throw new ReservationCancellationDeadlineException("The time has passed");

            return reservationRepository.deleteById(reservationId);
        }
        else throw new ReservationNotFoundException("The reservation doesn't exist");
    }

    @Transactional(readOnly = true)
    public List<Reservation> searchReservationAdvanced() {
        List<Reservation> reservations = reservationRepository.findByPaymentStatus(PaymentStatus.PENDING);
        return sortByMostRecent(reservations);
    }

    private List<Reservation> sortByMostRecent(List<Reservation> reservations) {
        LocalDate now = LocalDate.now();
        Comparator<Reservation> comparator = (r1, r2) -> {
            long diff1 = Math.abs(ChronoUnit.DAYS.between(now, r1.getCheckin()));
            long diff2 = Math.abs(ChronoUnit.DAYS.between(now, r2.getCheckin()));

            return Long.compare(diff1, diff2);
        };
        reservations.sort(comparator);
        return reservations;
    }

}
