# 🗑️ ARCHIVOS ELIMINADOS - DOCUMENTACIÓN

Este documento lista todos los archivos obsoletos que se eliminaron y explica por qué.

---

## 📝 DOCUMENTACIÓN OBSOLETA DE RAILWAY

**Eliminados:**
- `RAILWAY_QUICKSTART.md`
- `GUIA_DESPLIEGUE_RAILWAY.md`
- `RAILWAY_VARIABLES.md`
- `FLUJO_DEPLOY.md`
- `FIX_GRADLE_ERROR.md`
- `DONDE_EJECUTAR.md`
- `DEPLOY-RAILWAY.md`
- `COMANDOS_UTILES.md`

**Razón:** Múltiples guías duplicadas que causaban confusión. Ahora solo existe `RAILWAY_DEPLOY.md` con toda la información necesaria.

---

## 🌐 ARCHIVOS DE RENDER (Plataforma no usada)

**Eliminados:**
- `DEPLOY-RENDER.md`
- `README-RENDER.md`
- `build-render.bat`
- `render.yaml`
- `demo/src/main/resources/application-render.properties`

**Razón:** Render es otra plataforma de hosting. Solo usaremos Railway para simplicidad.

---

## 📜 SCRIPTS OBSOLETOS

**Eliminados:**
- `setup-railway.ps1`
- `update-railway.ps1`
- `railway-deploy.ps1`
- `setup-heroku.ps1`
- `deploy-heroku.ps1`

**Razón:** Múltiples scripts que hacían lo mismo. Ahora solo existe `deploy.ps1` que hace todo.

---

## 🐘 ARCHIVOS DE HEROKU (Plataforma no usada)

**Eliminados:**
- `heroku.yml`
- `setup-heroku.ps1`
- `deploy-heroku.ps1`

**Razón:** Heroku ya no tiene plan gratuito. Usamos Railway en su lugar.

**NOTA:** Se mantiene `application-heroku.yml` porque Railway usa las mismas variables de entorno.

---

## 🗄️ SQL OBSOLETOS

**Eliminados:**
- `TravelReserva.sql` (raíz)
- `init-db.sql` (raíz)
- `fix_sequence.sql` (raíz)
- `demo/fix_sequence.sql` (duplicado)
- `demo/src/main/resources/data-h2.sql`
- `demo/src/main/resources/data-fixed.sql`
- `demo/src/main/resources/add_more_services.sql`
- `demo/src/main/resources/add_missing_services.sql`

**Razón:** 
- Migraciones manuales ya aplicadas
- Duplicados innecesarios
- Datos de prueba obsoletos

**MANTIENE:**
- `demo/src/main/resources/data.sql` - Datos iniciales actuales
- `demo/src/main/resources/db/migration/` - Flyway migrations (activas)

---

## ⚙️ PROPERTIES OBSOLETOS

**Eliminados:**
- `application-render.properties` (plataforma no usada)
- `application-postgres.properties` (duplicado)
- `application-docker.properties` (no necesario)
- `application-h2.properties` (no usamos H2)

**Razón:** Perfiles de Spring Boot no utilizados o duplicados.

**MANTIENE:**
- `application.properties` - Configuración principal
- `application-heroku.yml` - Para Railway/producción (las variables son compatibles)

---

## 🔧 ARCHIVOS DE DEBUGGING

**Eliminados:**
- `demo/DATABASE_FIX_GUIDE.md`
- `demo/test-database.bat`

**Razón:** Guías de debugging temporales que ya no son necesarias. Los problemas de BD ya están resueltos.

---

## ✅ ARCHIVOS QUE SE MANTIENEN

### Raíz del Proyecto:
- `README.md` - Documentación principal
- `RAILWAY_DEPLOY.md` - Guía única de despliegue
- `deploy.ps1` - Script principal de deploy
- `limpiar.ps1` - Script de limpieza
- `Dockerfile` - Configuración Docker
- `.dockerignore` - Archivos ignorados por Docker
- `docker-compose.yml` - Para desarrollo local con Docker
- `docker-run.bat` / `docker-run.sh` - Scripts helper para Docker

### Código Fuente (demo/):
- `src/main/java/` - Todo el código Java
- `src/main/resources/application.properties` - Config principal
- `src/main/resources/application-heroku.yml` - Config producción
- `src/main/resources/data.sql` - Datos iniciales
- `src/main/resources/templates/` - Plantillas Thymeleaf
- `src/main/resources/static/` - CSS, JS, imágenes
- `src/main/resources/reports/` - Reportes JasperReports
- `src/main/resources/db/migration/` - Migraciones Flyway
- `build.gradle` - Configuración del proyecto
- `gradlew` / `gradlew.bat` - Gradle wrapper

---

## 🎯 RESUMEN

**Total eliminados:** ~30 archivos obsoletos

**Beneficios:**
- ✅ Proyecto más limpio y organizado
- ✅ Sin duplicados ni confusión
- ✅ Solo una guía de deploy (RAILWAY_DEPLOY.md)
- ✅ Solo un script de deploy (deploy.ps1)
- ✅ Fácil de mantener

---

## 🔄 SI NECESITAS RECUPERAR ALGO

Si por error se eliminó algo importante, puedes recuperarlo de Git:

```powershell
# Ver archivos eliminados
git log --diff-filter=D --summary

# Recuperar un archivo específico
git checkout HEAD~1 -- ruta/al/archivo
```

---

**Última limpieza:** 2025-12-02

