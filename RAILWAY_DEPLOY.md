# 🚀 DESPLEGAR EN RAILWAY - GUÍA DEFINITIVA

## PASO 1: SUBIR CÓDIGO A GITHUB

### ✅ Opción A - Script con Verificación (RECOMENDADO):
```powershell
cd "C:\Users\LENOVO\Documents\utp\ciclo7\integrador\demo (1)"
.\deploy.ps1
```

Este script:
- ✅ Verifica que todo esté correcto
- ✅ Limpia archivos obsoletos
- ✅ Sube el código a GitHub
- ✅ Te dice exactamente qué hacer después


### Opción B - Manual:
```powershell
# Si no tienes el repo en GitHub aún:
# 1. Crear repo: https://github.com/new (nombre: travel4u-app)

# 2. Configurar y subir
git remote add origin https://github.com/TU_USUARIO/travel4u-app.git
git branch -M main
git add -A
git commit -m "Deploy to Railway"
git push -u origin main

# Si ya tienes el repo configurado:
git add -A
git commit -m "Fix Railway build"
git push origin main
```

---

## PASO 2: CREAR PROYECTO EN RAILWAY

1. Ve a: **https://railway.app**
2. Login con GitHub
3. **New Project** → **Deploy from GitHub repo**
4. Selecciona: **travel4u-app**
5. Railway detectará el Dockerfile y comenzará a construir

---

## PASO 3: CONFIGURAR VARIABLES DE ENTORNO ⚠️ CRÍTICO

**🔴 SIN ESTAS VARIABLES LA APP NO FUNCIONA - CRASHEARÁ**

En Railway → Tu proyecto → **Variables**, añade estas 5 variables:

### Variable 1: SPRING_PROFILES_ACTIVE
```
SPRING_PROFILES_ACTIVE=heroku
```

### Variable 2: SPRING_DATASOURCE_URL
```
SPRING_DATASOURCE_URL=jdbc:postgresql://db.tiifltprjgtyfimhnezi.supabase.co:5432/postgres
```
⚠️ Si tu URL de Supabase es diferente, cámbiala aquí.

### Variable 3: SPRING_DATASOURCE_USERNAME
```
SPRING_DATASOURCE_USERNAME=postgres
```

### Variable 4: SPRING_DATASOURCE_PASSWORD 🔴 REQUERIDO
```
SPRING_DATASOURCE_PASSWORD=tu_password_real_aqui
```
⚠️ **REEMPLAZA** `tu_password_real_aqui` con tu contraseña **REAL** de Supabase.

**¿Dónde encontrar tu contraseña?**
1. Ve a: https://supabase.com/dashboard
2. Selecciona tu proyecto
3. Settings → Database
4. "Database Password" (si no la recuerdas, resetéala)

### Variable 5: PORT
```
PORT=8080
```

---

### ✅ VERIFICAR QUE LAS 5 VARIABLES ESTÉN CONFIGURADAS

Railway → Tu proyecto → Variables → Deberías ver:
- ✅ SPRING_PROFILES_ACTIVE = heroku
- ✅ SPRING_DATASOURCE_URL = jdbc:postgresql://...
- ✅ SPRING_DATASOURCE_USERNAME = postgres
- ✅ SPRING_DATASOURCE_PASSWORD = ********
- ✅ PORT = 8080

**Después de añadir las variables, Railway redesplegarás automáticamente.**

---

## PASO 4: GENERAR DOMINIO

1. En Railway → Tu proyecto → **Settings** → **Domains**
2. Click **Generate Domain**
3. Tu app estará en: `https://tu-app.up.railway.app`

---

## ✅ VERIFICAR DEPLOY EXITOSO

En Railway → **Deployments** → Último deploy → **View Logs**

Busca:
```
✅ BUILD SUCCESSFUL
✅ Started DemoApplication in X.XXX seconds
```

---

## 🔄 ACTUALIZAR APP (Deploys futuros)

Cada vez que hagas cambios:
```powershell
git add -A
git commit -m "Descripcion de cambios"
git push origin main
```

Railway redesplegarás automáticamente en 3-5 minutos.

---

## 🐛 SOLUCIÓN DE PROBLEMAS

### Error: "The connection attempt failed" ⚠️ MÁS COMÚN

**Síntomas:**
```
SQL Error: 0, SQLState: 08001
The connection attempt failed
Could not obtain connection to query metadata
```

**Causa:** Variables de entorno **NO configuradas** o incorrectas.

**Solución:**
1. Ve a Railway → Tu proyecto → **Variables**
2. Verifica que las 5 variables existan:
   - SPRING_PROFILES_ACTIVE
   - SPRING_DATASOURCE_URL
   - SPRING_DATASOURCE_USERNAME
   - SPRING_DATASOURCE_PASSWORD ← **Verifica especialmente esta**
   - PORT
3. Si faltan, añádelas (ver Paso 3)
4. Railway redesplegarás automáticamente
5. Espera 2-3 minutos y revisa los logs

**Checklist de verificación:**
- [ ] ¿La contraseña de Supabase es correcta?
- [ ] ¿La URL incluye el puerto `:5432`?
- [ ] ¿No hay espacios extras en las variables?
- [ ] ¿Supabase permite conexiones externas? (debería ser Sí por defecto)

---

### Error: "Unable to access jarfile gradle-wrapper.jar"

**Ya está solucionado en tu proyecto.** Si aún aparece:

1. Verifica que existe:
```powershell
Test-Path "demo\gradle\wrapper\gradle-wrapper.jar"
```

2. Si muestra `False`, regenera el wrapper:
```powershell
cd demo
.\gradlew wrapper
cd ..
```

3. Vuelve a subir:
```powershell
git add -A
git commit -m "Fix gradle wrapper"
git push origin main
```

### Error: "Cannot connect to database"

- Verifica la contraseña de Supabase en las variables
- Verifica la URL de conexión
- Asegúrate que Supabase permite conexiones externas (Settings → Database)

### Error: "Out of Memory"

Reduce el JAVA_OPTS en las variables:
```
JAVA_OPTS=-Xmx400m -Xms200m
```

---

## 📋 RESUMEN

| Paso | Acción | Tiempo |
|------|--------|--------|
| 1 | Subir a GitHub | 2 min |
| 2 | Crear proyecto Railway | 1 min |
| 3 | Configurar variables | 2 min |
| 4 | Generar dominio | 1 min |
| | **Build inicial** | 5-8 min |
| | **TOTAL** | ~15 min |

---

## 🆓 PLAN GRATUITO

- 500 horas/mes gratis
- NO requiere tarjeta de crédito
- Deploy automático
- SSL/HTTPS incluido

---

## 📞 SOPORTE

- Dashboard: https://railway.app/dashboard
- Docs: https://docs.railway.app
- Discord: https://discord.gg/railway

---

**¿Listo? Ejecuta: `.\deploy.ps1` 🚀**

