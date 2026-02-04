package com.bugbytes.moneytalks.business.services;

import com.bugbytes.moneytalks.models.Item;

import java.util.List;

public interface ItemService {
    List<Item> getAllItems();
    Item addItem(String title, String description);
    boolean updateItem(int id, String title, String description);
    boolean deleteItem(int id);
}
