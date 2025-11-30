# ==========================================
# GUÍA DE DESPLIEGUE EN RENDER
# (Alternativa gratuita a Heroku)
# ==========================================

## ¿POR QUÉ RENDER?
- ✅ 100% GRATUITO (sin tarjeta de crédito)
- ✅ Más fácil que Heroku
- ✅ Soporte para Docker
- ✅ SSL/HTTPS automático
- ✅ Deploy automático desde Git

## PASOS PARA DESPLEGAR EN RENDER:

### 1. CREAR CUENTA EN RENDER
- Ve a: https://render.com/
- Click en "Get Started for Free"
- Usa tu cuenta de GitHub o email

### 2. CONECTAR REPOSITORIO GITHUB
- Si no tienes el código en GitHub:
  ```powershell
  # Crear repositorio en GitHub primero (https://github.com/new)
  # Luego:
  git remote add origin https://github.com/TU_USUARIO/travel4u-app.git
  git branch -M main
  git push -u origin main
  ```

### 3. CREAR WEB SERVICE EN RENDER
1. En Dashboard de Render, click "New +"
2. Selecciona "Web Service"
3. Conecta tu repositorio de GitHub
4. Selecciona el repo "travel4u-app"
5. Configura:
   - **Name:** travel4u-app
   - **Region:** Oregon (US West) o Frankfurt (Europe)
   - **Branch:** main
   - **Runtime:** Docker
   - **Plan:** Free

### 4. CONFIGURAR VARIABLES DE ENTORNO
En la sección "Environment":
- SPRING_PROFILES_ACTIVE = heroku
- SPRING_DATASOURCE_URL = jdbc:postgresql://db.tiifltprjgtyfimhnezi.supabase.co:5432/postgres
- SPRING_DATASOURCE_USERNAME = postgres
- SPRING_DATASOURCE_PASSWORD = TU_PASSWORD_AQUI

### 5. DESPLEGAR
- Click en "Create Web Service"
- Render automáticamente:
  1. Clona tu repo
  2. Construye el Docker image
  3. Despliega la app
  4. Te da una URL: https://travel4u-app.onrender.com

### 6. DESPLIEGUES AUTOMÁTICOS
- Cada vez que hagas `git push origin main`
- Render automáticamente redesplega tu app

## VENTAJAS DE RENDER:
✅ No requiere tarjeta de crédito
✅ SSL/HTTPS gratis
✅ Deploy automático
✅ Logs en tiempo real
✅ Health checks automáticos
✅ 750 horas gratis/mes

## NOTAS:
- El plan Free se "duerme" después de 15 min de inactividad
- Primer request puede tardar ~30 segundos en "despertar"
- Perfecto para demos y proyectos universitarios

