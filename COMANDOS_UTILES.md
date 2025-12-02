# 🛠️ Comandos Útiles para Railway Deploy

## 📋 COMANDOS GIT

### Verificar estado actual
```powershell
git status
```

### Ver configuración de remotes
```powershell
git remote -v
```

### Ver rama actual
```powershell
git branch
```

### Cambiar a rama main
```powershell
git branch -M main
```

### Añadir todos los cambios
```powershell
git add .
```

### Hacer commit
```powershell
git commit -m "Descripción de cambios"
```

### Subir a GitHub
```powershell
git push -u origin main
```

### Forzar push (si hay conflictos)
```powershell
git push -u origin main --force
```
**⚠️ Cuidado: Esto sobrescribe el historial remoto**

---

## 🔧 COMANDOS DE VERIFICACIÓN

### Verificar que Gradle funciona
```powershell
cd demo
.\gradlew --version
```

### Compilar el proyecto localmente
```powershell
cd demo
.\gradlew clean build -x test
```

### Ver el JAR generado
```powershell
dir demo\build\libs\*.jar
```

### Verificar Dockerfile
```powershell
docker build -t travel4u-test .
```

---

## 🗃️ COMANDOS BASE DE DATOS

### Probar conexión a Supabase (requiere psql)
```powershell
psql "postgresql://postgres:TU_PASSWORD@db.tiifltprjgtyfimhnezi.supabase.co:5432/postgres"
```

### Verificar tablas desde SQL
```sql
\dt
SELECT * FROM servicio LIMIT 5;
SELECT * FROM proveedor LIMIT 5;
```

---

## 🚀 REDEPLOY RÁPIDO

### Hacer cambio y redesplegar
```powershell
# 1. Hacer cambios en tu código

# 2. Commit y push
git add .
git commit -m "Cambios realizados"
git push origin main

# 3. Railway redesplegarás automáticamente
```

---

## 🔍 TROUBLESHOOTING

### Si Git no reconoce cambios
```powershell
git add -A
git status
```

### Si hay conflictos de merge
```powershell
git pull origin main --rebase
# Resolver conflictos manualmente
git add .
git rebase --continue
git push origin main
```

### Si necesitas resetear a último commit
```powershell
git reset --hard HEAD
```

### Ver historial de commits
```powershell
git log --oneline -10
```

### Deshacer último commit (mantener cambios)
```powershell
git reset --soft HEAD~1
```

---

## 📦 COMANDOS DOCKER LOCALES

### Construir imagen
```powershell
docker build -t travel4u-local .
```

### Ejecutar contenedor localmente
```powershell
docker run -p 8080:8080 `
  -e SPRING_PROFILES_ACTIVE=heroku `
  -e SPRING_DATASOURCE_URL="jdbc:postgresql://db.tiifltprjgtyfimhnezi.supabase.co:5432/postgres" `
  -e SPRING_DATASOURCE_USERNAME=postgres `
  -e SPRING_DATASOURCE_PASSWORD=TU_PASSWORD `
  travel4u-local
```

### Ver logs del contenedor
```powershell
docker logs [CONTAINER_ID]
```

### Detener contenedor
```powershell
docker stop [CONTAINER_ID]
```

---

## 🌐 VERIFICAR DEPLOY EN RAILWAY

### Probar endpoints después del deploy
```powershell
# Verificar que la app responde
Invoke-WebRequest -Uri "https://tu-app.up.railway.app/api/servicios/count"

# Ver respuesta formateada
(Invoke-WebRequest -Uri "https://tu-app.up.railway.app/api/servicios").Content | ConvertFrom-Json
```

---

## 📊 LOGS Y MONITORING

### Railway CLI (si lo instalas)
```bash
# Instalar Railway CLI
npm i -g @railway/cli

# Login
railway login

# Ver logs en tiempo real
railway logs

# Abrir app en navegador
railway open
```

---

## 🔄 ROLLBACK EN RAILWAY

Si un deploy falla:

1. Ve a Railway Dashboard
2. Deployments → Ver historial
3. Click en un deploy anterior exitoso
4. "Redeploy" desde ese punto

---

## 🧹 LIMPIEZA

### Limpiar builds de Gradle
```powershell
cd demo
.\gradlew clean
```

### Limpiar imágenes Docker locales
```powershell
docker system prune -a
```

### Eliminar archivos temporales
```powershell
Remove-Item -Recurse -Force demo\build
Remove-Item -Recurse -Force demo\.gradle
```

---

## 📝 NOTAS

- Todos los comandos PowerShell deben ejecutarse desde la raíz del proyecto
- Para comandos dentro de `demo/`, primero navega: `cd demo`
- Railway redespliega automáticamente en cada push a `main`
- Los logs de Railway son en tiempo real y muy útiles para debugging

---

**💡 TIP:** Guarda este archivo para referencia rápida durante el desarrollo

