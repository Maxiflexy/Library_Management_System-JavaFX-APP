# Library Management System with JavaFX

## Project Overview

The Library Management System is a comprehensive Java application designed to manage books, members, and borrowing records in a library. This system demonstrates proficiency in Java development, incorporating:

- **JDBC with DAO Pattern**: For database operations
- **File Handling**: For logging and CSV exports
- **Java Collections Framework**: For in-memory data management
- **JavaFX**: For a modern, interactive user interface

The application serves as a practical implementation of a real-world system that combines several Java technologies to create a robust solution for library management.

## Features

### Book Management
- Add new books to the library
- Update book details (title, author, genre, available copies)
- Delete books from the library
- Search for books by title, author, or genre
- Display all books with pagination
- Sort books by title or genre

### Member Management
- Add new members to the library
- Update member details (name, email, phone)
- Delete members from the library
- Display all members with pagination

### Borrowing Management
- Allow members to borrow books
- Process book returns
- Display all borrowing records
- Track borrowed books using an efficient in-memory cache

### Logging and Reporting
- Log all system activities to a text file (library_log.txt)
- Export book or member details to CSV files

## Technical Implementation

### 1. JDBC with DAO Pattern

The system implements the Data Access Object (DAO) pattern to separate database operations from business logic:

- **DatabaseConnection**: Manages connections to the PostgreSQL database
- **DAO Interfaces**: Define contracts for data operations
- **DAO Implementations**: Provide concrete implementations of these contracts

Example of DAO Interface:
```java
public interface BookDAO {
    void addBook(Book book);
    void updateBook(Book book);
    void deleteBook(int bookId);
    List<Book> getAllBooks();
    Book getBookById(int bookId);
    List<Book> searchBooksByTitle(String title);
    List<Book> searchBooksByAuthor(String author);
    List<Book> searchBooksByGenre(String genre);
    boolean updateBookAvailability(int bookId, boolean increment);
}
```

### 2. File Handling

The system uses Java I/O classes for logging and exporting data:

- **Logger**: Uses BufferedWriter to log system activities
- **CSVExporter**: Exports book and member data to CSV files

```java
public class Logger {
    public void log(String message) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("library_log.txt", true))) {
            writer.write(message);
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
```

### 3. Java Collections Framework

The system efficiently manages data in memory using Java collections:

- **ArrayList**: Stores and manages lists of books and members
- **HashMap**: Tracks borrowed books by mapping book IDs to member IDs
- **Comparators**: Enables sorting of books by different criteria

```java
private List<Book> booksCache = new ArrayList<>();
private Map<Integer, Integer> borrowedBooks = new HashMap<>();  // bookId -> memberId
```

### 4. JavaFX Implementation

The JavaFX implementation creates a modern, user-friendly interface for the application:

- **Scene Management**: Multiple screens for different operations
- **FXML**: Separates UI layout from logic
- **Controllers**: Handle user interactions with UI elements
- **TableView**: Displays book and member data with sorting and filtering
- **Pagination**: Manages large datasets efficiently

## Project Structure

```
library-management-system/
├── pom.xml
├── README.md
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/
│   │   │       └── library/
│   │   │           ├── LibraryApplication.java
│   │   │           ├── config/
│   │   │           │   └── DatabaseConnection.java
│   │   │           ├── dao/
│   │   │           │   ├── interfaces/
│   │   │           │   │   ├── BookDAO.java
│   │   │           │   │   ├── MemberDAO.java
│   │   │           │   │   └── BorrowingDAO.java
│   │   │           │   └── impl/
│   │   │           │       ├── BookDAOImpl.java
│   │   │           │       ├── MemberDAOImpl.java
│   │   │           │       └── BorrowingDAOImpl.java
│   │   │           ├── model/
│   │   │           │   ├── Book.java
│   │   │           │   ├── Member.java
│   │   │           │   └── BorrowingRecord.java
│   │   │           ├── service/
│   │   │           │   ├── BookService.java
│   │   │           │   ├── MemberService.java
│   │   │           │   └── BorrowingService.java
│   │   │           ├── util/
│   │   │           │   ├── CSVExporter.java
│   │   │           │   └── Logger.java
│   │   │           └── ui/
│   │   │               ├── controller/
│   │   │               │   ├── MainController.java
│   │   │               │   ├── book/
│   │   │               │   │   ├── BookListController.java
│   │   │               │   │   ├── AddBookController.java
│   │   │               │   │   └── EditBookController.java
│   │   │               │   ├── member/
│   │   │               │   │   ├── MemberListController.java
│   │   │               │   │   ├── AddMemberController.java
│   │   │               │   │   └── EditMemberController.java
│   │   │               │   └── borrowing/
│   │   │               │       ├── BorrowingListController.java
│   │   │               │       ├── BorrowBookController.java
│   │   │               │       └── ReturnBookController.java
│   │   │               └── view/
│   │   │                   ├── main.fxml
│   │   │                   ├── book/
│   │   │                   │   ├── book_list.fxml
│   │   │                   │   ├── add_book.fxml
│   │   │                   │   └── edit_book.fxml
│   │   │                   ├── member/
│   │   │                   │   ├── member_list.fxml
│   │   │                   │   ├── add_member.fxml
│   │   │                   │   └── edit_member.fxml
│   │   │                   └── borrowing/
│   │   │                       ├── borrowing_list.fxml
│   │   │                       ├── borrow_book.fxml
│   │   │                       └── return_book.fxml
│   │   └── resources/
│   │       ├── config.properties
│   │       ├── css/
│   │       │   └── styles.css
│   │       └── images/
│   │           └── library_icon.png
│   └── test/
│       └── java/
│           └── com/
│               └── library/
│                   ├── dao/
│                   │   ├── BookDAOTest.java
│                   │   ├── MemberDAOTest.java
│                   │   └── BorrowingDAOTest.java
│                   └── service/
│                       ├── BookServiceTest.java
│                       ├── MemberServiceTest.java
│                       └── BorrowingServiceTest.java
```

## JavaFX UI Components

The JavaFX implementation provides a user-friendly interface with the following components:

### Main Application Window
- Menu bar with options for File and Help
- Toolbar with buttons for Books, Members, and Borrowings
- Main content area that dynamically loads different views
- Status bar with application information

### Book Management Views
- **Book List**: Displays all books with pagination
    - Search functionality for finding books
    - Sorting options by title and genre
    - Add, edit, and delete buttons
    - Export to CSV button
- **Add Book**: Form for adding a new book
- **Edit Book**: Form for updating book details

### Member Management Views
- **Member List**: Displays all members with pagination
    - Search functionality
    - Add, edit, and delete buttons
    - Export to CSV button
- **Add Member**: Form for adding a new member
- **Edit Member**: Form for updating member details

### Borrowing Management Views
- **Borrowing List**: Displays all borrowing records with pagination
    - Filter options for active/all records
    - Borrow and return buttons
- **Borrow Book**: Interface for selecting a book and member
- **Return Book**: Process for returning borrowed books

## Setup and Usage

### Prerequisites

- Java 11 or higher
- Maven
- PostgreSQL database

### Database Setup

1. Install PostgreSQL if you haven't already
2. Create the database and required tables using the provided SQL script:
   ```
   psql -U postgres -f database_schema.sql
   ```

### Configure Database Connection

Update the database connection settings in `src/main/resources/config.properties`:
```properties
db.url=jdbc:postgresql://localhost:5432/dreamdevs_library_db
db.username=postgres
db.password=password
db.driver=org.postgresql.Driver
```

### Build the Project

Navigate to the project root directory and use Maven to build the project:
```bash
mvn clean package
```

### Run the Application

Execute the JAR file:
```bash
java -jar target/library-management-system-1.0.1-jar-with-dependencies.jar
```

Or run it using Maven:
```bash
mvn javafx:run
```

## Testing

The project includes unit tests for all service and DAO classes. Run the tests using:
```bash
mvn test
```

## Assessment Criteria

1. **Functionality**: Complete implementation of all required features
2. **Code Quality**: Clean, modular, and well-documented code
3. **JDBC and DAO Pattern**: Proper implementation of the DAO pattern
4. **File Handling**: Correct implementation of file operations
5. **Java Collections Framework**: Effective use of collections for data management
6. **Error Handling**: Graceful handling of exceptions
7. **JavaFX UI**: Intuitive and responsive user interface

## Future Enhancements

1. User authentication and access control
2. Book reservation system
3. Fine calculation for late returns
4. Book recommendations based on borrowing history
5. Email notifications for due dates