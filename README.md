# LibraryManagementApp

LibraryManagementApp is a REST API built with Java 17, Spring Boot, and SQLite.

## Directory Structure

The application is organized into the following directories:

### Models
The models directory contains the classes that represent the entities of the system, such as:
- **Book**
- **User**
- **Rental**
- **RegisterInfo**

### Database
The database directory contains the classes responsible for interacting with the database, including:
- **DatabaseInitializer**
- **BookRepository**
- **UserRepository**
- **RentalRepository**
  
The database file (library_db.sqlite) will be created the first time the app is executed and all tables will be empty.

#### Database Schema
A simple diagram of the database schema:

![Database Schema](img.png)

### API 
The api directory contains the classes for handling API endpoints, such as:
- **BookAPI**
- **UserAPI**
- **RentalAPI**

## API Endpoints
The root of all API endpoints is http://localhost:8081/api

### User Endpoints
- **GET /api/users**:

  Retrieve a list of all users

  ```curl -L "http://localhost:8081/api/users"```
- **POST /api/users**:

  Create a new user
  
  ```curl -L "http://localhost:8081/api/users/register" -H "Content-Type: application/json" -d "{\"username\":\"user\",\"password\":\"user\",\"admin\":0}"```
  
  ```curl -L "http://localhost:8081/api/users/register" -H "Content-Type: application/json" -d "{\"username\":\"admin\",\"password\":\"admin\",\"admin\":1}"```

  
### Book Endpoints
- **GET /api/books**:

  Retrieve a list of all books
  
  ```curl -L "http://localhost:8081/api/books"```
- **GET /api/books/title/{title}**:

  Retrieve all books with the given title
  
  ```curl -L "http://localhost:8081/api/books/title/The Hobbit"```
- **GET /api/books/author/{author}**:

  Retrieve all books with the given author
  
  ```curl -L "http://localhost:8081/api/books/author/J.R.R Tolkien"```
- **GET /api/books/id/{id}**:

  Retrieve a specific book by its id
  
  ```curl -L "http://localhost:8081/api/books/id/1"```
- **POST /api/books/add**:

  Add a book to the library. Requires Basic HTTP Authentication with an admin's user credentials.
  
  ```curl -L "http://localhost:8081/api/books/add" -H "Content-Type: application/json" -u "admin:admin" -d "{\"title\": \"The Hobbit\",\"author\": \"J.R.R. Tolkien\" }"```
- **POST /api/books/delete/{id}**:

  Delete a book from the library based on its id. Requires Basic HTTP Authentication with an admin's user credentials.
  
  ```curl -L POST "http://localhost:8081/api/books/delete/1" -u "admin:admin"```
- **POST /api/books/update/{id}**:

  Update a book from the library based on its id. Requires Basic HTTP Authentication with an admin's user credentials.
  
  ```curl -L POST "http://localhost:8081/api/books/update/1" -H "Content-Type: application/json" -u "admin:admin" -d "{\"title\": \"The Lord Of The Rings\",\"author\": \"J.R.R. Tolkien\" }"```

### Rental Endpoints
- **GET /api/rentals**:

  Retrieve a list of all rentals
  
  ```curl -L "http://localhost:8081/api/rentals"```
- **POST /api/rentals/rent/{id}**:

  Rent a book from the library based on its id.  Requires Basic HTTP Authentication. Both admins and member users can access this endpoint.
  
  ```curl -L -X POST "http://localhost:8081/api/rentals/rent/1" -u "user:user"```
- **POST /api/rentals/return/{id}**:

  Return a book from the library based on its id.  Requires Basic HTTP Authentication. Both admins and member users can access this endpoint.

  ```curl -L -X POST "http://localhost:8081/api/rentals/return/1" -u "user:user"```
