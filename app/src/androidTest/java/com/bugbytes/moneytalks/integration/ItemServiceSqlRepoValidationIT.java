package com.bugbytes.moneytalks.integration;

import android.content.Context;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.bugbytes.moneytalks.business.services.ItemService;
import com.bugbytes.moneytalks.business.services.ItemServiceImpl;
import com.bugbytes.moneytalks.business.validation.ValidationException;
import com.bugbytes.moneytalks.persistence.ItemRepository;
import com.bugbytes.moneytalks.persistence.real.AppDbHelper;
import com.bugbytes.moneytalks.persistence.real.SqlItemRepository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

@RunWith(AndroidJUnit4.class)
public class ItemServiceSqlRepoValidationIT {

    private ItemService service;
    private Context context;

    @Before
    public void setup() {
        context = ApplicationProvider.getApplicationContext();
        context.deleteDatabase(AppDbHelper.DB_NAME);

        ItemRepository repo = new SqlItemRepository(new AppDbHelper(context));
        service = new ItemServiceImpl(repo);
    }

    @Test
    public void addItem_blankTitle_throwsValidationException() {
        ValidationException ex = assertThrows(
                ValidationException.class,
                () -> service.addItem("   ", "some description")
        );

        // Optional: if your ValidationException has a useful message
        // assertTrue(ex.getMessage().toLowerCase().contains("title"));
    }

    @Test
    public void addItem_blankTitle_doesNotPersistAnything() {
        // Ensure that a failed validation doesn't write to the DB
        assertThrows(
                ValidationException.class,
                () -> service.addItem("   ", "some description")
        );

        assertEquals(0, service.getAllItems().size());
    }

    @Test
    public void addItem_valid_persistsAndRepoReturnsIt() {
        // Add via service (this exercises service validation + repo write)
        var created = service.addItem("Laundry", "wash + dry");

        assertNotNull(created);
        assertTrue(created.getId() > 0);

        // Read back via persistence (same underlying DB/repo)
        var all = service.getAllItems();

        assertEquals(1, all.size());
        assertEquals(created.getId(), all.get(0).getId());
        assertEquals("Laundry", all.get(0).getTitle());
        assertEquals("wash + dry", all.get(0).getDescription());
    }


    // Helper for JUnit4 (since assertThrows is JUnit5)
    private static <T extends Throwable> T assertThrows(Class<T> expected, ThrowingRunnable r) {
        try {
            r.run();
        } catch (Throwable actual) {
            if (expected.isInstance(actual)) {
                return expected.cast(actual);
            }
            fail("Expected " + expected.getName() + " but got " + actual.getClass().getName());
        }
        fail("Expected " + expected.getName() + " but no exception was thrown");
        return null; // unreachable
    }

    private interface ThrowingRunnable {
        void run() throws Throwable;
    }
}
