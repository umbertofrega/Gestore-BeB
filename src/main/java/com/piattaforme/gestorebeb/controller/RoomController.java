package com.piattaforme.gestorebeb.controller;

import com.piattaforme.gestorebeb.model.entities.Room;
import com.piattaforme.gestorebeb.model.enums.RoomState;
import com.piattaforme.gestorebeb.model.enums.RoomType;
import com.piattaforme.gestorebeb.model.exceptions.RoomAlreadyExistsException;
import com.piattaforme.gestorebeb.model.services.RoomService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/rooms")
public class RoomController {

    private final RoomService roomService;

    public RoomController(RoomService roomService) {
        this.roomService = roomService;
    }

    //Create
    @PreAuthorize("hasRole('RECEPTIONIST') or hasRole('OWNER')")
    @PostMapping
    public ResponseEntity<?> addRoom(@RequestBody Room newRoom) {
        Room addedRoom;
        try {
            addedRoom = roomService.addRoom(newRoom);
        } catch (RoomAlreadyExistsException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "A room with this number already exists");
        }
        return new ResponseEntity<>(addedRoom, HttpStatus.CREATED);
    }

    //Read
    @GetMapping(value = "/{room_number}")
    public ResponseEntity<?> getRoomData(@PathVariable("room_number") int roomNumber) {
        try {
            return new ResponseEntity<>(roomService.getRoomByNumber(roomNumber),HttpStatus.OK);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Room not found");
        }
    }

    @GetMapping
    public List<Room> getAllRooms(){
        return roomService.getAll();
    }
    
    @GetMapping("/search")
    public ResponseEntity<List<Room>> searchRooms(
            @RequestParam("checkin") LocalDate checkIn,
            @RequestParam("checkout") LocalDate checkOut,
            @RequestParam(value = "type", required = false, defaultValue = "SINGLE") List<RoomType> types,
            @RequestParam(value = "maxPrice", required = false, defaultValue = "300") double maxPrice,
            @RequestParam(value = "minSize", required = false, defaultValue = "0") int minSize
    ) {
        List<Room> rooms = roomService.searchRoomsAdvanced(checkIn, checkOut, types, maxPrice, minSize);

        if (rooms.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No rooms available matching criteria");
        }
        return ResponseEntity.ok(rooms);
    }

    //Update
    @PreAuthorize("hasRole('RECEPTIONIST') or hasRole('OWNER')")
    @PutMapping(value = "/{room_number}")
    public ResponseEntity<?> changeRoom(@PathVariable("room_number") int roomNumber, @RequestBody Room newRoom) {
        Room room;
        try {
            room = roomService.updateRoom(roomNumber,newRoom);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return new ResponseEntity<>(room, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('RECEPTIONIST') or hasRole('OWNER')")
    @PutMapping(value = "/{room_number}/state")
    public ResponseEntity<?> changeState(@PathVariable("room_number") int roomNumber, @RequestBody RoomState newState) {
        Room newRoom = roomService.changeState(roomNumber,newState);
        return new ResponseEntity<>(newRoom, HttpStatus.OK);
    }

    //Delete
    @PreAuthorize("hasRole('RECEPTIONIST') or hasRole('OWNER')")
    @DeleteMapping(value = "/{room_number}")
    public ResponseEntity<?> deleteRoom(@PathVariable("room_number") int roomNumber) {
        Room deletedRoom;
        try {
            deletedRoom = roomService.deleteRoom(roomNumber);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Room not found");
        }
        return new ResponseEntity<>(deletedRoom, HttpStatus.OK);
    }
}
