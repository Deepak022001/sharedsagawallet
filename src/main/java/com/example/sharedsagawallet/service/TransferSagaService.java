package com.example.sharedsagawallet.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.example.sharedsagawallet.entities.TransactionEntity;
import com.example.sharedsagawallet.service.saga.SagaContext;
import com.example.sharedsagawallet.service.steps.SagaStepFactory;
import com.example.sharedsagawallet.service.steps.SagaStepFactory.SagaStepType;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.*;
import lombok.Builder;
import lombok.RequiredArgsConstructor;

@Slf4j
@RequiredArgsConstructor
@Service
public class TransferSagaService {
    private final TransactionService transactionService;
    private final SagaOrchestratorImpl sagaOrchestratorImpl;
   
    // We are going to first 
    // 1.Start the transaction
    // 2.we start the saga

    @Transactional
    public Long initiateTransfer(
        Long fromWalletId,
        Long toWalletId,
        BigDecimal amount,
        String description
    ){
        log.info("Initiating transfer from wallet {} to wallet {} with amount {} and description{}",fromWalletId,toWalletId,amount,description);
        // Create transaction
        TransactionEntity transactionEntity=transactionService
        .createTransaction(fromWalletId, toWalletId, amount, null);
        SagaContext sagaContext=SagaContext.builder()
        .data(
            Map.ofEntries(
                Map.entry("transactionId",transactionEntity.getId()),
            Map.entry("fromWalletId", fromWalletId),
            Map.entry("toWalletId", toWalletId),
            Map.entry("amount", amount),
            Map.entry("description", description)
            )
        ).build();
        // start saga
        Long sagaInstanceId=sagaOrchestratorImpl.startSaga(sagaContext);
        log.info("Sagainstance created with id{}", sagaInstanceId);
        transactionService.updateTransactionWithSagaInstaceId(transactionEntity.getId(), sagaInstanceId);
        exectueTransferSaga(sagaInstanceId);
        return sagaInstanceId;
    }
    // if you want to intiate the transfer you are going to create a transaction create a context create a sagcontext in the db updatethetransactionwithsagainstaceid  executetransfersaga
    public void exectueTransferSaga(Long sagaInstanceId){
        log.info("Executing transfer saga with id{}",sagaInstanceId);    
        try{
            for(SagaStepType step: SagaStepFactory.TransferMoneySagaSteps){
                boolean success=sagaOrchestratorImpl.executeStep(sagaInstanceId, step.toString());
                if(!success){
                    log.error("Failed to execute the step {]", step.toString());
                    sagaOrchestratorImpl.failSaga(sagaInstanceId);
                }
                sagaOrchestratorImpl.completeSaga(sagaInstanceId);
                log.info("Transfer saga completed with id{}", sagaInstanceId);
            }
        }catch(Exception e ){
            log.error("Failed to execute transfer saga with id {}", e);
            sagaOrchestratorImpl.failSaga(sagaInstanceId);
        }
    }

}
