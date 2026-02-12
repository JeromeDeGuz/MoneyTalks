# Overview
MoneyTalks helps users track their expenses efficiently. It allows adding, viewing, and categorizing expenses, with the flexibility to calculate totals and filter by date or category.  

The app follows a **3-tier architecture** for clean separation of concerns:  

- **Presentation Layer:** Handles the user interface (UI)  
- **Business Layer:** Manages core logic and application rules  
- **Persistence Layer:** Handles data storage and retrieval  


## Architecture

### 1. Presentation Layer (UI)
This layer handles everything the user sees and interacts with. It **displays data** and **collects user input**.  

**Components:**
- **AddExpenseActivity:** Screen to add new expenses. Collects name, amount, category, and date.
- **ExpenseListActivity:** Displays all recorded expenses in a list format.
- **ExpenseAdapter:** Bridges raw data with the UI, ensuring each expense is displayed correctly.
- **MoneyTalksApp:** Entry point of the app, initializes the connections between business and persistence layers.

---
### 2. Business Layer (Logic)
This layer contains the **core functionality** and **rules** of the app. It processes data and enforces validation without concern for storage or UI.  

**Components:**
- **ExpenseService:** Defines operations such as adding expenses, retrieving all expenses, and calculating totals.
- **ExpenseValidator:** Ensures user input is valid (e.g., non-empty name, positive amount, valid date).
- **ServicesImpl:** Implements `ExpenseService` operations, including calculations and managing data flow.

---
### 3. Persistence Layer (Storage)
Responsible for **storing and retrieving data**, this layer abstracts the storage mechanism, allowing flexibility to swap databases in the future.  

**Components:**
- **ExpenseRepository:** Interface that defines rules for saving and accessing expenses.
- **FakeRepository:** Stub implementation for development/testing. Stores data in memory while the app is running.

---
### 4. Models (Data Objects)
Models define the **structure of the data** used across the application.  

- **Expense:** Represents a single expense with the following fields:
  - `ID` – Unique identifier
  - `name` – Expense title
  - `amount` – Monetary value
  - `category` – Type of expense (e.g., Food, Travel, Bills)
  - `date` – Date of the expense


---

For a **clearer view of the 3-tier architecture** and how the components interact, see the diagram below:

![3-Tier Architecture Diagram](https://code.cs.umanitoba.ca/comp3350-winter2026/a02-g04-moneytalks/-/raw/0ab2916460865bcb447785bf826a2db2c12f8aad/Docs/Iteration-1%20material/Architecture/ArchitectureDiagram.jpg)

