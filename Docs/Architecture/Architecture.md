# Overview
MoneyTalks helps users track their expenses efficiently. It allows adding, viewing, and categorizing expenses, with the flexibility to calculate totals and filter by date or category.

The app follows a **3-tier architecture** for clean separation of concerns:  

- **Presentation Layer:** Handles the user interface (UI)  
- **Business Layer:** Manages core logic and application rules  
- **Persistence Layer:** Handles data storage and retrieval  


## Architecture

### 1. Application Layer 
This layer initializes application-wide services and wires the business layer to the persistence layer.

**Components**
- **MoneyTalksApp:** Entry point of the app. It initializes application-wide services and wires Business Layer to Persistence Layer.

In Iteration 2, it creates the SQLite database helper and repositories and provides them to the business services.

It:

- Creates `AppDbHelper` (SQLite database helper)
- Creates `SqlRepository` and provides it to `ExpenseServiceImpl`
- Creates `SqlCategoryRepository` and provides it to `CategoryServiceImpl`
- Creates `ExpenseValidator` and provides it to `ExpenseServiceImpl`

### 2. Presentation Layer (UI)
This layer handles everything the user sees and interacts with. It **displays data** and **collects user input**.  

**Components:**
- **AddAndEditExpense:** Screen to add or edit expenses. Collects name, amount, category, date, and notes.
- **ExpenseListActivity:** Displays all recorded expenses in a list format.
- **ManageCategoriesActivity:** Screen used to add, view, and delete categories.
- **SettingsActivity:** Displays application settings.
- **ExpenseAdapter:** Bridges raw expense data with the UI, ensuring each expense is displayed correctly.
- **CategoryAdapter:** Bridges raw category data with the UI.

**Interactions:**

- `AddAndEditExpense` → calls → `ExpenseService`
- `ExpenseListActivity` → calls → `ExpenseService`
- `ManageCategoriesActivity` → calls → `CategoryService`


---
### 3. Business Layer (Logic)
This layer contains the **core functionality** and **rules** of the app. It processes data and enforces validation without concern for storage or UI.  

**Components:**
- **ExpenseService:** Defines operations such as addExpense, deleteExpense, and getAllExpenses.
- **ExpenseValidator:** Ensures user input is valid (e.g., non-empty name, positive amount, valid date).
- **ExpenseServiceImpl:** Implements ExpenseService operations, including data management and simple calculations.
- **CategoryService:** Defines operations such as addCategory, deleteCategory, and getAllCategories.
- **CategoryServiceImpl:** Implements CategoryService operations and manages category data.


**Interactions:**
- `ExpenseServiceImpl` → uses → `ExpenseRepository`
- `CategoryServiceImpl` → uses → `CategoryRepository`
- `ExpenseValidator` → validates → user input


---
### 4. Persistence Layer (Storage)
Responsible for **storing and retrieving data**, this layer abstracts the storage mechanism, allowing flexibility to swap databases in the future.

In Iteration 1 a stub implementation (FakeRepository) was used.

In Iteration 2, a SQLite persistence layer has been implemented.

**Components:**
- **ExpenseRepository:** Interface that defines rules for saving and accessing expenses.
- **CategoryRepository:** Interface that defines rules for saving and accessing categories.
- **FakeRepository:** In-memory implementation used in Iteration 1 for development/testing.

1. Stores expenses during runtime.
2. Resets to default data when the app restarts.
3. Implements ExpenseRepository.


- **SqlRepository:** SQLite implementation of ExpenseRepository. It Handles:
1. inserting expenses
1. retrieving expenses
1. deleting expenses


- **SqlCategoryRepository:** SQLite implementation of CategoryRepository, It Handles:
1. inserting categories
1. retrieving categories
1. deleting categories


- **AppDbHelper:** SQLite database helper responsible for creating and managing the database.

- **DbContract:** Defines table names and column names for the database schema.


**Interactions:**
- `ExpenseServiceImpl` → calls → `ExpenseRepository`
- `CategoryServiceImpl` → calls → `CategoryRepository`
- `SqlRepository` — implements → `ExpenseRepository`
- `SqlCategoryRepository` — implements → `CategoryRepository`
- `SqlRepository` → uses → `AppDbHelper`
- `SqlCategoryRepository` → uses → `AppDbHelper`

---
### 5. Models (Data Objects)
Models define the **structure of the data** used across the application.  

- **Expense:** Represents a single expense with the following fields:
  - `ID` – Unique identifier
  - `name` – Expense title
  - `amount` – Monetary value
  - `category` – Type of expense (e.g., Food, Travel, Bills)
  - `date` – Date of the expense
  - `notes` - Optional field for fuller description of expense

- **Category:** Represents an expense category used to organize expenses with the following fields:
  - `ID` – Unique identifier
  - `name` – Category name (e.g., Food, Travel, Bills)
  - `budget` - It is a future feature that we will implement in iteration 3. 


**Model Usage:**
The models are used across all layers:

- **Presentation layer** – displaying expenses and categories
- **Business layer** – validation and logic
- **Persistence layer** – database storage 

---

For a **clearer view of the 3-tier architecture** and how the components interact, see the diagram below:

![3-Tier Architecture Diagram](Docs/Architecture/ArchitectureDiagram.png)


