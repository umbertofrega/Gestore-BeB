package com.piattaforme.gestorebeb.controller;

import com.piattaforme.gestorebeb.model.entities.Room;
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
        Room addedRoom = roomService.addRoom(newRoom);
        return new ResponseEntity<>(addedRoom, HttpStatus.CREATED);
    }

    //Read
    @GetMapping(value = "/{room_number}")
    public ResponseEntity<?> getRoom(@PathVariable("room_number") int roomNumber) {
        return new ResponseEntity<>(roomService.getRoomByNumber(roomNumber), HttpStatus.OK);
    }

    @GetMapping
    public List<Room> getAllRooms(){
        return roomService.getAll();
    }
    
    @GetMapping("/search")
    public ResponseEntity<List<Room>> searchRooms(
            @RequestParam("checkin") LocalDate checkIn,
            @RequestParam("checkout") LocalDate checkOut,
            @RequestParam(value = "maxPrice", required = false, defaultValue = "3000") double maxPrice,
            @RequestParam(value = "minSize", required = false, defaultValue = "0") int minSize,
            @RequestParam(value = "minGuests", required = false, defaultValue = "1") int minGuests
    ) {
        List<Room> rooms = roomService.searchRoomsAdvanced(checkIn, checkOut, minGuests, maxPrice, minSize);

        if (rooms.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No rooms available matching criteria");
        }
        return ResponseEntity.ok(rooms);
    }

    //Update
    @PreAuthorize("hasRole('RECEPTIONIST') or hasRole('OWNER')")
    @PutMapping(value = "/{room_number}")
    public ResponseEntity<?> updateRoom(@PathVariable("room_number") int roomNumber, @RequestBody Room newRoom) {
        Room room = roomService.updateRoom(roomNumber, newRoom);
        return new ResponseEntity<>(room, HttpStatus.OK);
    }


    //Delete
    @PreAuthorize("hasRole('RECEPTIONIST') or hasRole('OWNER')")
    @DeleteMapping(value = "/{room_number}")
    public ResponseEntity<?> deleteRoom(@PathVariable("room_number") int roomNumber) {
        Room deletedRoom = roomService.deleteRoom(roomNumber);
        return new ResponseEntity<>(deletedRoom, HttpStatus.OK);
    }
}
