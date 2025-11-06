# 🔧 Guía de Solución - Problemas de Base de Datos y Reportes

## 📋 Problemas Identificados

1. **Configuración mixta**: La aplicación usa H2 en configuración pero PostgreSQL en logs
2. **Consultas N+1**: Múltiples queries para cargar datos relacionados
3. **Reportes incompletos**: JasperReports no obtiene todos los datos necesarios
4. **Falta de validación**: No hay verificación de conectividad en startup

## 🛠️ Soluciones Implementadas

### 1. Configuración de Base de Datos Corregida

**Archivos creados/modificados:**
- `application.properties` → Configuración PostgreSQL por defecto
- `application-postgres.properties` → Configuración específica PostgreSQL
- `application-h2.properties` → Mantiene configuración H2 para desarrollo

### 2. Tests de Conectividad

**Archivos creados:**
- `DatabaseConnectionTest.java` → Verifica conexión y tablas
- `ReservaServiceTest.java` → Prueba creación y consulta de reservas
- `JasperReportServiceTest.java` → Valida generación de reportes

### 3. Diagnóstico Web

**Archivos creados:**
- `DiagnosticController.java` → API endpoints para diagnóstico
- `DiagnosticPageController.java` → Controlador de página
- `diagnostic.html` → Interfaz web para pruebas

### 4. Mejoras en JasperReportService

**Mejoras implementadas:**
- Mejor manejo de errores
- Diagnóstico de queries
- Validación de conexiones
- Logging detallado

## 🚀 Cómo Usar las Soluciones

### Opción 1: Usar PostgreSQL (Recomendado)

1. **Verificar PostgreSQL:**
   ```bash
   # Asegúrate de que PostgreSQL esté corriendo en puerto 8180
   # Base de datos: sufrirIntegrador
   # Usuario: postgres
   # Contraseña: conde-lemon
   ```

2. **Ejecutar aplicación:**
   ```bash
   ./gradlew bootRun
   ```

3. **Verificar conectividad:**
   - Ir a: `http://localhost:8081/diagnostic`
   - Ejecutar todos los tests desde la interfaz web

### Opción 2: Usar H2 para Desarrollo

1. **Cambiar perfil:**
   ```bash
   ./gradlew bootRun --args='--spring.profiles.active=h2'
   ```

2. **Acceder a consola H2:**
   - URL: `http://localhost:8081/h2-console`
   - JDBC URL: `jdbc:h2:mem:testdb`
   - Usuario: `sa`
   - Contraseña: (vacía)

### Opción 3: Ejecutar Tests

1. **Tests individuales:**
   ```bash
   ./gradlew test --tests DatabaseConnectionTest
   ./gradlew test --tests ReservaServiceTest
   ./gradlew test --tests JasperReportServiceTest
   ```

2. **Script automático:**
   ```bash
   # En Windows
   test-database.bat
   ```

## 🔍 Endpoints de Diagnóstico

### API Endpoints:
- `GET /api/diagnostic/database` → Estado de conexión BD
- `GET /api/diagnostic/reservas-data?userId=5` → Datos de reservas
- `GET /api/diagnostic/report-test?userId=5` → Test de reporte

### Página Web:
- `GET /diagnostic` → Interfaz de diagnóstico completa

## 📊 Verificación de Reportes

### 1. Verificar Datos Base
```sql
-- Verificar usuarios
SELECT id_usuario, email, nombres FROM usuarios;

-- Verificar reservas
SELECT r.id_reserva, r.estado, r.total, u.email 
FROM reserva r 
INNER JOIN usuarios u ON r.id_usuario = u.id_usuario;
```

### 2. Test Manual de Query de Reporte
```sql
-- Query exacta del reporte Reservas.jrxml
SELECT
    r.id_reserva,
    'TFU-' || EXTRACT(YEAR FROM COALESCE(r.created_at, CURRENT_TIMESTAMP)) || '-' || LPAD(CAST(r.id_reserva AS VARCHAR), 4, '0') as codigo_reserva,
    COALESCE(r.created_at, CURRENT_TIMESTAMP) as fecha_reserva,
    r.fecha_inicio,
    r.fecha_fin,
    COALESCE(r.estado, 'pendiente') as estado,
    COALESCE(r.total, 0) as total,
    COALESCE(r.moneda, 'PEN') as moneda,
    COALESCE(r.observaciones, 'Sin observaciones') as observaciones,
    COALESCE(u.nombres, 'Usuario') as nombres,
    COALESCE(u.apellidos, '') as apellidos,
    COALESCE(u.email, '') as email
FROM reserva r
INNER JOIN usuarios u ON r.id_usuario = u.id_usuario
WHERE r.id_usuario = 5  -- Cambiar por ID real
ORDER BY COALESCE(r.created_at, CURRENT_TIMESTAMP) DESC;
```

## 🐛 Solución de Problemas Comunes

### Error: "Connection refused"
- Verificar que PostgreSQL esté ejecutándose
- Confirmar puerto 8180
- Verificar credenciales

### Error: "Table doesn't exist"
- Ejecutar con `spring.jpa.hibernate.ddl-auto=create` una vez
- Verificar que `data.sql` se ejecute correctamente

### Reportes vacíos
- Usar `/diagnostic` para verificar datos
- Revisar logs de JasperReportService
- Confirmar que el usuario tenga reservas

### Consultas N+1
- Implementar `@EntityGraph` en repositorios
- Usar `JOIN FETCH` en queries personalizadas

## 📈 Próximos Pasos

1. **Optimización de Queries:**
   - Implementar lazy loading apropiado
   - Agregar índices en BD
   - Usar proyecciones para reportes

2. **Mejoras en Reportes:**
   - Cachear reportes compilados
   - Implementar reportes asíncronos
   - Agregar más formatos de exportación

3. **Monitoreo:**
   - Métricas de performance
   - Alertas de conectividad
   - Dashboard de salud del sistema