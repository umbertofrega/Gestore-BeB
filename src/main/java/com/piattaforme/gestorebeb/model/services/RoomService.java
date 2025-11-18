package com.piattaforme.gestorebeb.model.services;

import com.piattaforme.gestorebeb.model.entities.Room;
import com.piattaforme.gestorebeb.model.enums.RoomState;
import com.piattaforme.gestorebeb.model.repositories.RoomRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RoomService {

    private final RoomRepository roomRepository;

    public RoomService(RoomRepository roomRepository) {
        this.roomRepository=roomRepository;
    }

    @Transactional
    public Room addRoom(Room room) {
        checkExistance(room.getNumber());
        return roomRepository.save(room);
    }

    @Transactional
    public Room updateRoom(Room room) {
        checkExistance(room.getNumber());
        return roomRepository.save(room);
    }

    @Transactional
    public void deleteRoom(Integer number) {
        checkExistance(number);
        roomRepository.deleteByNumber(number);
    }

    @Transactional
    public Room changeState(Room room, RoomState newState) {
        checkExistance(room.getNumber());
        if(room.getState().equals(newState)) {
            return room;
        }
        room.setState(newState);
        return roomRepository.save(room);
    }

    private void checkExistance(Integer number) {
        if(!roomRepository.existsRoomByNumber(number)) {
            throw new IllegalArgumentException("Room number not found");
        }
    }
}
