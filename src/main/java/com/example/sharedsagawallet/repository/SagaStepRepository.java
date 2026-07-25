package com.example.sharedsagawallet.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.sharedsagawallet.entities.SagaStepEntity;
import com.example.sharedsagawallet.entities.enums.StepStatusEnum;
@Repository
public interface SagaStepRepository extends JpaRepository<SagaStepEntity,Long>{
    List<SagaStepEntity> findBySagaInstanceId(Long sagaInstanceId);

    List<SagaStepEntity>findBySagaInstanceIdAndStatus(@Param("sagaInstanceId") Long sagaInstanceId,StepStatusEnum sagaStatusEnum);

    @Query("SELECT s FROM SagaStep s WHERE s.sagaInstanceId = :sagaInstanceId AND s.status='COMPLETED'")
    List<SagaStepEntity>findCompletedStepsBySagaInstanceId(@Param("sagaInstanceId")Long sagaInstanceId);

    @Query("SELECT s FROM SagaStep s WHERE s.sagaInstanceId = :sagaInstanceId AND s.status IN('COMPLETED','COMPENSATED')")
    List<SagaStepEntity>findCompletedOrCompensatedStepsBySagaInstanceId(@Param("sagaInstanceId")Long sagaInstanceId);
}