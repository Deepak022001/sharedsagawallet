package com.example.sharedsagawallet.Dtos;

import java.math.BigDecimal;

import groovy.transform.builder.Builder;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TransferRequestDto {
    private Long fromWalletId;  //fromUserId
    private Long toWalletId;
    private BigDecimal amount;
    private String description;
}
