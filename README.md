# spring-library-rest-api

REST API created to manage library using Spring Boot. It distinguishes three user roles: admin, librarian, and reader, each with different access permissions to specific endpoints. JWT tokens are used for authentication.

## Table of Contents

- [How to run?](#how-to-run)
- [Documentation](#documentation)
	- [Authentication](#authentication)
	- [Role-based access](#role-based-access)
	- [Resources](#resources)
		- [Auth](#auth)
		- [User](#user)
		- [Book](#book)
- [Technology stack](#technology-stack)

## How to run?

To run the application locally, follow these steps:

1. Clone the repository: `git clone https://github.com/PrzemekBarczyk/spring-library-rest-api.git`
2. Navigate to the project directory: `cd spring-library-rest-api`
3. Build the project: `mvn clean install`
4. Run the application: `mvn spring-boot:run`
5. The API will be available at `http://localhost:8080/`

## Documentation

### Authentication

To access protected endpoints, users need to obtain a JWT token by sending a valid username and password to the `/login` endpoint. The token must then be included in the Authorization header of subsequent requests.

### Role-based access

The system has three roles:

| User             | Available URLs                     | Permissions                                                    | 
| ---------------- | ---------------------------------- | -------------------------------------------------------------- |
| admin            | `/admin`, `/librarian`, `/reader`  | • User management (display, add, edit or delete any user) <br> • Book management (display, add, edit, delete, reserve or borrow any book) |
| librarian        | `/librarian`, `/reader`            | • Reader management (display, add, edit or delete any reader) <br> • Book management (display, add, edit, delete, reserve or borrow any book) |
| reader           | `/reader`                          | • Display books with their status <br> • Display own reservations and loans <br> • Reserve available book <br> • Cancel own reservations |

### Resources

#### Auth

| Link                       | HTTP Method   | Description                              | 
| -------------------------- | ------------- | ---------------------------------------- |
| `/login`                   | POST          | Obtain access token                      |

Sample JSON request and response

Request Body:
```json
{
	"email": "admin@test.com",
	"password": "admin"
}

```
Response Body
```json
{
	"accessToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxIiwiZW1haWwiOiJhZG1pbkB0ZXN0LmNvbSIsInJvbGUiOiJBRE1JTiIsImV4cCI6MTY5MDI5MzI4OH0.YYi0pQ-giaUMSLxwx4oifmKktbrYRXrG0MgOUqzxn0k"
}

```

#### User

| Link                              | HTTP Method   | Description                                               | 
| --------------------------------- | ------------- | --------------------------------------------------------- |
| `/admin/users`                    | GET           | Get page of users                                         |
| `/librarian/users/readers`        | GET           | Get page of readers                                       |
| `/admin/user/{id}`                | GET           | Get user with `{id}`                                      |
| `/librarian/user/reader/{id}`     | GET           | Get reader with `{id}`                                    |
| `/admin/user/admin`               | POST          | Add new admin                                             |
| `/admin/user/librarian`           | POST          | Add new librarian                                         |
| `/librarian/user/reader`          | POST          | Add new reader                                            |
| `/admin/user/{id}`                | PUT           | Update user with `{id}`                                   |
| `/librarian/user/reader/{id}`     | PUT           | Update reader with `{id}`                                 |
| `/reader/user/logged/password`    | PATCH         | Update logged user's password                             |
| `/admin/user/{id}`                | DELETE        | Delete from the database user with `{id}`                 |
| `/librarian/user/reader/{id}`     | DELETE        | Delete from the database reader with `{id}`               |

Sample JSON respond (`/librarian/users/readers`)

Response Body
```json
{
    "content": [
        {
            "id": 2,
            "firstName": "Marian",
            "lastName": "Nowak",
            "email": "marina.nowak@test.com",
            "role": "READER"
        },
        {
            "id": 3,
            "firstName": "Grzegorz",
            "lastName": "Brzęczyszczykiewicz",
            "email": "grzegorz.brzeczyszczykiewicz@test.com",
            "role": "READER"
        }
    ],
    "pageable": {
        "sort": {
            "empty": false,
            "unsorted": false,
            "sorted": true
        },
        "offset": 0,
        "pageNumber": 0,
        "pageSize": 10,
        "paged": true,
        "unpaged": false
    },
    "totalElements": 2,
    "totalPages": 1,
    "last": true,
    "size": 10,
    "number": 0,
    "sort": {
        "empty": false,
        "unsorted": false,
        "sorted": true
    },
    "first": true,
    "numberOfElements": 2,
    "empty": false
}
```

#### Book

| Link                                                            | HTTP Method   | Description                                                            | 
| --------------------------------------------------------------- | ------------- | ---------------------------------------------------------------------- |
| `/librarian/books`                                              | GET           | Get page of books                                                      |
| `/reader/books`                                                 | GET           | Get page of books without connected reader                             |
| `/reader/books/user/logged`                                     | GET           | Get logged reader reserved and borrowed books                          |
| `/librarian/book/{id}`                                          | GET           | Get book with `{id}`     	                                             |
| `/reader/book/{id}`                                             | GET           | Get book with `{id}` without connected reader                          |
| `/librarian/book`                                               | POST          | Add new book                       	                                   |
| `/librarian/book/{id}`                                          | PUT           | Update book with `{id}`    	                                           |
| `/librarian/book/{bookId}/reader/{readerId}/reserve`            | PATCH         | Reserve book with `{bookId}` for reader with `{readerId}`    	         |
| `/reader/book/{bookId}/reader/logged/reserve`                   | PATCH         | Reserve book with `{bookId}` for logged reader     	                   |
| `/librarian/book/{bookId}/reader/{readerId}/cancel-reservation` | PATCH         | Cancel reservation of book with `{bookId}` for reader with `{readerId}`|
| `/reader/book/{bookId}/reader/logged/cancel-reservation`        | PATCH         | Cancel reservation of book with `{bookId}` for logged reader    	     |
| `/librarian/book/{bookId}/reader/{readerId}/borrow`             | PATCH         | Borrow book with `{bookId}` for reader with `{readerId}`  	           |
| `/librarian/book/{bookId}/reader/{readerId}/return`             | PATCH         | Return book with `{bookId}` from reader with `{readerId}`     	       |
| `/librarian/book/{id}`                                          | DELETE        | Delete book with `{id}`    	                                           |

Sample JSON response (`/librarian/books`)

Response Body
```json
{
	"id": 1,
	"title": "Ostatnie Życzenie",
	"author": "Andrzej Sapkowski",
	"publisher": "superNOWA",
	"publicationDate": "2014-11-01"
}
```

## Technology stack

- Java 17
- Spring Boot
- Spring Security
- JWT, Auth0
- JPA, Hibernate
- H2 Database
- Validation
- Lombok
