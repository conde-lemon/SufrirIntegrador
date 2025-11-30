se salen # ==========================================
# SCRIPT DE DESPLIEGUE EN RAILWAY
# ==========================================

Write-Host "========================================" -ForegroundColor Cyan
Write-Host "   Travel4U - Setup Railway" -ForegroundColor Cyan
Write-Host "========================================" -ForegroundColor Cyan
Write-Host ""

# Verificar que estamos en la carpeta correcta
if (-Not (Test-Path "demo\src")) {
    Write-Host "ERROR: Debes ejecutar este script desde la raíz del proyecto" -ForegroundColor Red
    exit 1
}

Write-Host "PASOS PARA DESPLEGAR EN RAILWAY:" -ForegroundColor Yellow
Write-Host ""
Write-Host "1. Ve a: https://railway.app" -ForegroundColor Cyan
Write-Host "2. Login con GitHub" -ForegroundColor Cyan
Write-Host "3. Click 'New Project' → 'Deploy from GitHub repo'" -ForegroundColor Cyan
Write-Host ""
Write-Host "Ahora vamos a preparar tu código para GitHub..." -ForegroundColor Yellow
Write-Host ""

# Verificar si ya tiene remote de GitHub
$hasGitHub = git remote -v | Select-String "github"
if (-Not $hasGitHub) {
    Write-Host "No tienes configurado GitHub como remote." -ForegroundColor Yellow
    Write-Host ""
    Write-Host "Primero crea un repo en GitHub:" -ForegroundColor Cyan
    Write-Host "https://github.com/new" -ForegroundColor Cyan
    Write-Host ""

    $repoUrl = Read-Host "Ingresa la URL de tu repo de GitHub (ej: https://github.com/usuario/travel4u-app.git)"

    if ([string]::IsNullOrWhiteSpace($repoUrl)) {
        Write-Host "ERROR: Debes proporcionar la URL del repositorio" -ForegroundColor Red
        exit 1
    }

    Write-Host ""
    Write-Host "Configurando remote de GitHub..." -ForegroundColor Yellow
    git remote add origin $repoUrl
}

# Verificar rama
Write-Host ""
Write-Host "Preparando código para deploy..." -ForegroundColor Yellow
$currentBranch = git rev-parse --abbrev-ref HEAD
Write-Host "Rama actual: $currentBranch" -ForegroundColor Cyan

# Cambiar a main si es necesario
if ($currentBranch -ne "main") {
    Write-Host "Cambiando a rama 'main'..." -ForegroundColor Yellow
    git branch -M main
}

# Agregar y commitear cambios
Write-Host ""
Write-Host "Agregando archivos..." -ForegroundColor Yellow
git add .

$hasChanges = git status --porcelain
if ($hasChanges) {
    $commitMsg = Read-Host "Mensaje del commit (Enter para automático)"
    if ([string]::IsNullOrWhiteSpace($commitMsg)) {
        $commitMsg = "Deploy to Railway $(Get-Date -Format 'yyyy-MM-dd HH:mm')"
    }
    git commit -m "$commitMsg"
}

# Push a GitHub
Write-Host ""
Write-Host "Subiendo código a GitHub..." -ForegroundColor Yellow
git push -u origin main

if ($LASTEXITCODE -eq 0) {
    Write-Host ""
    Write-Host "========================================" -ForegroundColor Green
    Write-Host "   ✓ Código subido a GitHub!" -ForegroundColor Green
    Write-Host "========================================" -ForegroundColor Green
    Write-Host ""
    Write-Host "SIGUIENTE PASO:" -ForegroundColor Yellow
    Write-Host ""
    Write-Host "1. Ve a Railway: https://railway.app" -ForegroundColor Cyan
    Write-Host "2. Login con GitHub" -ForegroundColor Cyan
    Write-Host "3. New Project → Deploy from GitHub repo" -ForegroundColor Cyan
    Write-Host "4. Selecciona tu repo: travel4u-app" -ForegroundColor Cyan
    Write-Host "5. Railway detectará el Dockerfile automáticamente" -ForegroundColor Cyan
    Write-Host ""
    Write-Host "6. Configura estas Variables de Entorno:" -ForegroundColor Yellow
    Write-Host ""
    Write-Host "   SPRING_PROFILES_ACTIVE=heroku" -ForegroundColor White
    Write-Host "   SPRING_DATASOURCE_URL=jdbc:postgresql://db.tiifltprjgtyfimhnezi.supabase.co:5432/postgres" -ForegroundColor White
    Write-Host "   SPRING_DATASOURCE_USERNAME=postgres" -ForegroundColor White
    Write-Host "   SPRING_DATASOURCE_PASSWORD=TU_PASSWORD" -ForegroundColor White
    Write-Host ""
    Write-Host "7. ¡Deploy automático! Tu app estará en:" -ForegroundColor Green
    Write-Host "   https://travel4u-app-production.up.railway.app" -ForegroundColor Cyan
    Write-Host ""
} else {
    Write-Host ""
    Write-Host "ERROR: No se pudo subir a GitHub" -ForegroundColor Red
    Write-Host "Verifica que tengas permisos y que el remote esté configurado correctamente" -ForegroundColor Yellow
}

