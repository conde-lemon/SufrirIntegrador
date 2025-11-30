# ==========================================
# GUÍA DE DESPLIEGUE EN RAILWAY.APP
# ¡Sin tarjeta de crédito!
# ==========================================

## ¿POR QUÉ RAILWAY?
✅ 500 horas gratis/mes (sin tarjeta)
✅ Soporta Docker/Spring Boot
✅ Compatible con Supabase
✅ Deploy automático desde GitHub
✅ SSL/HTTPS gratis
✅ Fácil de usar

## PASOS PARA DESPLEGAR:

### 1. CREAR CUENTA EN RAILWAY
- Ve a: https://railway.app
- Click "Login" → "Login with GitHub"
- Autoriza Railway en GitHub

### 2. SUBIR CÓDIGO A GITHUB (si no lo has hecho)
```powershell
# Crear repo en GitHub: https://github.com/new
# Nombre: travel4u-app

cd "C:\Users\LENOVO\Documents\utp\ciclo7\integrador\demo (1)"

# Añadir remote
git remote add origin https://github.com/TU_USUARIO/travel4u-app.git

# Cambiar a rama main
git branch -M main

# Push
git push -u origin main
```

### 3. CREAR PROYECTO EN RAILWAY
1. En Railway Dashboard, click "New Project"
2. Selecciona "Deploy from GitHub repo"
3. Autoriza acceso al repo
4. Selecciona "travel4u-app"
5. Railway detectará automáticamente Dockerfile

### 4. CONFIGURAR VARIABLES DE ENTORNO
En la pestaña "Variables":
```
SPRING_PROFILES_ACTIVE=heroku
SPRING_DATASOURCE_URL=jdbc:postgresql://db.tiifltprjgtyfimhnezi.supabase.co:5432/postgres
SPRING_DATASOURCE_USERNAME=postgres
SPRING_DATASOURCE_PASSWORD=TU_PASSWORD_AQUI
PORT=8080
```

### 5. CONFIGURAR PUERTO
Railway usa puerto 8080 por defecto, pero tu app usa 8081.

Opción A: En Variables de Railway añade:
```
PORT=8080
```

Opción B: Modifica application-heroku.yml para usar $PORT

### 6. DEPLOY AUTOMÁTICO
- Railway automáticamente:
  1. Construye tu Dockerfile
  2. Despliega la app
  3. Te da una URL: https://travel4u-app-production.up.railway.app

### 7. VER LOGS
- Click en tu servicio
- Pestaña "Deployments"
- Click en el último deploy
- Ver logs en tiempo real

## VENTAJAS:
✅ 500 horas gratis/mes (vs 550 de Heroku)
✅ NO requiere tarjeta de crédito
✅ Deploy automático con cada push
✅ Logs en tiempo real
✅ Métricas de CPU/RAM
✅ Dominios custom gratis

## LÍMITES DEL PLAN GRATUITO:
- 500 horas de ejecución/mes
- 512 MB RAM
- 1 GB disco
- Perfecto para demos/proyectos universitarios

## REDEPLOY AUTOMÁTICO:
Cada vez que hagas:
```powershell
git add .
git commit -m "Cambios"
git push origin main
```
Railway automáticamente redesplega tu app.

## TROUBLESHOOTING:

### Si falla el build:
1. Verificar que Dockerfile esté en la raíz del repo
2. Verificar logs en Railway
3. Asegurar que variables de entorno estén configuradas

### Si la app no responde:
1. Verificar logs
2. Asegurar que PORT esté configurado correctamente
3. Verificar conexión con Supabase

## COMANDOS ÚTILES:
```powershell
# Ver estado del repo
git status

# Ver logs de Railway
# (desde el dashboard web)

# Forzar redeploy
git commit --allow-empty -m "Trigger redeploy"
git push origin main
```

