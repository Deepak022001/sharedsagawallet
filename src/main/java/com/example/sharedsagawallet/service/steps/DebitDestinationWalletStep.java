package com.example.sharedsagawallet.service.steps;

import java.math.BigDecimal;

import org.springframework.stereotype.Service;

import com.example.sharedsagawallet.entities.Wallet;
import com.example.sharedsagawallet.repository.WalletRepository;
import com.example.sharedsagawallet.service.saga.SagaContext;
import com.example.sharedsagawallet.service.saga.SagaStep;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
@Service
@RequiredArgsConstructor
@Slf4j
public class DebitDestinationWalletStep implements SagaStep{
    private final WalletRepository walletRepository;
    @Override
    @Transactional
    public boolean execute(SagaContext context) {
        Long fromWalletId=context.getLong("fromWalletId");
        BigDecimal amount=context.getBigDecimal("amount");
        log.info("Debitting from source wallet {} with amount {}",fromWalletId,amount);
        Wallet wallet=walletRepository.findByIdWithLock(fromWalletId).orElseThrow(()->new RuntimeException("wallet not found"));
        log.info("Source wallet balance {} after debit",wallet.getBalance());
        context.put( "originalSourceWalletBalance",wallet.getBalance());


        wallet.debit(amount);
        walletRepository.save(wallet);

         log.info("Wallet saved with balance{}",wallet.getBalance());
        context.put( "sourceWalletAfterDebit",wallet.getBalance());

        return true;
    }

    @Override
    public boolean compensate(SagaContext context) {
        Long fromWalletId=context.getLong("fromWalletId");
        BigDecimal amount=context.getBigDecimal("amount");
        log.info("compensating from source wallet {} with amount {}",fromWalletId,amount);
        Wallet wallet=walletRepository.findByIdWithLock(fromWalletId)
        .orElseThrow(()->new RuntimeException("wallet not found"));

        log.info("Wallet fetched with balance {} ",wallet.getBalance());
        context.put("sourceWalletBeforeCompensation", wallet.getBalance());
        wallet.credit(amount);
        walletRepository.save(wallet);

         log.info("Wallet saved with balance{}",wallet.getBalance());
         context.put("sourceWalletAfterCompensation", wallet.getBalance());

        return true;
    }

    @Override
    public String getStepName() {
        return "null";
    }
    
}
