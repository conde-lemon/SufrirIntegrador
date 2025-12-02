# 🚀 GUÍA COMPLETA DE DESPLIEGUE EN RAILWAY

## 📋 RESUMEN RÁPIDO
Railway es una plataforma moderna que te permite desplegar aplicaciones Spring Boot de forma gratuita (500 horas/mes) sin necesidad de tarjeta de crédito.

Tu proyecto **YA ESTÁ CONFIGURADO** con:
- ✅ Dockerfile optimizado
- ✅ Configuración de Spring Boot para producción
- ✅ Scripts de ayuda

---

## 🎯 PASO 1: PREPARAR REPOSITORIO EN GITHUB

### 1.1 Crear repositorio en GitHub
1. Ve a: https://github.com/new
2. Nombre del repositorio: `travel4u-app` (o el nombre que prefieras)
3. **NO** marques "Initialize with README" (ya tienes código)
4. Click en "Create repository"

### 1.2 Subir tu código a GitHub
Abre PowerShell en la carpeta del proyecto y ejecuta:

```powershell
# Navegar a la carpeta del proyecto
cd "C:\Users\LENOVO\Documents\utp\ciclo7\integrador\demo (1)"

# Verificar estado de Git
git status

# Añadir remote de GitHub (reemplaza TU_USUARIO con tu usuario de GitHub)
git remote add origin https://github.com/TU_USUARIO/travel4u-app.git

# O si ya tienes un remote, actualizarlo:
git remote set-url origin https://github.com/TU_USUARIO/travel4u-app.git

# Cambiar a rama main
git branch -M main

# Añadir todos los archivos
git add .

# Hacer commit
git commit -m "Preparar para deploy en Railway"

# Subir a GitHub
git push -u origin main
```

**Nota:** GitHub te pedirá autenticación. Usa tu token de acceso personal si ya no aceptan contraseñas.

---

## 🎯 PASO 2: CREAR CUENTA EN RAILWAY

1. Ve a: **https://railway.app**
2. Click en **"Login"**
3. Selecciona **"Login with GitHub"**
4. Autoriza Railway para acceder a tu cuenta de GitHub
5. Te llevará al Dashboard de Railway

---

## 🎯 PASO 3: CREAR PROYECTO EN RAILWAY

### 3.1 Nuevo Proyecto
1. En el Dashboard de Railway, click en **"New Project"**
2. Selecciona **"Deploy from GitHub repo"**
3. Si es la primera vez, Railway te pedirá autorización:
   - Click en **"Configure GitHub App"**
   - Selecciona el repositorio `travel4u-app`
   - Click en **"Install & Authorize"**

### 3.2 Seleccionar Repositorio
1. Busca y selecciona tu repositorio: `travel4u-app`
2. Railway detectará automáticamente que tienes un **Dockerfile**
3. Click en **"Deploy Now"**

Railway comenzará a:
- 📦 Clonar tu repositorio
- 🔨 Construir la imagen Docker
- 🚀 Desplegar la aplicación

---

## 🎯 PASO 4: CONFIGURAR VARIABLES DE ENTORNO

**MUY IMPORTANTE:** Tu aplicación necesita estas variables para funcionar.

### 4.1 Acceder a Variables
1. En Railway, click en tu proyecto recién creado
2. En el panel de servicios, click en tu servicio (debería aparecer con el nombre de tu repo)
3. Ve a la pestaña **"Variables"**

### 4.2 Añadir Variables de Entorno
Click en **"+ New Variable"** y añade cada una de estas:

```env
SPRING_PROFILES_ACTIVE=heroku
```

```env
SPRING_DATASOURCE_URL=jdbc:postgresql://db.tiifltprjgtyfimhnezi.supabase.co:5432/postgres
```

```env
SPRING_DATASOURCE_USERNAME=postgres
```

```env
SPRING_DATASOURCE_PASSWORD=tu_password_de_supabase_aqui
```

```env
PORT=8080
```

**⚠️ IMPORTANTE:** Reemplaza `tu_password_de_supabase_aqui` con tu contraseña real de Supabase.

### 4.3 Guardar y Redesplegar
- Railway automáticamente redesplegarás después de añadir las variables
- Si no, haz click en **"Deploy"** manualmente

---

## 🎯 PASO 5: VERIFICAR DESPLIEGUE

### 5.1 Ver Logs
1. En tu servicio, ve a la pestaña **"Deployments"**
2. Click en el deployment más reciente
3. Verás los logs en tiempo real:
   ```
   Building...
   [+] Building...
   Starting application...
   Started DemoApplication in X seconds
   ```

### 5.2 Obtener URL de tu Aplicación
1. En la pestaña **"Settings"** de tu servicio
2. Sección **"Domains"**
3. Click en **"Generate Domain"**
4. Railway te dará una URL como:
   ```
   https://travel4u-app-production.up.railway.app
   ```

### 5.3 Probar tu Aplicación
Abre la URL en tu navegador:
- Página principal: `https://tu-app.up.railway.app`
- API servicios: `https://tu-app.up.railway.app/api/servicios`
- Login admin: `https://tu-app.up.railway.app/admin/login`

---

## 🎯 PASO 6: CONFIGURACIÓN ADICIONAL (OPCIONAL)

### 6.1 Dominio Personalizado
1. En **"Settings"** > **"Domains"**
2. Click en **"Custom Domain"**
3. Añade tu dominio (si tienes uno)

### 6.2 Ajustar Recursos
Railway asigna automáticamente:
- **RAM:** 512 MB (suficiente para Spring Boot)
- **CPU:** Compartida
- **Almacenamiento:** 1 GB

### 6.3 Variables de Entorno Adicionales (Opcional)
Si necesitas ajustar memoria de Java:
```env
JAVA_OPTS=-Xmx450m -Xms256m
```

---

## 🔄 DESPLIEGUES AUTOMÁTICOS

Railway está configurado para **deploy automático**. Cada vez que hagas push a GitHub:

```powershell
# Hacer cambios en tu código
git add .
git commit -m "Descripción de cambios"
git push origin main
```

Railway automáticamente:
1. ✅ Detecta el push
2. ✅ Construye nueva imagen Docker
3. ✅ Despliega la nueva versión
4. ✅ Hace health check
5. ✅ Cambia tráfico a nueva versión

---

## 📊 MONITOREO

### Ver Métricas
En Railway Dashboard:
- **CPU Usage:** Uso de CPU en tiempo real
- **Memory Usage:** Uso de memoria
- **Network:** Tráfico entrante/saliente
- **Logs:** Logs en tiempo real

### Ver Logs en Tiempo Real
```
Settings > Deployments > [Último deploy] > View Logs
```

---

## 🆓 LÍMITES DEL PLAN GRATUITO

Railway ofrece **500 horas gratis/mes**:
- ✅ Suficiente para proyectos universitarios
- ✅ Suficiente para demos
- ✅ ~20 días de servicio 24/7
- ✅ NO requiere tarjeta de crédito

**Cómo optimizar horas:**
- La app solo consume horas cuando está ejecutándose
- Puedes pausar el servicio cuando no lo uses
- Railway hiberna servicios inactivos (puedes desactivar esto)

---

## 🐛 TROUBLESHOOTING

### Problema: "Build Failed"
**Solución:**
1. Verifica que el Dockerfile esté en la raíz del proyecto
2. Revisa los logs de build en Railway
3. Asegúrate que `demo/build.gradle` existe

### Problema: "Application Error" o 503
**Solución:**
1. Verifica las variables de entorno (especialmente la contraseña de DB)
2. Revisa los logs: puede ser error de conexión a Supabase
3. Verifica que Supabase permita conexiones externas

### Problema: "Cannot connect to database"
**Solución:**
1. Verifica la URL de Supabase en las variables
2. Asegúrate que la IP de Railway no esté bloqueada en Supabase
3. Supabase permite todas las IPs por defecto, verifica en:
   - Supabase Dashboard > Settings > Database > Connection Pooling

### Problema: "Out of Memory"
**Solución:**
1. Reduce `JAVA_OPTS`:
   ```env
   JAVA_OPTS=-Xmx400m -Xms200m
   ```
2. Railway free tier tiene 512MB RAM
3. Spring Boot + PostgreSQL driver ~300-400MB

### Problema: "Port binding error"
**Solución:**
- Asegúrate que la variable `PORT=8080` está configurada
- El Dockerfile ya está configurado para usar `$PORT`

---

## 📝 CHECKLIST PRE-DEPLOY

Antes de desplegar, verifica:

- [ ] Código subido a GitHub
- [ ] Cuenta de Railway creada
- [ ] Repositorio conectado a Railway
- [ ] Variables de entorno configuradas:
  - [ ] SPRING_PROFILES_ACTIVE
  - [ ] SPRING_DATASOURCE_URL
  - [ ] SPRING_DATASOURCE_USERNAME
  - [ ] SPRING_DATASOURCE_PASSWORD
  - [ ] PORT
- [ ] Base de datos Supabase accesible
- [ ] Dominio generado en Railway

---

## 🎓 RECURSOS ADICIONALES

- **Documentación Railway:** https://docs.railway.app
- **Railway Discord:** https://discord.gg/railway
- **GitHub de tu proyecto:** https://github.com/TU_USUARIO/travel4u-app
- **Dashboard Railway:** https://railway.app/dashboard

---

## ✅ RESULTADO ESPERADO

Después de seguir todos los pasos:

1. ✅ Tu aplicación estará disponible en: `https://tu-app.up.railway.app`
2. ✅ Deploy automático con cada push a GitHub
3. ✅ Logs y métricas en tiempo real
4. ✅ SSL/HTTPS automático
5. ✅ 500 horas gratis/mes

---

## 🆘 ¿NECESITAS AYUDA?

Si encuentras algún problema:

1. **Revisa los logs en Railway** (99% de los errores están allí)
2. **Verifica las variables de entorno** (errores comunes)
3. **Prueba la conexión a Supabase** localmente primero
4. **Revisa este documento** de troubleshooting

---

## 📞 SIGUIENTE PASO

Una vez desplegado, puedes:
- Compartir la URL con tu profesor/equipo
- Configurar dominio personalizado
- Añadir más servicios (Redis, etc.)
- Configurar CI/CD avanzado

**¡Buena suerte con tu despliegue! 🚀**

