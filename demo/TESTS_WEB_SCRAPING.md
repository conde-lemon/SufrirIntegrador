# 🧪 Tests de Web Scraping - Travel4U

## 📋 Descripción

Este conjunto de tests valida la funcionalidad de web scraping de la aplicación Travel4U, específicamente el scraping de ofertas de vuelos desde Skyscanner.

## 🗂️ Archivos de Test Creados

### 1. **ScrapingServiceTest.java**
- **Tipo:** Test Unitario
- **Ubicación:** `src/test/java/com/travel4u/demo/scraper/service/`
- **Propósito:** Prueba la lógica del servicio de scraping sin hacer llamadas reales a internet
- **Características:**
  - ✅ Mock de Jsoup y Document
  - ✅ Prueba manejo de errores
  - ✅ Validación de ofertas de fallback
  - ✅ Verificación de extracción de datos
  - ✅ Validación de formato de precios
  - ✅ Prueba de extracción de URLs de imágenes

### 2. **ScrapingServiceIntegrationTest.java**
- **Tipo:** Test de Integración
- **Ubicación:** `src/test/java/com/travel4u/demo/scraper/service/`
- **Propósito:** Prueba el scraping real a Skyscanner (requiere internet)
- **Características:**
  - ✅ Conexión real a Skyscanner
  - ✅ Validación de datos obtenidos
  - ✅ Prueba de idempotencia
  - ✅ Verificación de performance (timeout 10s)
  - ✅ Validación de múltiples ofertas
  - ✅ Prueba de consistencia entre llamadas

### 3. **OfertaScrapingTest.java**
- **Tipo:** Test de Modelo
- **Ubicación:** `src/test/java/com/travel4u/demo/scraper/model/`
- **Propósito:** Valida el funcionamiento del modelo OfertaScraping
- **Características:**
  - ✅ Prueba de constructores (con/sin argumentos)
  - ✅ Validación de getters/setters (Lombok)
  - ✅ Prueba de equals y hashCode
  - ✅ Validación de toString
  - ✅ Manejo de valores nulos y vacíos
  - ✅ Prueba con caracteres especiales
  - ✅ Validación de diferentes formatos

---

## 🚀 Cómo Ejecutar los Tests

### Opción 1: Ejecutar TODOS los tests de scraping

```bash
# Desde la carpeta demo/
./gradlew test --tests "com.travel4u.demo.scraper.*"
```

### Opción 2: Ejecutar tests específicos

#### Tests Unitarios (no requieren internet):
```bash
./gradlew test --tests "ScrapingServiceTest"
```

#### Tests de Integración (requieren internet):
```bash
./gradlew test --tests "ScrapingServiceIntegrationTest"
```

#### Tests del Modelo:
```bash
./gradlew test --tests "OfertaScrapingTest"
```

### Opción 3: Desde IntelliJ IDEA

1. Abre el archivo de test
2. Click derecho en la clase
3. Selecciona "Run 'NombreDelTest'"

O para ejecutar un test individual:
1. Click derecho en el método @Test
2. Selecciona "Run 'nombreDelMetodo()'"

---

## 📊 Cobertura de Tests

### ScrapingServiceTest (Unitario)
- ✅ Manejo de errores de conexión
- ✅ Fallback cuando no hay elementos HTML
- ✅ Procesamiento exitoso de ofertas
- ✅ Omisión de tarjetas incompletas
- ✅ Extracción de URLs de imágenes
- ✅ Formato de precios

**Total:** 7 tests

### ScrapingServiceIntegrationTest (Integración)
- ✅ Conexión a Skyscanner
- ✅ Validación de datos obtenidos
- ✅ Múltiples ofertas
- ✅ Formato de precios
- ✅ URLs de imágenes válidas
- ✅ Manejo de errores de conexión (test manual)
- ✅ Sin títulos duplicados
- ✅ Performance (timeout)
- ✅ Presencia de descripciones
- ✅ Idempotencia

**Total:** 10 tests

### OfertaScrapingTest (Modelo)
- ✅ Constructor con todos los campos
- ✅ Constructor sin argumentos
- ✅ Setters
- ✅ Equals
- ✅ Not Equals
- ✅ ToString
- ✅ Valores nulos
- ✅ Strings vacíos
- ✅ Caracteres especiales
- ✅ Tipos de oferta
- ✅ Formatos de precio
- ✅ Formatos de URL
- ✅ Actualización parcial

**Total:** 13 tests

## **TOTAL GENERAL: 30 tests** 🎉

---

## ⚠️ Notas Importantes

### Tests de Integración
Los tests de integración **requieren conexión a internet** y pueden fallar si:
- No hay conexión a internet
- Skyscanner cambia la estructura HTML de su página
- Skyscanner bloquea el acceso (rate limiting)
- El sitio está en mantenimiento

### Test Manual Deshabilitado
El test `testConnectionFailure` está marcado con `@Disabled` porque requiere:
1. Ejecutar el test
2. Desconectar internet manualmente
3. Verificar que retorna ofertas de fallback

Para habilitarlo, elimina la anotación `@Disabled`.

---

## 📈 Resultados Esperados

### ✅ Todos los tests pasan
```
ScrapingServiceTest > testScrapeOfertasPrincipales_WhenScrapingFails_ShouldReturnFallback() PASSED
ScrapingServiceTest > testScrapeOfertasPrincipales_WhenNoElementsFound_ShouldReturnFallback() PASSED
...
BUILD SUCCESSFUL
```

### ⚠️ Tests de integración fallan (sin internet)
```
ScrapingServiceIntegrationTest > testConnection() FAILED
...
```
**Solución:** Conectar a internet y ejecutar nuevamente.

### ❌ Tests fallan por cambio en HTML
```
ScrapingServiceIntegrationTest > testScrapedOffersHaveValidData() FAILED
Expected: true, Actual: false
```
**Solución:** Actualizar los selectores CSS en `ScrapingService.java` según la nueva estructura HTML de Skyscanner.

---

## 🔍 Debugging

### Ver logs durante los tests
```bash
./gradlew test --tests "ScrapingServiceIntegrationTest" --info
```

### Ejecutar con más detalle
```bash
./gradlew test --tests "ScrapingServiceTest" --debug
```

### Ver solo tests fallidos
```bash
./gradlew test --tests "com.travel4u.demo.scraper.*" --rerun-tasks
```

---

## 🛠️ Mantenimiento

### Actualizar selectores CSS
Si Skyscanner cambia su HTML, actualiza estos selectores en `ScrapingService.java`:

```java
// Selector de tarjetas
Elements cards = doc.select("a.BpkLink_bpk-link__MWZlZ.DestinationsCards_card__YmQ4M");

// Selector de título
String titulo = card.select("h3.BpkText_bpk-text__ZWIzZ.BpkText_bpk-text--lg__YmYyY").text();

// Selector de descripción
String descripcion = card.select("p.BpkText_bpk-text__ZWIzZ.BpkText_bpk-text--sm__N2I5N").text();

// Selector de precio
String precio = card.select("p.BpkText_bpk-text__ZWIzZ.BpkText_bpk-text--xs__ZjZkN").text();

// Selector de imagen
Element imageDiv = card.selectFirst("div.DestinationsCards_image__Y2Y4Z");
```

### Herramientas para inspeccionar selectores:
1. Chrome DevTools (F12)
2. Selector Gadget (extensión de Chrome)
3. Copy > Copy selector (click derecho en elemento)

---

## 📝 Ejemplos de Uso

### Ejecutar un test específico y ver output
```bash
./gradlew test --tests "ScrapingServiceTest.testScrapeOfertasPrincipales_WhenScrapingSuccessful_ShouldReturnOffers" --info
```

### Ejecutar solo tests rápidos (unitarios)
```bash
./gradlew test --tests "ScrapingServiceTest" --tests "OfertaScrapingTest"
```

### Ejecutar tests con reporte HTML
```bash
./gradlew test --tests "com.travel4u.demo.scraper.*"
# Ver reporte en: build/reports/tests/test/index.html
```

---

## 🎯 Próximos Pasos

1. **Agregar más assertions** en tests de integración
2. **Crear tests para otros scrapers** (Trivago, Amadeus)
3. **Implementar tests de performance** (benchmark)
4. **Agregar tests de concurrencia** (múltiples scrapers simultáneos)
5. **Crear mocks más realistas** con datos HTML completos

---

## 📚 Referencias

- **Jsoup Documentation:** https://jsoup.org/
- **JUnit 5 User Guide:** https://junit.org/junit5/docs/current/user-guide/
- **Mockito Documentation:** https://javadoc.io/doc/org.mockito/mockito-core/latest/org/mockito/Mockito.html
- **Spring Boot Testing:** https://docs.spring.io/spring-boot/docs/current/reference/html/features.html#features.testing

---

## ✅ Checklist de Ejecución

- [ ] Ejecutar tests unitarios (`ScrapingServiceTest`)
- [ ] Ejecutar tests del modelo (`OfertaScrapingTest`)
- [ ] Conectar a internet
- [ ] Ejecutar tests de integración (`ScrapingServiceIntegrationTest`)
- [ ] Verificar que todos los tests pasen
- [ ] Revisar reporte HTML de cobertura
- [ ] Documentar cualquier fallo o comportamiento inesperado

---

**¡Tests listos para ejecutar! 🚀**

Para empezar, ejecuta:
```bash
cd demo
./gradlew test --tests "com.travel4u.demo.scraper.*"
```

