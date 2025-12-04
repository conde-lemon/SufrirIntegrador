# 🚀 MANUAL DE DESPLIEGUE EN RAILWAY - TRAVEL4U

## 📋 Tabla de Contenidos

1. [Preparación](#preparación)
2. [Crear Proyecto en Railway](#crear-proyecto-en-railway)
3. [Configurar Variables de Entorno](#configurar-variables-de-entorno)
4. [Generar Dominio Público](#generar-dominio-público)
5. [Verificación del Despliegue](#verificación-del-despliegue)
6. [Actualizar la Aplicación](#actualizar-la-aplicación)

---

## 📦 Preparación

### Requisitos Previos

- ✅ Cuenta en [Railway.app](https://railway.app)
- ✅ Cuenta en GitHub
- ✅ Base de datos Supabase configurada

### Repositorio del Proyecto

El código está alojado en:
```
https://github.com/conde-lemon/SufrirIntegrador
```

**No necesitas subir nada a GitHub**, el código ya está en el repositorio listo para desplegar.

---

## 🚂 PASO 1: Crear Proyecto en Railway

## 🚂 PASO 1: Crear Proyecto en Railway

1. **Ir a Railway:**
   - Abre: https://railway.app
   - Haz login con GitHub

2. **Crear nuevo proyecto:**
   - Click en **"New Project"**
   - Selecciona **"Deploy from GitHub repo"**
   - Autoriza el acceso a GitHub si es necesario

3. **Seleccionar repositorio:**
   - Busca y selecciona: **`conde-lemon/SufrirIntegrador`**
   - Click en el repositorio

4. **Railway detectará automáticamente:**
   - El Dockerfile
   - Comenzará a construir la aplicación
   - Verás los logs de build en tiempo real

5. **Esperar el primer build:**
   - Puede tomar 5-8 minutos
   - Railway descargará dependencias y compilará

---

## ⚙️ PASO 2: Configurar Variables de Entorno

### ⚠️ CRÍTICO - SIN ESTAS VARIABLES LA APP NO FUNCIONA

En Railway:
1. Click en tu proyecto
2. Ve a **"Variables"** (en el menú lateral)
3. Agrega las siguientes 5 variables:

### Variable 1: SPRING_PROFILES_ACTIVE
```
SPRING_PROFILES_ACTIVE=heroku
```
**Qué hace:** Activa el perfil de configuración para producción

### Variable 2: SPRING_DATASOURCE_URL
```
SPRING_DATASOURCE_URL=jdbc:postgresql://db.tiifltprjgtyfimhnezi.supabase.co:5432/postgres
```
**Qué hace:** URL de conexión a tu base de datos Supabase

⚠️ **IMPORTANTE:** Si tu URL de Supabase es diferente, cámbiala aquí.

**¿Cómo obtener tu URL de Supabase?**
1. Ve a: https://supabase.com/dashboard
2. Selecciona tu proyecto
3. Settings → Database
4. Busca "Connection string" o "Host"
5. El formato debe ser: `jdbc:postgresql://TU-HOST:5432/postgres`

### Variable 3: SPRING_DATASOURCE_USERNAME
```
SPRING_DATASOURCE_USERNAME=postgres
```
**Qué hace:** Usuario de la base de datos (normalmente es "postgres")

### Variable 4: SPRING_DATASOURCE_PASSWORD 🔴 REQUERIDO
```
SPRING_DATASOURCE_PASSWORD=zoet5w5ksSEdkikt
```
**Qué hace:** Contraseña de tu base de datos Supabase

⚠️ **REEMPLAZA** `zoet5w5ksSEdkikt` con tu contraseña **REAL** de Supabase.

**¿Dónde encontrar tu contraseña?**
1. Ve a: https://supabase.com/dashboard
2. Selecciona tu proyecto
3. Settings → Database
4. Busca "Database Password"
5. Si no la recuerdas, puedes resetearla allí

### Variable 5: PORT
```
PORT=8080
```
**Qué hace:** Puerto en el que la aplicación escuchará

---

### ✅ Verificar Variables Configuradas

En Railway → Variables, deberías ver:

| Variable | Valor |
|----------|-------|
| SPRING_PROFILES_ACTIVE | `heroku` |
| SPRING_DATASOURCE_URL | `jdbc:postgresql://...` |
| SPRING_DATASOURCE_USERNAME | `postgres` |
| SPRING_DATASOURCE_PASSWORD | `********` (oculta) |
| PORT | `8080` |

**Después de agregar las variables:**
- Railway redespleará automáticamente
- Espera 2-3 minutos
- Verifica los logs para confirmar que inició correctamente

---

## 🌐 PASO 3: Generar Dominio Público

1. En Railway, ve a tu proyecto
2. Click en **"Settings"** (engranaje)
3. Sección **"Domains"**
4. Click en **"Generate Domain"**
5. Railway te dará una URL pública, ejemplo:
   ```
   https://travel4u-production.up.railway.app
   ```

**Esta es la URL que usarás para acceder a tu aplicación.**

---

## ✅ PASO 4: Verificar Despliegue Exitoso

### Ver Logs del Despliegue

1. En Railway → Tu proyecto
2. Tab **"Deployments"**
3. Click en el último deployment
4. Ver **"View Logs"**

### Buscar Mensajes de Éxito

Deberías ver algo como:

```
✅ BUILD SUCCESSFUL in 3m 24s
✅ Started DemoApplication in 45.123 seconds
✅ Tomcat started on port(s): 8080
✅ Application is ready to serve requests
```

### Probar la Aplicación

1. **Abrir en el navegador:**
   ```
   https://TU-APP.up.railway.app
   ```

2. **Probar endpoints:**
   ```
   https://TU-APP.up.railway.app/
   https://TU-APP.up.railway.app/vuelos
   https://TU-APP.up.railway.app/login
   ```

3. **Verificar que funcione:**
   - La página principal carga
   - Puedes hacer búsquedas
   - El formulario responde
   - Los datos se muestran correctamente

---

## 🔄 PASO 5: Actualizar la Aplicación (Deploys Futuros)

**Railway detecta automáticamente los cambios** en el repositorio de GitHub.

Cada vez que se haga un push al repositorio `conde-lemon/SufrirIntegrador`:

**Railway automáticamente:**
- ✅ Detectará el push
- ✅ Iniciará un nuevo build
- ✅ Desplegará la nueva versión
- ✅ Tiempo estimado: 3-5 minutos

**No necesitas hacer nada adicional**, Railway sincroniza automáticamente con el repositorio.

---

## 📊 Monitoreo y Métricas

### Ver Métricas en Tiempo Real

Railway proporciona automáticamente:

1. **CPU Usage** - Uso del procesador
2. **Memory Usage** - Uso de RAM
3. **Network** - Tráfico de entrada/salida
4. **Deployments** - Historial de despliegues

**Cómo acceder:**
- Railway → Tu proyecto → Tab "Metrics"

### Ver Logs en Tiempo Real

```
Railway → Deployments → Latest → View Logs
```

Los logs muestran:
- Startup de la aplicación
- Requests HTTP
- Errores y excepciones
- Consultas a la base de datos (si está habilitado)

---

## 💰 Plan Gratuito de Railway

### Lo que incluye:

- ✅ **$5 USD de crédito mensual gratis**
- ✅ **500 horas de ejecución/mes**
- ✅ **NO requiere tarjeta de crédito**
- ✅ **Deploy automático desde GitHub**
- ✅ **SSL/HTTPS incluido gratis**
- ✅ **Builds ilimitados**

### Suficiente para:
- 1 aplicación Spring Boot pequeña-mediana
- Tráfico moderado (no masivo)
- Desarrollo y demos
- Proyectos académicos

### Optimización de costos:

1. **Railway pausa servicios inactivos** automáticamente
2. **Configura sleep mode** para ahorrar horas
3. **Monitorea el uso** en el dashboard

---

## 📋 Checklist Final

### Deploy
- [ ] Proyecto creado en Railway desde `conde-lemon/SufrirIntegrador`
- [ ] Variables de entorno configuradas (las 5)
- [ ] Build exitoso sin errores
- [ ] Dominio generado

### Post-Deploy
- [ ] Aplicación accesible en la URL pública
- [ ] Página principal carga correctamente
- [ ] Búsquedas funcionan
- [ ] Login/registro funciona
- [ ] Conexión a base de datos OK

---

## 📚 Recursos y Documentación

### Railway
- **Dashboard:** https://railway.app/dashboard
- **Documentación:** https://docs.railway.app
- **Discord:** https://discord.gg/railway
- **Status:** https://status.railway.app

### Supabase
- **Dashboard:** https://supabase.com/dashboard
- **Documentación:** https://supabase.com/docs
- **Status:** https://status.supabase.com

### Spring Boot
- **Documentación:** https://spring.io/projects/spring-boot
- **Guides:** https://spring.io/guides

---

## 🎯 Resumen Rápido

| Paso | Acción | Tiempo Estimado |
|------|--------|-----------------|
| 1 | Crear proyecto Railway | 1 minuto |
| 2 | Configurar variables (5) | 2 minutos |
| 3 | Generar dominio | 1 minuto |
| | **Build inicial** | 5-8 minutos |
| | **TOTAL** | **~12 minutos** |

---

## ✅ Verificación Final

Una vez desplegado, verifica:

1. **URL funciona:**
   ```
   https://tu-app.up.railway.app
   ```

2. **Endpoints principales:**
   - `/` - Página principal
   - `/login` - Login
   - `/registrar` - Registro
   - `/vuelos` - Búsqueda de vuelos

3. **Funcionalidades:**
   - Búsqueda de servicios
   - Login/registro
   - Reservas
   - Perfil de usuario

---

**Fecha:** 2025-12-04  
**Versión:** 3.0 - Desde Repositorio Existente  
**Estado:** ✅ LISTO PARA USAR

---

**¡Tu aplicación Travel4U está lista para Railway! 🎉**

**Repositorio:** https://github.com/conde-lemon/SufrirIntegrador
