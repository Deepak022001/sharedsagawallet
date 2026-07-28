package com.example.sharedsagawallet.Dtos;
import java.math.BigDecimal;

import groovy.transform.builder.Builder;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class CreditwalletRequestDto {
    private BigDecimal amount;
}
