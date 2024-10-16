package com.leoric.ecommerceshopbe.services.interfaces;

import com.leoric.ecommerceshopbe.models.Transaction;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface TransactionService {

    List<Transaction> findAll();

    Transaction findById(Long id);

    Transaction save(Transaction entity);

    void deleteById(Long id);
}
