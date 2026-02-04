package com.bugbytes.moneytalks.business.services;

import com.bugbytes.moneytalks.business.validation.ItemValidator;
import com.bugbytes.moneytalks.models.Expense;
import com.bugbytes.moneytalks.persistence.ItemRepository;

import java.util.List;

public class ItemServiceImpl implements ItemService {

    private final ItemRepository repo;
    private final ItemValidator validator;

    public ItemServiceImpl(ItemRepository repo) {
        this(repo, new ItemValidator());
    }

    public ItemServiceImpl(ItemRepository repo, ItemValidator validator) {
        this.repo = repo;
        this.validator = validator;
    }

    @Override
    public List<Expense> getAllItems() {
        return repo.getAll();
    }

    @Override
    public Expense addItem(String title, String description) {
        validator.validate(title, description);

        Expense toCreate = new Expense(0, title.trim(), safeTrim(description));

        return repo.add(toCreate);
    }

    @Override
    public boolean updateItem(int id, String title, String description) {
        validator.validate(title, description);

        Expense existing = repo.getById(id);
        if (existing == null) return false;

        Expense updated = new Expense(id, title.trim(), safeTrim(description));

        return repo.update(updated);
    }

    @Override
    public boolean deleteItem(int id) {
        return repo.delete(id);
    }

    private String safeTrim(String s) {
        if (s == null) return null;
        String trimmed = s.trim();
        return trimmed.isEmpty() ? null : trimmed;
    }
}
