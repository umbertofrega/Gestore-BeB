package com.piattaforme.gestorebeb.controller;

import com.piattaforme.gestorebeb.model.entities.Room;
import com.piattaforme.gestorebeb.model.enums.RoomState;
import com.piattaforme.gestorebeb.model.services.RoomService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/room")
public class RoomController {

    private final RoomService roomService;

    public RoomController(RoomService roomService) {
        this.roomService = roomService;
    }

    @GetMapping(value = "/{room_number}")
    public ResponseEntity<?> getRoomData(@PathVariable("room_number") int roomNumber) {
        try {
            return new ResponseEntity<>(roomService.getRoomByNumber(roomNumber),HttpStatus.FOUND);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Camera non trovata");
        }
    }

    @GetMapping
    public List<Room> getAllRooms(){
        return roomService.getAll();
    }

    @PostMapping(value = "/addRoom")
    public ResponseEntity<?> addRoom(@RequestBody Room newRoom) {
        roomService.addRoom(newRoom);
        return new ResponseEntity<>(newRoom, HttpStatus.CREATED);
    }

    @PutMapping(value = "/{room_number}/updateState")
    public ResponseEntity<?> changeState(@PathVariable("room_number") int roomNumber, @RequestBody RoomState newState) {
        Room newRoom = roomService.changeState(roomNumber,newState);
        return new ResponseEntity<>(newRoom, HttpStatus.OK);
    }

    @PutMapping(value = "/{room_number}/updateRoom")
    public ResponseEntity<?> changeRoom(@PathVariable("room_number") int roomNumber, @RequestBody Room newRoom) {
        roomService.updateRoom(roomNumber,newRoom);
        return new ResponseEntity<>(newRoom, HttpStatus.OK);
    }

    @DeleteMapping(value = "/{room_number}/delete")
    public ResponseEntity<?> deleteRoom(@PathVariable("room_number") int roomNumber) {
        Room deletedRoom = roomService.deleteRoom(roomNumber);
        return new ResponseEntity<>(deletedRoom, HttpStatus.NO_CONTENT);
    }
}
