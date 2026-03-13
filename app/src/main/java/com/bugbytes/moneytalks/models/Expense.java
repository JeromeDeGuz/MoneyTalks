package com.bugbytes.moneytalks.models;

import java.math.BigDecimal;
import java.time.LocalDate;

//Represents an expense and allows it to be serialized between activities.
public class Expense implements java.io.Serializable
{
    private final long id;
    private String name;
    private BigDecimal amount;
    private String category;
    private LocalDate date;
    private String note;

    //Constructor (@param: name, amount, category, date, note)
    public Expense(long id, String name, BigDecimal amount, String category, LocalDate date, String note)
    {
        this.id = id;
        this.name = name;
        this.amount = amount;
        this.category = category;
        this.date = date;
        this.note = note;
    }

    // Getters
    public long getId()
    {
        return id;
    }

    public String getName()
    {
        return name;
    }

    public BigDecimal getAmount()
    {
        return amount;
    }

    public LocalDate getDate()
    {
        return date;
    }

    public String getCategory()
    {
        return category;
    }

    public String getNote()
    {
        return note;
    }

    // Setters (Note: setId removed as per feedback to maintain immutability)
    public void setName(String name)
    {
        this.name = name;
    }


    public void setDate(LocalDate date)
    {
        this.date = date;
    }

    public void setCategory(String category)
    {
        this.category = category;
    }

    public void setNote(String note)
    {
        this.note = note;
    }
}