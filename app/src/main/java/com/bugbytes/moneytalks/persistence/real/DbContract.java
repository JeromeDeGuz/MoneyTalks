package com.bugbytes.moneytalks.persistence.real;

//DbContract: Defines the schema for the database tables and columns.
public final class DbContract
{
    //ExpenseEntry: Contains column definitions for the expenses table.
    public static class ExpenseEntry
    {
        public static final String TABLE_NAME = "expenses";
        public static final String COLUMN_ID = "id";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_AMOUNT = "amount";
        public static final String COLUMN_CATEGORY = "category";
        public static final String COLUMN_DATE = "date";
        public static final String COLUMN_NOTE = "note";
    }

    //CategoryEntry: Contains column definitions for the categories table.
    public static class CategoryEntry
    {
        public static final String TABLE_NAME = "categories";
        public static final String COLUMN_ID = "id";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_BUDGET = "budget";
    }
}