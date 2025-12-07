package com.piattaforme.gestorebeb.model.repositories;

import com.piattaforme.gestorebeb.model.entities.Reservation;
import com.piattaforme.gestorebeb.model.entities.Room;
import com.piattaforme.gestorebeb.model.enums.PaymentStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation,Integer> {
    List<Reservation> findByRoom(Room room);

    Reservation findById(int Id);
    Reservation deleteById(int Id);

    List<Reservation> findByPaymentStatus(PaymentStatus status);
}
