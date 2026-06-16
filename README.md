"# Bank_Management_System" 
# 🏦 Bank Management System

A production-grade **REST API backend** for core banking operations, built with **Spring Boot 3**, **Spring Security**, **JWT Authentication**, and **MySQL**. Supports account management, deposits, withdrawals, fund transfers, and transaction history — all secured with stateless JWT and role-based access control.

---

## 🚀 Tech Stack

| Layer | Technology |
|---|---|
| Language | Java 17 |
| Framework | Spring Boot 3.3.2 |
| Security | Spring Security + JWT (jjwt 0.11.5) |
| ORM | Spring Data JPA + Hibernate |
| Database | MySQL |
| Validation | Spring Boot Starter Validation (Bean Validation) |
| Build Tool | Maven |
| Utilities | Lombok |

---

## ✨ Features

- **JWT Authentication** — Stateless token-based login; every protected endpoint requires a valid Bearer token
- **Role-Based Access Control (RBAC)** — `ADMIN` and `CUSTOMER` roles with distinct permissions on each endpoint
- **Account Management** — Create, view, and manage bank accounts
- **Transactions** — Deposit, withdraw, and transfer funds between accounts
- **Transaction History** — Retrieve full or filtered transaction logs per account
- **Bean Validation** — All request DTOs validated at the controller layer
- **Global Exception Handling** — Consistent, structured JSON error responses across all failure cases
- **Layered Architecture** — Clean separation of Controller → Service → Repository layers

---

## 📁 Project Structure

```
src/main/java/org/example/bank_management/
├── controller/        # REST API endpoints
├── service/           # Business logic
├── repository/        # Spring Data JPA interfaces
├── entity/            # JPA entity classes
├── dto/               # Request and Response DTOs
├── security/          # JWT filter, provider, Spring Security config
└── exception/         # Global exception handler and custom exceptions
```

---

## 🔐 API Endpoints

### Auth
| Method | Endpoint | Access | Description |
|---|---|---|---|
| POST | `/api/auth/register` | Public | Register a new user/customer |
| POST | `/api/auth/login` | Public | Authenticate and receive JWT token |

### Account
| Method | Endpoint | Access | Description |
|---|---|---|---|
| POST | `/api/accounts` | ADMIN | Create a new bank account |
| GET | `/api/accounts/{id}` | ADMIN / CUSTOMER (own) | Get account details |
| GET | `/api/accounts` | ADMIN | List all accounts |

### Transactions
| Method | Endpoint | Access | Description |
|---|---|---|---|
| POST | `/api/transactions/deposit` | CUSTOMER | Deposit funds into account |
| POST | `/api/transactions/withdraw` | CUSTOMER | Withdraw funds from account |
| POST | `/api/transactions/transfer` | CUSTOMER | Transfer funds between accounts |
| GET | `/api/transactions/{accountId}` | ADMIN / CUSTOMER (own) | Get transaction history |

---

## ⚙️ Getting Started

### Prerequisites
- Java 17+
- Maven 3.8+
- MySQL 8+

### Setup

1. **Clone the repository**
   ```bash
   git clone https://github.com/thiruselvam880-oss/Bank_Management_System.git
   cd Bank_Management_System
   ```

2. **Create the MySQL database**
   ```sql
   CREATE DATABASE bank_management_db;
   ```

3. **Configure `application.properties`**
   ```properties
   spring.datasource.url=jdbc:mysql://localhost:3306/bank_management_db
   spring.datasource.username=your_mysql_username
   spring.datasource.password=your_mysql_password

   spring.jpa.hibernate.ddl-auto=update
   spring.jpa.show-sql=true

   app.jwt.secret=your_jwt_secret_key_here
   app.jwt.expiration=86400000
   ```

4. **Build and run**
   ```bash
   mvn clean install
   mvn spring-boot:run
   ```

   The API will start at `http://localhost:8080`

---

## 🧪 Testing with Postman

1. **Register** a user via `POST /api/auth/register`
2. **Login** via `POST /api/auth/login` — copy the returned JWT token
3. Add the token to the **Authorization** header as:
   ```
   Authorization: Bearer <your_token>
   ```
4. Call any protected endpoint

---

## 🗄️ Database Schema (auto-created by Hibernate)

**`users`** — stores registered users and their roles  
**`accounts`** — bank accounts linked to users (account number, balance, type)  
**`transactions`** — full history of deposits, withdrawals, and transfers

---

## 📌 Key Design Decisions

- **Stateless JWT** — No server-side session storage; each request is self-authenticated
- **DTO Pattern** — Entities never exposed directly; separate request/response DTOs used throughout
- **PreparedStatement-equivalent safety** — Spring Data JPA parameterized queries prevent SQL injection
- **RBAC on every endpoint** — Method-level and URL-level security config ensures no endpoint is accidentally left open

---

## 👨‍💻 Author

**Thiru Selvam T**  
[![LinkedIn](https://img.shields.io/badge/LinkedIn-Connect-blue?logo=linkedin)](https://www.linkedin.com/in/thiru-selvam-999826258)
[![GitHub](https://img.shields.io/badge/GitHub-Profile-black?logo=github)](https://github.com/thiruselvam880-oss)

---

## 📄 License

This project is open source and available under the [MIT License](LICENSE).
