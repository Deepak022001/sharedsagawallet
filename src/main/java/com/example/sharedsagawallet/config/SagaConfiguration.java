package com.example.sharedsagawallet.config;

import java.util.HashMap;
import java.util.Map;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.example.sharedsagawallet.service.steps.CreditDestinationWalletStep;
import com.example.sharedsagawallet.service.steps.DebitSourceWalletStep;
import com.example.sharedsagawallet.service.steps.UpdateTransactionStatus;
import com.example.sharedsagawallet.service.steps.SagaStepFactory.SagaStepType;
import com.example.sharedsagawallet.service.saga.SagaStep;
@Configuration
public class SagaConfiguration {
    @Bean
    public Map<String,SagaStep> SagaStepMap(
        DebitSourceWalletStep debitSourceWalletStep,
        CreditDestinationWalletStep creditDestinationWalletStep,
        UpdateTransactionStatus updateTransactionStatus
    ){
    Map<String,SagaStep>SagaStepMap=new HashMap<>();
    SagaStepMap.put(SagaStepType.DEBIT_SOURCE_WALLET_STEP.toString(), debitSourceWalletStep);
    SagaStepMap.put(SagaStepType.CREDIT_DESTINATION_WALLET_STEP.toString(), creditDestinationWalletStep)
    SagaStepMap.put(SagaStepType.UPDATE_TRANSACTION_STATUS_STEP.toString(), updateTransactionStatus)    ;
    }
}
