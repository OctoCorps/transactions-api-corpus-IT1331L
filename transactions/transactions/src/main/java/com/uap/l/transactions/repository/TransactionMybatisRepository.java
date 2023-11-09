package com.uap.l.transactions.repository;


import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface TransactionMybatisRepository {

    @Insert("INSERT INTO transactions (customerName, customerAddress, totalAmount) " +
            "VALUES (#{customerName}, #{customerAddress}, #{totalAmount})")
    @Options(useGeneratedKeys = true, keyProperty = "transactionId", keyColumn = "transactionId")
    int insertTransaction(Transaction transaction);

    @Select("SELECT * FROM transactions WHERE transactionId = #{transactionId}")
    Transaction findTransactionById(int transactionId);

    @Insert("INSERT INTO transaction_items (transactionId, barcode, itemName, unitPrice, quantityPurchased) " +
            "VALUES (#{transactionId}, #{barcode}, #{itemName}, #{unitPrice}, #{quantityPurchased})")
    int insertTransactionItem(TransactionItem transactionItem);

    @Select("SELECT * FROM transaction_items WHERE transactionId = #{transactionId}")
    List<TransactionItem> findTransactionItemsByTransactionId(int transactionId);
}



