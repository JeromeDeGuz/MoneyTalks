package com.bugbytes.moneytalks.persistence;

import com.bugbytes.moneytalks.models.Expense;
import java.util.List;

public interface ItemRepository {
    List<Expense> getAll();

    Expense getById(int id);

    Expense add(Expense item);

    boolean update(Expense item);

    boolean delete(int id);
}
