package com.example.sharedsagawallet.service.saga;

public interface SagaStep {

    boolean execute(SagaContext context);

    boolean compensate(SagaContext context)

    String getStepName();
    

}
