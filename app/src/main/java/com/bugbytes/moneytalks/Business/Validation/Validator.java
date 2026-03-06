package com.bugbytes.moneytalks.Business.Validation;

//Generic interface for validation logic.
public interface Validator<T>
{
    //Validates an object of type T.
    void validate(T target);
}