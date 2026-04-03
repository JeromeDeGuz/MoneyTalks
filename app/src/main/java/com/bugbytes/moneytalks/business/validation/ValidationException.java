package com.bugbytes.moneytalks.business.validation;

//Custom exception thrown when expense or category validation fails.
public class ValidationException extends Exception
{
    //ValidationException: Creates a validation exception with a specific error message. Takes in @param message.
    public ValidationException(String message)
    {
        super(message);
    }
}