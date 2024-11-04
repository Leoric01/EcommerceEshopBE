package com.leoric.ecommerceshopbe.controllers;

import com.leoric.ecommerceshopbe.models.Seller;
import com.leoric.ecommerceshopbe.models.Transaction;
import com.leoric.ecommerceshopbe.services.interfaces.TransactionService;
import com.leoric.ecommerceshopbe.utils.GlobalUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/transaction")
public class TransactionController {
    private final TransactionService transactionService;
    private final GlobalUtil globalUtil;

    @GetMapping("/seller")
    public ResponseEntity<List<Transaction>> getTransactionsBySeller(Authentication authentication) {
        Seller seller = globalUtil.getPrincipalAsSeller(authentication);
        List<Transaction> transactions = transactionService.getTransactionsBySellerId(seller);
        return ResponseEntity.ok(transactions);
    }

    @GetMapping()
    public ResponseEntity<List<Transaction>> getAllTransactions() {
        List<Transaction> transactions = transactionService.getAllTransactions();
        return ResponseEntity.ok(transactions);
    }
}