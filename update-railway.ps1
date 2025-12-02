# ==========================================
# SCRIPT PARA ACTUALIZAR CODIGO EN GITHUB
# Despues de correcciones
# ==========================================

Write-Host "`n========================================" -ForegroundColor Cyan
Write-Host "   Actualizando Railway Deploy" -ForegroundColor Cyan
Write-Host "========================================" -ForegroundColor Cyan
Write-Host ""

# Verificar que estamos en la carpeta correcta
if (-Not (Test-Path "demo\src")) {
    Write-Host "ERROR: Debes ejecutar este script desde la raiz del proyecto" -ForegroundColor Red
    exit 1
}

Write-Host "Archivos corregidos:" -ForegroundColor Green
Write-Host "  - Dockerfile (rutas de Gradle corregidas)" -ForegroundColor White
Write-Host "  - .dockerignore (gradle-wrapper.jar incluido)" -ForegroundColor White
Write-Host ""

# Verificar estado de Git
Write-Host "Verificando cambios..." -ForegroundColor Yellow
$hasChanges = git status --porcelain
if (-Not $hasChanges) {
    Write-Host "No hay cambios para subir." -ForegroundColor Gray
    exit 0
}

Write-Host "Cambios detectados`n" -ForegroundColor Cyan

# Añadir archivos
Write-Host "Agregando archivos corregidos..." -ForegroundColor Yellow
git add Dockerfile
git add .dockerignore

# Commit
$commitMsg = "Fix: Corregir Dockerfile y dockerignore para Railway build"
Write-Host "Haciendo commit: $commitMsg" -ForegroundColor Yellow
git commit -m "$commitMsg"

if ($LASTEXITCODE -ne 0) {
    Write-Host "Error al hacer commit" -ForegroundColor Red
    exit 1
}

# Push
Write-Host "`nSubiendo cambios a GitHub..." -ForegroundColor Yellow
git push origin main

if ($LASTEXITCODE -eq 0) {
    Write-Host ""
    Write-Host "========================================" -ForegroundColor Green
    Write-Host "   Cambios subidos exitosamente!" -ForegroundColor Green
    Write-Host "========================================" -ForegroundColor Green
    Write-Host ""
    Write-Host "QUE PASARA AHORA:" -ForegroundColor Yellow
    Write-Host ""
    Write-Host "1. Railway detectara el push automaticamente" -ForegroundColor Cyan
    Write-Host "2. Comenzara un nuevo build en 10-30 segundos" -ForegroundColor Cyan
    Write-Host "3. Esta vez el build deberia completarse correctamente" -ForegroundColor Cyan
    Write-Host ""
    Write-Host "MONITOREAR EL BUILD:" -ForegroundColor Yellow
    Write-Host "1. Ve a: https://railway.app/dashboard" -ForegroundColor Cyan
    Write-Host "2. Selecciona tu proyecto" -ForegroundColor Cyan
    Write-Host "3. Pestaña 'Deployments'" -ForegroundColor Cyan
    Write-Host "4. Ve el progreso en tiempo real" -ForegroundColor Cyan
    Write-Host ""
    Write-Host "BUSCA EN LOS LOGS:" -ForegroundColor Yellow
    Write-Host "  'BUILD SUCCESSFUL'" -ForegroundColor Green
    Write-Host "  'Started DemoApplication in X.XXX seconds'" -ForegroundColor Green
    Write-Host ""
    Write-Host "Si el build falla nuevamente, revisa los logs en Railway." -ForegroundColor Gray
    Write-Host ""
} else {
    Write-Host ""
    Write-Host "========================================" -ForegroundColor Red
    Write-Host "   Error al subir a GitHub" -ForegroundColor Red
    Write-Host "========================================" -ForegroundColor Red
    Write-Host ""
    Write-Host "Intenta manualmente:" -ForegroundColor Yellow
    Write-Host "  git push origin main" -ForegroundColor White
    Write-Host ""
}

