package com.piattaforme.gestorebeb.model.repositories;

import com.piattaforme.gestorebeb.model.entities.Reservation;
import com.piattaforme.gestorebeb.model.entities.Room;
import com.piattaforme.gestorebeb.model.enums.RoomType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation,Integer> {
    List<Reservation> findByRoom(Room room);
    Reservation getById(int Id);
    Reservation deleteById(int Id);

    @Query("SELECT r FROM Reservation r WHERE r.room.type = :type")
    List<Reservation> findByRoomType(@Param("type") RoomType type);
}
