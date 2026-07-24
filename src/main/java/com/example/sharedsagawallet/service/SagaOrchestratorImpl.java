package com.example.sharedsagawallet.service;

import java.util.List;
import org.springframework.stereotype.Service;

import com.example.sharedsagawallet.entities.SagaInstance;
import com.example.sharedsagawallet.entities.enums.SagaStatus;
import com.example.sharedsagawallet.repository.SagaInstanceRepository;
import com.example.sharedsagawallet.service.saga.SagaContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import lombok.RequiredArgsConstructor;
import com.example.sharedsagawallet.service.saga.SagaStep;
@Service
@Slf4j
@RequiredArgsConstructor
public class SagaOrchestratorImpl implements SagaOrchestrator{
    private final ObjectMapper objectMapper;
    private final List<SagaStep>sagaSteps;
    private final SagaInstanceRepository sagaInstanceRepository;
    @Override
    public Long startSaga(SagaContext context) {
        try{
            // convert context(java object) to string
            String contextJson = objectMapper.writeValueAsString(context);
            SagaInstance sagaInstance = SagaInstance.builder()
            .context(contextJson)
            .status(SagaStatus.STARTED)
            .build();
        //    sagaInstanceRepository.save(sagaInstance);
            sagaInstance=sagaInstanceRepository.save(sagaInstance);
            log.info("Started saga with id{}",sagaInstance.getId());
            return sagaInstance.getId();
        }catch(Exception e){
            log.error("Error Starting saga", e);
            throw new RuntimeException("Error starting saga",e);
        }
    }

  private SagaStep getStepByName(String stepName) {
    return sagaSteps.stream()
            .filter(step -> step.getStepName().equals(stepName))
            .findFirst()
            .orElseThrow(() -> new RuntimeException("Step not found"));
}
    @Override
    public boolean executeStep(Long sagaInstanceId, String stepName) {
        SagaInstance sagaInstance=sagaInstanceRepository.findById(sagaInstanceId)
        .orElseThrow(()->new RuntimeException("Saga with sagaInstaneId is not found"));

    }

    @Override
    public boolean compensateStep(Long sagaInstanceId, String stepName) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'compensateStep'");
    }

    @Override
    public SagaInstance getSagaInstance(Long sagaInstaceId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getSagaInstance'");
    }

    @Override
    public void compensateSaga(Long sagaInstanceId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'compensateSaga'");
    }

    @Override
    public void failSaga(Long sagaInstanceId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'failSaga'");
    }

    @Override
    public void completeSaga(Long sagaInstanceId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'completeSaga'");
    }

    
}