package com.piattaforme.gestorebeb.model.services;

import com.piattaforme.gestorebeb.model.entities.Room;
import com.piattaforme.gestorebeb.model.enums.RoomState;
import com.piattaforme.gestorebeb.model.exeptions.RoomAlredyExistsException;
import com.piattaforme.gestorebeb.model.exeptions.RoomNotFoundException;
import com.piattaforme.gestorebeb.model.repositories.AdminRepository;
import com.piattaforme.gestorebeb.model.repositories.RoomRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RoomService {

    private final RoomRepository roomRepository;

    public RoomService(RoomRepository roomRepository, AdminRepository adminRepository) {
        this.roomRepository=roomRepository;
    }

    @Transactional
    public Room addRoom(Room room) {
        //TODO:check on admin
        if(room!=null && !roomRepository.existsRoomByNumber(room.getNumber()))
            return roomRepository.save(room);
        throw new RoomAlredyExistsException("The room alredy exists");
    }

    @Transactional
    public Room updateRoom(int RoomNumber, Room room) {
        if(room!=null && roomRepository.existsRoomByNumber(room.getNumber()))
            return roomRepository.save(room);
        throw new RoomNotFoundException("Maybe the Room does not exist");
    }

    @Transactional
    public void deleteRoom(int roomNumber) {
        if(roomRepository.existsRoomByNumber(roomNumber))
            roomRepository.deleteByNumber(roomNumber);
        throw new RoomNotFoundException("Maybe the Room does not exist");
    }

    @Transactional
    public Room changeState(int roomNumber, RoomState newState) {
        if(roomRepository.existsRoomByNumber(roomNumber)) {
            Room room = roomRepository.getRoomByNumber(roomNumber);
            if (room.getState().equals(newState)) {
                return room;
            }
            room.setState(newState);
            return roomRepository.save(room);
        }
        throw new RoomNotFoundException("Maybe the Room does not exist");
    }

    @Transactional(readOnly = true)
    public Room getRoomByNumber(int number) {
        Room room = roomRepository.getRoomByNumber(number);
        if (room == null)
            throw new RoomNotFoundException("Maybe the Room does not exist");
        return room;
    }
}
