# Travel4U - Project Structure

## Root Directory Layout

```
demo (1)/
├── .amazonq/rules/memory-bank/     # Project documentation
├── amadeus_data/                   # Scraped flight offer JSON files
├── demo/                           # Main Spring Boot application
│   ├── src/main/java/              # Java source code
│   ├── src/main/resources/         # Configuration and templates
│   ├── src/test/java/              # Test classes
│   ├── build.gradle                # Gradle build configuration
│   └── gradlew, gradlew.bat        # Gradle wrapper scripts
├── logs/                           # Application log files
└── README.md                       # Project documentation
```

## Java Package Structure

```
com.travel4u.demo/
├── controller/                     # Web controllers (MVC)
│   ├── HomeController              # Homepage and dashboard
│   ├── ServiciosController         # Service search/listing
│   ├── ReservaController           # Reservation workflow
│   ├── PagoController              # Payment processing
│   ├── AdminController             # Admin panel
│   └── ReporteController           # Report generation
├── model/                          # JPA entities
│   ├── Usuario                     # User accounts
│   ├── Servicio                    # Travel services
│   ├── Proveedor                   # Service providers
│   ├── Reserva                     # Bookings
│   ├── DetalleReserva              # Booking details
│   ├── Pago                        # Payments
│   ├── Oferta                      # Promotional offers
│   ├── Equipaje                    # Baggage options
│   └── Paquete                     # Travel packages
├── repository/                     # Data access layer
│   ├── UsuarioRepository
│   ├── ServicioRepository
│   ├── ReservaRepository
│   ├── PagoRepository
│   └── OfertaRepository
├── service/                        # Business logic
│   ├── ReservaService
│   ├── PagoService
│   ├── OfertaService
│   └── JasperReportService
├── scraper/                        # Web scraping
│   ├── service/
│   │   ├── ScrapingService         # Skyscanner scraper
│   │   └── AmadeusDataExtractorService
│   └── model/                      # Scraper DTOs
├── security/                       # Security configuration
│   ├── WebSecurityConfig           # Spring Security setup
│   └── CustomUserDetailsService    # User authentication
└── DemoApplication.java            # Application entry point
```

## Resources Structure

```
src/main/resources/
├── templates/                      # Thymeleaf HTML templates
│   ├── fragments/                  # Reusable template fragments
│   │   ├── header.html             # Common header
│   │   └── reserva-card.html       # Reservation card component
│   ├── ADMIN/                      # Admin-specific templates
│   │   └── admin.html              # Admin dashboard
│   ├── index.html                  # Homepage
│   ├── login.html                  # Login page
│   ├── registrar.html              # Registration page
│   ├── servicios-resultados.html   # Service search results
│   ├── asientos.html               # Seat selection
│   ├── pago.html                   # Payment form
│   ├── pasarela-paypal.html        # PayPal gateway
│   ├── confirmacion-pago.html      # Payment confirmation
│   ├── reservas.html               # User reservations list
│   └── reserva-detalle.html        # Reservation details
├── static/
│   ├── css/                        # Stylesheets
│   │   ├── header_style.css        # Header component
│   │   ├── index1_style.css        # Homepage styles
│   │   ├── reservas_style.css      # Reservations styles
│   │   ├── asientos_style.css      # Seat selection
│   │   ├── pago_style.css          # Payment form
│   │   └── confirmacion-pago.css   # Confirmation page
│   ├── js/                         # JavaScript files
│   │   ├── js.js                   # Common utilities
│   │   ├── asientos.js             # Seat selection logic
│   │   ├── pago.js                 # Payment form validation
│   │   └── confirmacion-pago.js    # Confirmation animations
│   └── img/                        # Images and icons
├── reports/                        # JasperReports templates
│   ├── boleta-reserva.jrxml        # Booking receipt
│   ├── Reservas.jrxml              # User reservations
│   ├── Reporte-usuario.jrxml       # User report
│   └── Reporte-Promociones.jrxml   # Promotions report
├── db/migration/                   # Database migrations
│   ├── V2__add_missing_tables.sql
│   ├── V3__add_more_services_and_reservations.sql
│   └── V4__add_more_service_types.sql
├── data.sql                        # Initial data seed
├── data-h2.sql                     # H2-specific seed data
├── application.properties          # Main configuration
├── application-postgres.properties # PostgreSQL profile
├── application-h2.properties       # H2 profile
└── logback-spring.xml              # Logging configuration
```

## Component Relationships

### Reservation Flow
```
User → ServiciosController → Service Selection
    → ReservaController → Seat Selection (asientos.html)
    → PagoController → Payment Gateway (pasarela-paypal.html)
    → PagoService → Payment Processing
    → Confirmation (confirmacion-pago.html)
```

### Authentication Flow
```
Login Request → WebSecurityConfig
    → CustomUserDetailsService → UsuarioRepository
    → Authentication Success → Redirect to Homepage
```

### Report Generation Flow
```
Admin Request → ReporteController
    → JasperReportService → Database Query
    → JasperReports Engine → PDF Generation
    → HTTP Response (application/pdf)
```

### Scraping Flow
```
Scheduled Task → ScrapingService
    → Jsoup HTTP Request → Skyscanner
    → Parse HTML → Extract Offers
    → OfertaService → Save to Database
```

## Data Flow Architecture

### Read Operations
```
Controller → Service → Repository → Database
    → Entity Mapping → Return to Service
    → Business Logic → Return to Controller
    → Model Attributes → Thymeleaf Rendering
```

### Write Operations
```
Form Submission → Controller Validation
    → Service Business Logic → Repository Save
    → Database Transaction → Confirmation Response
```

## Module Dependencies

### Core Dependencies
- Spring Boot Starters → Spring Framework
- Hibernate → Database ORM
- PostgreSQL Driver → Database connectivity

### Feature Dependencies
- Jsoup → Web scraping capability
- Amadeus SDK → Flight data integration
- JasperReports → PDF generation
- Thymeleaf Security → Template authorization

## Configuration Hierarchy
1. application.properties (base)
2. application-{profile}.properties (profile-specific)
3. Environment variables (override)
4. Command-line arguments (highest priority)
