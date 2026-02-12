package com.bugbytes.moneytalks.Models;

//Serializable usage: allows expense object to be converted into a format that can be passed between activities or stored temporarily (confirm with lauren, see other past projects to see their implementation)
public class Expense implements java.io.Serializable
{
    private int id;
    private String name;
    private double amount;
    private String category; //more about category in iteration 2.
    private String date;   //simple for Iteration 1 (can change later)
    private String note;   //optional

    public Expense(String name, double amount, String category, String date, String note) {
        this.name = name;
        this.amount = amount;
        this.category = category;
        this.date = date;
        this.note = note;
    }

    //Getters
    public int getId()
    {
        return id;
    }

    public String getName()
    {
        return name;
    }

    public double getAmount()
    {
        return amount;
    }

    public String getCategory()
    {
        return category;
    }

    public String getDate()

    {
        return date;
    }

    public String getNote()
    {
        return note;
    }

    //Setters
    public void setId(int id)
    {
        this.id = id;
    }

    public void setAmount(double amount)
    {
        this.amount = amount;
    }

    public void setName(String name)
    {
        this.name = name;
    }
    public void setCategory(String category)
    {
        this.category = category;
    }

    public void setDate(String date)
    {
        this.date = date;
    }

    public void setNote(String note)
    {
        this.note = note;
    }
}
