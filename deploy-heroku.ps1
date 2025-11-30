# ==========================================
# SCRIPT DE DESPLIEGUE EN HEROKU
# ==========================================
# Uso: .\deploy-heroku.ps1

Write-Host "========================================" -ForegroundColor Cyan
Write-Host "   Travel4U - Despliegue en Heroku" -ForegroundColor Cyan
Write-Host "========================================" -ForegroundColor Cyan
Write-Host ""

# Nombre de la app en Heroku (CAMBIA ESTO si usaste otro nombre)
$APP_NAME = "travel4u-integrador"

# Verificar que estamos en la carpeta correcta
if (-Not (Test-Path "demo\src")) {
    Write-Host "ERROR: Debes ejecutar este script desde la raíz del proyecto" -ForegroundColor Red
    exit 1
}

Write-Host "[1/5] Verificando login en Heroku..." -ForegroundColor Yellow
heroku whoami
if ($LASTEXITCODE -ne 0) {
    Write-Host "ERROR: No estás autenticado en Heroku. Ejecuta 'heroku login' primero" -ForegroundColor Red
    exit 1
}

Write-Host ""
Write-Host "[2/5] Agregando cambios a Git..." -ForegroundColor Yellow
git add .
git status

Write-Host ""
Write-Host "[3/5] Creando commit..." -ForegroundColor Yellow
$commitMessage = Read-Host "Mensaje del commit (Enter para usar mensaje automático)"
if ([string]::IsNullOrWhiteSpace($commitMessage)) {
    $commitMessage = "Deploy $(Get-Date -Format 'yyyy-MM-dd HH:mm:ss')"
}
git commit -m "$commitMessage"

Write-Host ""
Write-Host "[4/5] Desplegando en Heroku..." -ForegroundColor Yellow
git push heroku master

if ($LASTEXITCODE -eq 0) {
    Write-Host ""
    Write-Host "========================================" -ForegroundColor Green
    Write-Host "   ✓ Despliegue exitoso!" -ForegroundColor Green
    Write-Host "========================================" -ForegroundColor Green
    Write-Host ""
    Write-Host "[5/5] Abriendo aplicación..." -ForegroundColor Yellow
    heroku open -a $APP_NAME

    Write-Host ""
    Write-Host "Ver logs: heroku logs --tail -a $APP_NAME" -ForegroundColor Cyan
} else {
    Write-Host ""
    Write-Host "ERROR: El despliegue falló" -ForegroundColor Red
    Write-Host "Ver logs de error: heroku logs --tail -a $APP_NAME" -ForegroundColor Yellow
}

