# 🚀 Travel4U - Despliegue en Render

## 📋 Pasos para desplegar en Render

### 1. Preparar el repositorio
```bash
# Subir código a GitHub
git add .
git commit -m "Preparado para Render"
git push origin main
```

### 2. Configurar en Render
1. Ve a [render.com](https://render.com) y crea una cuenta
2. Conecta tu repositorio de GitHub
3. Crea un nuevo **Web Service**
4. Selecciona tu repositorio `travel4u`

### 3. Configuración del servicio
- **Name**: `travel4u`
- **Environment**: `Docker`
- **Dockerfile Path**: `./Dockerfile.render`
- **Plan**: `Free`

### 4. Variables de entorno
Configura estas variables en Render:
```
SPRING_PROFILES_ACTIVE=render
JAVA_OPTS=-Xmx512m -Xms256m -XX:+UseG1GC
```

### 5. Desplegar
- Haz clic en **Create Web Service**
- Render construirá y desplegará automáticamente
- La URL será: `https://travel4u-[random].onrender.com`

## 🧪 Probar localmente antes del despliegue
```bash
# Windows
build-render.bat

# O manualmente
docker build -f Dockerfile.render -t travel4u-render .
docker run -p 8081:8081 -e PORT=8081 travel4u-render
```

## 📊 Características del despliegue
- ✅ **Base de datos**: H2 en memoria (sin PostgreSQL externo)
- ✅ **Puerto dinámico**: Se adapta al puerto de Render
- ✅ **Memoria optimizada**: 512MB máximo
- ✅ **Logs simplificados**: Para mejor rendimiento
- ✅ **Health check**: Endpoint `/` para monitoreo

## 🔧 Configuración automática
El archivo `render.yaml` permite despliegue automático desde GitHub.

## 📝 Notas importantes
- **Plan Free**: 750 horas/mes, se duerme después de 15 min de inactividad
- **Base de datos**: Los datos se pierden al reiniciar (H2 en memoria)
- **Primer arranque**: Puede tomar 2-3 minutos en plan gratuito
- **URL**: Render asigna una URL automáticamente

## 🚨 Limitaciones del plan gratuito
- Se duerme después de 15 minutos sin actividad
- 750 horas de uso por mes
- Arranque lento después de dormir
- Sin base de datos persistente

## 🔄 Actualizaciones
Cada push a `main` desplegará automáticamente en Render.