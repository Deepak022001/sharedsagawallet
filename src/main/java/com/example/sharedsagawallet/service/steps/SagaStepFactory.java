package com.example.sharedsagawallet.service.steps;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.example.sharedsagawallet.service.saga.SagaStepInterface;

@Component
public class SagaStepFactory {

    private final Map<String, SagaStepInterface> sagaSteps;

    public SagaStepFactory(List<SagaStepInterface> steps) {

        this.sagaSteps = steps.stream()
                .collect(Collectors.toMap(
                        SagaStepInterface::getStepName,
                        Function.identity()
                ));
    }

    public SagaStepInterface getSagaStep(String stepName) {
        return sagaSteps.get(stepName);
    }

    public static final List<SagaStepType> TransferMoneySagaSteps =
            List.of(
                    SagaStepType.DEBIT_SOURCE_WALLET_STEP,
                    SagaStepType.CREDIT_DESTINATION_WALLET_STEP,
                    SagaStepType.UPDATE_TRANSACTION_STATUS_STEP
            );

    public enum SagaStepType {
        DEBIT_SOURCE_WALLET_STEP,
        CREDIT_DESTINATION_WALLET_STEP,
        UPDATE_TRANSACTION_STATUS_STEP
    }
}