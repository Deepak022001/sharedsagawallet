package com.example.sharedsagawallet.Dtos;
import java.math.BigDecimal;

import groovy.transform.builder.Builder;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DebitwalletRequestDto {
    private BigDecimal amount;
}
