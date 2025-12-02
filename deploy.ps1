# ==========================================
# VERIFICACION Y DESPLIEGUE RAILWAY
# Verifica todo antes de subir
# ==========================================

Write-Host "`n========================================" -ForegroundColor Cyan
Write-Host "   VERIFICACION PRE-DEPLOY" -ForegroundColor Cyan
Write-Host "========================================" -ForegroundColor Cyan
Write-Host ""

$errores = 0

# 1. Verificar ubicación
Write-Host "[1/5] Verificando ubicacion..." -ForegroundColor Yellow
if (Test-Path "demo\src") {
    Write-Host "      OK - Ubicacion correcta`n" -ForegroundColor Green
} else {
    Write-Host "      ERROR - Ejecuta desde la raiz del proyecto`n" -ForegroundColor Red
    exit 1
}

# 2. Verificar gradle-wrapper.jar
Write-Host "[2/5] Verificando gradle-wrapper.jar..." -ForegroundColor Yellow
if (Test-Path "demo\gradle\wrapper\gradle-wrapper.jar") {
    $size = (Get-Item "demo\gradle\wrapper\gradle-wrapper.jar").Length
    Write-Host "      OK - Archivo existe ($size bytes)`n" -ForegroundColor Green
} else {
    Write-Host "      ERROR - gradle-wrapper.jar no existe`n" -ForegroundColor Red
    $errores++
}

# 3. Verificar Dockerfile
Write-Host "[3/5] Verificando Dockerfile..." -ForegroundColor Yellow
$dockerContent = Get-Content "Dockerfile" -Raw
if ($dockerContent -match "COPY demo/gradle/ gradle/") {
    Write-Host "      OK - Dockerfile correcto`n" -ForegroundColor Green
} else {
    Write-Host "      ERROR - Dockerfile tiene sintaxis incorrecta`n" -ForegroundColor Red
    $errores++
}

# 4. Verificar .dockerignore
Write-Host "[4/5] Verificando .dockerignore..." -ForegroundColor Yellow
$dockerignoreContent = Get-Content ".dockerignore" -Raw
if ($dockerignoreContent -match "gradle-wrapper\.jar") {
    Write-Host "      ERROR - .dockerignore bloquea gradle-wrapper.jar`n" -ForegroundColor Red
    $errores++
} else {
    Write-Host "      OK - .dockerignore correcto`n" -ForegroundColor Green
}

# 5. Verificar Git
Write-Host "[5/5] Verificando Git..." -ForegroundColor Yellow
$hasRemote = git remote -v 2>$null | Select-String "origin"
if ($hasRemote) {
    Write-Host "      OK - Remote configurado`n" -ForegroundColor Green
} else {
    Write-Host "      ADVERTENCIA - No hay remote configurado`n" -ForegroundColor Yellow
}

Write-Host "========================================" -ForegroundColor Cyan

if ($errores -gt 0) {
    Write-Host "   ENCONTRADOS $errores ERRORES" -ForegroundColor Red
    Write-Host "========================================" -ForegroundColor Cyan
    Write-Host ""
    Write-Host "Corrige los errores antes de continuar." -ForegroundColor Yellow
    exit 1
}

Write-Host "   TODAS LAS VERIFICACIONES PASARON" -ForegroundColor Green
Write-Host "========================================" -ForegroundColor Cyan
Write-Host ""

# Preguntar si continuar
$continuar = Read-Host "Deseas continuar con el deploy? (S/N)"
if ($continuar -ne "S" -and $continuar -ne "s") {
    Write-Host "Deploy cancelado." -ForegroundColor Yellow
    exit 0
}

Write-Host ""
Write-Host "Preparando deploy..." -ForegroundColor Yellow
Write-Host ""

# Limpiar archivos obsoletos
Write-Host "Limpiando archivos obsoletos..." -ForegroundColor Yellow
$archivosObsoletos = @(
    # Documentacion obsoleta
    "RAILWAY_QUICKSTART.md",
    "GUIA_DESPLIEGUE_RAILWAY.md",
    "RAILWAY_VARIABLES.md",
    "FLUJO_DEPLOY.md",
    "FIX_GRADLE_ERROR.md",
    "DONDE_EJECUTAR.md",
    "DEPLOY-RAILWAY.md",
    "COMANDOS_UTILES.md",
    "DEPLOY-RENDER.md",
    "README-RENDER.md",

    # Scripts obsoletos
    "setup-railway.ps1",
    "update-railway.ps1",
    "railway-deploy.ps1",
    "setup-heroku.ps1",
    "deploy-heroku.ps1",

    # Archivos de plataformas no usadas
    "build-render.bat",
    "heroku.yml",
    "render.yaml",

    # SQL obsoletos
    "TravelReserva.sql",
    "init-db.sql",

    # Archivos dentro de demo/
    "demo\DATABASE_FIX_GUIDE.md",
    "demo\test-database.bat",
    "demo\fix_sequence.sql"
)

$eliminados = 0
foreach ($archivo in $archivosObsoletos) {
    if (Test-Path $archivo) {
        Remove-Item $archivo -Force
        $eliminados++
    }
}
Write-Host "   Eliminados: $eliminados archivos obsoletos`n" -ForegroundColor Gray

# Git add
Write-Host "Agregando archivos a Git..." -ForegroundColor Yellow
git add -A

# Verificar cambios
$cambios = git status --porcelain
if (-Not $cambios) {
    Write-Host "   No hay cambios nuevos para subir`n" -ForegroundColor Gray
    Write-Host "Si el build fallo en Railway, ve al dashboard:" -ForegroundColor Yellow
    Write-Host "https://railway.app/dashboard" -ForegroundColor Cyan
    Write-Host ""
    Write-Host "Y verifica los logs del deployment." -ForegroundColor Yellow
    exit 0
}

# Mostrar cambios
Write-Host "   Cambios detectados:`n" -ForegroundColor Cyan
git status --short | ForEach-Object { Write-Host "      $_" -ForegroundColor White }
Write-Host ""

# Commit
$timestamp = Get-Date -Format "yyyy-MM-dd HH:mm"
$commitMsg = "Fix: Railway deploy - Dockerfile y dockerignore corregidos - $timestamp"
Write-Host "Commit: $commitMsg" -ForegroundColor Yellow
git commit -m "$commitMsg"

if ($LASTEXITCODE -ne 0) {
    Write-Host "   ERROR en commit`n" -ForegroundColor Red
    exit 1
}

# Push
Write-Host "`nSubiendo a GitHub..." -ForegroundColor Yellow
git push origin main

if ($LASTEXITCODE -eq 0) {
    Write-Host ""
    Write-Host "========================================" -ForegroundColor Green
    Write-Host "   CODIGO SUBIDO EXITOSAMENTE" -ForegroundColor Green
    Write-Host "========================================" -ForegroundColor Green
    Write-Host ""
    Write-Host "AHORA EN RAILWAY:" -ForegroundColor Yellow
    Write-Host ""
    Write-Host "1. Ve a: https://railway.app/dashboard" -ForegroundColor Cyan
    Write-Host "2. Selecciona tu proyecto" -ForegroundColor Cyan
    Write-Host "3. Railway detectara el cambio en 10-30 segundos" -ForegroundColor White
    Write-Host "4. Comenzara un nuevo build automaticamente" -ForegroundColor White
    Write-Host ""
    Write-Host "MONITOREAR:" -ForegroundColor Yellow
    Write-Host "   Deployments > Ultimo deploy > View Logs" -ForegroundColor White
    Write-Host ""
    Write-Host "BUSCAR EN LOGS:" -ForegroundColor Yellow
    Write-Host "   'BUILD SUCCESSFUL in Xs Ym' ✅" -ForegroundColor Green
    Write-Host "   'Started DemoApplication in X.XXX seconds' ✅" -ForegroundColor Green
    Write-Host ""
    Write-Host "Si configuraste las variables de entorno," -ForegroundColor White
    Write-Host "el build deberia completarse exitosamente." -ForegroundColor Green
    Write-Host ""
} else {
    Write-Host ""
    Write-Host "========================================" -ForegroundColor Red
    Write-Host "   ERROR AL SUBIR A GITHUB" -ForegroundColor Red
    Write-Host "========================================" -ForegroundColor Red
    Write-Host ""
    Write-Host "Posibles causas:" -ForegroundColor Yellow
    Write-Host "1. No estas autenticado en Git" -ForegroundColor White
    Write-Host "2. No tienes permisos en el repositorio" -ForegroundColor White
    Write-Host "3. El remote no esta configurado" -ForegroundColor White
    Write-Host ""
    Write-Host "Verifica con: git remote -v" -ForegroundColor Cyan
    Write-Host ""
}

