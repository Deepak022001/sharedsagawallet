package com.example.sharedsagawallet.service;

import com.example.sharedsagawallet.service.saga.SagaContext;
import com.example.sharedsagawallet.entities.SagaInstanceEntity;
public interface SagaOrchestrator {
    // Going to take Context object and it intiates a new sagainstance 
    Long startSaga(SagaContext context);

    boolean executeStep(Long sagaInstanceId,String stepName);

    boolean compensateStep(Long sagaInstanceId,String stepName);

    SagaInstanceEntity getSagaInstance(Long sagaInstaceId);

    void compensateSaga(Long sagaInstanceId);

    void failSaga(Long sagaInstanceId);

    void completeSaga(Long sagaInstanceId);
}