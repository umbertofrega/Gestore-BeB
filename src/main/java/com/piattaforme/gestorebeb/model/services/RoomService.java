package com.piattaforme.gestorebeb.model.services;

import com.piattaforme.gestorebeb.model.entities.Room;
import com.piattaforme.gestorebeb.model.enums.RoomState;
import com.piattaforme.gestorebeb.model.enums.RoomType;
import com.piattaforme.gestorebeb.model.exceptions.RoomAlreadyExistsException;
import com.piattaforme.gestorebeb.model.exceptions.RoomMismatchException;
import com.piattaforme.gestorebeb.model.exceptions.RoomNotDeletableException;
import com.piattaforme.gestorebeb.model.exceptions.RoomNotFoundException;
import com.piattaforme.gestorebeb.model.repositories.RoomRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class RoomService {

    private final RoomRepository roomRepository;

    public RoomService(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }

    @Transactional
    public Room addRoom(Room room) {
        if (room != null && !roomRepository.existsRoomByNumber(room.getNumber()))
            return roomRepository.save(room);
        throw new RoomAlreadyExistsException("The room alredy exists");
    }

    @Transactional
    public Room updateRoom(int roomNumber, Room room) {
        if (room == null) throw new IllegalArgumentException();
        if (room.getNumber() != roomNumber) {
            if(roomRepository.existsRoomByNumber(room.getNumber()))
                throw new RoomMismatchException("Room ID mismatch: Cannot update Room because the number in the request body already exists");
        }
        if (roomRepository.existsRoomByNumber(roomNumber))
            return roomRepository.save(room);
        throw new RoomNotFoundException("The Room does not exist");
    }

    @Transactional
    public Room deleteRoom(int roomNumber) {

        if (roomRepository.existsRoomByNumber(roomNumber)){
            if(isReserved(roomNumber))
                throw new RoomNotDeletableException("The room is reserved, you can't delete it.");

            return roomRepository.deleteByNumber(roomNumber);
        }
        throw new RoomNotFoundException("The Room does not exist");
    }

    @Transactional
    public Room changeState(int roomNumber, RoomState newState) {
        if (roomRepository.existsRoomByNumber(roomNumber)) {
            Room room = roomRepository.getRoomByNumber(roomNumber);
            if (room.getState().equals(newState)) {
                return room;
            }
            room.setState(newState);
            return roomRepository.save(room);
        }
        throw new RoomNotFoundException("The Room does not exist");
    }

    private boolean isReserved(int roomNumber){
       for(Room r : roomRepository.getReserved(LocalDate.now()))
           if(r.getNumber() == roomNumber)
               return true;
       return false;
    }

    @Transactional(readOnly = true)
    public Room getRoomByNumber(int number) {
        Room room = roomRepository.getRoomByNumber(number);
        if (room == null)
            throw new RoomNotFoundException("The Room does not exist");
        return room;
    }

    @Transactional(readOnly = true)
    public List<Room> getAll() {
        return roomRepository.findAll();
    }

    public List<Room> searchRoomsAdvanced(LocalDate checkIn, LocalDate checkOut, List<RoomType> types, double maxPrice, int minSize) {
        List<Room> avaliableRooms = roomRepository.getAvaliable(checkIn,checkOut);
        List<Room> result = new ArrayList<>();
        for(Room r: avaliableRooms) {
           if(types.contains(r.getType()) && r.getSize()>minSize && r.getPrice() < maxPrice)
               result.add(r);
        }
        return result;
    }
}
