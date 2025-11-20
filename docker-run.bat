@echo off
echo ========================================
echo    Travel4U - Docker Deployment
echo ========================================

echo.
echo [1/4] Deteniendo contenedores existentes...
docker-compose down

echo.
echo [2/4] Construyendo imagenes...
docker-compose build --no-cache

echo.
echo [3/4] Iniciando servicios...
docker-compose up -d

echo.
echo [4/4] Verificando estado de los servicios...
timeout /t 10 /nobreak > nul
docker-compose ps

echo.
echo ========================================
echo    Despliegue completado!
echo ========================================
echo.
echo Aplicacion disponible en: http://localhost:8081
echo Base de datos PostgreSQL en: localhost:5432
echo.
echo Para ver logs: docker-compose logs -f travel4u-app
echo Para detener: docker-compose down
echo.
pause