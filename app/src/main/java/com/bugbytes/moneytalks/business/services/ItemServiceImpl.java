package com.bugbytes.moneytalks.business.services;

import com.bugbytes.moneytalks.business.validation.ItemValidator;
import com.bugbytes.moneytalks.models.Item;
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
    public List<Item> getAllItems() {
        return repo.getAll();
    }

    @Override
    public Item addItem(String title, String description) {
        validator.validate(title, description);

        Item toCreate = new Item(0, title.trim(), safeTrim(description));

        return repo.add(toCreate);
    }

    @Override
    public boolean updateItem(int id, String title, String description) {
        validator.validate(title, description);

        Item existing = repo.getById(id);
        if (existing == null) return false;

        Item updated = new Item(id, title.trim(), safeTrim(description));

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
