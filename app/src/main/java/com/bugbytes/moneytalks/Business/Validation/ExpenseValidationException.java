package com.bugbytes.moneytalks.Business.Validation;

public class ExpenseValidationException extends RuntimeException
{
    public ExpenseValidationException(String message)
    {
        super(message);
    }
}
