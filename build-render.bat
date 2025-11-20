@echo off
echo ========================================
echo    Travel4U - Build for Render
echo ========================================

echo.
echo [1/3] Construyendo imagen Docker para Render...
docker build -t travel4u-render .

echo.
echo [2/3] Probando imagen localmente...
docker run -d -p 8081:8081 -e PORT=8081 --name travel4u-test travel4u-render

echo.
echo [3/3] Esperando que la aplicacion inicie...
timeout /t 15 /nobreak > nul

echo.
echo ========================================
echo    Build completado!
echo ========================================
echo.
echo Para probar localmente: http://localhost:8081
echo Para detener: docker stop travel4u-test
echo Para limpiar: docker rm travel4u-test
echo.
echo Pasos para Render:
echo 1. Sube el codigo a GitHub
echo 2. Conecta el repositorio en Render
echo 3. Render detectará automáticamente el Dockerfile
echo 4. Configura las variables de entorno
echo.
pause