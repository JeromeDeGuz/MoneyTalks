package com.bugbytes.moneytalks.Presistence;

import com.bugbytes.moneytalks.Models.Expense;
import java.util.List;

public interface ExpenseRepository {

    List<Expense> getAll();

    Expense getById(int id);

    void add(Expense expense);

    void update(Expense expense);

    boolean delete(int id);
}
