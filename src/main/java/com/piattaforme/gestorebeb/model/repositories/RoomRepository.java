package com.piattaforme.gestorebeb.model.repositories;

import com.piattaforme.gestorebeb.model.entities.Room;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoomRepository extends JpaRepository<Room,Integer> {

    boolean existsRoomByNumber(int number);
    Room deleteByNumber(int number);
    Room getRoomByNumber(int number);
}
