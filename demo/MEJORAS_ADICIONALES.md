# 🔍 PROBLEMAS ADICIONALES Y MEJORAS RECOMENDADAS

## 🔐 Seguridad en application.properties

### ❌ Problema: Credenciales Expuestas

**Archivo:** `src/main/resources/application.properties`

**Problema:**
```properties
spring.datasource.username=postgres
spring.datasource.password=Abc1234
```

Las credenciales de la base de datos están **hardcodeadas** en el código fuente.
Esto es un **RIESGO DE SEGURIDAD** si el código se sube a GitHub o se comparte.

### ✅ Solución: Variables de Entorno

#### Opción 1: Variables de Entorno

**Modificar application.properties:**
```properties
spring.datasource.url=${DB_URL:jdbc:postgresql://localhost:5432/SistemaReservas}
spring.datasource.username=${DB_USERNAME:postgres}
spring.datasource.password=${DB_PASSWORD:}
```

**Configurar variables en el sistema:**
```bash
# Windows (cmd.exe)
set DB_USERNAME=postgres
set DB_PASSWORD=Abc1234

# Windows (PowerShell)
$env:DB_USERNAME="postgres"
$env:DB_PASSWORD="Abc1234"

# Linux/Mac
export DB_USERNAME=postgres
export DB_PASSWORD=Abc1234
```

#### Opción 2: Archivo de Configuración Externo

**Crear:** `application-local.properties` (NO subir a Git)

```properties
# application-local.properties
spring.datasource.username=postgres
spring.datasource.password=Abc1234
```

**Agregar a .gitignore:**
```
application-local.properties
application-prod.properties
```

**Ejecutar con:**
```bash
java -jar demo.jar --spring.profiles.active=local
```

#### Opción 3: Spring Cloud Config Server

Para proyectos más grandes, usar un servidor de configuración centralizado.

---

## 📊 Configuración de Producción vs Desarrollo

### Crear Perfiles de Spring

**application.properties** (configuración base):
```properties
spring.application.name=demo
server.port=8081
spring.main.allow-bean-definition-overriding=true
```

**application-dev.properties** (desarrollo):
```properties
# Desarrollo - PostgreSQL Local
spring.datasource.url=jdbc:postgresql://localhost:5432/SistemaReservas
spring.datasource.username=${DB_USERNAME:postgres}
spring.datasource.password=${DB_PASSWORD:Abc1234}

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true

# Logging detallado
logging.level.root=INFO
logging.level.com.travel4u=DEBUG
logging.level.org.hibernate.SQL=DEBUG
```

**application-prod.properties** (producción):
```properties
# Producción - Supabase o servidor remoto
spring.datasource.url=${DB_URL}
spring.datasource.username=${DB_USERNAME}
spring.datasource.password=${DB_PASSWORD}

spring.jpa.hibernate.ddl-auto=validate
spring.jpa.show-sql=false
spring.jpa.properties.hibernate.format_sql=false

# Pool de conexiones optimizado
spring.datasource.hikari.maximum-pool-size=10
spring.datasource.hikari.minimum-idle=5
spring.datasource.hikari.connection-timeout=20000

# Logging productivo
logging.level.root=WARN
logging.level.com.travel4u=INFO
```

**Ejecutar según ambiente:**
```bash
# Desarrollo
./gradlew bootRun --args='--spring.profiles.active=dev'

# Producción
java -jar demo.jar --spring.profiles.active=prod
```

---

## 🗄️ Optimizaciones de Base de Datos

### 1. Pool de Conexiones (HikariCP)

Agregar a `application.properties`:

```properties
# HikariCP - Pool de conexiones
spring.datasource.hikari.maximum-pool-size=10
spring.datasource.hikari.minimum-idle=5
spring.datasource.hikari.idle-timeout=300000
spring.datasource.hikari.max-lifetime=600000
spring.datasource.hikari.connection-timeout=20000
spring.datasource.hikari.leak-detection-threshold=60000
```

### 2. Configuración de Hibernate

```properties
# Hibernate optimizaciones
spring.jpa.properties.hibernate.jdbc.batch_size=20
spring.jpa.properties.hibernate.order_inserts=true
spring.jpa.properties.hibernate.order_updates=true
spring.jpa.properties.hibernate.jdbc.batch_versioned_data=true

# Cache de segundo nivel (opcional)
spring.jpa.properties.hibernate.cache.use_second_level_cache=false
```

---

## 📝 Configuración de Logging

### Crear logback-spring.xml

**Archivo:** `src/main/resources/logback-spring.xml`

```xml
<?xml version="1.0" encoding="UTF-8"?>
<configuration>
    <!-- Console Appender -->
    <appender name="CONSOLE" class="ch.qos.logback.core.ConsoleAppender">
        <encoder>
            <pattern>%d{yyyy-MM-dd HH:mm:ss} - %msg%n</pattern>
        </encoder>
    </appender>

    <!-- File Appender -->
    <appender name="FILE" class="ch.qos.logback.core.rolling.RollingFileAppender">
        <file>logs/travel4u.log</file>
        <rollingPolicy class="ch.qos.logback.core.rolling.TimeBasedRollingPolicy">
            <fileNamePattern>logs/travel4u.%d{yyyy-MM-dd}.log</fileNamePattern>
            <maxHistory>30</maxHistory>
        </rollingPolicy>
        <encoder>
            <pattern>%d{yyyy-MM-dd HH:mm:ss} [%thread] %-5level %logger{36} - %msg%n</pattern>
        </encoder>
    </appender>

    <!-- Root Logger -->
    <root level="INFO">
        <appender-ref ref="CONSOLE" />
        <appender-ref ref="FILE" />
    </root>

    <!-- Logger específico para tu aplicación -->
    <logger name="com.travel4u" level="DEBUG" additivity="false">
        <appender-ref ref="CONSOLE" />
        <appender-ref ref="FILE" />
    </logger>
</configuration>
```

### Agregar dependencia en build.gradle

```groovy
dependencies {
    // ...existing dependencies...
    
    // Logging
    implementation 'org.springframework.boot:spring-boot-starter-logging'
}
```

---

## 🔒 Configuración de CORS Mejorada

### Clase de Configuración Global

**Crear:** `src/main/java/com/travel4u/demo/config/WebConfig.java`

```java
package com.travel4u.demo.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**")
                .allowedOrigins("http://localhost:8081", "http://localhost:3000")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(true)
                .maxAge(3600);
    }
}
```

**Beneficio:** Elimina la necesidad de `@CrossOrigin` en cada controlador.

---

## 🛡️ Configuración de Actuator (Monitoreo)

### 1. Agregar dependencia

**build.gradle:**
```groovy
dependencies {
    // Actuator para monitoreo
    implementation 'org.springframework.boot:spring-boot-starter-actuator'
}
```

### 2. Configurar endpoints

**application.properties:**
```properties
# Actuator
management.endpoints.web.exposure.include=health,info,metrics
management.endpoint.health.show-details=when-authorized
management.metrics.enable.jvm=true
```

### 3. Proteger con Security

**WebSecurityConfig.java:**
```java
.authorizeHttpRequests(auth -> auth
    // ...existing rules...
    .requestMatchers("/actuator/**").hasRole("ADMIN")
    // ...rest of rules...
)
```

---

## 📦 Validación de Datos

### 1. Agregar dependencia

**build.gradle:**
```groovy
dependencies {
    // Validación
    implementation 'org.springframework.boot:spring-boot-starter-validation'
}
```

### 2. Actualizar modelo Usuario

**Usuario.java:**
```java
import jakarta.validation.constraints.*;

@Entity
@Data
@Table(name="usuarios")
public class Usuario {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idUsuario;

    @NotBlank(message = "El nombre es obligatorio")
    @Size(min = 2, max = 50, message = "El nombre debe tener entre 2 y 50 caracteres")
    private String nombres;

    @NotBlank(message = "Los apellidos son obligatorios")
    @Size(min = 2, max = 50, message = "Los apellidos deben tener entre 2 y 50 caracteres")
    private String apellidos;

    @NotBlank(message = "El email es obligatorio")
    @Email(message = "Email inválido")
    private String email;

    @NotBlank(message = "La contraseña es obligatoria")
    @Size(min = 4, message = "La contraseña debe tener al menos 4 caracteres")
    private String password;

    @Pattern(regexp = "^\\+?[0-9]{9,15}$", message = "Teléfono inválido")
    private String telefono;

    @NotBlank
    private String rol;

    // ...rest of fields...
}
```

### 3. Usar @Valid en controladores

**AppController.java:**
```java
@PostMapping("/registrar")
public String processRegistration(@Valid Usuario usuario, 
                                   BindingResult result,
                                   RedirectAttributes redirectAttributes) {
    if (result.hasErrors()) {
        // Manejar errores de validación
        redirectAttributes.addFlashAttribute("errors", result.getAllErrors());
        return "redirect:/registrar";
    }
    
    // ...rest of code...
}
```

---

## 🔧 Manejo Global de Excepciones

### Crear GlobalExceptionHandler

**Archivo:** `src/main/java/com/travel4u/demo/exception/GlobalExceptionHandler.java`

```java
package com.travel4u.demo.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ModelAndView handleGenericException(Exception ex) {
        ModelAndView mav = new ModelAndView("error");
        mav.addObject("error", "Ha ocurrido un error: " + ex.getMessage());
        return mav;
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ModelAndView handleAccessDenied(AccessDeniedException ex) {
        ModelAndView mav = new ModelAndView("error");
        mav.addObject("error", "Acceso denegado. No tienes permisos.");
        return mav;
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, Object>> handleIllegalArgument(IllegalArgumentException ex) {
        Map<String, Object> response = new HashMap<>();
        response.put("success", false);
        response.put("message", ex.getMessage());
        return ResponseEntity.badRequest().body(response);
    }
}
```

---

## 📄 .gitignore Mejorado

**Crear/Actualizar:** `.gitignore`

```gitignore
# Compiled class files
*.class
build/
target/
out/
bin/

# Log files
*.log
logs/

# Package files
*.jar
*.war
*.nar
*.ear

# Gradle
.gradle/
gradle-app.setting
!gradle-wrapper.jar

# IDE
.idea/
*.iml
*.iws
.vscode/
.settings/
.classpath
.project

# OS
.DS_Store
Thumbs.db

# Configuración local (IMPORTANTE)
application-local.properties
application-prod.properties
application-dev.properties

# Contraseñas y secretos
*.env
.env.local
secrets.yml

# Archivos temporales
tmp/
temp/
*.tmp
*.swp
*~
```

---

## 🚀 Script de Inicio

### Crear start.bat (Windows)

**Archivo:** `start.bat`

```batch
@echo off
echo ========================================
echo    TRAVEL4U - Sistema de Reservas
echo ========================================
echo.
echo Iniciando aplicacion...
echo.

REM Configurar variables de entorno
set DB_USERNAME=postgres
set DB_PASSWORD=Abc1234
set SPRING_PROFILES_ACTIVE=dev

REM Ejecutar aplicación
gradlew.bat bootRun

pause
```

### Crear start.sh (Linux/Mac)

**Archivo:** `start.sh`

```bash
#!/bin/bash

echo "========================================"
echo "   TRAVEL4U - Sistema de Reservas"
echo "========================================"
echo ""
echo "Iniciando aplicación..."
echo ""

# Configurar variables de entorno
export DB_USERNAME=postgres
export DB_PASSWORD=Abc1234
export SPRING_PROFILES_ACTIVE=dev

# Ejecutar aplicación
./gradlew bootRun
```

---

## 📊 Métricas y Monitoreo

### Agregar Micrometer (opcional)

**build.gradle:**
```groovy
dependencies {
    // Métricas
    implementation 'io.micrometer:micrometer-registry-prometheus'
}
```

**application.properties:**
```properties
# Prometheus
management.metrics.export.prometheus.enabled=true
management.endpoints.web.exposure.include=health,info,metrics,prometheus
```

---

## ✅ CHECKLIST FINAL DE IMPLEMENTACIÓN

### Seguridad
- [ ] Implementar BCrypt (ver INSTRUCCIONES_BCRYPT.md)
- [ ] Mover credenciales a variables de entorno
- [ ] Configurar HTTPS en producción
- [ ] Revisar permisos de endpoints
- [ ] Agregar rate limiting (opcional)

### Configuración
- [ ] Crear perfiles dev/prod
- [ ] Configurar pool de conexiones
- [ ] Implementar logging estructurado
- [ ] Agregar .gitignore completo

### Código
- [ ] Eliminar repositorio duplicado
- [ ] Agregar validaciones con @Valid
- [ ] Implementar GlobalExceptionHandler
- [ ] Reemplazar System.out con Logger
- [ ] Configurar CORS globalmente

### Testing
- [ ] Agregar tests unitarios
- [ ] Tests de integración
- [ ] Tests de seguridad
- [ ] Pruebas de carga (opcional)

### Documentación
- [ ] README con instrucciones de instalación
- [ ] Documentar API con Swagger
- [ ] Diagramas de arquitectura
- [ ] Manual de despliegue

---

## 📚 Recursos Adicionales

- [Spring Boot Best Practices](https://www.baeldung.com/spring-boot-best-practices)
- [Spring Security Reference](https://docs.spring.io/spring-security/reference/index.html)
- [Twelve-Factor App](https://12factor.net/)
- [OWASP Top 10](https://owasp.org/www-project-top-ten/)

