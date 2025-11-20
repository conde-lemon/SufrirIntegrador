# Travel4U - Project Structure

## Root Directory Organization

```
demo (1)/
├── .amazonq/rules/memory-bank/     # AI assistant memory bank
├── amadeus_data/                   # Cached flight data from Amadeus API
├── demo/                          # Main Spring Boot application
├── logs/                          # Application log files
├── README.md                      # Project documentation
└── TravelReserva.sql             # Database schema
```

## Main Application Structure (`demo/`)

### Core Application Layout
```
demo/src/main/java/com/travel4u/demo/
├── config/                        # Configuration classes
├── controller/                    # Web controllers (MVC)
├── factory/                       # Factory pattern implementations
├── oferta/                        # Offer/promotion domain
├── reserva/                       # Reservation domain
├── scraper/                       # Web scraping services
├── security/                      # Security configuration
├── service/                       # Business logic services
├── servicio/                      # Service domain (flights, hotels)
├── usuario/                       # User domain
└── DemoApplication.java           # Spring Boot main class
```

## Domain-Driven Architecture

### Reservation Domain (`reserva/`)
- **Models**: `Reserva`, `Detalle_Reserva`, `Equipaje`, `Pago`, `Paquete`
- **Controllers**: `ReservaController` - handles booking workflow
- **Repositories**: JPA repositories for data access
- **Services**: `PagoService` - payment processing logic

### User Domain (`usuario/`)
- **Models**: `Usuario` - user entity with roles
- **Controllers**: User management and authentication
- **Repositories**: User data access layer
- **Services**: User business logic

### Service Domain (`servicio/`)
- **Models**: `Servicio` - travel services (flights, hotels, etc.)
- **Controllers**: Service search and display
- **Repositories**: Service data management
- **Services**: Service business logic and JasperReports integration

### Scraper Domain (`scraper/`)
- **Models**: Data models for scraped content
- **Controllers**: Scraping endpoints and testing
- **Services**: `AmadeusDataExtractorService` - API integration

## Configuration Layer (`config/`)

### Database Configuration
- `DatabaseConfig.java` - Primary database setup
- `DatabaseInitializer.java` - Data initialization
- `SafeDatabaseInitializer.java` - Safe startup procedures

### Security Configuration (`security/`)
- `WebSecurityConfig.java` - Spring Security setup
- `UserDetailsServiceImpl.java` - Custom user details service
- `CustomAuthenticationSuccessHandler.java` - Login success handling

## Controller Layer Architecture

### Main Controllers
- `AppController.java` - Home page and main navigation
- `ServiciosController.java` - Service search and display
- `ReservaController.java` - Booking workflow
- `PagoController.java` - Payment processing
- `AdminController.java` - Administrative functions

### Specialized Controllers
- `AmadeusController.java` - Amadeus API integration
- `ScraperController.java` - Web scraping operations
- `ReporteController.java` - Report generation
- `DiagnosticController.java` - System health checks

## Resource Organization (`src/main/resources/`)

### Templates (`templates/`)
```
templates/
├── ADMIN/                         # Admin panel templates
├── fragments/                     # Reusable template fragments
├── *.html                        # Page templates (Thymeleaf)
```

### Static Resources (`static/`)
```
static/
├── css/                          # Stylesheets
├── img/                          # Images and assets
└── js/                           # JavaScript files
```

### Configuration Files
- `application.properties` - Main configuration
- `application-postgres.properties` - PostgreSQL config
- `application-h2.properties` - H2 database config
- `logback-spring.xml` - Logging configuration

### Database Scripts
- `data.sql` - Initial data population
- `add_missing_services.sql` - Service data additions
- `schema.sql` - Database schema definitions

### Reports (`reports/`)
- `*.jrxml` - JasperReports templates for PDF generation

## Architectural Patterns

### MVC Pattern
- **Models**: JPA entities in domain packages
- **Views**: Thymeleaf templates with fragments
- **Controllers**: Spring MVC controllers handling HTTP requests

### Repository Pattern
- Interface-based repositories extending `JpaRepository`
- Custom query methods following Spring Data conventions
- Separation of data access from business logic

### Service Layer Pattern
- Business logic encapsulated in service classes
- Transaction management through Spring annotations
- Clear separation between controllers and data access

### Factory Pattern
- `DAOFactory.java` - Centralized repository creation
- Abstraction of data access object creation

## Data Flow Architecture

### Request Processing Flow
1. **HTTP Request** → Controller
2. **Controller** → Service Layer
3. **Service** → Repository Layer
4. **Repository** → Database
5. **Response** ← Template Engine (Thymeleaf)

### Security Flow
1. **Authentication** → Spring Security Filter Chain
2. **Authorization** → Role-based access control
3. **Session Management** → Spring Security Session

### Payment Flow
1. **Reservation Creation** → Database
2. **Payment Processing** → External Payment Gateway
3. **Confirmation** → Email Service + PDF Generation

## Integration Points

### External APIs
- **Amadeus API**: Flight data and booking
- **PayPal API**: Payment processing
- **Web Scraping**: Skyscanner data extraction

### Database Integration
- **PostgreSQL**: Production database
- **H2**: Development/testing database
- **JPA/Hibernate**: ORM layer

### Reporting Integration
- **JasperReports**: PDF report generation
- **Email Service**: Notification delivery