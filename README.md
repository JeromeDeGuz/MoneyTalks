# 🧧 MoneyTalks
MoneyTalks is an offline-first expense tracking app that keeps money logging simple and stress-free. Users can quickly add daily expenses, organize them by category, and clearly see where their money goes without accounts, bank connections, or complicated setup. Everything stays local, private, and easy to manage.

## 🌱 Vision Statement
The vision of MoneyTalks is to provide a simple and accessible expense tracking solution that helps users gain clarity and control over their personal finances.

[Read the Vision Statement](Docs/Iteration-0%20material/VisionStatement.md)

## 🦾 Team Agreement
This project follows a shared team agreement that defines expectations for collaboration, communication, and individual responsibilities.

[View the Team Agreement](Docs/Iteration-0%20material/work-agreement-template.docx)

## 🐞 Team Members
We’re Bug Bytes, the team behind MoneyTalks:
- Ali, Zia
- De Guzman, Jerome
- Ekeh, Chukwuemeka Benedict-Mary
- Lo, Yu-Ting

## 📂 Project Materials
- [Iteration 0 Materials](Docs/Iteration-0%20material/)
- [Iteration 1 Materials](Docs/Iteration-1%20material/)
- [Iteration 2 Materials](Docs/Iteration-2%20material/)
- [Architecture Overview](Docs/Architecture/Architecture.md)
- [Architecture Diagram](Docs/Architecture/ArchitectureDiagram.png)
- [Coding Standards](Docs/Coding_Standards.pdf)

## 🏗 Architecture
MoneyTalks follows a 3-tier architecture to keep the code organized and easier to maintain.

- **Presentation Layer**: Handles screens, adapters, and user interaction
- **Business Layer**: Handles app rules, validation, and service logic
- **Persistence Layer**: Handles repositories, default data, and SQLite storage

The application layer (`MoneyTalksApp`) wires the services and repositories together when the app starts.

For more details, see the [Architecture Overview](Docs/Architecture/Architecture.md).

## 📱 Current Features
- Add new expenses
- Edit existing expenses
- Delete expenses
- View all expenses
- Manage categories (add, edit, delete)
- Filter expenses by category
- Sort expenses by date
- Set a budget for each category
- View monthly budget summaries by month and year
- Switch between light mode and dark mode
- Store all app data locally using SQLite

## 🧪 Testing
MoneyTalks includes:
- Unit tests for business logic, validation, and models
- Integration tests for expense, category, filter/sort, and budget flows
- UI tests for core user actions
- JaCoCo support for unit test coverage reports

Useful Gradle commands:

```bash
./gradlew testDebugUnitTest
./gradlew connectedDebugAndroidTest
./gradlew jacocoTestReport
```

## 🛠 Dependencies
The following are the main tools and libraries required to build and run MoneyTalks.

### SDK & Tools
- **Android SDK:** Minimum 26 (Android 8.0)
- **Compile SDK:** 34
- **Target SDK:** 34
- **Java:** JDK 17
- **Gradle:** 8.8
- **Build Features:** ViewBinding enabled, Proguard rules included for release

### AndroidX Libraries
- **Core:** `androidx.core:core-ktx`
- **AppCompat:** `androidx.appcompat:appcompat`
- **Material Components:** `com.google.android.material:material`
- **ConstraintLayout:** `androidx.constraintlayout:constraintlayout`
- **Navigation:** `androidx.navigation:navigation-fragment-ktx`, `androidx.navigation:navigation-ui-ktx`
- **Activity KTX:** `androidx.activity:activity-ktx`

### Unit Testing
- **JUnit 5**
- **Mockito**
- **JUnit Platform Launcher**

### Android Instrumented Testing
- **AndroidX JUnit**
- **Espresso Core**
- **Espresso Contrib**
- **AndroidX Test Core, Runner, and Rules**

## 🚀 How to Run
1. Clone the repository:

```bash
git clone https://code.cs.umanitoba.ca/comp3350-winter2026/a02-g04-moneytalks
```

2. Open the project in Android Studio.
3. Sync the Gradle files.
4. Build and run the app on an emulator or Android device.

## 🧭 Main Screens
- **ExpenseListActivity**: Main screen for viewing, filtering, and sorting expenses
- **AddAndEditExpense**: Screen for adding or editing an expense
- **ManageCategoriesActivity**: Screen for category management
- **BudgetActivity**: Screen for viewing and updating category budgets
- **SettingsActivity**: Screen for theme settings

## 🔒 Privacy
MoneyTalks is designed as an offline-first app. It does not require account creation or bank connections, and user data stays on the device.
