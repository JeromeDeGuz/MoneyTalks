# 🧧MoneyTalks
MoneyTalks is an offline-first expense tracking app that keeps money logging simple and stress-free. Users can quickly add daily expenses and clearly see where their money goes, without accounts, bank connections, or complicated setup. Everything stays local, private, and easy to manage.

## 🌱 Vision Statement
The vision of MoneyTalks is to provide a simple and accessible expense tracking solution that helps users gain clarity and control over their personal finances.

[Read the Vision Statement](https://code.cs.umanitoba.ca/comp3350-winter2026/a02-g04-moneytalks/-/blob/main/Docs/Iteration-0%20material/VisionStatement.md)

## 🦾 Team Agreement
This project follows a shared team agreement that defines expectations for collaboration, communication, and individual responsibilities.

[View the Team Agreement](https://code.cs.umanitoba.ca/comp3350-winter2026/a02-g04-moneytalks/-/blob/main/Docs/Iteration-0%20material/work-agreement-template.docx)

## 🐞 Team Members
We’re Bug Bytes, the team behind MoneyTalks:
- Ali, Zia
- De Guzman, Jerome
- Ekeh, Chukwuemeka Benedict-Mary
- Lo, Yu-Ting

## 📂 Project Materials

- [Iteration 0 Materials](Docs/Iteration-0 material/)  
- [Iteration 1 Materials](Docs/Iteration-1 material/)
- [Iteration 2 Materials](Docs/Iteration-2 material/)  
- [Architecture Folder](Docs/Architecture/)

## 📱 Current Features

- Adding expenses
- Editing existing expenses
- Deleting expenses
- Viewing all expenses
- Managing expense categories (add, edit, delete)
- Filtering expenses by category
- Sorting expenses by date
- Offline data storage using SQLite

## 🛠 Dependencies
The following are the key tools and libraries required to build and run MoneyTalks:

- **Android SDK:** Minimum: 26 (Android 8.0)
- **Java:** JDK 17  
- **Gradle:** 8.8  
- **AndroidX Libraries:**  
  - Core: androidx.core:core-ktx  
  - AppCompat: androidx.appcompat:appcompat  
  - Material: com.google.android.material:material  
  - ConstraintLayout: androidx.constraintlayout:constraintlayout  
- **Navigation:** androidx.navigation:navigation-fragment-ktx, navigation-ui-ktx  
- **Activity:** androidx.activity:activity-ktx  
- **Unit Testing:** JUnit  

## 🚀 How to Run

1. Clone the repository:  
```bash
git clone https://code.cs.umanitoba.ca/comp3350-winter2026/a02-g04-moneytalks
```
2. Open the project in Android Studio.
3. Sync Gradle:
    - Android Studio will usually prompt to "Sync Project with Gradle Files."
    - Wait for all dependencies to download and the build to finish.
4. Build and run the app on an emulator or device.
5. Use the app:
    - `AddAndEditExpense` to add or edit expenses
    - `ExpenseListActivity` to view, filter, and sort expenses
    - `ManageCategoriesActivity` to manage categories


