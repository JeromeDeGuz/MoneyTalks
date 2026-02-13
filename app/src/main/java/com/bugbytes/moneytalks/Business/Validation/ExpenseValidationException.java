package com.bugbytes.moneytalks.Business.Validation;

//Custom exception thrown when expense validation fails.
public class ExpenseValidationException extends RuntimeException
{
    //Creates a validation exception (@param: message describing the error).
    public ExpenseValidationException(String message)
    {
        super(message);
    }
}
