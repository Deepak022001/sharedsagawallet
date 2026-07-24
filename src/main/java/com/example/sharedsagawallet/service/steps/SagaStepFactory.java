package com.example.sharedsagawallet.service.steps;

import java.util.Map;

import org.springframework.stereotype.Component;

import com.example.sharedsagawallet.entities.SagaStep;

import lombok.RequiredArgsConstructor;
@Component
@RequiredArgsConstructor
public class SagaStepFactory {
    private final Map<String,SagaStep>sagaStepName;

    public static enum SagaStepType{
        DEBIT_SOURCE_WALLET_STEP,
        CREDIT_DESTINATION_WALLET_STEP,
        UPDATE_TRANSACTION_STATUS_STEP
    }
    public SagaStep getSagaStep(String stepName){
        return sagaStepName.get(stepName);
    }
}
