package com.leoric.ecommerceshopbe.services.impl;

import com.leoric.ecommerceshopbe.models.Order;
import com.leoric.ecommerceshopbe.models.Seller;
import com.leoric.ecommerceshopbe.models.Transaction;
import com.leoric.ecommerceshopbe.repositories.SellerRepository;
import com.leoric.ecommerceshopbe.repositories.TransactionRepository;
import com.leoric.ecommerceshopbe.services.interfaces.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;

    private final SellerRepository sellerRepository;


    @Override
    public List<Transaction> getAllTransactions() {
        return transactionRepository.findAll();
    }

    @Override
    public List<Transaction> getTransactionsBySellerId(Seller seller) {
        return transactionRepository.findBySellerId(seller.getId());
    }

    @Override
    public Transaction findById(Long id) {
        return transactionRepository.findById(id).orElseThrow(() -> new RuntimeException("Transaction not found"));
    }

    @Override
    public Transaction save(Transaction entity) {
        return transactionRepository.save(entity);
    }

    @Override
    public void deleteById(Long id) {
        transactionRepository.deleteById(id);
    }

    @Override
    public Transaction createTransaction(Order order) {
        Seller seller = sellerRepository.getReferenceById(order.getSellerId());
        Transaction transaction = new Transaction();
        transaction.setSeller(seller);
        transaction.setCustomer(order.getUser());
        transaction.setOrder(order);

        return transactionRepository.save(transaction);
    }
}
