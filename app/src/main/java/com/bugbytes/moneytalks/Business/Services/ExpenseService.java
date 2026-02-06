package com.bugbytes.moneytalks.Business.Services;
//Model layer
import com.bugbytes.moneytalks.Models.Expense;
import java.util.List;

//This defines business rule for our app, acts as contract that any ExpenseService implementation must follow. (@ekeh stuff here)
public interface ExpenseService
{
    void addExpense(Expense expense); //used by zia's screen. Ekeh will add rules in implementation.
    List<Expense> getAllExpenses(); //used by jenna screen to list all the expenses.
}