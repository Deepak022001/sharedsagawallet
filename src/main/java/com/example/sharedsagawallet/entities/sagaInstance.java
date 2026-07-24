package com.example.sharedsagawallet.entities;

import org.apache.calcite.model.JsonType;

import com.example.sharedsagawallet.entities.enums.SagaStatus;


import com.fasterxml.jackson.annotation.JsonSubTypes.Type;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Builder;
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@Table(name = "saga_instance")
public class SagaInstance {
    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)    
    private Long id;

    @Column(name = "payload",nullable = false)
    private SagaStatus status=SagaStatus.STARTED;

    @Column(name = "currentStep")
    private String currentStep;

    @Type(JsonType.class)
    @Column(name = "context",columnDefinition =  "json")
    private String context;

}
