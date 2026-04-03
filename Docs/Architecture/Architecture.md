# MoneyTalks Architecture

## Overview
MoneyTalks is an offline-first Android expense tracker. Users can add, edit, delete, view, sort, and filter expenses, manage categories, assign category budgets, and review monthly budget summaries. The app keeps data local on the device and is organized around a **3-tier architecture** with an **application/composition root** that wires dependencies together.

### High-level layers
- **Application layer:** Creates and wires concrete objects.
- **Presentation layer:** Activities, adapters, and UI event handling.
- **Business layer:** Application rules, validation, filtering, sorting, budget calculations.
- **Persistence layer:** Repository interfaces plus fake and SQLite implementations.
- **Models:** Shared data objects passed across layers.

---

## Package structure

```text
com.bugbytes.moneytalks
├── application
│   └── MoneyTalksApp
├── presentation
│   ├── ExpenseListActivity
│   ├── AddAndEditExpense
│   ├── ManageCategoriesActivity
│   ├── BudgetActivity
│   ├── SettingsActivity
│   ├── ExpenseAdapter
│   ├── CategoryAdapter
│   ├── BudgetAdapter
│   └── SimpleItemSelectedListener
├── business
│   ├── services
│   │   ├── ExpenseService / ExpenseServiceImpl
│   │   ├── CategoryService / CategoryServiceImpl
│   │   └── BudgetService / BudgetServiceImpl
│   └── validation
│       ├── ExpenseValidator
│       ├── CategoryValidator
│       ├── Validator
│       └── ValidationException
├── persistence
│   ├── ExpenseRepository
│   ├── CategoryRepository
│   ├── DefaultContent
│   ├── fake
│   │   ├── FakeExpenseRepository
│   │   └── FakeCategoryRepository
│   └── real
│       ├── AppDbHelper
│       ├── DbContract
│       ├── SqlExpenseRepository
│       └── SqlCategoryRepository
└── models
    ├── Expense
    ├── Category
    └── BudgetSummary
```

---

## 1. Application layer
This layer acts as the app's **composition root**. `MoneyTalksApp` is responsible for creating concrete implementations and exposing ready-to-use services to the presentation layer.

### Responsibilities
- Loads the saved theme mode from `SharedPreferences` at app startup.
- Chooses the persistence implementation:
  - `SqlExpenseRepository` and `SqlCategoryRepository` when SQLite is enabled.
  - `FakeExpenseRepository` and `FakeCategoryRepository` when the in-memory option is used.
- Populates initial sample data through `DefaultContent` only when **both** repositories are empty.
- Creates validators and business services.
- Exposes service getters for use by activities.

### Wiring
- `ExpenseRepository` -> `ExpenseServiceImpl`
- `CategoryRepository` + `ExpenseRepository` + `CategoryValidator` -> `CategoryServiceImpl`
- `ExpenseValidator` -> `ExpenseServiceImpl`
- `CategoryService` + `ExpenseService` -> `BudgetServiceImpl`

### Why this matters
The UI never constructs repositories directly. The business layer depends on repository **interfaces**, so the app can switch between fake and real persistence with minimal impact on the rest of the code.

---

## 2. Presentation layer
The presentation layer contains Android activities and adapters. It handles user interaction, screen navigation, and rendering data returned by the business layer.

### Activities
- **`ExpenseListActivity`**
  - Main expense screen.
  - Displays all expenses in a `RecyclerView`.
  - Supports sorting by date and filtering by category.
  - Navigates to add/edit, budget, and settings screens.

- **`AddAndEditExpense`**
  - Form screen for creating or updating an expense.
  - Collects name, amount, category, date, and note.
  - Can add a category inline through a dialog.
  - After saving, asks `BudgetService` for the selected category's monthly summary and shows an over-budget warning when needed.

- **`ManageCategoriesActivity`**
  - Displays categories in a list.
  - Supports add, edit, and delete operations.

- **`BudgetActivity`**
  - Displays monthly budget summaries for all categories.
  - Lets the user switch year and month.
  - Allows editing a category's budget.

- **`SettingsActivity`**
  - Navigates to category management and budget screens.
  - Saves light/dark theme preference using `SharedPreferences`.

### Adapters / UI helpers
- **`ExpenseAdapter`**: Binds `Expense` data to expense rows and handles edit/delete UI events.
- **`CategoryAdapter`**: Binds `Category` data and includes an extra add-category row.
- **`BudgetAdapter`**: Binds `BudgetSummary` data and highlights over-budget spending.
- **`SimpleItemSelectedListener`**: Simplifies spinner selection callbacks in the budget screen.

### Presentation-layer dependencies
- `ExpenseListActivity` -> `ExpenseService`
- `AddAndEditExpense` -> `ExpenseService`, `CategoryService`, `BudgetService`
- `ManageCategoriesActivity` -> `CategoryService`
- `BudgetActivity` -> `BudgetService`
- `SettingsActivity` -> Android settings/navigation APIs

---

## 3. Business layer
The business layer contains the app's rules and processing logic. It is independent of Android UI widgets and focuses on validation, use-case logic, and data transformation.

### 3.1 Services

#### `ExpenseService` / `ExpenseServiceImpl`
Handles expense-related use cases:
- Add a new expense.
- Update an existing expense.
- Delete an expense.
- Retrieve all expenses.
- Sort expenses by date.
- Filter expenses by category and then sort them.
- Retrieve an expense by ID.
- Provide string-based add/update helpers for presentation-layer form input.

`ExpenseServiceImpl` validates expenses before saving and delegates storage to `ExpenseRepository`.

#### `CategoryService` / `CategoryServiceImpl`
Handles category-related use cases:
- Add, update, delete, and retrieve categories.
- Prevent deletion of a category that is still used by expenses.
- Propagate category renames to the expense data.
- Calculate monthly spending for a category.
- Check whether a category has exceeded its budget in a target month.

`CategoryServiceImpl` depends on both `CategoryRepository` and `ExpenseRepository` because category operations can affect expense records.

#### `BudgetService` / `BudgetServiceImpl`
Handles budget-summary use cases:
- Build monthly summaries for all categories.
- Build a summary for one category.
- Update the budget amount for a category.

`BudgetServiceImpl` does not talk directly to repositories. Instead, it composes existing business services:
- Reads categories from `CategoryService`
- Reads expenses from `ExpenseService`
- Updates budgets through `CategoryService`

This keeps budget logic at the business-service level instead of duplicating persistence access.

### 3.2 Validation
- **`ExpenseValidator`** checks:
  - non-null expense object
  - non-empty name
  - name is not only digits
  - name length between 2 and 50
  - amount greater than zero
  - non-empty category
  - non-null date
  - date not in the future
  - note length at most 500

- **`CategoryValidator`** checks:
  - non-empty category name
  - no duplicate category names (case-insensitive)
  - name is not only digits
  - budget is not null
  - budget is not negative

- **`ValidationException`** is the domain-level exception used when business rules are violated.
- **`Validator<T>`** is the shared validation interface.

### Business-layer dependency summary
- `ExpenseServiceImpl` -> `ExpenseRepository`, `ExpenseValidator`
- `CategoryServiceImpl` -> `CategoryRepository`, `ExpenseRepository`, `CategoryValidator`
- `BudgetServiceImpl` -> `CategoryService`, `ExpenseService`

---

## 4. Persistence layer
The persistence layer abstracts storage behind repository interfaces.

### 4.1 Repository interfaces
- **`ExpenseRepository`**
  - add/delete/update expenses
  - get all expenses
  - get expense by ID
  - check emptiness
  - check whether a category is used by any expense
  - update expense category references after a category rename

- **`CategoryRepository`**
  - add/delete/update categories
  - get all categories
  - find a category by name
  - check emptiness

These interfaces isolate the business layer from the concrete storage mechanism.

### 4.2 Fake repositories
Used for in-memory runtime storage:
- **`FakeExpenseRepository`**
  - stores expenses in a static list
  - generates IDs in memory
  - supports CRUD and category synchronization

- **`FakeCategoryRepository`**
  - stores categories in memory
  - supports CRUD lookups by name

These are useful for testing and for keeping the business layer decoupled from SQLite.

### 4.3 Real SQLite repositories
Used for persistent device storage:
- **`AppDbHelper`**
  - creates and upgrades the SQLite database
  - creates `expenses` and `categories` tables

- **`DbContract`**
  - defines table and column names for both tables

- **`SqlExpenseRepository`**
  - stores `Expense` rows in SQLite
  - converts between DB rows and `Expense` objects
  - supports category usage checks and bulk category rename updates

- **`SqlCategoryRepository`**
  - stores `Category` rows in SQLite
  - persists category budgets as well as names
  - retrieves category records by name or as a full list

### 4.4 Default content seeding
`DefaultContent` inserts starter categories and starter expenses, but only when **both** repositories are empty. This avoids partial reseeding problems.

---

## 5. Models
These classes are shared data objects used across multiple layers.

### `Expense`
Represents one expense.
- `id`
- `name`
- `amount`
- `category`
- `date`
- `note`

### `Category`
Represents one expense category.
- `id`
- `name`
- `budget`

### `BudgetSummary`
Represents the calculated monthly budget state for one category.
- `categoryName`
- `budget`
- `spentThisMonth`
- derived helpers such as `isOverBudget()` and `getOverAmount()`

---

## 6. Main data flows

### Add expense flow
1. User enters data in `AddAndEditExpense`.
2. Screen sends data to `ExpenseService`.
3. `ExpenseValidator` validates the expense.
4. `ExpenseRepository` saves the expense.
5. `BudgetService` computes the updated category summary.
6. UI shows an over-budget dialog if spending exceeds the category budget.

### Edit category flow
1. User edits a category in `ManageCategoriesActivity` or updates a budget in `BudgetActivity`.
2. Request goes to `CategoryService` or `BudgetService`.
3. `CategoryServiceImpl` updates the category record.
4. If the category name changed, `ExpenseRepository.updateExpenseCategory(...)` syncs related expenses.

### Delete category flow
1. User requests deletion in `ManageCategoriesActivity`.
2. `CategoryServiceImpl` checks whether any expense still uses that category.
3. If linked expenses exist, deletion is rejected.
4. Otherwise, `CategoryRepository` removes the category.

---

## 7. Architectural rules and dependency direction
MoneyTalks follows these dependency rules:
- Presentation depends on business services, not repositories.
- Business depends on repository interfaces, not concrete SQLite classes.
- Persistence implements the interfaces required by business.
- The application layer is the only place that knows the concrete wiring.
- Models move between layers as plain data objects.

In short, the dependency direction is:

```text
Presentation -> Business -> Persistence
                ^
                |
           Application wiring
```

---

## 8. Notes on the current design
- The app now includes a real **budget feature**, so category budgets are no longer just a future placeholder.
- Theme mode is stored locally with `SharedPreferences`, which complements the main repository-based data persistence.
- The architecture supports both fake and SQLite storage without changing presentation code.
- Budget logic is implemented as a separate business service built on top of the existing category and expense services.

---

## Summary
MoneyTalks uses a layered architecture that keeps UI, business rules, and storage concerns separated. `MoneyTalksApp` wires the system together, repositories hide the storage details, validators protect domain rules, and the budget feature is implemented as a higher-level business service built from the category and expense services.
