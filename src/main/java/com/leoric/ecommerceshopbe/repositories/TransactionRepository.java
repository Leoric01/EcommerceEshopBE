package com.leoric.ecommerceshopbe.repositories;

import com.leoric.ecommerceshopbe.models.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

}
