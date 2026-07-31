package com.example.sharedsagawallet.controller;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.sharedsagawallet.Dtos.CreateWalletRequestDto;
import com.example.sharedsagawallet.Dtos.CreditwalletRequestDto;
import com.example.sharedsagawallet.Dtos.DebitwalletRequestDto;
import com.example.sharedsagawallet.entities.WalletEntity;
import com.example.sharedsagawallet.service.WalletService;

import lombok.extern.slf4j.*;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/wallets")
@Slf4j
public class WalletController {
    private final WalletService walletService;

    @PostMapping
    public ResponseEntity<WalletEntity> createWallet(@RequestBody CreateWalletRequestDto request){
        try {
            WalletEntity walletEntity=walletService.createWallet(request.getUserId());
            return ResponseEntity.status(HttpStatus.CREATED).body(walletEntity);
        } catch (Exception e) {
            log.error("Error creating wallet", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<List<WalletEntity>>getWalletById(@PathVariable Long id){
        List<WalletEntity> walletEntity=walletService.getWalletById(id);
        return ResponseEntity.ok(walletEntity);
    }

    @GetMapping("/{id}/balance")
    public ResponseEntity<BigDecimal>getWalletBalance(@PathVariable Long id){
        BigDecimal balance=walletService.getWalletBalance(id);
        return ResponseEntity.ok(balance);
    }

    @GetMapping("/user/{userId}")
    public WalletEntity getWalletByUserId(@PathVariable Long userId) {
        WalletEntity wallets = walletService.getWalletByUserId(userId);
        return wallets;
    }
    
    @PostMapping("/{userId}/debit")
        public ResponseEntity<WalletEntity> debitWallet(
        @PathVariable Long userId,
        @RequestBody DebitwalletRequestDto request) {
    walletService.debitWallet(userId, request.getAmount());
    WalletEntity walletEntity=walletService.getWalletByUserId(userId);
    return ResponseEntity.ok(walletEntity);
}
       
    @PostMapping("/{userId}/credit")
        public ResponseEntity<WalletEntity> debitWallet(
        @PathVariable Long userId,
        @RequestBody CreditwalletRequestDto request) {
    walletService.credit(userId, request.getAmount());
    WalletEntity walletEntity=walletService.getWalletByUserId(userId);
    return ResponseEntity.ok(walletEntity);
    }

}