// archivo: trivagoScraper.js
import axios from "axios";
import * as cheerio from "cheerio";
import fs from "fs";
import path from "path";
import { fileURLToPath } from "url";

// Para obtener la ruta del directorio actual en ES modules
const __filename = fileURLToPath(import.meta.url);
const __dirname = path.dirname(__filename);

const URL = "https://www.trivago.com.mx/es-MX/srl/hoteles-tacna-per%C3%BA?search=200-66649;dr-20251116-20251210-s;drs-40;rc-1-2-6";

async function scrapeTrivago() {
  try {
    console.log("🔍 Iniciando scraping de Trivago...");
    
    const { data: html } = await axios.get(URL, {
      headers: {
        "User-Agent": "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36",
        "Accept": "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8",
        "Accept-Language": "es-ES,es;q=0.8,en-US;q=0.5,en;q=0.3",
        "Accept-Encoding": "gzip, deflate, br",
        "Connection": "keep-alive",
        "Upgrade-Insecure-Requests": "1",
      },
    });

    const $ = cheerio.load(html);
    const hotels = [];

    $("article[data-testid='item']").each((i, el) => {
      const name = $(el).find("[data-testid='item-name']").text().trim();
      const image = $(el).find("img[data-testid='accommodation-main-image']").attr("src") || "";
      const rating = $(el).find("[data-testid='aggregate-rating'] span[itemprop='ratingValue']").text().trim();
      const reviewCount = $(el).find("[data-testid='aggregate-rating'] span strong").next().text().trim();
      const hotelType = $(el).find("[data-testid='accommodation-type'] .O3UXGr").text().trim();
      const price = $(el).find("[data-testid='recommended-price']").first().text().trim();
      const advertiser = $(el).find("[data-testid^='advertiser-details']").first().text().trim();
      const distance = $(el).find("[data-testid='distance-label-section']").text().trim();
      const highlights = $(el).find("[data-testid='hotel-highlights-text']").text().trim();
      const stars = $(el).find("[data-testid='star']").length;

      if (name) {
        hotels.push({
          name,
          image,
          rating,
          reviewCount,
          hotelType,
          price,
          advertiser,
          distance,
          highlights,
          stars,
        });
      }
    });

    // Guardar los resultados en un JSON en la misma carpeta
    const outputPath = path.join(__dirname, "hoteles_tacna.json");
    fs.writeFileSync(outputPath, JSON.stringify(hotels, null, 2), "utf-8");
    
    console.log(`✅ Datos guardados en: ${outputPath}`);
    console.log(`📊 Total de hoteles encontrados: ${hotels.length}`);
    
    if (hotels.length > 0) {
      console.log("\n🏨 Ejemplo de hotel encontrado:");
      console.log(JSON.stringify(hotels[0], null, 2));
    }
    
  } catch (error) {
    console.error("❌ Error al hacer scraping:", error.message);
    if (error.response) {
      console.error("Status:", error.response.status);
    }
  }
}

scrapeTrivago();