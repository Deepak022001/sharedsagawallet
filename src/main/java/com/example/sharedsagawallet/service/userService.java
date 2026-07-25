 package com.example.sharedsagawallet.service;


import java.util.List;

import org.springframework.stereotype.Service;

import com.example.sharedsagawallet.entities.userEntity;
import com.example.sharedsagawallet.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import lombok.RequiredArgsConstructor;

@Service
@Slf4j
@RequiredArgsConstructor
public class userService {
    private final UserRepository userRepository;

    public userEntity creatUser(userEntity user){
        log.info("Creating a user:{}",user.getEmail());
        userEntity newUser=userRepository.save(user);
        log.info("User created with id {} in database shardwallet {} ",newUser.getId(),(newUser.getId()%2+1));
        return newUser;
    }

    public userEntity getUserById( Long id){
    return userRepository.findById(id)
        .orElseThrow(()->new RuntimeException("user not found"));
    }

    public List<userEntity> getUsersByName(String name){
        return userRepository.findByNameContainingIgnoreCase(name);
    }
    
}
