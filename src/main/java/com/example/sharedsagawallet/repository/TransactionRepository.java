package com.example.sharedsagawallet.repository;

import com.example.sharedsagawallet.entities.Transaction;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.example.sharedsagawallet.entities.enums.TransactionStatus;;;
public interface TransactionRepository extends JpaRepository<Transaction,Long>  {
    // All the debit transaction
    List<Transaction>findByFromWalletId(Long fromWalletId);

    // All the credit transaction
    List<Transaction>findByToWalletId(Long toWalletId);

    // All the transaction for a wallet
    // Fetch all transactions related to a wallet, whether it sent
    // or received the money.
    @Query("SELECT t FROM Transaction t WHERE t.fromWalletId=:walletId OR t.fromWalletId=:walletId")
    List<Transaction>findByWalletId(@Param("walletId")Long walletId);

    List<Transaction>findByStatus(TransactionStatus status);
    
    List<Transaction>findBySagaInstanceId(Long sagaInstanceId);
}
