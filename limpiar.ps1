# ==========================================
# LIMPIEZA COMPLETA DE ARCHIVOS OBSOLETOS
# ==========================================

Write-Host "`n========================================" -ForegroundColor Cyan
Write-Host "   LIMPIEZA COMPLETA" -ForegroundColor Cyan
Write-Host "========================================" -ForegroundColor Cyan
Write-Host ""

# Lista de archivos a eliminar (raiz del proyecto)
$archivosRaiz = @(
    # Documentacion obsoleta de Railway
    "RAILWAY_QUICKSTART.md",
    "GUIA_DESPLIEGUE_RAILWAY.md",
    "RAILWAY_VARIABLES.md",
    "FLUJO_DEPLOY.md",
    "FIX_GRADLE_ERROR.md",
    "DONDE_EJECUTAR.md",
    "DEPLOY-RAILWAY.md",
    "COMANDOS_UTILES.md",

    # Documentacion de Render (no usaremos)
    "DEPLOY-RENDER.md",
    "README-RENDER.md",

    # Scripts obsoletos
    "setup-railway.ps1",
    "update-railway.ps1",
    "railway-deploy.ps1",
    "setup-heroku.ps1",
    "deploy-heroku.ps1",

    # Archivos de otras plataformas
    "build-render.bat",
    "heroku.yml",
    "render.yaml",
    "setup-railway.ps1",

    # SQL obsoletos de migracion manual
    "TravelReserva.sql",
    "init-db.sql",
    "fix_sequence.sql"
)

# Archivos dentro de demo/
$archivosDentroDemo = @(
    "demo\DATABASE_FIX_GUIDE.md",
    "demo\test-database.bat",
    "demo\fix_sequence.sql"
)

# Archivos de resources obsoletos
$archivosResources = @(
    "demo\src\main\resources\data-h2.sql",
    "demo\src\main\resources\data-fixed.sql",
    "demo\src\main\resources\add_more_services.sql",
    "demo\src\main\resources\add_missing_services.sql",
    "demo\src\main\resources\application-render.properties",
    "demo\src\main\resources\application-postgres.properties",
    "demo\src\main\resources\application-docker.properties",
    "demo\src\main\resources\application-h2.properties"
)

# Combinar todas las listas
$archivosAEliminar = $archivosRaiz + $archivosDentroDemo + $archivosResources

$eliminados = 0
$noEncontrados = 0

Write-Host "Eliminando archivos obsoletos..." -ForegroundColor Yellow
Write-Host ""

Write-Host "[RAIZ DEL PROYECTO]" -ForegroundColor Cyan
foreach ($archivo in $archivosRaiz) {
    if (Test-Path $archivo) {
        try {
            Remove-Item $archivo -Force
            Write-Host "  [OK] $archivo" -ForegroundColor Green
            $eliminados++
        } catch {
            Write-Host "  [ERROR] $archivo" -ForegroundColor Red
        }
    } else {
        Write-Host "  [--] $archivo (no existe)" -ForegroundColor Gray
        $noEncontrados++
    }
}

Write-Host "`n[CARPETA DEMO]" -ForegroundColor Cyan
foreach ($archivo in $archivosDentroDemo) {
    if (Test-Path $archivo) {
        try {
            Remove-Item $archivo -Force
            Write-Host "  [OK] $archivo" -ForegroundColor Green
            $eliminados++
        } catch {
            Write-Host "  [ERROR] $archivo" -ForegroundColor Red
        }
    } else {
        Write-Host "  [--] $archivo (no existe)" -ForegroundColor Gray
        $noEncontrados++
    }
}

Write-Host "`n[RESOURCES]" -ForegroundColor Cyan
foreach ($archivo in $archivosResources) {
    if (Test-Path $archivo) {
        try {
            Remove-Item $archivo -Force
            Write-Host "  [OK] $archivo" -ForegroundColor Green
            $eliminados++
        } catch {
            Write-Host "  [ERROR] $archivo" -ForegroundColor Red
        }
    } else {
        Write-Host "  [--] $archivo (no existe)" -ForegroundColor Gray
        $noEncontrados++
    }
}

Write-Host ""
Write-Host "========================================" -ForegroundColor Cyan
Write-Host "   RESUMEN" -ForegroundColor Cyan
Write-Host "========================================" -ForegroundColor Cyan
Write-Host "Archivos eliminados: $eliminados" -ForegroundColor Green
Write-Host "Archivos no encontrados: $noEncontrados" -ForegroundColor Gray
Write-Host ""

if ($eliminados -gt 0) {
    Write-Host "ARCHIVOS ESENCIALES QUE QUEDAN:" -ForegroundColor Yellow
    Write-Host ""
    Write-Host "[RAIZ]" -ForegroundColor Cyan
    Write-Host "  - README.md" -ForegroundColor White
    Write-Host "  - RAILWAY_DEPLOY.md" -ForegroundColor White
    Write-Host "  - deploy.ps1" -ForegroundColor White
    Write-Host "  - limpiar.ps1" -ForegroundColor White
    Write-Host "  - Dockerfile" -ForegroundColor White
    Write-Host "  - .dockerignore" -ForegroundColor White
    Write-Host "  - docker-compose.yml (por si usas Docker local)" -ForegroundColor White
    Write-Host "  - docker-run.bat / docker-run.sh (scripts Docker)" -ForegroundColor White
    Write-Host ""
    Write-Host "[DEMO/SRC]" -ForegroundColor Cyan
    Write-Host "  - Codigo fuente Java" -ForegroundColor White
    Write-Host "  - application.properties (config principal)" -ForegroundColor White
    Write-Host "  - application-heroku.yml (para Railway/produccion)" -ForegroundColor White
    Write-Host "  - data.sql (datos iniciales)" -ForegroundColor White
    Write-Host "  - Templates Thymeleaf" -ForegroundColor White
    Write-Host "  - Reportes JasperReports" -ForegroundColor White
    Write-Host ""
}

Write-Host "Limpieza completada." -ForegroundColor Green
Write-Host ""
Write-Host "SIGUIENTE PASO:" -ForegroundColor Yellow
Write-Host "  Ejecuta: .\deploy.ps1" -ForegroundColor Cyan
Write-Host ""

