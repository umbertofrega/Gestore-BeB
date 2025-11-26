package com.piattaforme.gestorebeb.model.repositories;

import com.piattaforme.gestorebeb.model.entities.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface RoomRepository extends JpaRepository<Room,Integer> {

    boolean existsRoomByNumber(int number);
    Room deleteByNumber(int number);
    Room getRoomByNumber(int number);

    @Query("SELECT r FROM Room r WHERE r.number NOT IN (" +
            "    SELECT res.room.number " +
            "    FROM Reservation res " +
            "    WHERE res.room.number = r.number AND " +
            " (:checkin < res.checkout AND :checkout > res.checkin)" +
            ") AND r.state = 'AVAILABLE'")
    List<Room> getAvaliable(@Param("checkn") LocalDate checkIn,@Param("checkout") LocalDate checkOut);

    @Query("SELECT DISTINCT res.room FROM Reservation res WHERE res.checkout > :now")
    List<Room> getReserved(@Param("now") LocalDate now);
}
