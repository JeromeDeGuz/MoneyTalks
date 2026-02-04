package com.bugbytes.moneytalks.business.services;

import com.bugbytes.moneytalks.models.Expense;

import java.util.List;

public interface ItemService {
    List<Expense> getAllItems();
    Expense addItem(String title, String description);
    boolean updateItem(int id, String title, String description);
    boolean deleteItem(int id);
}
