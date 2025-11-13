# Trivago Scraper

## Instalación y Ejecución

### 1. Instalar Node.js
Descargar e instalar Node.js desde: https://nodejs.org/

### 2. Navegar a la carpeta del scraper
```bash
cd "c:\Users\LENOVO\Documents\utp\ciclo7\integrador\demo (1)\demo\src\main\resources\scraping"
```

### 3. Instalar dependencias
```bash
npm install
```

### 4. Ejecutar el scraper
```bash
npm start
```
o
```bash
node trivagoScraper.js
```

## Resultado
- Se generará un archivo `hoteles_tacna.json` en la misma carpeta
- El archivo contendrá información de hoteles extraída de Trivago

## Estructura del JSON
```json
[
  {
    "name": "Nombre del hotel",
    "image": "URL de la imagen",
    "rating": "Calificación",
    "reviewCount": "Número de reseñas",
    "hotelType": "Tipo de alojamiento",
    "price": "Precio",
    "advertiser": "Proveedor",
    "distance": "Distancia al centro",
    "highlights": "Características destacadas",
    "stars": "Número de estrellas"
  }
]
```

## Comandos disponibles
- `npm start` - Ejecuta el scraper
- `npm run scrape` - Alias para ejecutar el scraper