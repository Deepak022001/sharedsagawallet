package com.example.sharedsagawallet.repository;

import com.example.sharedsagawallet.entities.TransactionEntity;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.example.sharedsagawallet.entities.enums.TransactionStatusEnum;;;
public interface TransactionRepository extends JpaRepository<TransactionEntity,Long>  {
    // All the debit transaction
    List<TransactionEntity>findByFromWalletId(Long fromWalletId);

    // All the credit transaction
    List<TransactionEntity>findByToWalletId(Long toWalletId);

    // All the transaction for a wallet
    // Fetch all transactions related to a wallet, whether it sent
    // or received the money.
    @Query("SELECT t FROM TransactionEntity t WHERE t.fromWalletId=:walletId OR t.fromWalletId=:walletId")
    List<TransactionEntity>findByWalletId(@Param("walletId")Long walletId);

    List<TransactionEntity>findByStatus(TransactionStatusEnum status);
    
    List<TransactionEntity>findBySagaInstanceId(Long sagaInstanceId);
}
