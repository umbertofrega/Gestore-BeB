package com.piattaforme.gestorebeb.model.repositories;

import com.piattaforme.gestorebeb.model.entities.Guest;
import com.piattaforme.gestorebeb.model.entities.Reservation;
import com.piattaforme.gestorebeb.model.entities.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface GuestRepository extends JpaRepository<Guest,Integer> {
    boolean existsByEmail(String email);

    Guest findByEmail(String email);

    @Query("SELECT r FROM Reservation r WHERE r.guest.id = :id")
    List<Reservation> findReservationById(@Param("id") int id);

    @Query("SELECT DISTINCT r.guest FROM Reservation r WHERE r.checkin<=:now AND r.checkout > :now")
    List<Guest> findInHouse(@Param("now") LocalDate date);

    @Query("SELECT r.room FROM Reservation r " +
            "WHERE r.guest.id = :guestId " +
            "AND :now >= r.checkin AND :now < r.checkout")
    Room findActiveRoomById(@Param("guestId") int guestId, @Param("now") LocalDate now);
}
