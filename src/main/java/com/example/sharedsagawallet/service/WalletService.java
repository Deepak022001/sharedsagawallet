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
    public WalletEntity getWalletByUserId(Long userId){
        return walletRepository.findByUserId(userId).get(0);
    }
    
    public List<WalletEntity> getWalletById(Long id){
        return walletRepository
        .findByUserId(id);
    }

    @Transactional
    public void debitWallet(Long userId,BigDecimal amount){
        log.info("Debitting {} from wallet {}",amount,userId);
        WalletEntity walletEntity=getWalletByUserId(userId);
        BigDecimal newBalance=walletEntity.getBalance().subtract(amount);
        walletRepository.updateBalanceByUserId(userId, newBalance);
        log.info("Debit successful for wallet {}",  userId);
    }
    @Transactional
    public void credit(Long userId,BigDecimal amount){
        log.info("Crediting {} to wallet{}",amount ,userId);
        WalletEntity walletEntity=getWalletByUserId(userId);
        walletEntity.setBalance(walletEntity.getBalance().add(amount));
        walletRepository.updateBalanceByUserId(userId, walletEntity.getBalance());
        log.info("Credit successful for wallet {}", userId);
    }
    public BigDecimal getWalletBalance(Long walletId){
        return getWalletByUserId(walletId).getBalance();
    }
}