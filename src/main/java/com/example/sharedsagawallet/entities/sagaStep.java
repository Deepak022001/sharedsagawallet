package com.example.sharedsagawallet.entities;
import com.example.sharedsagawallet.entities.enums.StepStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "saga_step")
public class sagaStep {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "saga_instance_id",nullable = false)
    private Long sagaInstanceId;

    @Column(name = "step_name",nullable = false)
    private String stepName;

    @Column(name = "status",nullable = false)
    private StepStatus status;

    @Column(name = "error_message",nullable = true)
    private String errorMessage;

    @Column(name = "stepData",columnDefinition = "json")
    private String stepData;
}
