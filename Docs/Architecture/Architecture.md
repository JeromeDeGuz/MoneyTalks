# Overview
MoneyTalks helps users track their expenses efficiently. It allows adding, viewing, and categorizing expenses, with the flexibility to calculate totals and filter by date or category.

The app follows a **3-tier architecture** for clean separation of concerns:  

- **Presentation Layer:** Handles the user interface (UI)  
- **Business Layer:** Manages core logic and application rules  
- **Persistence Layer:** Handles data storage and retrieval  


## Architecture

### 1. Application Layer 
This layer initializes application-wide services and wires the business layer to the persistence layer. Wiring the SqlExpense

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

1. **services**

- **ExpenseService:** Defines operations such as addExpense, deleteExpense, updateExpense, getAllExpenses, getExpensesSortedByDate, getExpensesByCategorySortedByDate, and getExpenseById.
- **CategoryService:** Defines operations such as addCategory, updateCategory, deleteCategory, getCategory, and getAllCategories.
- **ExpenseServiceImpl:** Implements ExpenseService operations, including expense validation, editing, deleting, filtering by category, and sorting by date.
- **CategoryServiceImpl:** Implements CategoryService operations and manages category data, including updating category names and preventing deletion of categories that still contain expenses.


2. **validation**

- **ExpenseValidator:** Ensures user input for expenses is valid (e.g., non-empty name, positive amount, valid category, and valid date).
- **CategoryValidator:** Ensures category input is valid (e.g., non-empty category name, no duplicate category names, and category name cannot contain only numbers).
- **ValidationException:** Custom exception thrown when validation rules are violated.
- **Validator:** Generic validation interface used to enforce validation rules for different models.


**Interactions:**
- ExpenseServiceImpl → uses → ExpenseRepository
- CategoryServiceImpl → uses → CategoryRepository
- CategoryServiceImpl → uses → ExpenseRepository
- ExpenseServiceImpl → uses → Validator<Expense>
- CategoryServiceImpl → uses → Validator<Category>
- ExpenseValidator → validates → Expense
- CategoryValidator → validates → Category
- Validator → throws → ValidationException


---

### 4. Persistence Layer (Storage)
Responsible for storing and retrieving data, this layer abstracts the storage mechanism, allowing flexibility to swap databases in the future.

In Iteration 1, a stub implementation (FakeExpenseRepository) was used. In Iteration 2, a full SQLite persistence layer has been implemented under the real package. With an addition of FakeCategoryRepository that was included to reflect/mirror the behaviour of the SqlCategoryRepository.

**Components:**

1. **fake**
- **FakeExpenseRepository:** In-memory implementation of ExpenseRepository. It uses a static list to persist data during runtime and utilizes DefaultContent for sample data.
- **FakeCategoryRepository:** In-memory implementation of CategoryRepository.

Behavior: These components store data in memory during runtime and reset to default data whenever the app restarts.

2. **real (SQLite Implementation)**
- **AppDbHelper:** SQLite database helper responsible for creating the database and managing schema versions.

- **DbContract:** Defines the formal schema (table names and column names) for the database.

- **SqlExpenseRepository:** SQLite implementation of ExpenseRepository. It handles:
    1. Inserting, updating, and deleting expenses.
    1. Retrieving expenses (all or by specific ID).
    1. Syncing expense categories when a category name is updated.

- **SqlCategoryRepository:** SQLite implementation of CategoryRepository. It handles:

  1. CRUD operations for categories.
  1. Retrieving categories by name.
    

3. **Core Interfaces & Exceptions**: 
- **ExpenseRepository:** Interface defining the contract for expense data operations (Add, Delete, Update, Get, Category Sync).
- **CategoryRepository:** Interface defining the contract for category management (Add, Delete, Update, Find).
- **DefaultContent:** Centralized class that populates repositories with initial sample data (e.g., "Uber", "Rent", "Spotify") if they are empty.
- **PersistenceException:** A custom RuntimeException thrown when database operations fail.


**Interactions:**

- `ExpenseServiceImpl` → calls → `ExpenseRepository`
- `CategoryServiceImpl` → calls → `CategoryRepository`
- `CategoryServiceImpl` → calls → ExpenseRepository` (for checking category usage)
- `FakeExpenseRepository` — implements → `ExpenseRepository`
- `FakeCategoryRepository` — implements → `CategoryRepository`
- `SqlExpenseRepository` — implements → `ExpenseRepository`
- `SqlCategoryRepository` — implements → `CategoryRepository`
- `SqlExpenseRepository` / `SqlCategoryRepository` → uses → `AppDbHelper`


**Additional Business & Validation Logic**:
To ensure data integrity before it reaches the persistence layer, the following interactions occur:

- `ExpenseServiceImpl` → uses → `Validator<Expense>`
- `CategoryServiceImpl` → uses → `Validator<Category>`
- `ExpenseValidator` → validates → `Expense`
- `CategoryValidator` → validates → `Category`
- `Validator` → throws → ValidationException (if rules are breached)


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


