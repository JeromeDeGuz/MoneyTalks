package com.bugbytes.moneytalks.Business.Services;

import com.bugbytes.moneytalks.Models.Expense;

import java.util.List;


public interface ExpenseService {
    List<Expense> getAllItems();
    void addItem(String name, double amount, String category, String date, String note);
    boolean updateItem(int id, String name, double amount, String category, String date, String note);
    boolean deleteItem(int id);




}
