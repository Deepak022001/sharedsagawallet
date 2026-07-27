package com.example.sharedsagawallet.service;
import java.math.BigDecimal;
import java.util.List;
import org.springframework.stereotype.Service;
import com.example.sharedsagawallet.entities.TransactionEntity;
import com.example.sharedsagawallet.entities.enums.TransactionStatusEnum;
import com.example.sharedsagawallet.repository.TransactionRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
@Service
@Slf4j
@RequiredArgsConstructor
public class TransactionService {
    private final TransactionRepository transactionRepository;
    @Transactional
    public TransactionEntity createTransaction(Long fromWalletId,Long toWalletId,BigDecimal amount,String description){
        log.info("Creating a transaction from wallet {} to wallet {} with amount {} and description {}",fromWalletId,toWalletId,amount,description);
        TransactionEntity transactionEntity=TransactionEntity
        .builder()
        .fromWalletId(fromWalletId)
        .toWalletId(toWalletId).amount(amount)
        .description(description)
        .build();
        TransactionEntity savedTransaction=transactionRepository.save(transactionEntity);
        log.info("Transaction created with id{}", savedTransaction);
        return savedTransaction;
    }
    public TransactionEntity getTransactionById(Long id){
        return transactionRepository.findById(id).orElseThrow(()->new RuntimeException("Transaction not found"));
    }
    public List<TransactionEntity> getTransactionByWalletId(Long walletId){
        return transactionRepository.findByWalletId(walletId);
    }
    public List<TransactionEntity>getTransactionByFromWalletId(Long fromWalletId){
        return transactionRepository.findByFromWalletId(fromWalletId);
    }
    public List<TransactionEntity> getTransactionByToWalletId(Long toWalletId) {
    return transactionRepository.findByToWalletId(toWalletId);
    }
    public List<TransactionEntity>getTransactionBySagaInstanceId(Long sagaInstanceId){
        return transactionRepository.findBySagaInstanceId(sagaInstanceId);
    }
    public List<TransactionEntity>getTransactionByStatus(TransactionStatusEnum transactionStatus){
        return transactionRepository.findByStatus(transactionStatus);
    }
}
