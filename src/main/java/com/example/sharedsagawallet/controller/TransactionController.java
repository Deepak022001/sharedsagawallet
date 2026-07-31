package com.example.sharedsagawallet.controller;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

import org.springframework.http.HttpStatus;

import com.example.sharedsagawallet.Dtos.TransferRequestDto;
import com.example.sharedsagawallet.Dtos.TransferResponseDto;
import com.example.sharedsagawallet.entities.TransactionEntity;
import com.example.sharedsagawallet.entities.userEntity;
import com.example.sharedsagawallet.service.TransactionService;
import com.example.sharedsagawallet.service.userService;

import groovy.util.logging.Slf4j;

import com.example.sharedsagawallet.service.TransferSagaService;
import lombok.RequiredArgsConstructor;
@lombok.extern.slf4j.Slf4j

@Controller
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/transactions")
public class TransactionController {
    // private final TransactionService transactionService;
    private final TransferSagaService transferSagaService;

    @PostMapping
    public ResponseEntity<TransferResponseDto>createTransaction(@RequestBody TransferRequestDto transferRequestDto){
        try{
        Long sagaInstanceId=transferSagaService.initiateTransfer(
        transferRequestDto.getFromWalletId(),
        transferRequestDto.getToWalletId(),
        transferRequestDto.getAmount(),
        transferRequestDto.getDescription());
        return ResponseEntity.status(HttpStatus.CREATED).body(
            TransferResponseDto.builder().sagaInstanceid(sagaInstanceId).build());
    }catch(Exception e){
        log.error("Error creating transaction", e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }
}
}
