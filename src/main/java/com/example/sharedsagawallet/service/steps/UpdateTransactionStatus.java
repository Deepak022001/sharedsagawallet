package com.example.sharedsagawallet.service.steps;

import org.springframework.stereotype.Service;

import com.example.sharedsagawallet.entities.TransactionEntity;
import com.example.sharedsagawallet.entities.enums.TransactionStatusEnum;
import com.example.sharedsagawallet.repository.TransactionRepository;
import com.example.sharedsagawallet.service.saga.SagaContext;
import com.example.sharedsagawallet.service.saga.SagaStepInterface;
import com.example.sharedsagawallet.service.steps.SagaStepFactory.SagaStepType;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
@Service
@RequiredArgsConstructor
@Slf4j
public class UpdateTransactionStatus implements SagaStepInterface{
    private final TransactionRepository transactionRepository;
    @Override
    public boolean execute(SagaContext context) {
        Long transactionId=context.getLong("transactionId");
        log.info("Updating transaction status for transaction {} ",transactionId);

        TransactionEntity transaction=transactionRepository.findById(transactionId)
        .orElseThrow(()->new RuntimeException("Transaction Not found"));

        context.put("originalTransactionStatus", transaction.getStatus());

        transaction.setStatus(TransactionStatusEnum.SUCCESS);
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

        TransactionStatusEnum origTransactionStatus=TransactionStatusEnum
        .valueOf(context.getString("originalTransactionStatus"));

        log.info("Compensating transaction status for transaction{}", transactionId);

        TransactionEntity transaction=transactionRepository.findById(transactionId)
        .orElseThrow(()->new RuntimeException("Transaction Not found"));

        transaction.setStatus(origTransactionStatus);
        transactionRepository.save(transaction);

        log.info("Transaction status compensated for transaction{}", transactionId);
        return true;

    }

    @Override
    public String getStepName() {
        return SagaStepType.UPDATE_TRANSACTION_STATUS_STEP.toString();
    }
}
    
