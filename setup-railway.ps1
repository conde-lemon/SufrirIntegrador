# ==========================================
# SCRIPT DE DESPLIEGUE EN RAILWAY
# Travel4U - Automatizacion de Deploy
# ==========================================

Write-Host "`n========================================" -ForegroundColor Cyan
Write-Host "   Travel4U - Setup Railway" -ForegroundColor Cyan
Write-Host "========================================" -ForegroundColor Cyan
Write-Host ""

# Verificar que estamos en la carpeta correcta
if (-Not (Test-Path "demo\src")) {
    Write-Host "ERROR: Debes ejecutar este script desde la raiz del proyecto" -ForegroundColor Red
    Write-Host "   Ubicacion actual: $PWD" -ForegroundColor Yellow
    Write-Host "   Navega a: C:\Users\LENOVO\Documents\utp\ciclo7\integrador\demo (1)" -ForegroundColor Yellow
    exit 1
}

Write-Host "Ubicacion correcta verificada`n" -ForegroundColor Green

Write-Host "PASOS PARA DESPLEGAR EN RAILWAY:" -ForegroundColor Yellow
Write-Host ""
Write-Host "1. Ve a: https://railway.app" -ForegroundColor Cyan
Write-Host "2. Login con GitHub" -ForegroundColor Cyan
Write-Host "3. Click 'New Project' -> 'Deploy from GitHub repo'" -ForegroundColor Cyan
Write-Host ""
Write-Host "Ahora vamos a preparar tu codigo para GitHub...`n" -ForegroundColor Yellow

# Verificar si ya tiene remote de GitHub
$hasGitHub = git remote -v 2>$null | Select-String "github"
if (-Not $hasGitHub) {
    Write-Host "No tienes configurado GitHub como remote." -ForegroundColor Yellow
    Write-Host ""
    Write-Host "Primero crea un repo en GitHub:" -ForegroundColor Cyan
    Write-Host "   https://github.com/new" -ForegroundColor White
    Write-Host ""
    Write-Host "   Nombre sugerido: travel4u-app" -ForegroundColor Green
    Write-Host "   NO marques 'Initialize with README'" -ForegroundColor Red
    Write-Host ""

    $repoUrl = Read-Host "Ingresa la URL de tu repo de GitHub (ej: https://github.com/usuario/travel4u-app.git)"

    if ([string]::IsNullOrWhiteSpace($repoUrl)) {
        Write-Host "`nERROR: Debes proporcionar la URL del repositorio" -ForegroundColor Red
        exit 1
    }

    Write-Host ""
    Write-Host "Configurando remote de GitHub..." -ForegroundColor Yellow
    git remote add origin $repoUrl

    if ($LASTEXITCODE -eq 0) {
        Write-Host "Remote configurado correctamente`n" -ForegroundColor Green
    } else {
        Write-Host "Error al configurar remote. Verifica la URL.`n" -ForegroundColor Red
        exit 1
    }
} else {
    Write-Host "GitHub ya esta configurado:" -ForegroundColor Green
    git remote -v | ForEach-Object { Write-Host "   $_" -ForegroundColor White }
    Write-Host ""
}

# Verificar rama
Write-Host "Verificando configuracion de Git..." -ForegroundColor Yellow
$currentBranch = git rev-parse --abbrev-ref HEAD 2>$null
if ($LASTEXITCODE -ne 0) {
    Write-Host "Este directorio no es un repositorio Git valido" -ForegroundColor Red
    Write-Host "   Inicializando Git..." -ForegroundColor Yellow
    git init
    $currentBranch = "master"
}
Write-Host "   Rama actual: $currentBranch" -ForegroundColor Cyan

# Cambiar a main si es necesario
if ($currentBranch -ne "main") {
    Write-Host "   Cambiando a rama 'main'..." -ForegroundColor Yellow
    git branch -M main
    Write-Host "   Rama cambiada a 'main'`n" -ForegroundColor Green
} else {
    Write-Host "   Ya estas en rama 'main'`n" -ForegroundColor Green
}

# Agregar y commitear cambios
Write-Host "Preparando archivos para commit..." -ForegroundColor Yellow
git add .

$hasChanges = git status --porcelain
if ($hasChanges) {
    Write-Host "   Archivos modificados encontrados`n" -ForegroundColor Cyan
    $commitMsg = Read-Host "Mensaje del commit (presiona Enter para mensaje automatico)"
    if ([string]::IsNullOrWhiteSpace($commitMsg)) {
        $commitMsg = "Deploy to Railway $(Get-Date -Format 'yyyy-MM-dd HH:mm')"
        Write-Host "   Usando mensaje: $commitMsg" -ForegroundColor Gray
    }
    git commit -m "$commitMsg"
    if ($LASTEXITCODE -eq 0) {
        Write-Host "   Commit realizado correctamente`n" -ForegroundColor Green
    }
} else {
    Write-Host "   No hay cambios para commitear`n" -ForegroundColor Gray
}

# Push a GitHub
Write-Host "Subiendo codigo a GitHub..." -ForegroundColor Yellow
git push -u origin main 2>&1 | Out-String | Write-Host

if ($LASTEXITCODE -eq 0) {
    Write-Host ""
    Write-Host "========================================" -ForegroundColor Green
    Write-Host "   Codigo subido a GitHub!" -ForegroundColor Green
    Write-Host "========================================" -ForegroundColor Green
    Write-Host ""
    Write-Host "SIGUIENTE PASO: CONFIGURAR RAILWAY" -ForegroundColor Yellow
    Write-Host ""
    Write-Host "1. Ve a Railway:" -ForegroundColor Cyan
    Write-Host "   https://railway.app" -ForegroundColor White
    Write-Host ""
    Write-Host "2. Login con GitHub y autoriza Railway" -ForegroundColor Cyan
    Write-Host ""
    Write-Host "3. New Project -> Deploy from GitHub repo" -ForegroundColor Cyan
    Write-Host ""
    Write-Host "4. Selecciona tu repositorio" -ForegroundColor Cyan
    Write-Host ""
    Write-Host "5. Railway detectara el Dockerfile automaticamente" -ForegroundColor Cyan
    Write-Host ""
    Write-Host "6. Configura estas Variables de Entorno:" -ForegroundColor Yellow
    Write-Host ""
    Write-Host "   Variable: SPRING_PROFILES_ACTIVE" -ForegroundColor White
    Write-Host "   Valor:    heroku" -ForegroundColor Gray
    Write-Host ""
    Write-Host "   Variable: SPRING_DATASOURCE_URL" -ForegroundColor White
    Write-Host "   Valor:    jdbc:postgresql://db.tiifltprjgtyfimhnezi.supabase.co:5432/postgres" -ForegroundColor Gray
    Write-Host ""
    Write-Host "   Variable: SPRING_DATASOURCE_USERNAME" -ForegroundColor White
    Write-Host "   Valor:    postgres" -ForegroundColor Gray
    Write-Host ""
    Write-Host "   Variable: SPRING_DATASOURCE_PASSWORD" -ForegroundColor White
    Write-Host "   Valor:    [TU_PASSWORD_DE_SUPABASE]" -ForegroundColor Red
    Write-Host ""
    Write-Host "   Variable: PORT" -ForegroundColor White
    Write-Host "   Valor:    8080" -ForegroundColor Gray
    Write-Host ""
    Write-Host "7. En Settings -> Domains -> Generate Domain" -ForegroundColor Cyan
    Write-Host ""
    Write-Host "Tu app estara disponible en:" -ForegroundColor Green
    Write-Host "   https://travel4u-app-production.up.railway.app" -ForegroundColor Cyan
    Write-Host ""
    Write-Host "Para mas detalles, revisa:" -ForegroundColor Yellow
    Write-Host "   GUIA_DESPLIEGUE_RAILWAY.md" -ForegroundColor White
    Write-Host ""
} else {
    Write-Host ""
    Write-Host "========================================" -ForegroundColor Red
    Write-Host "   Error al subir codigo" -ForegroundColor Red
    Write-Host "========================================" -ForegroundColor Red
    Write-Host ""
    Write-Host "Posibles causas:" -ForegroundColor Yellow
    Write-Host ""
    Write-Host "1. No has configurado autenticacion de GitHub" -ForegroundColor White
    Write-Host "   Solucion: Configura un Personal Access Token" -ForegroundColor Gray
    Write-Host "   https://github.com/settings/tokens" -ForegroundColor Cyan
    Write-Host ""
    Write-Host "2. La URL del repositorio es incorrecta" -ForegroundColor White
    Write-Host "   Solucion: Verifica con 'git remote -v'" -ForegroundColor Gray
    Write-Host ""
    Write-Host "3. No tienes permisos en el repositorio" -ForegroundColor White
    Write-Host "   Solucion: Verifica que seas dueno del repo" -ForegroundColor Gray
    Write-Host ""
    Write-Host "Intenta subir manualmente:" -ForegroundColor Yellow
    Write-Host "   git push -u origin main" -ForegroundColor White
    Write-Host ""
}

