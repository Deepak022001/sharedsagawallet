package com.example.sharedsagawallet.repository;
import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.web.bind.annotation.RestController;

import com.example.sharedsagawallet.entities.user;

@RestController
public interface UserRepository extends CrudRepository<user,Long>{
    List<user> findByNameContainingIgnoreCase(String name);        
}
