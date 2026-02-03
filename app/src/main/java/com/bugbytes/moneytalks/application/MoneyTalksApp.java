package com.bugbytes.moneytalks.application;

import android.app.Application;

import com.bugbytes.moneytalks.business.services.ItemService;
import com.bugbytes.moneytalks.business.services.ItemServiceImpl;
import com.bugbytes.moneytalks.persistence.ItemRepository;
import com.bugbytes.moneytalks.persistence.real.AppDbHelper;
import com.bugbytes.moneytalks.persistence.real.SqlItemRepository;

public class MoneyTalksApp extends Application {

    private ItemService itemService;

    @Override
    public void onCreate() {
        super.onCreate();

        ItemRepository repo = new SqlItemRepository(new AppDbHelper(getApplicationContext()));
        itemService = new ItemServiceImpl(repo);
    }

    public ItemService getItemService() {
        return itemService;
    }
}
