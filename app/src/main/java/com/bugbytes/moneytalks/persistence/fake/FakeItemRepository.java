package com.bugbytes.moneytalks.persistence.fake;

import com.bugbytes.moneytalks.persistence.ItemRepository;
import com.bugbytes.moneytalks.models.Expense;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FakeItemRepository implements ItemRepository {

    private final List<Expense> items = new ArrayList<>();
    private int nextId = 1;

    @Override
    public List<Expense> getAll() {
        return Collections.unmodifiableList(items);
    }

    @Override
    public Expense getById(int id) {
        for (Expense item : items) {
            if (item.getId() == id) return item;
        }
        return null;
    }

    @Override
    public Expense add(Expense item) {
        if (item == null) return null;

        int id = item.getId();
        if (id == 0) {
            id = nextId++;
        } else if (getById(id) != null) {
            // id already exists
            return null;
        }

        Expense stored = new Expense(id, item.getTitle(), item.getDescription());
        items.add(stored);
        return stored;
    }

    @Override
    public boolean update(Expense item) {
        if (item == null) return false;

        for (int i = 0; i < items.size(); i++) {
            if (items.get(i).getId() == item.getId()) {
                Expense updated = new Expense(item.getId(), item.getTitle(), item.getDescription());
                items.set(i, updated);
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean delete(int id) {
        for (int i = 0; i < items.size(); i++) {
            if (items.get(i).getId() == id) {
                items.remove(i);
                return true;
            }
        }
        return false;
    }
}
