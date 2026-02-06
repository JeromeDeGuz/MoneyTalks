package com.bugbytes.moneytalks.Business.Services;

import com.bugbytes.moneytalks.Business.Services.ExpenseService;
import com.bugbytes.moneytalks.Business.Validation.ExpenseValidator;
import com.bugbytes.moneytalks.Presistence.ExpenseRepository;
import com.bugbytes.moneytalks.Models.Expense;

import java.util.List;

public class ExpenseServiceImpl implements ExpenseService {

    private final ExpenseRepository expenseRepository;
    private final ExpenseValidator expenseValidator;

    public ExpenseServiceImpl(ExpenseRepository expenseRepository){
        this(expenseRepository, new ExpenseValidator());
    }

    public ExpenseServiceImpl(ExpenseRepository expenseRepository, ExpenseValidator validator) {
        this.expenseRepository = expenseRepository;
        this.expenseValidator = validator;
    }

    @Override
    public List<Expense> getAllItems() {
        return expenseRepository.getAll();
    }

    @Override
    public Expense addItem(String name, double amount, String category, String date, String note ) {
        expenseValidator.validate(name, amount);

        Expense toCreate = new Expense(0, name, 0, category, date, note);

        return expenseRepository.add(toCreate);


    }

    @Override
    public boolean updateItem(int id, String name, double amount, String category, String date, String note) {
        expenseValidator.validate(name, amount);

        Expense existing = expenseRepository.getById(id);
        if (existing == null) {
            return false;
        }

        Expense updated = new Expense(id, name, amount, category, date, note);
        return expenseRepository.update(updated);

    }
    @Override
    public boolean deleteItem(int id) {
        return expenseRepository.delete(id);
    }

    private String safeTrim(String s) {
        if (s == null) return null;
        String trimmed = s.trim();
        return trimmed.isEmpty() ? null : trimmed;
    }
}
