# Travel4U - Technology Stack

## Programming Languages
- **Java 21**: Primary backend language
- **HTML5**: Template markup with Thymeleaf
- **CSS3**: Styling with custom stylesheets
- **JavaScript (ES6+)**: Client-side interactivity
- **SQL**: Database queries and migrations

## Backend Framework
- **Spring Boot 3.5.6**: Core application framework
- **Spring Web**: RESTful web services and MVC
- **Spring Data JPA**: Database abstraction with Hibernate
- **Spring Security**: Authentication and authorization
- **Thymeleaf**: Server-side template engine
- **Thymeleaf Spring Security**: Security integration for templates

## Database
- **PostgreSQL**: Primary production database
  - Host: localhost:8180
  - Database: sufrirIntegrador
  - User: postgres
- **H2 Database**: In-memory database for development/testing
- **Hibernate**: ORM implementation
- **Flyway/Liquibase**: Database migrations (via db/migration/)

## Build System
- **Gradle 8.14.3**: Build automation
- **Gradle Wrapper**: Included for consistent builds
- Build file: `build.gradle`

## Key Dependencies

### Core Spring Boot
```gradle
spring-boot-starter-data-jpa
spring-boot-starter-web
spring-boot-starter-thymeleaf
spring-boot-starter-security
spring-boot-devtools (dev only)
```

### External APIs & Libraries
```gradle
com.amadeus:amadeus-java:8.1.0          # Flight search API
org.jsoup:jsoup:1.17.2                  # Web scraping
```

### Reporting
```gradle
net.sf.jasperreports:jasperreports:6.21.0
net.sf.jasperreports:jasperreports-fonts:6.21.0
```

### Utilities
```gradle
org.projectlombok:lombok                # Boilerplate reduction
org.thymeleaf.extras:thymeleaf-extras-springsecurity6
```

### Testing
```gradle
spring-boot-starter-test
spring-security-test
junit-platform-launcher
```

## Development Tools
- **Lombok**: Reduces boilerplate code (@Data, @Getter, @Setter, etc.)
- **Spring Boot DevTools**: Hot reload during development
- **Logback**: Logging framework (configured in logback-spring.xml)

## External Services

### Amadeus API
- **Purpose**: Flight search and booking data
- **Environment**: Test/Sandbox
- **Configuration**: API key and secret in application.properties
- **Usage**: Flight offer extraction and data enrichment

### Web Scraping
- **Target**: Skyscanner website
- **Library**: Jsoup
- **Purpose**: Extract real-time flight offers
- **Storage**: JSON files in amadeus_data/ directory

## Database Schema

### Main Tables
- `usuarios`: User accounts
- `proveedor`: Service providers
- `servicio`: Travel services (flights, cruises, buses)
- `reserva`: Booking records
- `detalle_reserva`: Reservation line items
- `pago`: Payment transactions
- `oferta`: Promotional offers
- `equipaje`: Baggage options
- `paquete`: Travel packages
- `reserva_equipaje`: Reservation-baggage junction
- `historial`: User search history

## Development Commands

### Build & Run
```bash
# Build project
./gradlew build

# Run application
./gradlew bootRun

# Run with H2 profile
./gradlew bootRun --args='--spring.profiles.active=h2'

# Run tests
./gradlew test

# Clean build
./gradlew clean build
```

### Database
```bash
# Run database tests
./gradlew test --tests DatabaseConnectionTest

# Execute SQL scripts manually via PostgreSQL client
psql -U postgres -d sufrirIntegrador -f data.sql
```

## Configuration Profiles

### Default (PostgreSQL)
- Database: PostgreSQL on localhost:8180
- Port: 8081
- DDL: update (preserves data)
- SQL init: always

### H2 Profile
- Database: In-memory H2
- Console: Enabled at /h2-console
- DDL: create-drop (fresh on each start)
- Port: 8081

## Logging Configuration
- **Framework**: Logback
- **Config**: logback-spring.xml
- **Log Location**: ./logs/travel4u.log
- **Rotation**: Daily, 30-day retention, 1GB max
- **Levels**: 
  - com.travel4u.demo: DEBUG
  - org.hibernate.SQL: DEBUG
  - Root: INFO

## Server Configuration
- **Port**: 8081
- **Context Path**: / (root)
- **Base URL**: http://localhost:8081

## Security Configuration
- **Password Encoding**: NoOpPasswordEncoder (DEVELOPMENT ONLY - insecure)
- **Session Management**: Default Spring Security
- **CSRF**: Enabled
- **Login**: Form-based at /login
- **Logout**: POST to /logout

## Report Templates
- **Format**: JRXML (JasperReports XML)
- **Location**: src/main/resources/reports/
- **Templates**:
  - boleta-reserva.jrxml: Booking receipt
  - Reservas.jrxml: User reservations report
  - Reporte-usuario.jrxml: User management report
  - Reporte-Promociones.jrxml: Promotions report

## Static Assets
- **CSS**: /static/css/ (component-specific stylesheets)
- **JavaScript**: /static/js/ (form validation, animations)
- **Images**: /static/img/ (logos, backgrounds)
- **Theme Color**: Purple (#7b1fa2)
