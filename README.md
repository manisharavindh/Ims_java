# Library Management System

A Java-based Library Management System with JavaFX frontend and MySQL backend.

## Technology Stack

- **Language:** Java 25
- **Build Tool:** Maven
- **Frontend:** JavaFX (FXML + CSS)
- **Database:** MySQL
- **Connectivity:** JDBC

## Project Structure

```
lms_java/
├── src/main/java/com/lms/
│   ├── Main.java              — Application entry point
│   ├── controller/            — JavaFX FXML controllers
│   ├── model/                 — Data model classes (Book, Member, etc.)
│   ├── dao/                   — Data Access Objects (JDBC queries)
│   ├── service/               — Business logic layer
│   └── util/                  — Utility classes (DB connection, etc.)
├── src/main/resources/
│   ├── fxml/                  — FXML layout files
│   └── css/                   — Stylesheets
├── database/
│   └── schema.sql             — MySQL database schema
├── pom.xml                    — Maven configuration
└── README.md
```

## How to Run

### Prerequisites
- Java 25 (JDK)
- Maven
- MySQL Server

### Steps

1. Clone the repository:
   ```bash
   git clone https://github.com/your-username/lms_java.git
   cd lms_java
   ```

2. Build the project:
   ```bash
   mvn clean compile
   ```

3. Run the application:
   ```bash
   mvn javafx:run
   ```

## Features (Planned)

- User login and authentication
- Dashboard with statistics
- Book management (CRUD)
- Member management (CRUD)
- Issue and return books
- Reports (overdue books, borrowing history, etc.)
