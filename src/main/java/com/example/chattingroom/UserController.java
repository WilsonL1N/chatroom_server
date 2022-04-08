package com.example.chattingroom;

// import java.util.*;

import com.alibaba.fastjson.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "http://localhost:8082")
@RequestMapping("/Users")
public class UserController {
    @Autowired
    private UserService userservice;

    public UserController() {
        // System.out.println("UserController() called...");
    }

    @RequestMapping(value="/checkNameAvailable", method = RequestMethod.GET)
    public ResponseEntity<?> checkNameAvailable(@RequestParam("name") String name) {
        boolean isAvailable = userservice.checkNameAvailable(name);
        JSONObject res = new JSONObject();
        res.put("isAvailable", isAvailable);
        return ResponseEntity.status(HttpStatus.OK).body(res);
    }

    @RequestMapping(value="/login", method = RequestMethod.POST)
    public ResponseEntity<?> login(@RequestBody User user) {
        int status = userservice.login(user.getName(), user.getPassword());
        JSONObject res = new JSONObject();
        if (status == -1) {
            res.put("status", "-1");
            res.put("msg", "Account does not exist.");
        } else if (status == -2) {
            res.put("status", "-2");
            res.put("msg", "The password is incorrect.");
        } else {
            res.put("status", "0");
            res.put("uid", Integer.toString(status));
            res.put("msg","Success");
        }
        return ResponseEntity.status(HttpStatus.OK).body(res);
    }

    @RequestMapping(value="/signup", method = RequestMethod.POST)
    public ResponseEntity<?> signup(@RequestBody User user) {
        int id = userservice.signup(user.getName(), user.getPassword());
        JSONObject res = new JSONObject();
        if (id != 0) {
            res.put("status", "0");
            res.put("uid", Integer.toString(id));
            res.put("msg","Success");
        } else {
            res.put("status", "-1");
            res.put("msg", "The user name already exists.");
        }
        return ResponseEntity.status(HttpStatus.OK).body(res);
    }

    // @GetMapping
    // public List<User> list() {
    //     return userservice.list();
    // }

    // @GetMapping("/{uid}")
    // public ResponseEntity<?> getUser(@PathVariable int uid) {
    //     User u = userservice.get(uid);
    //     if (u != null) {
    //         return ResponseEntity.status(HttpStatus.OK).body(u);
    //     } else {
    //         return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User does not exist.");
    //     }
    // }

    // @PostMapping
    // public void create(@RequestBody User user) {
    //     userservice.create(user);
    // }

    // @PutMapping
    // public void update(@RequestBody User user) {
    //     userservice.update(user);
    // }

    // @DeleteMapping("/{uid}")
    // public void delete(@PathVariable int uid) {
    //     userservice.delete(uid);
    // }
}
