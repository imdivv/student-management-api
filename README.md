# Student Management API

A RESTful Student Management System built using Java and Spring Boot, featuring CRUD operations, input validation, exception handling, pagination, sorting, searching, filtering, and automated testing.

## Tech Stack

- **Language:** Java 17
- **Framework:** Spring Boot 4.1.1
- **Database:** MySQL 8
- **ORM:** Spring Data JPA / Hibernate
- **Build Tool:** Maven
- **API Documentation:** Swagger / OpenAPI
- **Testing:** JUnit 5, Mockito, MockMvc
- **API Testing:** Postman

## Features

- Create, retrieve, update, and delete student records.
- Request and response DTOs for structured API communication.
- Input validation using Jakarta Validation.
- Centralized exception handling.
- Appropriate HTTP response status codes.
- Pagination and sorting.
- Search students by name or keyword.
- Filter students by course and age.
- Interactive API documentation using Swagger.
- Unit and controller tests.

## API Endpoints

| Method | Endpoint | Description |
|---|---|---|
| POST | `/students` | Create a student |
| GET | `/students` | Get students with pagination and sorting |
| PUT | `/students/{id}` | Update a student |
| DELETE | `/students/{id}` | Delete a student |
| GET | `/students/search` | Search by exact name |
| GET | `/students/filter/course` | Filter by course |
| GET | `/students/filter/age` | Filter by minimum age |
| GET | `/students/search/keyword` | Search by name keyword |

## Getting Started

### Prerequisites

- Java 17 or later
- MySQL 8
- Maven (or use the included Maven Wrapper)

### 1. Clone the repository

```bash
git clone https://github.com/imdivv/student-management-api.git
cd student-management-api
```

### 2. Create the database

```sql
CREATE DATABASE studentdb;
```

### 3. Configure environment variables

The application uses environment variables for database credentials.

Set these before running the application:

```text
DB_URL=jdbc:mysql://localhost:3306/studentdb
DB_USERNAME=root
DB_PASSWORD=your_mysql_password
```

Replace the password with your local MySQL password.

### 4. Run the application

On Windows:

```bash
mvnw.cmd spring-boot:run
```

The application runs at:

`http://localhost:8080`

### 5. Access Swagger UI

`http://localhost:8080/swagger-ui.html`

Use Swagger UI to explore and test the API endpoints.

## Running Tests

Run the complete test suite using:

```bash
mvnw.cmd test
```

The project includes service-layer unit tests and controller-layer tests covering successful operations, validation failures, and exception handling.

## Project Structure

```text
src/
├── main/
│   ├── java/com/example/student/
│   │   ├── config/
│   │   ├── dto/
│   │   ├── GlobalExceptionHandler.java
│   │   ├── Student.java
│   │   ├── StudentController.java
│   │   ├── StudentNotFoundException.java
│   │   ├── StudentRepository.java
│   │   ├── StudentService.java
│   │   └── StudentapiApplication.java
│   └── resources/
│       └── application.properties
└── test/
    └── java/com/example/student/
```

## Author

**Divyansh Varshney**

GitHub: [@imdivv](https://github.com/imdivv)