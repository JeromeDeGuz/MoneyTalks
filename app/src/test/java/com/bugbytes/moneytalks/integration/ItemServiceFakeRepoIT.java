package com.bugbytes.moneytalks.integration;

import com.bugbytes.moneytalks.business.services.ItemService;
import com.bugbytes.moneytalks.business.services.ItemServiceImpl;
import com.bugbytes.moneytalks.models.Item;
import com.bugbytes.moneytalks.persistence.ItemRepository;
import com.bugbytes.moneytalks.persistence.fake.FakeItemRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ItemServiceFakeRepoIT {

    private ItemRepository repo;
    private ItemService service;

    @BeforeEach
    void setup() {
        repo = new FakeItemRepository();          // real implementation (in-memory), not a mock
        service = new ItemServiceImpl(repo);      // real service
    }

    @Test
    void addUpdateDelete_flow_worksAcrossLayers() {
        // ADD (UI would call service with raw strings)
        Item created = service.addItem(
                "  Laundry  ",
                "  wash + dry  ");

        assertNotNull(created);
        assertTrue(created.getId() > 0);
        assertEquals("Laundry", created.getTitle());
        assertEquals("wash + dry", created.getDescription());

        // READ (service -> repo)
        List<Item> afterAdd = service.getAllItems();
        assertEquals(1, afterAdd.size());
        assertEquals(created.getId(), afterAdd.get(0).getId());

        // UPDATE
        boolean updated = service.updateItem(
                created.getId(),
                "Laundry (updated)",
                null
        );
        assertTrue(updated);

        Item reloaded = repo.getById(created.getId()); // directly check persistence state
        assertNotNull(reloaded);
        assertEquals("Laundry (updated)", reloaded.getTitle());
        assertNull(reloaded.getDescription());

        // DELETE
        boolean deleted = service.deleteItem(created.getId());
        assertTrue(deleted);
        assertEquals(0, service.getAllItems().size());
        assertNull(repo.getById(created.getId()));
    }

}
