package com.bugbytes.moneytalks.Presistence.Fake;

import com.bugbytes.moneytalks.Models.Expense;
import com.bugbytes.moneytalks.Presistence.ExpenseRepository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class FakeRepository implements ExpenseRepository {

    static List<Expense> expenses = new ArrayList<Expense>();

    private int nextId = 1;


    public FakeRepository(){
        Expense expense1 = new Expense(1, "expense1", 100, "category1", "date1", "note1");
        Expense expense2 = new Expense(2, "expense2", 200, "category2", "date2", "note2");
        Expense expense3 = new Expense(3, "expense3", 300, "category3", "date3", "note3");
        Expense expense4 = new Expense(4, "expense4", 400, "category4", "date4", "note4");
        expenses.add(expense1);
        expenses.add(expense2);
        expenses.add(expense3);
        expenses.add(expense4);
    }


    @Override
    public List<Expense> getAll(){
        return Collections.unmodifiableList(expenses);
        //return a read only list of expenses
    }

    @Override
    public Expense getById(int id){
        //traverse List and find id
        for (Expense expense : expenses) {
            if (expense.getId() == id) {
                return expense;
            }
        }
        return null;
    }

    @Override
    public void add(Expense expense){
        expenses.add(expense);
    }

    @Override
    public boolean update(Expense expense){

    }

    @Override
    public boolean delete(int id){
        Expense toDelete = getById(id);

        if(getById(id).getId() == id){
            //expense exists
            expenses.remove(toDelete);
            return true;
        }

        return false;
    }

}
