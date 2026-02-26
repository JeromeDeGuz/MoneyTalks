package com.bugbytes.moneytalks.Models;

public class Category {
    private int id;
    private String categoryName;

    private double categoryBudget;

    public Category(String categoryName){
        this.categoryName = categoryName;
    }

    public String getCategoryName(){
        return this.categoryName;
    }

    //allows for editing of categories
    public void setCategoryName(String categoryName){
        this.categoryName = categoryName;
    }

    public double getCategoryBudget(){
        return this.categoryBudget;
    }

    public void setCategoryBudget(double categoryBudget){
        this.categoryBudget = categoryBudget;
    }



}
