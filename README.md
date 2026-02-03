# COMP 3350 – Sample Android Project

**Instructor:** Lauren Himbeault
**Term:** Winter 2026

This repository contains a **sample Android application** demonstrating the architectural, testing, and tooling expectations for the COMP 3350 course project. This **does not** showcase good coding standards, appropriate SOLID design principles or other coding/design expectations as seen in class. This should not be followed as any kind of standard with respect to the actual content of the files.
Students may:

* Use this project as a **starting point**, or
* Use it as a **reference implementation** when designing their own approved project.

The goal is not visual polish. The goal is **clean architecture, correct dependency direction, and testable design**.

---

## Project Overview

This sample implements a **very simple to-do list** application:

* Users can add to-do items with a title, description, and tags
* Items are stored in a **local SQLite database**
* The UI displays items in a list
* The app is intentionally minimal and visually “ugly”

The simplicity is intentional. This project exists to demonstrate **structure**, not features.

---

## Architecture & Package Structure

This project enforces **strict separation of concerns**.

```
com.lameault.sample_project
│
├── presentation/      // Android UI (Activities, Adapters, UI logic)
│
├── business/           // Domain logic, services, validation
│
├── persistence/        // Repository interfaces + implementations
│   ├── fake/           // In-memory repositories (ArrayList)
│   └── real/           // SQLite repositories
│
├── models/             // Plain data objects (Item, Keyword)
│
└── application/        // Composition root (wiring dependencies)
```

### Dependency Rules (STRICT)

* `presentation → business → persistence`
* `application` wires concrete implementations together
* **No Android imports** (`android.*`, `androidx.*`) are allowed in:

    * `business`
    * `persistence`
* Android-specific code belongs **only** in:

    * `presentation`
    * a small amount in `application`

Violating these rules will result in grading penalties.

---

## Persistence & Database

* The app uses **SQLite** via `SupportSQLiteOpenHelper`
* **Room is NOT permitted**
* SQL statements live in the `persistence` layer
* The **real app always uses the real SQLite repository**
* Fake repositories exist **only for testing**

---

## Testing Strategy

This project demonstrates **three levels of testing**.

### 1. Unit Tests (JVM)

**Location:** `src/test/java`

* Uses **JUnit 5**
* No Android dependencies
* Tests business logic in isolation
* Fake repositories are used here

Example:

* `ItemServiceImplTest`

Run with:

```
./gradlew testDebugUnitTest
```

---

### 2. Integration Tests (JVM or Instrumented)

**Location:** `src/test/java` or `src/androidTest/java` (depending on needs)

* Real services + real repositories
* Fake DB (in-memory) or real SQLite
* Validates cross-layer behavior

Example:

* `ItemServiceFakeRepoIT`
* `ItemServiceSqlRepoIT`

---

### 3. End-to-End (E2E) UI Tests

**Location:** `src/androidTest/java`

* Uses **Espresso**
* Runs on emulator or device
* Launches the real app
* Interacts with UI
* Verifies data is persisted and displayed

Example:

* `AddTodoItemE2ETest`

These tests use the **real SQLite database** and clear it before each run.

---

## Running All Tests (One Click)

Android Studio cannot natively run JVM tests and instrumented tests together.

A **Compound Run Configuration** is recommended:

1. `Unit Tests` → `:app:testDebugUnitTest`
2. `Android Tests` → Instrumented tests
3. `All Tests` → Compound configuration combining both

CLI equivalent:

```
./gradlew testDebugUnitTest connectedDebugAndroidTest
```

---

## SDK & Tooling Requirements

### Android SDK

* `compileSdk = 35`
* `targetSdk = 35`
* `minSdk = 34`

### Java

* **Java 17**
* JDK 17 or higher must be installed
* Kotlin is **not used** in this project

### Tools

* Android Studio (current stable)
* Android Emulator (Pixel 9 device profile recommended)
* Gradle Wrapper (included)

---

## Version Control

* Git is required
* Repository hosted on:
  [https://code.cs.umanitoba.ca](https://code.cs.umanitoba.ca)
* Commit early
* Push often
* Late submissions receive **zero**

A `.gitignore` is provided. Do not modify it unless you know exactly why.

---

## Notes to Students

* This project is **intentionally simple**
* Visual quality does not matter
* Architecture, testing, and correctness do
* Do not copy blindly — **understand what each layer does**
* Ask questions early

---

## License / Usage

This sample project is provided for **educational use only** within COMP 3350.

Do not reuse it outside the course without permission.