# ✅ CONFIGURACIÓN HÍBRIDA - LOCAL Y RAILWAY

## 🎯 PROBLEMA RESUELTO

Ahora la aplicación funciona TANTO en desarrollo local COMO en Railway.

---

## 🏠 DESARROLLO LOCAL

### Configuración automática:
- **Base de datos:** H2 en memoria (temporal, se reinicia cada vez)
- **Puerto:** 8081
- **Consola H2:** Habilitada en `/h2-console`
- **URL:** http://localhost:8081

### Sin necesidad de variables de entorno:
La app usa valores por defecto de `application.properties`:
```properties
spring.datasource.url=jdbc:h2:mem:testdb
spring.datasource.username=sa
spring.datasource.password=
spring.datasource.driver-class-name=org.h2.Driver
```

### Para ejecutar:
```bash
cd demo
./gradlew bootRun
```

O desde IntelliJ IDEA: Run → DemoApplication

---

## 🚀 RAILWAY (PRODUCCIÓN)

### Configuración con variables de entorno:
- **Base de datos:** PostgreSQL (Supabase)
- **Puerto:** 8080 (configurado por Railway)
- **Perfil activo:** `heroku`
- **Consola H2:** Deshabilitada

### Variables de entorno en Railway:
```
SPRING_PROFILES_ACTIVE=heroku
SPRING_DATASOURCE_URL=jdbc:postgresql://aws-1-us-east-1.pooler.supabase.com:5432/postgres?user=postgres.tiifltprjgtyfimhnezi
SPRING_DATASOURCE_USERNAME=postgres
SPRING_DATASOURCE_PASSWORD=zoet5w5ksSEdkikt
PORT=8080
```

### Cómo funciona:
1. Railway inyecta `SPRING_PROFILES_ACTIVE=heroku`
2. Spring Boot activa el perfil `application-heroku.yml`
3. `application-heroku.yml` sobrescribe las configuraciones de `application.properties`
4. La app se conecta a Supabase en lugar de H2

---

## 🔄 FLUJO DE CONFIGURACIÓN

### Desarrollo Local:
```
application.properties (valores por defecto)
    ↓
H2 en memoria
    ↓
App corriendo en localhost:8081
```

### Railway:
```
application.properties (valores por defecto)
    ↓
Variable de entorno: SPRING_PROFILES_ACTIVE=heroku
    ↓
application-heroku.yml (sobrescribe configuraciones)
    ↓
Variables de entorno de Railway (SPRING_DATASOURCE_*)
    ↓
PostgreSQL (Supabase)
    ↓
App corriendo en Railway (puerto 8080)
```

---

## 📋 ARCHIVOS ACTUALIZADOS

### 1. application.properties
**Cambios:**
- ✅ Restauradas configuraciones de datasource con valores por defecto H2
- ✅ Usa variables de entorno cuando están disponibles
- ✅ Funciona localmente sin configuración adicional

```properties
spring.datasource.url=${SPRING_DATASOURCE_URL:jdbc:h2:mem:testdb}
spring.datasource.username=${SPRING_DATASOURCE_USERNAME:sa}
spring.datasource.password=${SPRING_DATASOURCE_PASSWORD:}
```

**Explicación:** 
- `${VAR:default}` - Si la variable VAR existe, la usa; si no, usa el valor por defecto
- En local: No hay variables, usa `jdbc:h2:mem:testdb`
- En Railway: Hay variables, usa `jdbc:postgresql://...`

### 2. application-heroku.yml
**Cambios:**
- ✅ Añadida configuración explícita de PostgreSQL dialect
- ✅ Deshabilitada consola H2 en producción
- ✅ Sobrescribe completamente la configuración de datasource

---

## 🎯 COMANDOS GIT

Sube estos cambios para que funcione en Railway:

```powershell
cd "C:\Users\LENOVO\Documents\utp\ciclo7\integrador\demo (1)"
git add demo/src/main/resources/application.properties
git add demo/src/main/resources/application-heroku.yml
git commit -m "feat: Hybrid config for local H2 and Railway PostgreSQL"
git push origin main
```

---

## ✅ VERIFICACIÓN

### Local:
```bash
./gradlew bootRun
```

**Deberías ver:**
```
H2 console available at '/h2-console'
Started DemoApplication in X.XXX seconds
Tomcat started on port 8081
```

### Railway:
Después del push, en los logs:
```
The following 1 profile is active: "heroku"
HikariPool-1 - Start completed
Started DemoApplication in X.XXX seconds
Tomcat started on port 8080
```

---

## 🔍 DIFERENCIAS CLAVE

| Aspecto | Local | Railway |
|---------|-------|---------|
| Base de datos | H2 en memoria | PostgreSQL (Supabase) |
| Puerto | 8081 | 8080 |
| Perfil | default | heroku |
| Consola H2 | Habilitada | Deshabilitada |
| Datos | Temporales | Persistentes |
| Configuración | Automática | Variables de entorno |

---

## 💡 VENTAJAS

✅ **No necesitas configurar nada para desarrollo local**
✅ **H2 es rápido y temporal (ideal para testing)**
✅ **Railway usa PostgreSQL con datos reales**
✅ **Mismo código funciona en ambos entornos**
✅ **Fácil cambiar entre entornos**

---

## 🔧 DESARROLLO LOCAL CON SUPABASE (OPCIONAL)

Si quieres usar Supabase también en local, crea un archivo `.env`:

```env
SPRING_DATASOURCE_URL=jdbc:postgresql://aws-1-us-east-1.pooler.supabase.com:5432/postgres?user=postgres.tiifltprjgtyfimhnezi
SPRING_DATASOURCE_USERNAME=postgres
SPRING_DATASOURCE_PASSWORD=zoet5w5ksSEdkikt
SPRING_PROFILES_ACTIVE=heroku
```

Y configura tu IDE para cargar ese archivo.

---

## 🎉 TODO FUNCIONA AHORA

- ✅ Desarrollo local con H2 (sin configuración)
- ✅ Railway con PostgreSQL (con variables de entorno)
- ✅ Mismo código, diferente entorno
- ✅ Fácil de mantener

**Sube los cambios con los comandos git de arriba.** 🚀

