package com.leoric.ecommerceshopbe.services.interfaces;

import com.leoric.ecommerceshopbe.models.Order;
import com.leoric.ecommerceshopbe.models.Seller;
import com.leoric.ecommerceshopbe.models.Transaction;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface TransactionService {


    List<Transaction> getAllTransactions();

    List<Transaction> getTransactionsBySellerId(Seller seller);


    Transaction findById(Long id);

    Transaction save(Transaction entity);

    void deleteById(Long id);

    Transaction createTransaction(Order order);
}
