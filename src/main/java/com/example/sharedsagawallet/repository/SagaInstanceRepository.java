package com.example.sharedsagawallet.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.sharedsagawallet.entities.SagaInstance;
import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import com.example.sharedsagawallet.entities.user;

@Repository

public interface SagaInstanceRepository extends JpaRepository<SagaInstance,Long>{


}
