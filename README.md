# UniBank

A RESTful banking system built with **Spring Boot 3**, featuring JWT-based authentication, account management, and transaction handling.

## Tech Stack

| Layer | Technology |
|---|---|
| Language | Java 21 |
| Framework | Spring Boot 3.5 |
| Security | Spring Security + JWT (jjwt 0.12) |
| Persistence | Spring Data JPA + MySQL |
| Utilities | Lombok |
| Build | Maven |

## Features

- User registration and login with JWT authentication
- Secure, stateless REST API
- Account and transaction management
- Input validation and error handling
- Role-based access control via Spring Security

## Getting Started

### Prerequisites

- Java 21+
- Maven 3.9+
- MySQL 8+

### Setup

1. **Clone the repository**
   ```bash
   git clone https://github.com/j0hn-char/UniBank.git
   cd UniBank
   ```

2. **Configure the database**

   Create a MySQL database and update `src/main/resources/application.properties`:
   ```properties
   spring.datasource.url=jdbc:mysql://localhost:3306/unibank
   spring.datasource.username=YOUR_USERNAME
   spring.datasource.password=YOUR_PASSWORD
   ```

3. **Run the application**
   ```bash
   ./mvnw spring-boot:run
   ```

   The API will be available at `http://localhost:8080`.

## API Overview

| Method | Endpoint | Description |
|---|---|---|
| POST | `/api/auth/register` | Register a new user |
| POST | `/api/auth/login` | Login and receive a JWT |
| GET | `/api/accounts` | Get user accounts |
| POST | `/api/transactions` | Create a transaction |

> Full API documentation coming soon.

## Project Structure

```
src/
└── main/
    ├── java/com/unibank/
    │   ├── config/       # Security & app config
    │   ├── controller/   # REST controllers
    │   ├── model/        # JPA entities
    │   ├── repository/   # Spring Data repositories
    │   ├── service/      # Business logic
    │   └── dto/          # Data transfer objects
    └── resources/
        └── application.properties
```

## License

This project is licensed under the MIT License — see [LICENSE](LICENSE) for details.
