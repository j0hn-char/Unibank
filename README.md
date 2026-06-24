<div align="center">

# UniBank

**A secure, production-structured banking system with a REST API backend**

Built with Spring Boot 3 · Java 21 · JWT · MySQL

[![Java](https://img.shields.io/badge/Java_21-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)](#)
[![Spring Boot](https://img.shields.io/badge/Spring_Boot_3-6DB33F?style=for-the-badge&logo=springboot&logoColor=white)](#)
[![Spring Security](https://img.shields.io/badge/Spring_Security-6DB33F?style=for-the-badge&logo=springsecurity&logoColor=white)](#)
[![MySQL](https://img.shields.io/badge/MySQL-4479A1?style=for-the-badge&logo=mysql&logoColor=white)](#)
[![Maven](https://img.shields.io/badge/Maven-C71A36?style=for-the-badge&logo=apachemaven&logoColor=white)](#)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow?style=for-the-badge)](#)

</div>


## About

UniBank is a banking system that simulates core banking functionality: account management, fund transfers, and loan processing, with a focus on clean architecture and security best practices.

**Highlights:**
- Stateless JWT authentication with Spring Security
- Full loan lifecycle: apply, approve/reject, repay
- Paginated transaction history per account
- Global exception handling with structured error responses
- Clean layered architecture: Controller, Service, Repository


## Tech Stack

| Layer | Technology |
|---|---|
| Language | Java 21 |
| Framework | Spring Boot 3.5 |
| Security | Spring Security + JWT (jjwt 0.12) |
| Persistence | Spring Data JPA + MySQL |
| Utilities | Lombok |
| Build | Maven |


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
   spring.jpa.hibernate.ddl-auto=update
   ```

3. **Run the application**
   ```bash
   ./mvnw spring-boot:run
   ```

   The API will be available at `http://localhost:8080`.


## REST API Reference

All endpoints except `/api/auth/**` require a `Bearer` JWT token in the `Authorization` header.

### Auth
| Method | Endpoint | Description |
|--------|----------|-------------|
| `POST` | `/api/auth/register` | Register a new user |
| `POST` | `/api/auth/login` | Login and receive a JWT |

### Accounts
| Method | Endpoint | Description |
|--------|----------|-------------|
| `POST` | `/api/accounts` | Open a new account |
| `GET` | `/api/accounts` | Get all accounts for the authenticated user |
| `GET` | `/api/accounts/{id}` | Get a specific account by ID |
| `DELETE` | `/api/accounts/{id}` | Close an account |

### Transactions
| Method | Endpoint | Description |
|--------|----------|-------------|
| `POST` | `/api/transactions/deposit` | Deposit funds into an account |
| `POST` | `/api/transactions/withdraw` | Withdraw funds from an account |
| `POST` | `/api/transactions/transfer` | Transfer funds between accounts |
| `GET` | `/api/transactions/account/{accountId}` | Get paginated transaction history |

### Loans
| Method | Endpoint | Description |
|--------|----------|-------------|
| `POST` | `/api/loans` | Apply for a loan |
| `PUT` | `/api/loans/{id}/approve` | Approve a loan |
| `PUT` | `/api/loans/{id}/reject` | Reject a loan |
| `POST` | `/api/loans/{id}/repay` | Make a loan repayment |
| `GET` | `/api/loans` | Get all loans for the authenticated user |


## Project Structure

```
src/main/java/com/unibank/bankingSystem/
├── controller/                  # REST controllers
│   ├── AccountController
│   ├── AuthController
│   ├── LoanController
│   └── TransactionController
├── dto/                         # Request and response objects
│   ├── AccountRequest / AccountResponse
│   ├── AuthResponse
│   ├── ErrorResponse
│   ├── LoanRequest / LoanResponse / LoanRepaymentRequest
│   ├── LoginRequest / RegisterRequest
│   └── TransactionRequest / TransactionResponse / TransferRequest
├── exception/                   # Custom exceptions and global handler
│   ├── BadRequestException
│   ├── DuplicateResourceException
│   ├── GlobalExceptionHandler
│   ├── InsufficientFundsException
│   ├── ResourceNotFoundException
│   └── UnauthorizedException
├── model/                       # JPA entities and enums
│   ├── Account, Loan, Transaction, User
│   └── AccountStatus, AccountType, LoanStatus, Role, TransType (enums)
├── repository/                  # Spring Data repositories
│   ├── AccountRepository
│   ├── LoanRepository
│   ├── TransactionRepository
│   └── UserRepository
├── security/                    # JWT and Spring Security config
│   ├── JwtAuthFilter
│   ├── JwtService
│   ├── SecurityConfig
│   └── UserDetailsServiceImpl
└── service/                     # Business logic
    ├── AccountService
    ├── AuthService
    ├── LoanService
    └── TransactionService
```


## License

This project is licensed under the MIT License. See [LICENSE](LICENSE) for details.
