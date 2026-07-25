package com.example.sharedsagawallet.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.sharedsagawallet.entities.SagaInstanceEntity;
import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import com.example.sharedsagawallet.entities.userEntity;

@Repository

public interface SagaInstanceRepository extends JpaRepository<SagaInstanceEntity,Long>{


}
