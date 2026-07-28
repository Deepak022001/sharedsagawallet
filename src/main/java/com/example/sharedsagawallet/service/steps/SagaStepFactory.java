package com.example.sharedsagawallet.service.steps;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.example.sharedsagawallet.entities.SagaStepEntity;
import com.example.sharedsagawallet.service.saga.SagaStepInterface;

import lombok.RequiredArgsConstructor;
@Component
@RequiredArgsConstructor
public class SagaStepFactory {
    private final Map<String,SagaStepInterface>sagaStepName;

     public static final List<SagaStepFactory.SagaStepType>TransferMoneySagaSteps=List.of(
        SagaStepFactory.SagaStepType.DEBIT_SOURCE_WALLET_STEP,
        SagaStepFactory.SagaStepType.CREDIT_DESTINATION_WALLET_STEP,
        SagaStepFactory.SagaStepType.UPDATE_TRANSACTION_STATUS_STEP
    );
    
    public static enum SagaStepType{
        DEBIT_SOURCE_WALLET_STEP,
        CREDIT_DESTINATION_WALLET_STEP,
        UPDATE_TRANSACTION_STATUS_STEP
    }
    public SagaStepInterface getSagaStep(String stepName){
        return sagaStepName.get(stepName);
    }
}
