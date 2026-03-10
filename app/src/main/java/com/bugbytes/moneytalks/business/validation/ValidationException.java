package com.bugbytes.moneytalks.business.validation;

//Custom exception thrown when expense validation fails.
public class ValidationException extends RuntimeException
{
    //Creates a validation exception (@param: message describing the error).
    public ValidationException(String message)
    {
        super(message);
    }
}
