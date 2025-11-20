# Travel4U - Development Guidelines

## Code Quality Standards

### Package Organization
- **Domain-Driven Structure**: Organize code by business domains (`reserva`, `usuario`, `servicio`, `scraper`)
- **Layer Separation**: Each domain contains `model`, `controller`, `repository`, and `service` packages
- **Configuration Isolation**: Keep configuration classes in dedicated `config` package
- **Security Centralization**: All security-related classes in `security` package

### Naming Conventions
- **Classes**: PascalCase with descriptive names (`AmadeusDataExtractorService`, `ReservaController`)
- **Methods**: camelCase with action-oriented names (`extraerDatosParaPromociones`, `procesarPagoPayPal`)
- **Variables**: camelCase with meaningful names (`reservaGuardada`, `precioTotal`)
- **Constants**: UPPER_SNAKE_CASE (`UPPER_SNAKE_CASE` for static final fields)
- **Database Fields**: snake_case in annotations (`@Column(name = "precio_unitario")`)

### Entity and Model Patterns
- **JPA Annotations**: Use `@Entity`, `@Table(name="table_name")` for all entities
- **Primary Keys**: Use `@Id @GeneratedValue(strategy=GenerationType.IDENTITY)` pattern
- **Lombok Integration**: Consistent use of `@Getter`, `@Setter`, `@ToString`, `@AllArgsConstructor`, `@NoArgsConstructor`
- **BigDecimal for Money**: Always use `BigDecimal` for monetary values, never `float` or `double`
- **Audit Fields**: Include `@CreationTimestamp` and `@UpdateTimestamp` for tracking
- **Relationship Mapping**: Use `@ManyToOne(fetch = FetchType.LAZY)` for performance

### Repository Layer Standards
- **Interface Naming**: Prefix with `I` and suffix with `DAO` (`IReservaDAO`, `IUsuarioDAO`)
- **JpaRepository Extension**: All repositories extend `JpaRepository<Entity, IdType>`
- **Custom Queries**: Use Spring Data method naming conventions (`findByUsuarioOrderByCreatedAtDesc`)
- **Repository Annotation**: Always annotate with `@Repository`

### Service Layer Patterns
- **Constructor Injection**: Use constructor-based dependency injection consistently
- **Service Annotation**: Always annotate with `@Service`
- **Logging Integration**: Include SLF4J logger in all service classes
- **Transaction Management**: Implicit through Spring Boot auto-configuration
- **Error Handling**: Use try-catch blocks with proper logging

### Controller Architecture
- **Annotation Usage**: Use `@Controller` for MVC, `@RestController` for API endpoints
- **Request Mapping**: Use `@RequestMapping` at class level, specific mappings at method level
- **Parameter Binding**: Use `@RequestParam`, `@PathVariable`, `@AuthenticationPrincipal` appropriately
- **Response Handling**: Return view names for MVC, `ResponseEntity` for REST APIs
- **Error Responses**: Use `ResponseStatusException` for HTTP error handling

## Structural Conventions

### Database Integration Patterns
- **Multiple Database Support**: Configuration for both PostgreSQL (production) and H2 (development)
- **Connection Configuration**: Externalized database credentials in `application.properties`
- **Schema Management**: Use `spring.jpa.hibernate.ddl-auto=update` for development
- **Data Initialization**: Use `data.sql` files for initial data population

### Security Implementation
- **Spring Security Integration**: Use `@EnableWebSecurity` and `@EnableMethodSecurity`
- **Role-Based Access**: Implement `hasRole("ADMIN")` for administrative functions
- **Authentication Flow**: Custom success handler for post-login routing
- **Password Encoding**: Currently using `NoOpPasswordEncoder` (development only)
- **CSRF Protection**: Disabled for API endpoints, enabled for form submissions

### External API Integration
- **Amadeus API**: Use constructor injection for `Amadeus` client
- **Error Handling**: Implement fallback mechanisms (simulated data) when APIs fail
- **Data Caching**: Store API responses in local files for debugging and backup
- **Rate Limiting**: Include `Thread.sleep()` to respect API rate limits

### Logging Standards
- **Logger Declaration**: `private static final Logger logger = LoggerFactory.getLogger(ClassName.class)`
- **Log Levels**: Use `INFO` for business operations, `DEBUG` for detailed tracing, `ERROR` for exceptions
- **Structured Logging**: Include relevant context (IDs, user info) in log messages
- **Exception Logging**: Always log stack traces for unexpected errors

## Semantic Patterns Overview

### Dependency Injection Patterns
- **Constructor Injection**: Preferred method for all dependencies
- **Final Fields**: Mark injected dependencies as `final`
- **No Field Injection**: Avoid `@Autowired` on fields, use constructor injection

### Data Access Patterns
- **Repository Pattern**: Interface-based repositories with Spring Data JPA
- **Optional Handling**: Use `Optional<T>` return types and `.orElseThrow()` for error handling
- **Query Methods**: Leverage Spring Data naming conventions for automatic query generation
- **Pagination Support**: Include `Pageable` parameters where appropriate

### Error Handling Patterns
- **Exception Translation**: Convert checked exceptions to runtime exceptions with context
- **HTTP Status Codes**: Use appropriate status codes (`404` for not found, `403` for forbidden)
- **User-Friendly Messages**: Provide meaningful error messages for end users
- **Logging Before Throwing**: Log errors before throwing exceptions

### Business Logic Patterns
- **Service Layer Encapsulation**: Keep business logic in service classes, not controllers
- **Validation Logic**: Implement validation at service layer and controller parameter level
- **Transaction Boundaries**: Service methods define transaction boundaries
- **Domain Model Integrity**: Maintain business rules within entity classes where appropriate

## Frequently Used Annotations

### Spring Framework Annotations
- `@Service` - Service layer components (5/5 files)
- `@Controller` - MVC controllers (4/5 files)  
- `@RestController` - REST API controllers (2/5 files)
- `@RequestMapping` - URL mapping (4/5 files)
- `@Autowired` - Dependency injection (2/5 files, prefer constructor injection)

### JPA/Hibernate Annotations
- `@Entity` - JPA entities (all model classes)
- `@Table(name="table_name")` - Table mapping (all entities)
- `@Id` - Primary key identification (all entities)
- `@GeneratedValue(strategy=GenerationType.IDENTITY)` - Auto-generated IDs (all entities)
- `@Column` - Column mapping with specific names and constraints
- `@ManyToOne(fetch = FetchType.LAZY)` - Relationship mapping (performance-oriented)
- `@JoinColumn` - Foreign key specification

### Lombok Annotations
- `@Getter` / `@Setter` - Property accessors (all entities)
- `@ToString` - String representation (all entities)
- `@AllArgsConstructor` / `@NoArgsConstructor` - Constructor generation (all entities)
- `@Data` - Combined getter/setter/toString/equals/hashCode (some entities)

### Security Annotations
- `@EnableWebSecurity` - Security configuration
- `@EnableMethodSecurity` - Method-level security
- `@AuthenticationPrincipal` - Current user injection

### Validation and Configuration
- `@Configuration` - Configuration classes
- `@Bean` - Bean definition methods
- `@RequestParam` - Request parameter binding
- `@PathVariable` - URL path variable binding

## Code Quality Practices

### Performance Considerations
- **Lazy Loading**: Use `FetchType.LAZY` for entity relationships
- **Connection Pooling**: Rely on HikariCP default configuration
- **Query Optimization**: Use appropriate fetch strategies and avoid N+1 queries
- **Caching Strategy**: Implement file-based caching for external API responses

### Security Best Practices
- **Input Validation**: Validate all user inputs at controller and service levels
- **SQL Injection Prevention**: Use parameterized queries through JPA
- **Authentication Checks**: Verify user authentication before processing requests
- **Authorization Enforcement**: Check user roles for administrative functions

### Maintainability Standards
- **Single Responsibility**: Each class has a clear, single purpose
- **Dependency Inversion**: Depend on interfaces, not concrete implementations
- **Configuration Externalization**: Keep environment-specific settings in properties files
- **Documentation**: Include JavaDoc for public methods and complex business logic

### Testing Considerations
- **Test Database**: Use H2 for testing, PostgreSQL for production
- **Mock External Dependencies**: Mock external APIs and services in tests
- **Integration Testing**: Test complete request-response cycles
- **Security Testing**: Include authentication and authorization tests