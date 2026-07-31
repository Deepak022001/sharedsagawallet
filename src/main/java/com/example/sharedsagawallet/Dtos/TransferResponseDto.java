package com.example.sharedsagawallet.Dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder

public class TransferResponseDto {
    Long sagaInstanceid;
}
