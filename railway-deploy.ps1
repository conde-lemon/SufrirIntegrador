# ==========================================
# LIMPIAR Y DESPLEGAR EN RAILWAY
# Script completo de preparacion
# ==========================================

Write-Host "`n========================================" -ForegroundColor Cyan
Write-Host "   Limpieza y Deploy Railway" -ForegroundColor Cyan
Write-Host "========================================" -ForegroundColor Cyan
Write-Host ""

# Verificar que estamos en la carpeta correcta
if (-Not (Test-Path "demo\src")) {
    Write-Host "ERROR: Ejecuta desde la raiz del proyecto" -ForegroundColor Red
    Write-Host "Ubicacion actual: $PWD" -ForegroundColor Yellow
    exit 1
}

# Eliminar archivos obsoletos
Write-Host "Eliminando archivos obsoletos..." -ForegroundColor Yellow
$archivosObsoletos = @(
    "RAILWAY_QUICKSTART.md",
    "GUIA_DESPLIEGUE_RAILWAY.md",
    "RAILWAY_VARIABLES.md",
    "FLUJO_DEPLOY.md",
    "FIX_GRADLE_ERROR.md",
    "DONDE_EJECUTAR.md",
    "DEPLOY-RAILWAY.md",
    "COMANDOS_UTILES.md"
)

foreach ($archivo in $archivosObsoletos) {
    if (Test-Path $archivo) {
        Remove-Item $archivo -Force
        Write-Host "  Eliminado: $archivo" -ForegroundColor Gray
    }
}
Write-Host "Archivos obsoletos eliminados`n" -ForegroundColor Green

# Verificar que gradle-wrapper.jar existe
Write-Host "Verificando gradle-wrapper.jar..." -ForegroundColor Yellow
if (Test-Path "demo\gradle\wrapper\gradle-wrapper.jar") {
    Write-Host "  OK: gradle-wrapper.jar existe`n" -ForegroundColor Green
} else {
    Write-Host "  ERROR: No se encuentra gradle-wrapper.jar" -ForegroundColor Red
    Write-Host "  Necesitas regenerar el wrapper de Gradle" -ForegroundColor Yellow
    exit 1
}

# Git: Añadir todos los cambios
Write-Host "Preparando cambios para Git..." -ForegroundColor Yellow
git add -A

# Git: Ver estado
$hasChanges = git status --porcelain
if (-Not $hasChanges) {
    Write-Host "No hay cambios para subir`n" -ForegroundColor Gray
} else {
    Write-Host "Cambios detectados`n" -ForegroundColor Cyan

    # Commit
    $commitMsg = "Fix Railway: Corregir Dockerfile y limpiar docs - $(Get-Date -Format 'yyyy-MM-dd HH:mm')"
    Write-Host "Commit: $commitMsg" -ForegroundColor Yellow
    git commit -m "$commitMsg"

    # Push
    Write-Host "`nSubiendo a GitHub..." -ForegroundColor Yellow
    git push origin main

    if ($LASTEXITCODE -eq 0) {
        Write-Host ""
        Write-Host "========================================" -ForegroundColor Green
        Write-Host "   CAMBIOS SUBIDOS EXITOSAMENTE" -ForegroundColor Green
        Write-Host "========================================" -ForegroundColor Green
        Write-Host ""
        Write-Host "QUE HACER AHORA:" -ForegroundColor Yellow
        Write-Host ""
        Write-Host "1. Ve a: https://railway.app/dashboard" -ForegroundColor Cyan
        Write-Host "2. Selecciona tu proyecto" -ForegroundColor Cyan
        Write-Host "3. Railway comenzara a reconstruir en 10-30 segundos" -ForegroundColor Cyan
        Write-Host "4. Ve a 'Deployments' para ver el progreso" -ForegroundColor Cyan
        Write-Host ""
        Write-Host "BUSCA EN LOS LOGS:" -ForegroundColor Yellow
        Write-Host "  'BUILD SUCCESSFUL'" -ForegroundColor Green
        Write-Host "  'Started DemoApplication'" -ForegroundColor Green
        Write-Host ""
        Write-Host "Si aun falla, lee: RAILWAY_DEPLOY.md" -ForegroundColor White
        Write-Host ""
    } else {
        Write-Host ""
        Write-Host "ERROR al subir a GitHub" -ForegroundColor Red
        Write-Host "Intenta manualmente: git push origin main" -ForegroundColor Yellow
        Write-Host ""
    }
}

