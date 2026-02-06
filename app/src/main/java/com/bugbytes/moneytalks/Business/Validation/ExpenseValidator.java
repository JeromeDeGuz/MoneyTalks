package com.bugbytes.moneytalks.Business.Validation;

public class ExpenseValidator {
    public void validate(String name, double amount){
        if(name == null || name.trim().isEmpty()){
            throw new ExpenseValidationException("Name cannot be empty");
        }
        if(amount <= 0){
            throw new ExpenseValidationException("Amount must be greater than zero");
        }
    }
}
