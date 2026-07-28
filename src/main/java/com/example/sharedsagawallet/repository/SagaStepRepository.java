package com.example.sharedsagawallet.repository;
import java.util.List;
import java.util.Optional;
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

    Optional<SagaStepEntity>findBySagaInstanceIdAndStepNameAndStatus(Long sagaInstanceId,String stepName,StepStatusEnum status);

    @Query("""
        SELECT s
        FROM SagaStepEntity s
        WHERE s.sagaInstanceId = :sagaInstanceId
        AND s.status = :status
        """)
            List<SagaStepEntity> findBySagaInstanceIdAndStatus1(
            Long sagaInstanceId,
            StepStatusEnum status
);

    @Query("""
        SELECT s
        FROM SagaStepEntity s
        WHERE s.sagaInstanceId = :sagaInstanceId
        AND s.status IN (
            com.example.sharedsagawallet.entities.enums.StepStatusEnum.COMPLETED,
            com.example.sharedsagawallet.entities.enums.StepStatusEnum.COMPENSATED
        )
    """)
List<SagaStepEntity> findCompletedOrCompensatedStepsBySagaInstanceId(
    @Param("sagaInstanceId") Long sagaInstanceId
);
}