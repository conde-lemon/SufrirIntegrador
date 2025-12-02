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
cd "C:\Users\LENOVO\Documents\utp\ciclo7\integrador\demo (1)"
.\railway-deploy.ps1
```

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

## PASO 3: CONFIGURAR VARIABLES DE ENTORNO

En Railway → Tu proyecto → **Variables**, añade:

```
SPRING_PROFILES_ACTIVE=heroku
SPRING_DATASOURCE_URL=jdbc:postgresql://db.tiifltprjgtyfimhnezi.supabase.co:5432/postgres
SPRING_DATASOURCE_USERNAME=postgres
SPRING_DATASOURCE_PASSWORD=TU_PASSWORD_DE_SUPABASE
PORT=8080
```

⚠️ **IMPORTANTE:** Reemplaza `TU_PASSWORD_DE_SUPABASE` con tu contraseña real.

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

**¿Listo? Ejecuta: `.\railway-deploy.ps1` 🚀**

