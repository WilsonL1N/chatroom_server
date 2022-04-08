package com.example.chattingroom;

import java.util.*;

// import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = "http://localhost:8082")
@RequestMapping("/rooms")
public class chatroomController {
    private List<Room> rooms;
    private int roomId=0;

    // @Autowired
    // private MsgWebSocketService socketservice;

    public chatroomController() {
        this.rooms = new ArrayList<Room>();
    }

    @RequestMapping(value="/getRooms", method=RequestMethod.GET)
    public ResponseEntity<?> getRooms() {
        return ResponseEntity.status(HttpStatus.OK).body(this.rooms);
    }

    @RequestMapping(value="/delRoom", method=RequestMethod.GET)
    public ResponseEntity<?> delRooms(@RequestParam("roomId") int roomId) {
        for (Room i : rooms) {
            if (i.getRoomId() == roomId) {
                rooms.remove(i);
                System.out.println(rooms.size());
                return ResponseEntity.status(HttpStatus.OK).body("ok");
            }
        }
        return null;
    }

    @RequestMapping(value="/checkRoomName", method=RequestMethod.GET)
    public ResponseEntity<?> checkRoomName(@RequestParam("name") String name) {
        Map<String, String> res = new HashMap<String, String>();
        for (Room i : rooms) {
            if (i.getName().equals(name)) {
                res.put("isAvailable", Boolean.toString(false));
                return ResponseEntity.status(HttpStatus.OK).body(res);
            }
        }
        res.put("isAvailable", Boolean.toString(true));
        return ResponseEntity.status(HttpStatus.OK).body(res);
    }

    @RequestMapping(value="/addRoom", method=RequestMethod.POST)
    public ResponseEntity<?> addRoom(@RequestBody Room room) {
        Map<String, String> res = new HashMap<String, String>();
        for (Room i : rooms) {
            if (i.getName().equals(room.getName())) {
                res.put("status", "-1");
                res.put("msg","Room name already exists");
                return ResponseEntity.status(HttpStatus.OK).body(res);
            }
        }
        this.roomId ++;
        room.setRoomId(this.roomId);
        if (room.getPassword().length()==0) {
            room.setPrivate(false);
        } else {
            room.setPrivate(true);
        }
        rooms.add(room);
        res.put("status", "0");
        res.put("msg","Success");
        res.put("roomId", Integer.toString(room.getRoomId()));
        return ResponseEntity.status(HttpStatus.OK).body(res);
    }

    @RequestMapping(value="/enterRoom", method=RequestMethod.POST)
    public ResponseEntity<?> enterRoom(@RequestBody Room room) {
        Map<String, String> res = new HashMap<String, String>();
        for (Room i : rooms) {
            if (i.getRoomId() == room.getRoomId()) {
                if (i.getPassword().equals(room.getPassword())) {
                    res.put("status", "0");
                    res.put("msg", "Success");
                } else {
                    res.put("status", "-1");
                    res.put("msg","Incorrect Password");
                }
            }
        }
        return ResponseEntity.status(HttpStatus.OK).body(res);
    }
}
