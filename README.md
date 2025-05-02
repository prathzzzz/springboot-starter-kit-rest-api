# Spring Boot REST API Starter Kit

A clean, modular Spring Boot REST API starter kit that follows Object-Oriented Programming principles and best practices. This project serves as a foundation for building robust, maintainable, and scalable RESTful APIs.

## Features

- **Layered Architecture**
  - Controller layer for handling HTTP requests
  - Service layer for business logic
  - Repository layer for data access
  - DTOs with ModelMapper for clean data transfer

- **Security**
  - Spring Security integration
  - JWT-based authentication
  - Role-based access control

- **Data Management**
  - Spring Data JPA for database operations
  - MySQL database support
  - Data validation using Spring Validation

- **Best Practices**
  - SOLID principles implementation
  - Clean code architecture
  - Meaningful naming conventions
  - Separation of concerns
  - Centralized exception handling
  - Standardized API responses
  - Integrated logging with SLF4J

## Tech Stack

- Java 17
- Spring Boot 3.4.5
- Spring Security
- Spring Data JPA
- MySQL
- JWT (JSON Web Tokens)
- ModelMapper
- Lombok
- JUnit 5
- Mockito
- AssertJ

## Prerequisites

- Java 17 or higher
- Maven
- MySQL 8.0 or higher

## Getting Started

1. **Clone the repository**
   ```bash
   git clone [repository-url]
   cd spring-starter-kit
   ```

2. **Configure the database**
   - Create a MySQL database
   - Update the `.env` file with your database credentials:
     ```
     DB_URL=jdbc:mysql://localhost:3306/your_database
     DB_USERNAME=your_username
     DB_PASSWORD=your_password
     JWT_SECRET=your_jwt_secret
     ```

3. **Build the project**
   ```bash
   mvn clean install
   ```

4. **Run the application**
   ```bash
   mvn spring-boot:run
   ```

The application will start on `http://localhost:8080`

## Project Structure

```
src/
├── main/
│   ├── java/
│   │   └── com/url/
│   │       ├── config/         # Configuration classes
│   │       ├── controller/     # REST controllers
│   │       ├── dto/            # Data Transfer Objects
│   │       ├── exception/      # Exception handling
│   │       ├── model/          # Entity models
│   │       ├── repository/     # JPA repositories
│   │       ├── security/       # Security configuration
│   │       ├── service/        # Business logic
│   │       └── util/           # Utility classes
│   └── resources/
│       ├── application.yml     # Application configuration
│       └── logback-spring.xml  # Logging configuration
```

## API Documentation

The API documentation will be available at:
- Swagger UI: `http://localhost:8080/swagger-ui.html`
- OpenAPI JSON: `http://localhost:8080/v3/api-docs`

## Testing

Run the test suite:
```bash
mvn test
```

## Logging

Logs are configured using SLF4J and can be found in the `logs` directory. The logging configuration is defined in `logback-spring.xml`.

## Contributing

1. Fork the repository
2. Create your feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add some amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

## License

This project is licensed under the MIT License - see the LICENSE file for details.

## Acknowledgments

- Spring Boot Team
- Spring Security Team
- All contributors and maintainers 