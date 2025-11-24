package com.piattaforme.gestorebeb.controller;

import com.piattaforme.gestorebeb.model.entities.Room;
import com.piattaforme.gestorebeb.model.enums.RoomState;
import com.piattaforme.gestorebeb.model.services.RoomService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/room")
public class RoomController {

    private final RoomService roomService;

    public RoomController(RoomService roomService) {
        this.roomService = roomService;
    }

    @GetMapping(value = "/{room_number}")
    public Room getRoomData(@PathVariable("room_number") int roomNumber) {
        try {
            return roomService.getRoomByNumber(roomNumber);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Camera non trovata");
        }
    }

    @PostMapping(value = "/addRoom")
    public Room addRoom(@RequestBody Room newRoom) {
        return roomService.addRoom(newRoom);
    }

    @PutMapping(value = "/{room_number}/updateState")
    public Room changeState(@PathVariable("room_number") int roomNumber, @RequestBody RoomState newState) {
        return roomService.changeState(roomNumber,newState);
    }
}
