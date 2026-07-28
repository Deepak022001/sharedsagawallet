package com.example.sharedsagawallet.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.sharedsagawallet.entities.WalletEntity;

import jakarta.persistence.LockModeType;

@Repository
public interface WalletRepository extends JpaRepository<WalletEntity,Long>{
    List<WalletEntity>findByUserId(Long userId);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT w FROM WalletEntity w WHERE w.id = :id")
    Optional<WalletEntity> findByIdWithLock(@Param("id") Long id);
}
