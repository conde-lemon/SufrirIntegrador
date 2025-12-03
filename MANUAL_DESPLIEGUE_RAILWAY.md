# [RAILWAY] MANUAL DE DESPLIEGUE EN RAILWAY - TRAVEL4U

## [INDICE] Tabla de Contenidos

1. [Preparación](#preparación)
2. [Despliegue del Backend (Microservicio)](#despliegue-del-backend)
3. [Despliegue del Frontend (React)](#despliegue-del-frontend)
4. [Configuración de Variables de Entorno](#configuración-de-variables)
5. [Verificación y Testing](#verificación)
6. [Solución de Problemas](#solución-de-problemas)

---

## [PKG] Preparación

### Requisitos Previos

- ✅ Cuenta en [Railway.app](https://railway.app)
- ✅ Cuenta en GitHub
- ✅ Base de datos Supabase configurada
- ✅ Código del proyecto en GitHub

### Estructura del Proyecto

```
travel4u-microservices/
├── backend/
│   └── servicios-service/        # Microservicio Spring Boot
└── frontend/
    └── travel4u-frontend/        # Aplicación React
```

---

## [TOOL] PARTE 1: Despliegue del Backend

### Paso 1: Preparar el Repositorio

1. **Crear repositorio en GitHub** (si no existe):

```bash
cd travel4u-microservices
git init
git add .
git commit -m "Initial commit - Travel4U microservices"
git remote add origin https://github.com/TU-USUARIO/travel4u-microservices.git
git push -u origin main
```

2. **Asegurar que existe `system.properties`** en `backend/servicios-service/`:

```properties
java.runtime.version=17
```

### Paso 2: Crear Proyecto en Railway

1. Ir a [railway.app](https://railway.app)
2. Click en **"New Project"**
3. Seleccionar **"Deploy from GitHub repo"**
4. Autorizar acceso a GitHub
5. Seleccionar el repositorio `travel4u-microservices`

### Paso 3: Configurar el Servicio Backend

1. **Configurar Root Directory**:
   - Click en el servicio
   - Settings → Root Directory
   - Establecer: `backend/servicios-service`

2. **Configurar Build Command**:
   - Settings → Build
   - Build Command: `./gradlew build -x test`

3. **Configurar Start Command**:
   - Settings → Deploy
   - Start Command: `java -jar build/libs/servicios-service-1.0.0.jar`

### Paso 4: Configurar Variables de Entorno

En Settings → Variables, agregar:

```env
# Base de Datos
SPRING_DATASOURCE_URL=jdbc:postgresql://aws-1-us-east-1.pooler.supabase.com:5432/postgres
SPRING_DATASOURCE_USERNAME=postgres
SPRING_DATASOURCE_PASSWORD=zoet5w5ksSEdkikt

# JPA
SPRING_JPA_HIBERNATE_DDL_AUTO=update
SPRING_JPA_SHOW_SQL=false

# Servidor
SERVER_PORT=8080
SPRING_PROFILES_ACTIVE=railway

# CORS (usar el dominio del frontend cuando esté desplegado)
CORS_ALLOWED_ORIGINS=*
```

### Paso 5: Actualizar application.yml

Crear `application-railway.yml` en `src/main/resources/`:

```yaml
spring:
  application:
    name: servicios-service
  
  datasource:
    url: ${SPRING_DATASOURCE_URL}
    username: ${SPRING_DATASOURCE_USERNAME}
    password: ${SPRING_DATASOURCE_PASSWORD}
    driver-class-name: org.postgresql.Driver
    hikari:
      maximum-pool-size: 5
      minimum-idle: 2
  
  jpa:
    hibernate:
      ddl-auto: ${SPRING_JPA_HIBERNATE_DDL_AUTO:update}
    show-sql: ${SPRING_JPA_SHOW_SQL:false}
    properties:
      hibernate:
        dialect: org.hibernate.dialect.PostgreSQLDialect

server:
  port: ${PORT:8080}

logging:
  level:
    root: INFO
    com.travel4u: INFO

management:
  endpoints:
    web:
      exposure:
        include: health,info
```

### Paso 6: Actualizar SecurityConfig para CORS

En `SecurityConfig.java`:

```java
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Value("${CORS_ALLOWED_ORIGINS:*}")
    private String corsAllowedOrigins;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
            .csrf(csrf -> csrf.disable())
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/**").permitAll()
                .requestMatchers("/actuator/**").permitAll()
                .anyRequest().authenticated()
            );

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList(corsAllowedOrigins.split(",")));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("*"));
        configuration.setAllowCredentials(false);
        
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
```

### Paso 7: Desplegar

1. Hacer commit de los cambios:

```bash
git add .
git commit -m "Configure for Railway deployment"
git push
```

2. Railway detectará el push y comenzará el despliegue automáticamente

3. Esperar a que el deploy termine (5-10 minutos)

4. Railway te dará una URL pública, ejemplo:
   ```
   https://servicios-service-production.up.railway.app
   ```

### Paso 8: Verificar el Despliegue

Probar el health endpoint:

```bash
curl https://TU-URL.railway.app/actuator/health
```

Respuesta esperada:
```json
{"status":"UP"}
```

Probar la API:
```bash
curl https://TU-URL.railway.app/api/vuelos
```

---

## [WEB] PARTE 2: Despliegue del Frontend

### Paso 1: Preparar el Frontend para Producción

1. **Actualizar `serviciosAPI.js`** para usar variables de entorno:

```javascript
// src/services/serviciosAPI.js
const API_BASE_URL = process.env.REACT_APP_API_URL || 'http://localhost:8083/api';

const serviciosAPI = {
  // ...resto del código sin cambios
};
```

2. **Crear archivo `.env.production`** en `frontend/travel4u-frontend/`:

```env
REACT_APP_API_URL=https://TU-BACKEND-URL.railway.app/api
```

### Paso 2: Crear Nuevo Servicio en Railway

1. En el mismo proyecto de Railway
2. Click en **"New"** → **"GitHub Repo"**
3. Seleccionar el mismo repositorio
4. Configurar:
   - **Root Directory**: `frontend/travel4u-frontend`
   - **Build Command**: `npm install && npm run build`
   - **Start Command**: `npx serve -s build -p $PORT`

### Paso 3: Configurar Variables de Entorno del Frontend

En Settings → Variables:

```env
REACT_APP_API_URL=https://TU-BACKEND-URL.railway.app/api
NODE_VERSION=18
```

### Paso 4: Actualizar package.json

Agregar al `package.json`:

```json
{
  "scripts": {
    "start": "react-scripts start",
    "build": "react-scripts build",
    "serve": "serve -s build -p $PORT"
  },
  "dependencies": {
    // ...dependencias existentes
    "serve": "^14.2.0"
  }
}
```

### Paso 5: Desplegar Frontend

```bash
git add .
git commit -m "Configure frontend for Railway"
git push
```

Railway desplegará automáticamente.

### Paso 6: Actualizar CORS en Backend

Una vez que tengas la URL del frontend, actualiza la variable de entorno del backend:

```env
CORS_ALLOWED_ORIGINS=https://TU-FRONTEND-URL.railway.app
```

---

## [SYNC] Configuración de Dominios Personalizados (Opcional)

### Backend

1. En Railway, ir al servicio backend → Settings → Domains
2. Click en **"Generate Domain"** o agregar dominio personalizado
3. Ejemplo: `api.travel4u.com`

### Frontend

1. En Railway, ir al servicio frontend → Settings → Domains
2. Click en **"Generate Domain"** o agregar dominio personalizado
3. Ejemplo: `www.travel4u.com`

---

## [GRAPH] Monitoreo y Logs

### Ver Logs en Tiempo Real

1. Click en el servicio
2. Tab **"Deployments"**
3. Click en el último deployment
4. Ver logs en tiempo real

### Métricas

Railway proporciona automáticamente:
- CPU usage
- Memory usage
- Network traffic
- Request count

---

## 🔍 Verificación Final

### Checklist de Despliegue

#### Backend
- [ ] Servicio desplegado exitosamente
- [ ] Health endpoint responde: `/actuator/health`
- [ ] API endpoints responden: `/api/vuelos`
- [ ] Conexión a Supabase funciona
- [ ] CORS configurado correctamente

#### Frontend
- [ ] Build exitoso
- [ ] Aplicación accesible en URL pública
- [ ] Conecta correctamente con backend
- [ ] Páginas cargan sin errores
- [ ] Búsquedas funcionan

### URLs de Prueba

```bash
# Backend
curl https://TU-BACKEND.railway.app/actuator/health
curl https://TU-BACKEND.railway.app/api/vuelos
curl https://TU-BACKEND.railway.app/api/cruceros

# Frontend
# Abrir en navegador
https://TU-FRONTEND.railway.app
https://TU-FRONTEND.railway.app/vuelos
https://TU-FRONTEND.railway.app/cruceros
```

---

## [DEBUG] Solución de Problemas

### Backend no inicia

**Problema**: Error en el build
- Verificar logs en Railway
- Verificar que Java 17 está configurado
- Verificar que Gradle compila localmente

**Problema**: Error de conexión a BD
- Verificar variables de entorno
- Verificar credenciales de Supabase
- Verificar que IP de Railway está permitida en Supabase

### Frontend no conecta con Backend

**Problema**: CORS errors
- Verificar variable `CORS_ALLOWED_ORIGINS` en backend
- Debe incluir la URL del frontend

**Problema**: API URL incorrecta
- Verificar `REACT_APP_API_URL` en frontend
- Debe apuntar a la URL del backend

### Deploy falla

**Problema**: Build timeout
- Reducir dependencias innecesarias
- Optimizar build process
- Aumentar timeout en Railway settings

**Problema**: Port binding error
- Asegurar que se usa variable `$PORT`
- Railway asigna puerto automáticamente

---

## 💰 Costos y Límites

### Plan Gratuito de Railway

- **$5 USD de crédito mensual gratuito**
- Suficiente para:
  - 1 backend pequeño
  - 1 frontend estático
  - Tráfico moderado

### Optimización de Costos

1. **Usar sleep mode**: Railway pausa servicios inactivos
2. **Optimizar resources**: Configurar límites de memoria/CPU
3. **Cachear builds**: Railway cachea dependencias

---

## [DOC] Mantenimiento

### Actualizar el Código

```bash
# Local
git add .
git commit -m "Update: descripción"
git push

# Railway desplegará automáticamente
```

### Rollback a Versión Anterior

1. En Railway → Deployments
2. Click en deployment anterior
3. Click en **"Redeploy"**

### Escalar Verticalmente

1. Settings → Resources
2. Ajustar CPU/RAM según necesidad
3. Costo aumenta proporcionalmente

---

## [TARGET] Arquitectura Final

```
Internet
    ↓
Railway Frontend (React)
https://travel4u-frontend.railway.app
    ↓ HTTP Requests
Railway Backend (Spring Boot)
https://travel4u-backend.railway.app
    ↓ JDBC
Supabase PostgreSQL
aws-1-us-east-1.pooler.supabase.com
```

---

## [OK] Checklist Final de Despliegue

### Pre-Deploy
- [ ] Código funciona localmente
- [ ] Tests pasan
- [ ] Variables de entorno documentadas
- [ ] .gitignore actualizado

### Deploy Backend
- [ ] Repositorio en GitHub
- [ ] Proyecto creado en Railway
- [ ] Variables de entorno configuradas
- [ ] Build exitoso
- [ ] Health check funciona

### Deploy Frontend
- [ ] API URL configurada
- [ ] Build de producción exitoso
- [ ] Conecta con backend
- [ ] CORS configurado

### Post-Deploy
- [ ] Ambos servicios funcionando
- [ ] URLs públicas funcionando
- [ ] Monitoreo configurado
- [ ] Documentación actualizada

---

## [BOOK] Recursos Adicionales

- [Railway Documentation](https://docs.railway.app/)
- [Spring Boot on Railway](https://docs.railway.app/guides/java)
- [React on Railway](https://docs.railway.app/guides/react)
- [Supabase Documentation](https://supabase.com/docs)

---

## [HELP] Soporte

### Railway Discord
- [Discord Community](https://discord.gg/railway)

### Logs y Debugging
```bash
# Ver logs en tiempo real
railway logs

# Conectar a shell del servicio
railway shell
```

---

**Fecha de Creación**: 2025-12-03  
**Versión**: 1.0  
**Estado**: ✅ COMPLETO

*** ¡Tu aplicación Travel4U está lista para producción en Railway! ***
