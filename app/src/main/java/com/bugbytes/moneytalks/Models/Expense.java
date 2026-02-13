package com.bugbytes.moneytalks.Models;

//Represents an expense and allows it to be serialized between activities.
public class Expense implements java.io.Serializable
{
    private int id;
    private String name;
    private double amount; //Price of the expense
    private String category; //Expense category
    private String date;     //Expense date
    private String note;     //Optional note

    //Constructor (@param: name, amount, category, date, note)
    public Expense(String name, double amount, String category, String date, String note)
    {
        this.name = name;
        this.amount = amount;
        this.category = category;
        this.date = date;
        this.note = note;
    }

    // Getters
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

    public String getDate()
    {
        return date;
    }

    public String getNote()
    {
        return note;
    }

    // Setters
    public void setId(int id)
    {
        this.id = id;
    }
}

