package com.bugbytes.moneytalks.models;

public class Expense
{
    private int id;
    private double amount;
    private String category; //food, entertainmet, medical, grocery etc

    private String date;
    private String note;

    public Expense(int id, double amount, String category, String date, String note)
    {
        this.id = id;
        this.amount = amount;
        this.category = category;
        this.date = date;
        this.note = note;
    }

    public int getId()
    {
        return id;
    }

    public double getAmount()
    {
        return amount;
    }

    public String getCategory()
    {
        return category;
    }

    public String getDate() {
        return date;
    }

    public String getNote()
    {
        return note;
    }

    // Setters
    public void setAmount(double amount)
    {
        this.amount = amount;
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