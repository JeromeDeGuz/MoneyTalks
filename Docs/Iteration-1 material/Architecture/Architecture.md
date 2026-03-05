# Overview
MoneyTalks helps users track their expenses efficiently. It allows adding, viewing, and categorizing expenses, with the flexibility to calculate totals and filter by date or category.  

The app follows a **3-tier architecture** for clean separation of concerns:  

- **Presentation Layer:** Handles the user interface (UI)  
- **Business Layer:** Manages core logic and application rules  
- **Persistence Layer:** Handles data storage and retrieval  


## Architecture

### 1. Application Layer 
This layer initializes application-wide services and wires the business layer to the persistence layer.

**Components:**

- **MoneyTalksApp:** Entry point of the app. It initializes application-wide services and wires Business Layer to Persistence Layer. It also creates FakeRepository and provides it to `ExpenseServiceImpl` and creates `ExpenseValidator` and provides it to `ExpenseServiceImpl`.


### 2. Presentation Layer (UI)
This layer handles everything the user sees and interacts with. It **displays data** and **collects user input**.  

**Components:**
- **AddExpense:** Screen to add new expenses. Collects name, amount, category, date, and notes
- **ExpenseListActivity:** Displays all recorded expenses in a list format.
- **ExpenseAdapter:** Bridges raw data with the UI, ensuring each expense is displayed correctly.

**Interactions:**
- `AddExpense` → calls → `ExpenseService`
- `ExpenseListActivity` → calls → `ExpenseService`


---
### 3. Business Layer (Logic)
This layer contains the **core functionality** and **rules** of the app. It processes data and enforces validation without concern for storage or UI.  

**Components:**
- **ExpenseService:** Defines operations such as addExpense, deleteExpense, and getAllExpenses.
- **ExpenseValidator:** Ensures user input is valid (e.g., non-empty name, positive amount, valid date).
- **ExpenseServicesImpl:** Implements `ExpenseService` operations, including data management and simple calculations (e.g., totals, filtering planned for future iterations).

**Interactions:**
- `ExpenseServiceImpl` → uses → `ExpenseRepository`
- `ExpenseValidator` → validates → user input


**Notes:**
Features like filtering by category/date and calculating totals are planned for future iterations.

---
### 4. Persistence Layer (Storage)
Responsible for **storing and retrieving data**, this layer abstracts the storage mechanism, allowing flexibility to swap databases in the future.  

**Components:**
- **ExpenseRepository:** Interface that defines rules for saving and accessing expenses.
- **FakeRepository:** In-memory implementation for development/testing.
  - Stores expenses during runtime.
  - Resets to default data when the app restarts.
  - Implements ExpenseRepository.


**Interactions:**
- `ExpenseServiceImpl` → calls → `ExpenseRepository`
- `FakeRepository` — implements→ `ExpenseRepository`


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


---

For a **clearer view of the 3-tier architecture** and how the components interact, see the diagram below:

![3-Tier Architecture Diagram](https://code.cs.umanitoba.ca/comp3350-winter2026/a02-g04-moneytalks/-/raw/main/Docs/Iteration-1%20material/Architecture/ArchitectureDiagram.jpg)


