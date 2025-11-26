package com.piattaforme.gestorebeb.controller;

import com.piattaforme.gestorebeb.model.entities.Room;
import com.piattaforme.gestorebeb.model.enums.RoomState;
import com.piattaforme.gestorebeb.model.services.RoomService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/room")
public class RoomController {

    private final RoomService roomService;

    public RoomController(RoomService roomService) {
        this.roomService = roomService;
    }

    //Create
    @PostMapping(value = "/addRoom")
    public ResponseEntity<?> addRoom(@RequestBody Room newRoom) {
        roomService.addRoom(newRoom);
        return new ResponseEntity<>(newRoom, HttpStatus.CREATED);
    }

    //Read
    @GetMapping(value = "/{room_number}")
    public ResponseEntity<?> getRoomData(@PathVariable("room_number") int roomNumber) {
        try {
            return new ResponseEntity<>(roomService.getRoomByNumber(roomNumber),HttpStatus.FOUND);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Room not found");
        }
    }

    @GetMapping
    public List<Room> getAllRooms(){
        return roomService.getAll();
    }

    @GetMapping
    public ResponseEntity<?> getAvaliableRooms(LocalDate checkin, LocalDate checkout) {
        List<Room> rooms = roomService.getAvaliable(checkin, checkout);
        if (rooms == null || rooms.isEmpty())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "There are no avaliable rooms");
        return new ResponseEntity<>(rooms, HttpStatus.FOUND);
    }

    //Update
    @PutMapping(value = "/{room_number}/updateRoom")
    public ResponseEntity<?> changeRoom(@PathVariable("room_number") int roomNumber, @RequestBody Room newRoom) {
        Room room;
        try {
            room = roomService.updateRoom(roomNumber,newRoom);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return new ResponseEntity<>(room, HttpStatus.OK);
    }

    @PutMapping(value = "/{room_number}/updateState")
    public ResponseEntity<?> changeState(@PathVariable("room_number") int roomNumber, @RequestBody RoomState newState) {
        Room newRoom = roomService.changeState(roomNumber,newState);
        return new ResponseEntity<>(newRoom, HttpStatus.OK);
    }

    //Delete
    @DeleteMapping(value = "/{room_number}/delete")
    public ResponseEntity<?> deleteRoom(@PathVariable("room_number") int roomNumber) {
        Room deletedRoom;
        try {
            deletedRoom = roomService.deleteRoom(roomNumber);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Room not found");
        }
        return new ResponseEntity<>(deletedRoom, HttpStatus.NO_CONTENT);
    }
}
