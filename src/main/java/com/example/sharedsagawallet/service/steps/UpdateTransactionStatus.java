package com.example.sharedsagawallet.service.steps;

import org.springframework.stereotype.Service;

import com.example.sharedsagawallet.entities.Transaction;
import com.example.sharedsagawallet.entities.enums.TransactionStatus;
import com.example.sharedsagawallet.repository.TransactionRepository;
import com.example.sharedsagawallet.service.saga.SagaContext;
import com.example.sharedsagawallet.service.saga.SagaStep;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
@Service
@RequiredArgsConstructor
@Slf4j
public class UpdateTransactionStatus implements SagaStep{
    private final TransactionRepository transactionRepository;
    @Override
    public boolean execute(SagaContext context) {
        Long transactionId=context.getLong("transactionId");
        log.info("Updating transaction status for transaction {} ",transactionId);

        Transaction transaction=transactionRepository.findById(transactionId)
        .orElseThrow(()->new RuntimeException("Transaction Not found"));

        context.put("originalTransactionStatus", transaction.getStatus());

        transaction.setStatus(TransactionStatus.SUCCESS);
        transactionRepository.save(transaction);

        log.info("Transaction status updated for transaction {}",transactionId);

        context.put("originalTransactionAfterUpdate", transaction.getStatus());

        log.info("Update transaction status step executed successfully" );

        return true;
    }

    @Override
    public boolean compensate(SagaContext context) {

        Long transactionId=context.getLong("transactionId");
        log.info("Updating transaction status for transaction {} ",transactionId);

        TransactionStatus origTransactionStatus=TransactionStatus
        .valueOf(context.getString("originalTransactionStatus"));

        log.info("Compensating transaction status for transaction{}", transactionId);

        Transaction transaction=transactionRepository.findById(transactionId)
        .orElseThrow(()->new RuntimeException("Transaction Not found"));

        transaction.setStatus(origTransactionStatus);
        transactionRepository.save(transaction);

        log.info("Transaction status compensated for transaction{}", transactionId);
        return true;

    }

    @Override
    public String getStepName() {
        return "UpdateTransactionStatus";
    }
}
    
