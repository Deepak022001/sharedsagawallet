package com.example.sharedsagawallet.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.sharedsagawallet.entities.SagaInstanceEntity;
import com.example.sharedsagawallet.entities.SagaStepEntity;
import com.example.sharedsagawallet.entities.enums.SagaStatusEnum;
import com.example.sharedsagawallet.entities.enums.StepStatusEnum;
import com.example.sharedsagawallet.repository.SagaInstanceRepository;
import com.example.sharedsagawallet.repository.SagaStepRepository;
import com.example.sharedsagawallet.service.saga.SagaContext;
import com.example.sharedsagawallet.service.saga.SagaStepInterface;
import com.example.sharedsagawallet.service.steps.SagaStepFactory;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import lombok.RequiredArgsConstructor;
@Service
@Slf4j
@RequiredArgsConstructor
public class SagaOrchestratorImpl implements SagaOrchestrator{
    private final ObjectMapper objectMapper;
    // private final List<SagaStepInterface>sagaSteps;
    private final SagaInstanceRepository sagaInstanceRepository;
    private final SagaStepRepository sagaStepRepository;
    private final SagaStepFactory sagaStepFactory;
    @Override
    public Long startSaga(SagaContext context) {
        try{
            // convert context(java object) to string
            String contextJson = objectMapper.writeValueAsString(context);
            SagaInstanceEntity sagaInstance = SagaInstanceEntity.builder()
            .context(contextJson)
            .status(SagaStatusEnum.STARTED)
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
//  Another way to get steps object instead of making sagastepfactory
//   private SagaStepInterface getStepByName(String stepName) {
//     return sagaSteps.stream()
//             .filter(step -> step.getStepName().equals(stepName))
//             .findFirst()
//             .orElseThrow(() -> new RuntimeException("Step not found"));
// }
    @Override
    @Transactional
    public boolean executeStep(Long sagaInstanceId, String stepName) {

        // 1.We fetched the database record according to sagaInstanceId(parameter given in the function)
        SagaInstanceEntity sagaInstance=sagaInstanceRepository.findById(sagaInstanceId)
        .orElseThrow(()->new RuntimeException("Saga Not Found"));
        // SagaStepInterface sagaStepInterface=getStepByName(stepName);

        // 2.we fetched the correct Object acc to stepName
        SagaStepInterface sagaStepInterface=sagaStepFactory.getSagaStep(stepName);
        if(sagaStepInterface==null){
            log.error("Saga step not found for step naame{}", sagaStepFactory.getSagaStep(stepName));
            throw new RuntimeException("Saga step found");
        }

        SagaStepEntity sagastepentity=sagaStepRepository
        .findBySagaInstanceIdAndStatus(sagaInstanceId,StepStatusEnum.PENDING)
        .stream()
        .filter(step->step.getStepName().equals(stepName))
        .findFirst()
        .orElse(
            SagaStepEntity.builder().sagaInstanceId(sagaInstanceId).stepName(stepName).status(StepStatusEnum.PENDING).build()
        );
        if(sagastepentity.getId()==null){
            sagastepentity=sagaStepRepository.save(sagastepentity);
        }

        try{
            SagaContext sagaContext=objectMapper
            .readValue(sagaInstance.getContext(), SagaContext.class);
            sagastepentity.setStatus(StepStatusEnum.RUNNING);
            sagaStepRepository.save(sagastepentity);

            boolean SUCCESS=sagaStepInterface.execute(sagaContext);
            if(SUCCESS==true){
                sagastepentity.setStatus(StepStatusEnum.COMPLETED);
                sagaStepRepository.save(sagastepentity);

                sagaInstance.setCurrentStep(stepName);
                sagaInstance.setStatus(SagaStatusEnum.RUNNING);
                sagaInstanceRepository.save(sagaInstance);
                return true;
            }else{
                sagastepentity.setStatus(StepStatusEnum.FAILED);
                sagaStepRepository.save(sagastepentity);
                log.error("Step {} failed", stepName);
                return false;
            }
        }catch(Exception e){
            sagastepentity.setStatus(StepStatusEnum.FAILED);
            sagaStepRepository.save(sagastepentity);
            log.error("Error reading saga context", e);
            return false;
        }
    }

    @Override
    public boolean compensateStep(Long sagaInstanceId, String stepName) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'compensateStep'");
    }

    @Override
    public SagaInstanceEntity getSagaInstance(Long sagaInstaceId) {
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