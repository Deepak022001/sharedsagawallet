package com.example.sharedsagawallet.repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.sharedsagawallet.entities.WalletEntity;

import jakarta.persistence.LockModeType;

@Repository
public interface WalletRepository extends JpaRepository<WalletEntity, Long> {
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT w FROM WalletEntity w WHERE w.userId = :id")
    Optional<WalletEntity> findByIdWithLock(@Param("id") Long id);

    @Modifying
    @Query("UPDATE WalletEntity w SET w.balance = :balance WHERE w.userId = :userId")
    int updateBalanceByUserId(@Param("userId") Long userId, @Param("balance") BigDecimal balance);
}
