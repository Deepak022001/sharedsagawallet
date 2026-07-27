package com.example.sharedsagawallet.entities;

import java.math.BigDecimal;

import org.hibernate.annotations.Collate;

import com.example.sharedsagawallet.entities.enums.TransactionStatusEnum;
import com.example.sharedsagawallet.entities.enums.TransactionTypeEnum;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Builder;
@Entity
@Table(name = "transaction")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TransactionEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "from_wallet_id",nullable = false)
    private Long fromWalletId;

    @Column(name = "to_wallet_id",nullable = false)
    private Long toWalletId;

    @Column(name = "amount",nullable = false)
    private BigDecimal amount;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "status",nullable = false)
    private TransactionStatusEnum status=TransactionStatusEnum.PENDING;

    @Enumerated(EnumType.STRING)
    @Column(name = "transaction_type",nullable = false)
    private TransactionTypeEnum type=TransactionTypeEnum.TRANSFER;

    @Column(name = "saga_instance_id",nullable = false)
    private String description;

    @Column(name = "saga_instance_id",nullable = false)
    private Long sagaInstanceId;
}
