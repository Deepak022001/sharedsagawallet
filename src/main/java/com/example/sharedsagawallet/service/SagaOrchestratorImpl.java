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
public class SagaOrchestratorImpl implements SagaOrchestrator {
    private final ObjectMapper objectMapper;
    // private final List<SagaStepInterface>sagaSteps;
    private final SagaInstanceRepository sagaInstanceRepository;
    private final SagaStepRepository sagaStepRepository;
    private final SagaStepFactory sagaStepFactory;

    @Override
    // This sagacontext will come from the transactionsagaservice file
    //
    public Long startSaga(SagaContext context) {
        try {
            // convert context(java object) to string
            String contextJson = objectMapper.writeValueAsString(context);
            SagaInstanceEntity sagaInstance = SagaInstanceEntity.builder()
                    .context(contextJson)
                    .status(SagaStatusEnum.STARTED)
                    .currentStep(
                            SagaStepFactory.SagaStepType.DEBIT_SOURCE_WALLET_STEP.toString())
                    // First Point where Saga started when we assigned
                    // context from Sagacontext to sagainstaceentity
                    .build();
            // We saved the Sagainstance(consists In memory Object SagaContext)
            sagaInstance = sagaInstanceRepository.save(sagaInstance);
            for (SagaStepFactory.SagaStepType step : SagaStepFactory.TransferMoneySagaSteps) {
                SagaStepEntity sagaStep = SagaStepEntity.builder()
                        .sagaInstanceId(sagaInstance.getId())
                        .stepName(step.name())
                        .status(StepStatusEnum.PENDING)
                        .build();
                sagaStep = sagaStepRepository.save(sagaStep);
            }
            sagaInstance = sagaInstanceRepository.save(sagaInstance);
            log.info("Started saga with id{}", sagaInstance.getId());
            return sagaInstance.getId();
        } catch (Exception e) {
            log.error("Error Starting saga", e);
            throw new RuntimeException("Error starting saga", e);
        }
    }

    // Another way to get steps object instead of making sagastepfactory
    // private SagaStepInterface getStepByName(String stepName) {
    // return sagaSteps.stream()
    // .filter(step -> step.getStepName().equals(stepName))
    // .findFirst()
    // .orElseThrow(() -> new RuntimeException("Step not found"));
    // }
    @Override
    @Transactional
    public boolean executeStep(Long sagaInstanceId, String stepName) {
        // 1.We fetched the database record according to sagaInstanceId(parameter given
        // in the function)
        SagaInstanceEntity sagaInstance = sagaInstanceRepository
                .findById(sagaInstanceId)
                .orElseThrow(() -> new RuntimeException("Saga Not Found"));
        // SagaStepInterface sagaStepInterface=getStepByName(stepName);
        // 2.we fetched the correct Object acc to stepName and on this object we will be
        // working on
        SagaStepInterface sagaStepInterface = sagaStepFactory.getSagaStep(stepName);
        if (sagaStepInterface == null) {
            log.error("Saga step not found for step naame{}", sagaStepFactory.getSagaStep(stepName));
            throw new RuntimeException("Saga step found");
        }
        // WE have to fetch the database record with correct id and status = pending
        // only because only pending
        // step is useful to us
        SagaStepEntity sagastepentity = sagaStepRepository
                .findBySagaInstanceIdAndStepNameAndStatus(sagaInstanceId, stepName, StepStatusEnum.PENDING)
                .orElseThrow(() -> new RuntimeException("Saga Not found"));
        if (sagastepentity.getId() == null) {
            sagastepentity = sagaStepRepository.save(sagastepentity);
        }
        try {
            SagaContext sagaContext = objectMapper
                    .readValue(sagaInstance.getContext(), SagaContext.class);
            sagastepentity.markAsRunning();
            sagaStepRepository.save(sagastepentity);
            boolean SUCCESS = sagaStepInterface.execute(sagaContext);
            if (SUCCESS == true) {
                sagastepentity.markAsCompleted();
                sagaStepRepository.save(sagastepentity);

                sagaInstance.setCurrentStep(stepName);
                sagaInstance.markAsRunning();
                sagaInstanceRepository.save(sagaInstance);
                return true;
            } else {
                sagastepentity.markAsFailed();
                sagaStepRepository.save(sagastepentity);
                log.error("Step {} failed", stepName);
                return false;
            }
        } catch (Exception e) {
            sagastepentity.markAsFailed();
            sagaStepRepository.save(sagastepentity);
            log.error("Error reading saga context", e);
            return false;
        }
    }

    @Override
    public boolean compensateStep(Long sagaInstanceId, String stepName) {
        SagaInstanceEntity sagaInstance = sagaInstanceRepository
                .findById(sagaInstanceId)
                .orElseThrow(() -> new RuntimeException("Saga Not Found"));
        SagaStepInterface sagaStepInterface = sagaStepFactory.getSagaStep(stepName);
        if (sagaStepInterface == null) {
            log.error("Saga step not found for step naame{}", sagaStepFactory.getSagaStep(stepName));
            throw new RuntimeException("Saga step found");
        }

        SagaStepEntity sagastepentity = sagaStepRepository
                .findBySagaInstanceIdAndStepNameAndStatus(sagaInstanceId, stepName, StepStatusEnum.COMPLETED)
                .orElse(
                        null
                // No such steps found in the db
                );
        if (sagastepentity.getId() == null) {
            log.info("Steps {} not found in the db for saga instance {}, so it is already compensated or not executed",
                    stepName, sagaInstanceId);
            return true;
        }
        try {
            SagaContext sagaContext = objectMapper
                    .readValue(sagaInstance.getContext(), SagaContext.class);
            sagastepentity.markAsCompensating();
            sagaStepRepository.save(sagastepentity);
            boolean SUCCESS = sagaStepInterface.compensate(sagaContext);
            if (SUCCESS == true) {
                sagastepentity.markAsCompensated();
                sagaStepRepository.save(sagastepentity);

                log.info("Step {} compensated successfully", stepName);
                return true;
            } else {
                sagastepentity.markAsFailed();
                sagaStepRepository.save(sagastepentity);
                log.error("Step {} failed", stepName);
                return false;
            }
        } catch (Exception e) {
            sagastepentity.markAsFailed();
            sagaStepRepository.save(sagastepentity);
            log.error("Error reading saga context", e);
            return false;
        }
    }

    @Override
    public SagaInstanceEntity getSagaInstance(Long sagaInstaceId) {
        return sagaInstanceRepository.findById(sagaInstaceId)
                .orElseThrow(() -> new RuntimeException("Saga with instance id not found"));
    }

    @Override
    public void failSaga(Long sagaInstanceId) {
        SagaInstanceEntity sagaInstanceEntity = sagaInstanceRepository.findById(sagaInstanceId)
                .orElseThrow(() -> new RuntimeException("Saga with instance id not found"));
        sagaInstanceEntity.markAsFailed();
        sagaInstanceRepository.save(sagaInstanceEntity);
        compensateSaga(sagaInstanceId);
        log.info("Saga {} failed", sagaInstanceId);
    }

    @Override
    public void completeSaga(Long sagaInstanceId) {
        SagaInstanceEntity sagaInstanceEntity = sagaInstanceRepository.findById(sagaInstanceId)
                .orElseThrow(() -> new RuntimeException("Saga with instance id not found"));
        sagaInstanceEntity.markAsCompleted();
        sagaInstanceRepository.save(sagaInstanceEntity);
    }

    @Override
    public void compensateSaga(Long sagaInstanceId) {
        SagaInstanceEntity sagaInstanceEntity = sagaInstanceRepository.findById(sagaInstanceId)
                .orElseThrow(() -> new RuntimeException("sagainstance id not found"));

        sagaInstanceEntity.markAsCompensating();
        sagaInstanceRepository.save(sagaInstanceEntity);

        boolean allCompensated = true;
        List<SagaStepEntity> completedSteps = sagaStepRepository
                .findCompletedOrCompensatedStepsBySagaInstanceId(sagaInstanceId);
        for (SagaStepEntity completedStep : completedSteps) {
            boolean completed = this.compensateStep(sagaInstanceId, completedStep.getStepName());
            if (!completed) {
                allCompensated = false;
            }
        }
        if (allCompensated) {
            sagaInstanceEntity.markAsCompensated();
            sagaInstanceRepository.save(sagaInstanceEntity);
            log.info("Saga {} compensated successfully", sagaInstanceId);
        } else {
            log.error("Saga  {} compnesation failed", sagaInstanceId);
        }
    }

}

// 23:08
