package com.bugbytes.moneytalks.business.validation;

//Custom exception thrown when expense validation fails.
public class ValidationException extends Exception
{
    //ValidationException: Creates a validation exception. Takes in @param message and returns nothing.
    public ValidationException(String message)
    {
        super(message);
    }
}