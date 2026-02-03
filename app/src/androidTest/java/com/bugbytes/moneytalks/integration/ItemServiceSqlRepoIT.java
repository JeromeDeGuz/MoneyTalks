package com.bugbytes.moneytalks.integration;

import android.content.Context;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.bugbytes.moneytalks.business.services.ItemService;
import com.bugbytes.moneytalks.business.services.ItemServiceImpl;
import com.bugbytes.moneytalks.models.Item;
import com.bugbytes.moneytalks.persistence.ItemRepository;
import com.bugbytes.moneytalks.persistence.real.AppDbHelper;
import com.bugbytes.moneytalks.persistence.real.SqlItemRepository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

import static org.junit.Assert.*;

@RunWith(AndroidJUnit4.class)
public class ItemServiceSqlRepoIT {

    private ItemRepository repo;
    private ItemService service;

    @Before
    public void setup() {
        Context context = ApplicationProvider.getApplicationContext();

        // Clean DB so test is deterministic
        context.deleteDatabase(AppDbHelper.DB_NAME);

        repo = new SqlItemRepository(new AppDbHelper(context));
        service = new ItemServiceImpl(repo);
    }

    @Test
    public void addUpdateDelete_flow_worksAcrossLayers() {
        Item created = service.addItem(
                "  Laundry  ",
                "  wash + dry  ");

        assertNotNull(created);
        assertTrue(created.getId() > 0);
        assertEquals("Laundry", created.getTitle());
        assertEquals("wash + dry", created.getDescription());

        List<Item> afterAdd = service.getAllItems();
        assertEquals(1, afterAdd.size());
        assertEquals(created.getId(), afterAdd.get(0).getId());

        boolean updated = service.updateItem(
                created.getId(),
                "Laundry (updated)",
                null);
        assertTrue(updated);

        Item reloaded = repo.getById(created.getId());
        assertNotNull(reloaded);
        assertEquals("Laundry (updated)", reloaded.getTitle());
        assertNull(reloaded.getDescription());

        boolean deleted = service.deleteItem(created.getId());
        assertTrue(deleted);
        assertEquals(0, service.getAllItems().size());
        assertNull(repo.getById(created.getId()));
    }
}
