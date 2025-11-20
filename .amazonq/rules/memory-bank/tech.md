# Travel4U - Technology Stack

## Core Technologies

### Programming Language
- **Java 21** - Latest LTS version with modern language features
- **Language Level**: Java 21 toolchain configuration

### Framework Stack
- **Spring Boot 3.5.6** - Main application framework
- **Spring Web** - MVC web framework and REST API support
- **Spring Data JPA** - Data persistence with Hibernate ORM
- **Spring Security** - Authentication and authorization
- **Thymeleaf** - Server-side template engine for web pages

## Database Technologies

### Production Database
- **PostgreSQL** - Primary production database
  - Host: `localhost:8180`
  - Database: `sufrirIntegrador`
  - User: `postgres`
  - Hibernate Dialect: `PostgreSQLDialect`

### Development Database
- **H2 Database** - In-memory database for development and testing
- **JPA/Hibernate** - ORM with automatic DDL generation

### Database Configuration
- **DDL Auto**: `update` mode for schema management
- **SQL Initialization**: Enabled with continue-on-error
- **Connection Pooling**: Default HikariCP

## External Integrations

### APIs and Services
- **Amadeus API 8.1.0** - Flight search and booking integration
  - Environment: Test mode
  - API Key and Secret configured
- **PayPal Integration** - Payment processing gateway

### Web Scraping
- **Jsoup 1.17.2** - HTML parsing and web scraping
- **Node.js Integration** - External scraping scripts
- **Skyscanner Scraping** - Flight offer extraction

## Frontend Technologies

### Template Engine
- **Thymeleaf** - Server-side rendering with Spring integration
- **Thymeleaf Spring Security** - Security integration for templates

### Static Resources
- **CSS** - Custom stylesheets
- **JavaScript** - Client-side functionality
- **Bootstrap** - UI framework (implied from templates)

## Development Tools

### Build System
- **Gradle 8.14.3** - Build automation and dependency management
- **Gradle Wrapper** - Version-locked build tool

### Development Support
- **Spring Boot DevTools** - Hot reloading and development utilities
- **Lombok** - Code generation for boilerplate reduction

### Code Quality
- **Annotation Processing** - Compile-time code generation
- **Spring Boot Actuator** - Application monitoring (implied)

## Reporting and Documentation

### Report Generation
- **JasperReports 6.21.0** - PDF report generation
- **JasperReports Fonts 6.21.0** - Font support for reports

### Documentation
- **Markdown** - Documentation format
- **JavaDoc** - Code documentation (standard)

## Testing Framework

### Testing Stack
- **JUnit Platform** - Test execution platform
- **Spring Boot Test** - Integration testing support
- **Spring Security Test** - Security testing utilities
- **H2 Database** - Test database

## Logging and Monitoring

### Logging Framework
- **Logback** - Logging implementation
- **SLF4J** - Logging facade
- **Custom Log Configuration** - `logback-spring.xml`

### Log Levels
- **DEBUG** - Application-level debugging
- **SQL Logging** - Hibernate SQL statement logging
- **Parameter Binding** - SQL parameter tracing

## Server Configuration

### Application Server
- **Embedded Tomcat** - Default Spring Boot server
- **Port**: 8081 (custom configuration)

### Profile Management
- **Multiple Profiles** - postgres, h2 configurations
- **Environment-specific Properties** - Separate config files

## Security Technologies

### Authentication & Authorization
- **Spring Security 6** - Security framework
- **BCrypt** - Password hashing (standard)
- **Session Management** - HTTP session handling
- **CSRF Protection** - Cross-site request forgery prevention

### Security Configuration
- **Role-based Access Control** - Admin/User roles
- **Custom Authentication Success Handler**
- **UserDetailsService Implementation**

## Data Processing

### JSON Processing
- **Jackson** - JSON serialization/deserialization (Spring Boot default)
- **Amadeus Data** - JSON flight data caching

### Date/Time Handling
- **Java Time API** - Modern date/time processing
- **LocalDateTime**, **LocalDate** - Temporal data types

## Development Commands

### Build Commands
```bash
# Build project
./gradlew build

# Run application
./gradlew bootRun

# Run tests
./gradlew test

# Clean build
./gradlew clean build
```

### Database Commands
```bash
# Test database connection
test-database.bat

# Apply database fixes
# Execute fix_sequence.sql
```

## Deployment Configuration

### Environment Variables
- **Database Credentials** - Externalized for production
- **API Keys** - Amadeus credentials
- **Server Configuration** - Port and context settings

### Production Considerations
- **PostgreSQL** - Production database
- **Logging** - File-based logging with rotation
- **Security** - Production security configurations
- **Performance** - Connection pooling and caching

## Dependencies Overview

### Core Dependencies
```gradle
spring-boot-starter-data-jpa
spring-boot-starter-web
spring-boot-starter-thymeleaf
spring-boot-starter-security
```

### External Libraries
```gradle
amadeus-java:8.1.0
jsoup:1.17.2
jasperreports:6.21.0
```

### Database Drivers
```gradle
postgresql (runtime)
h2database (runtime)
```

### Development Tools
```gradle
lombok (compile-only)
spring-boot-devtools (development-only)
```

## Version Compatibility

### Java Compatibility
- **Minimum**: Java 21
- **Target**: Java 21 LTS
- **Toolchain**: Gradle Java 21 toolchain

### Spring Boot Compatibility
- **Version**: 3.5.6
- **Spring Framework**: 6.x (included)
- **Jakarta EE**: 9+ (Spring Boot 3.x requirement)