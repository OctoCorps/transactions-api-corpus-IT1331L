package com.uap.l.transactions.controller;

import com.uap.l.transactions.repository.Transaction;
import com.uap.l.transactions.repository.TransactionItem;
import com.uap.l.transactions.repository.TransactionMybatisRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/transactions")
public class TransactionController {

    private final TransactionMybatisRepository transactionRepository;

    @Autowired
    public TransactionController(TransactionMybatisRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    @PostMapping
    public int createTransaction(@RequestBody Transaction transaction) {
        //check if transaction items is null because it had error when i first run it
        List<TransactionItem> transactionItems = transaction.getTransactionItems();
        if (transactionItems == null) {
            transactionItems = new ArrayList<>(); // Initialize the list if it is null
        }

        // calculate amount
        double totalAmount = 0;
        for (TransactionItem item : transactionItems) {
            totalAmount += item.getUnitPrice() * item.getQuantityPurchased();
        }

        // set total amount
        transaction.setTotalAmount(totalAmount);

        // save transaction
        int transactionId = transactionRepository.insertTransaction(transaction);

        // save transaction items
        for (TransactionItem item : transactionItems) {
            item.setTransactionId(transactionId);
            transactionRepository.insertTransactionItem(item);
        }

        return transactionId;
    }

    @PostMapping("/{transactionId}/items")
    public int addTransactionItem(@PathVariable int transactionId, @RequestBody TransactionItem transactionItem) {
        // save transaction id to database
        transactionItem.setTransactionId(transactionId);
        return transactionRepository.insertTransactionItem(transactionItem);
    }

    @GetMapping("/{transactionId}")
    public Transaction getTransactionById(@PathVariable int transactionId) {
        // Fetch from database
        return transactionRepository.findTransactionById(transactionId);
    }
}
