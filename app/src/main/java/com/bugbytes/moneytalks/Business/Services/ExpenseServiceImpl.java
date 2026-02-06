package com.bugbytes.moneytalks.Business.Services;

import com.bugbytes.moneytalks.Business.Services.ExpenseService;
import com.bugbytes.moneytalks.Presistence.ExpenseRepository;
import com.bugbytes.moneytalks.Models.Expense;
public class ExpenseServiceImpl implements ExpenseService {

    private final ExpenseRepository expenseRepository;
    private final ExpenseValidator expenseValidator;


    public ExpenseServiceImpl(ExpenseRepository expenseRepository) {
        this.expenseRepository = expenseRepository;
        this.expenseValidator = new ExpenseValidator();
    }

    @Override
    public List<Expense> getAll() {
        return expenseRepository.getAll();
    }

    @Override
    public Expense getById(int id) {
        return expenseRepository.getById(id);
    }

}
