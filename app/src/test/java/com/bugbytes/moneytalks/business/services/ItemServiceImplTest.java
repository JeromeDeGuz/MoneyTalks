package com.bugbytes.moneytalks.business.services;

import com.bugbytes.moneytalks.business.validation.ValidationException;
import com.bugbytes.moneytalks.models.Item;
import com.bugbytes.moneytalks.persistence.ItemRepository;
import com.bugbytes.moneytalks.persistence.fake.FakeItemRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


class ItemServiceImplTest {

    private ItemRepository repo;
    private ItemService service;

    @BeforeEach
    void setup() {
        repo = new FakeItemRepository();
        service = new ItemServiceImpl(repo);
    }

    @Test
    void getAllItems_initiallyEmpty() {
        List<Item> items = service.getAllItems();
        assertNotNull(items);
        assertEquals(0, items.size());
    }

    @Test
    void addItem_assignsId_andTrimsFields() {
        Item created = service.addItem("  Buy milk  ", "  2% please  ");

        assertNotNull(created);
        assertTrue(created.getId() > 0);
        assertEquals("Buy milk", created.getTitle());
        assertEquals("2% please", created.getDescription());
    }

    @Test
    void addItem_nullOrBlankDescription_becomesNull() {
        Item created1 = service.addItem("Task", "   ");
        assertNotNull(created1);
        assertNull(created1.getDescription());

        Item created2 = service.addItem("Task2", null);
        assertNotNull(created2);
        assertNull(created2.getDescription());
    }

    @Test
    void addItem_rejectsBlankTitle() {
        assertThrows(ValidationException.class,
                () -> service.addItem("   ", "desc"));
        assertEquals(0, service.getAllItems().size());
    }

    @Test
    void addItem_rejectsTooLongTitle() {
        String longTitle = "x".repeat(61);
        assertThrows(ValidationException.class,
                () -> service.addItem(longTitle, "desc"));
    }

    @Test
    void addItem_rejectsTooLongDescription() {
        String longDesc = "x".repeat(501);
        assertThrows(ValidationException.class,
                () -> service.addItem("Title", longDesc));
    }

    @Test
    void addItem_tags_areTrimmed_deduped_caseInsensitive_andBlanksRemoved() {
        Item created = service.addItem(
                "Task",
                "Desc"
        );

        assertNotNull(created);
    }

    @Test
    void updateItem_updatesExisting_returnsTrue() {
        Item created = service.addItem("Old", "Old desc");

        boolean ok = service.updateItem(created.getId(), "New", "New desc");
        assertTrue(ok);

        Item reloaded = repo.getById(created.getId());
        assertNotNull(reloaded);
        assertEquals("New", reloaded.getTitle());
        assertEquals("New desc", reloaded.getDescription());

    }

    @Test
    void updateItem_nonexistent_returnsFalse() {
        boolean ok = service.updateItem(999, "Title", "Desc");
        assertFalse(ok);
    }

    @Test
    void updateItem_rejectsInvalidTitle_andDoesNotModify() {
        Item created = service.addItem("Good", "Desc");

        assertThrows(ValidationException.class,
                () -> service.updateItem(created.getId(), "   ", "New"));

        Item reloaded = repo.getById(created.getId());
        assertNotNull(reloaded);
        assertEquals("Good", reloaded.getTitle());
        assertEquals("Desc", reloaded.getDescription());
    }

    @Test
    void deleteItem_existing_returnsTrue_andRemoves() {
        Item created = service.addItem("Task", "Desc");

        boolean ok = service.deleteItem(created.getId());
        assertTrue(ok);

        assertNull(repo.getById(created.getId()));
        assertEquals(0, service.getAllItems().size());
    }

    @Test
    void deleteItem_nonexistent_returnsFalse() {
        boolean ok = service.deleteItem(12345);
        assertFalse(ok);
    }

    @Test
    void addTwoItems_idsAreDifferent_andGetAllReturnsBoth() {
        Item a = service.addItem("A", null);
        Item b = service.addItem("B", null);

        assertNotEquals(a.getId(), b.getId());

        List<Item> items = service.getAllItems();
        assertEquals(2, items.size());
    }

}
