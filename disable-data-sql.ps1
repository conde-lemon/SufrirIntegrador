# ==========================================
# DESHABILITAR DATA.SQL
# ==========================================

$dataFile = "demo\src\main\resources\data.sql"
$backupFile = "demo\src\main\resources\data.sql.backup"

if (Test-Path $dataFile) {
    Move-Item -Path $dataFile -Destination $backupFile -Force
    Write-Host "✅ data.sql renombrado a data.sql.backup" -ForegroundColor Green
    Write-Host "   El archivo ya no se ejecutará automáticamente" -ForegroundColor Gray
} else {
    Write-Host "⚠️  data.sql no encontrado o ya fue renombrado" -ForegroundColor Yellow
}

