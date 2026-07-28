package com.example.sharedsagawallet.service;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.sharedsagawallet.entities.WalletEntity;
import com.example.sharedsagawallet.repository.WalletRepository;

import jakarta.transaction.Transactional;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
@Service
@Slf4j
@Builder
@RequiredArgsConstructor
public class WalletService {
    private final WalletRepository walletRepository;    
    public WalletEntity createWallet(Long userId){
        WalletEntity walletEntity=WalletEntity
        .builder()
        .userId(userId)
        .isActive(true)
        .balance(BigDecimal.ZERO)
        .build();  
        walletEntity=walletRepository.save(walletEntity);
        return walletEntity;
    }
    public WalletEntity getWalletById(Long walletId){
        return walletRepository
        .findById(walletId)
        .orElseThrow(()->new RuntimeException("Wallet Not found"));
    }
    public List<WalletEntity> getWalletByUserId(Long userId){
        return walletRepository.findByUserId(userId);
    }
    @Transactional
    public void debitWallet(Long walletId,BigDecimal amount){
        log.info("Debitting {} from wallet {}",amount,walletId);
        WalletEntity walletEntity=getWalletById(walletId);
        walletEntity.debit(amount);
        walletRepository.save(walletEntity);
        log.info("Debit successful for wallet {}",  walletId);
    }
    @Transactional
    public void credit(Long walletId,BigDecimal amount){
        log.info("Crediting {} to wallet{}",amount ,walletId);
        WalletEntity walletEntity=getWalletById(walletId);
        walletEntity.credit(amount);
        walletRepository.save(walletEntity);
        log.info("Credit successful for wallet {}", walletId);
    }
    public BigDecimal getWalletBalance(Long walletId){
        return getWalletById(walletId).getBalance();
    }
}
