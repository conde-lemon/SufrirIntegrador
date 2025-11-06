@echo off
echo ===================================
echo TRAVEL4U - TEST DE BASE DE DATOS
echo ===================================
echo.

echo Ejecutando tests de conectividad...
echo.

cd /d "%~dp0"

echo 1. Test de conexion a PostgreSQL...
gradlew test --tests DatabaseConnectionTest

echo.
echo 2. Test de servicio de reservas...
gradlew test --tests ReservaServiceTest

echo.
echo 3. Test de generacion de reportes...
gradlew test --tests JasperReportServiceTest

echo.
echo ===================================
echo TESTS COMPLETADOS
echo ===================================
pause