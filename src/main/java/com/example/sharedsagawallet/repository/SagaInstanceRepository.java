package com.example.sharedsagawallet.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.sharedsagawallet.entities.SagaInstanceEntity;
import org.springframework.stereotype.Repository;

@Repository

public interface SagaInstanceRepository extends JpaRepository<SagaInstanceEntity,Long>{


}
