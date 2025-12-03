# Script para corregir warnings en ScrapingServiceTest.java

Write-Host "Aplicando correcciones a ScrapingServiceTest.java..." -ForegroundColor Yellow

$archivo = "demo\src\test\java\com\travel4u\demo\scraper\service\ScrapingServiceTest.java"

if (Test-Path $archivo) {
    $contenido = Get-Content $archivo -Raw

    # Corrección 1: Agregar throws IOException a métodos que lo necesitan
    $contenido = $contenido -replace 'void testScrapeOfertasPrincipales_WhenScrapingSuccessful_ShouldReturnOffers\(\) \{',
                                     'void testScrapeOfertasPrincipales_WhenScrapingSuccessful_ShouldReturnOffers() throws IOException {'

    $contenido = $contenido -replace 'void testScrapeOfertasPrincipales_ShouldSkipCardsWithMissingData\(\) \{',
                                     'void testScrapeOfertasPrincipales_ShouldSkipCardsWithMissingData() throws IOException {'

    $contenido = $contenido -replace 'void testImageUrlExtraction\(\) \{',
                                     'void testImageUrlExtraction() throws IOException {'

    $contenido = $contenido -replace 'void testPriceFormatting\(\) \{',
                                     'void testPriceFormatting() throws IOException {'

    # Corrección 2: Reemplazar .get(0) con .getFirst()
    $contenido = $contenido -replace 'ofertas\.get\(0\)', 'ofertas.getFirst()'

    # Guardar cambios
    Set-Content -Path $archivo -Value $contenido -Encoding UTF8

    Write-Host "✅ Correcciones aplicadas exitosamente" -ForegroundColor Green
    Write-Host "   - Agregados throws IOException" -ForegroundColor Gray
    Write-Host "   - Reemplazados .get(0) con .getFirst()" -ForegroundColor Gray
} else {
    Write-Host "❌ Archivo no encontrado: $archivo" -ForegroundColor Red
}

