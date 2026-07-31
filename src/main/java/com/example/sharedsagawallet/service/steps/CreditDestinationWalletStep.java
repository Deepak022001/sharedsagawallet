package com.example.sharedsagawallet.service.steps;

import java.math.BigDecimal;

import org.springframework.stereotype.Service;

import com.example.sharedsagawallet.entities.WalletEntity;
import com.example.sharedsagawallet.repository.WalletRepository;
import com.example.sharedsagawallet.service.saga.SagaContext;
import com.example.sharedsagawallet.service.saga.SagaStepInterface;
import com.example.sharedsagawallet.service.steps.SagaStepFactory.SagaStepType;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class CreditDestinationWalletStep implements SagaStepInterface{
    private final WalletRepository walletRepository;

    @Override
    @Transactional
    public boolean execute(SagaContext context) {
        
        // Step 1 .Get the destination wallet id from the context
        Long toWalletId=context.getLong("toWalletId");
        BigDecimal amount=context.getBigDecimal("amount");
        log.info("Crediting destination wallet {} with amount {}", toWalletId, amount);
        
        // Step 2 .Fetch the destination wallet from the database with a lock 
        WalletEntity wallet = walletRepository.findByIdWithLock(toWalletId)
        .orElseThrow(() -> new RuntimeException("Wallet not found"));
        log.info("Wallet fetched with balance {} ",wallet.getBalance());
        context.put("originalToWalletBalance",wallet.getBalance());
        
        // Step 3 .Credit the destination wallet
        walletRepository.updateBalanceByUserId(toWalletId, wallet.getBalance().add(amount));
        log.info("Wallet fetched with balance {} ",wallet.getBalance());
        context.put("updatedToWalletBalance",wallet.getBalance());
        
        // Step 4 .Update the context with the changes 
        return true;
    }

    @Override
    public boolean compensate(SagaContext context) {
         // Step 1 .Get the destination wallet id from the context
        Long toWalletId=context.getLong("toWalletId");
        BigDecimal amount=context.getBigDecimal("amount");

        log.info("Compensating credit of destination wallet {} with amount {}",toWalletId,amount);
        // Step 2 .Fetch the destination wallet from the database with a lock 
        WalletEntity wallet=walletRepository.findByIdWithLock(toWalletId)
        .orElseThrow(()->new RuntimeException("wallet not found"));
        log.info("Wallet fetched with balance {}", wallet.getBalance());


        // Step 3 .Debit the destination wallet
        walletRepository.updateBalanceByUserId(toWalletId, wallet.getBalance().subtract(amount));
        log.info("Wallet saved with balance {}", wallet.getBalance());
        context.put("toWalletBalanceAfterCreditCompensation", wallet.getBalance());

        log.info("Credit compensation wallet step compensation executed successfully");
        // Step 4 .Update the context with the changes 
        return true;
    }

    @Override
    public String getStepName() {
        return SagaStepType.CREDIT_DESTINATION_WALLET_STEP.toString();
    }


}
