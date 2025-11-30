# ==========================================
# SCRIPT DE VERIFICACIÓN Y DESPLIEGUE HEROKU
# ==========================================

Write-Host "========================================" -ForegroundColor Cyan
Write-Host "   Travel4U - Setup Heroku" -ForegroundColor Cyan
Write-Host "========================================" -ForegroundColor Cyan
Write-Host ""

# Variables
$APP_NAME = "travel4u-integrador"
$SUPABASE_URL = "jdbc:postgresql://db.tiifltprjgtyfimhnezi.supabase.co:5432/postgres"

# 1. Verificar login
Write-Host "[1/7] Verificando login en Heroku..." -ForegroundColor Yellow
heroku whoami 2>$null
if ($LASTEXITCODE -ne 0) {
    Write-Host "ERROR: No estás logueado. Ejecutando login..." -ForegroundColor Red
    heroku login
}

# 2. Verificar que la app existe
Write-Host ""
Write-Host "[2/7] Verificando si la app existe..." -ForegroundColor Yellow
heroku apps:info -a $APP_NAME 2>$null
if ($LASTEXITCODE -ne 0) {
    Write-Host "La app no existe. Creándola..." -ForegroundColor Yellow
    Write-Host ""
    Write-Host "IMPORTANTE: Si da error 'verify your account':" -ForegroundColor Red
    Write-Host "1. Ve a: https://dashboard.heroku.com/account/billing" -ForegroundColor Yellow
    Write-Host "2. Añade método de pago (no te cobrarán nada)" -ForegroundColor Yellow
    Write-Host "3. Espera 5 minutos" -ForegroundColor Yellow
    Write-Host "4. Ejecuta este script nuevamente" -ForegroundColor Yellow
    Write-Host ""

    heroku create $APP_NAME
    if ($LASTEXITCODE -ne 0) {
        Write-Host ""
        Write-Host "ERROR: No se pudo crear la app" -ForegroundColor Red
        Write-Host "¿Ya verificaste tu cuenta en heroku.com/verify?" -ForegroundColor Yellow
        exit 1
    }
}

# 3. Configurar stack
Write-Host ""
Write-Host "[3/7] Configurando stack container..." -ForegroundColor Yellow
heroku stack:set container -a $APP_NAME

# 4. Configurar variables de entorno
Write-Host ""
Write-Host "[4/7] Configurando variables de entorno..." -ForegroundColor Yellow

# Pedir password de Supabase
$supabasePassword = Read-Host "Ingresa tu password de Supabase" -AsSecureString
$BSTR = [System.Runtime.InteropServices.Marshal]::SecureStringToBSTR($supabasePassword)
$password = [System.Runtime.InteropServices.Marshal]::PtrToStringAuto($BSTR)

heroku config:set SPRING_PROFILES_ACTIVE=heroku -a $APP_NAME
heroku config:set SPRING_DATASOURCE_URL="$SUPABASE_URL" -a $APP_NAME
heroku config:set SPRING_DATASOURCE_USERNAME="postgres" -a $APP_NAME
heroku config:set SPRING_DATASOURCE_PASSWORD="$password" -a $APP_NAME

# 5. Conectar Git remote
Write-Host ""
Write-Host "[5/7] Configurando Git remote..." -ForegroundColor Yellow
git remote remove heroku 2>$null
heroku git:remote -a $APP_NAME

# 6. Hacer commit de cambios pendientes
Write-Host ""
Write-Host "[6/7] Preparando código..." -ForegroundColor Yellow
git add .
$hasChanges = git status --porcelain
if ($hasChanges) {
    $commitMsg = Read-Host "Mensaje del commit (Enter para automático)"
    if ([string]::IsNullOrWhiteSpace($commitMsg)) {
        $commitMsg = "Deploy to Heroku $(Get-Date -Format 'yyyy-MM-dd HH:mm')"
    }
    git commit -m "$commitMsg"
}

# 7. Desplegar
Write-Host ""
Write-Host "[7/7] Desplegando en Heroku..." -ForegroundColor Yellow
Write-Host "Esto puede tardar varios minutos..." -ForegroundColor Cyan

# Obtener nombre de la rama actual
$currentBranch = git rev-parse --abbrev-ref HEAD

Write-Host "Rama actual: $currentBranch" -ForegroundColor Cyan
git push heroku ${currentBranch}:main

if ($LASTEXITCODE -eq 0) {
    Write-Host ""
    Write-Host "========================================" -ForegroundColor Green
    Write-Host "   ✓ Despliegue exitoso!" -ForegroundColor Green
    Write-Host "========================================" -ForegroundColor Green
    Write-Host ""
    Write-Host "Abriendo aplicación..." -ForegroundColor Yellow
    Start-Sleep -Seconds 2
    heroku open -a $APP_NAME

    Write-Host ""
    Write-Host "Ver logs: heroku logs --tail -a $APP_NAME" -ForegroundColor Cyan
} else {
    Write-Host ""
    Write-Host "ERROR: El despliegue falló" -ForegroundColor Red
    Write-Host "Ver logs: heroku logs --tail -a $APP_NAME" -ForegroundColor Yellow
}

