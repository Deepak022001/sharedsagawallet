package com.example.sharedsagawallet.repository;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.sharedsagawallet.entities.user;

@Repository
public interface UserRepository extends JpaRepository<user,Long>{
    List<user> findByNameContainingIgnoreCase(String name);        
}
