package com.piattaforme.gestorebeb.model.services;

import com.piattaforme.gestorebeb.model.entities.Room;
import com.piattaforme.gestorebeb.model.enums.RoomState;
import com.piattaforme.gestorebeb.model.exceptions.conflict.RoomAlreadyExistsException;
import com.piattaforme.gestorebeb.model.exceptions.conflict.RoomMismatchException;
import com.piattaforme.gestorebeb.model.exceptions.notFound.RoomNotFoundException;
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
        if (room == null) throw new IllegalArgumentException("Room must not be null");
        if (roomRepository.existsRoomByNumber(room.getNumber()))
            throw new RoomAlreadyExistsException("The room alredy exists");

        return roomRepository.save(room);
    }

    @Transactional
    public Room updateRoom(int roomNumber, Room room) {
        if (room == null) throw new IllegalArgumentException();
        if (room.getNumber() != roomNumber) {
            throw new RoomMismatchException("Room ID mismatch, you cant change the room number");
        }
        if (roomRepository.existsRoomByNumber(roomNumber))
            return roomRepository.save(room);
        throw new RoomNotFoundException("The Room does not exist");
    }

    @Transactional
    public Room deleteRoom(int roomNumber) {
        Room room = roomRepository.getRoomByNumber(roomNumber);
        if (room == null) throw new RoomNotFoundException("The Room does not exist");

        room.setState(RoomState.HIDDEN);
        return roomRepository.save(room);
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

    @Transactional(readOnly = true)
    public Room getRoomByNumber(int number) {
        Room room = roomRepository.getRoomByNumberAndState(number, RoomState.AVAILABLE);
        if (room == null)
            throw new RoomNotFoundException("The Room does not exist");
        return room;
    }

    @Transactional(readOnly = true)
    public List<Room> getAll() {
        return roomRepository.findByStateNot(RoomState.HIDDEN);
    }

    public List<Room> searchRoomsAdvanced(LocalDate checkIn, LocalDate checkOut, int minGuests, double maxPrice, int minSize) {
        List<Room> avaliableRooms = roomRepository.getAvaliable(checkIn,checkOut);
        List<Room> result = new ArrayList<>();
        for(Room r: avaliableRooms) {
            if (r.getMaxGuests() >= minGuests && r.getPrice() <= maxPrice && r.getSize() > minSize)
               result.add(r);
        }
        return result;
    }
}
