package com.bugbytes.moneytalks.persistence;

import com.bugbytes.moneytalks.models.Item;
import java.util.List;

public interface ItemRepository {
    List<Item> getAll();

    Item getById(int id);

    Item add(Item item);

    boolean update(Item item);

    boolean delete(int id);
}
