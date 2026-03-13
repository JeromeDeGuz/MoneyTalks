package com.bugbytes.moneytalks.models;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

//Represents an expense and allows it to be serialized between activities.
public class Expense implements Serializable
{
    private final long id;
    private String name;
    private BigDecimal amount;
    private String category;
    private LocalDate date;
    private String note;

    //Expense: Constructor for creating an expense object. Takes in @param id and name and amount and category and date and note.
    public Expense(long id, String name, BigDecimal amount, String category, LocalDate date, String note)
    {
        this.id = id;
        this.name = name;
        this.amount = amount;
        this.category = category;
        this.date = date;
        this.note = note;
    }

    //getId: Returns the unique identifier for the expense. Takes in nothing and @return long id.
    public long getId()
    {
        return id;
    }

    //getName: Returns the description of the expense. Takes in nothing and @return String name.
    public String getName()
    {
        return name;
    }

    //getAmount: Returns the monetary value. Takes in nothing and @return BigDecimal amount.
    public BigDecimal getAmount()
    {
        return amount;
    }

    //getDate: Returns the date of the transaction. Takes in nothing and @return LocalDate date.
    public LocalDate getDate()
    {
        return date;
    }

    //getCategory: Returns the assigned category name. Takes in nothing and @return String category.
    public String getCategory()
    {
        return category;
    }

    //getNote: Returns additional details about the expense. Takes in nothing and @return String note.
    public String getNote()
    {
        return note;
    }

    //setName: Updates the expense name. Takes in @param name.
    public void setName(String name)
    {
        this.name = name;
    }

    //setDate: Updates the transaction date. Takes in @param date.
    public void setDate(LocalDate date)
    {
        this.date = date;
    }

    //setCategory: Updates the expense category. Takes in @param category.
    public void setCategory(String category)
    {
        this.category = category;
    }

    //setNote: Updates the additional notes. Takes in @param note.
    public void setNote(String note)
    {
        this.note = note;
    }
}