package com.example.sharedsagawallet.controller;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

import org.springframework.http.HttpStatus;
import com.example.sharedsagawallet.entities.userEntity;
import com.example.sharedsagawallet.service.userService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/users")
public class userController {
    private final userService userService;
    @PostMapping()
    public ResponseEntity<userEntity> createUser(@RequestBody userEntity user){
        userEntity newUser= userService.creatUser(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(newUser);
    }

    @GetMapping("/{id}")
    public ResponseEntity<userEntity> getUserById(@PathVariable Long id){
        userEntity foundUser=userService.getUserById(id);
        return ResponseEntity.ok(foundUser);
    }
    
    @GetMapping("/name")
    public ResponseEntity<List<userEntity>> getUserByName(@RequestParam String name){
        List<userEntity>allUsers= userService.getUsersByName(name);
        return ResponseEntity.ok(allUsers);
    }
}


// 37:00