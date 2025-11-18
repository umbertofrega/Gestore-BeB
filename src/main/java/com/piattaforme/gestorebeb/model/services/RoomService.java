package com.piattaforme.gestorebeb.model.services;

import com.piattaforme.gestorebeb.model.entities.Room;
import com.piattaforme.gestorebeb.model.enums.RoomState;
import com.piattaforme.gestorebeb.model.exeptions.RoomAlredyExistsException;
import com.piattaforme.gestorebeb.model.exeptions.RoomNotFoundException;
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
        if(room!=null && !roomRepository.existsRoomByNumber(room.getNumber()))
            return roomRepository.save(room);
        throw new RoomAlredyExistsException("The room alredy exists");
    }

    @Transactional
    public Room updateRoom(Room room) {
        if(room!=null && roomRepository.existsRoomByNumber(room.getNumber()))
            return roomRepository.save(room);
        throw new RoomNotFoundException("Maybe the Room does not exist");
    }

    @Transactional
    public void deleteRoom(Room room) {
        if(room!=null && roomRepository.existsRoomByNumber(room.getNumber()))
            roomRepository.delete(room);
        throw new RoomNotFoundException("Maybe the Room does not exist");
    }

    @Transactional
    public Room changeState(Room room, RoomState newState) {
        if(room!=null && roomRepository.existsRoomByNumber(room.getNumber())) {
            if (room.getState().equals(newState)) {
                return room;
            }
            room.setState(newState);
            return roomRepository.save(room);
        }
        throw new RoomNotFoundException("Maybe the Room does not exist");
    }
}
