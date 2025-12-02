# SufrirIntegrador
Repositorio para subir los avances del proyecto para el curso Integrador I de la carrera de Ingeniería de Sistemas e Informática en la UTP.

## 🚀 DESPLIEGUE EN RAILWAY (GRATIS - 500 horas/mes)

### Script Automatizado ⚡
```powershell
.\deploy.ps1
```
Este script verifica todo, limpia archivos obsoletos y sube el código.

### Guía Completa 📚
Ver: **[RAILWAY_DEPLOY.md](./RAILWAY_DEPLOY.md)** - Guía paso a paso completa

---

## 📜 Descripción del Proyecto
**Travel4U** es una aplicación web desarrollada con Spring Boot que busca centralizar la búsqueda y reserva de servicios de viaje. La plataforma integra funcionalidades de autenticación de usuarios, scraping de ofertas de vuelos en tiempo real.

## ✨ Características Principales
- **Gestión de Usuarios:** Registro e inicio de sesión con Spring Security.
- **Creación de Admin por Defecto:** Al iniciar, la aplicación crea un usuario administrador (`admin@travel4u.com`) si no existe.
- **Web Scraping:** Extrae ofertas de vuelos de Skyscanner para mostrarlas en la página principal.
- **Gestión de Reservas:** Los usuarios pueden ver y gestionar sus reservas.
- **Módulos de Reportes:** Estructura preparada para la generación de reportes (visto en las ramas y configuración de seguridad).

## 🛠️ Tecnologías Utilizadas
- **Backend:**
    - Java 21
    - Spring Boot 3.5.6
    - Spring Web
    - Spring Data JPA (Hibernate)
    - Spring Security
- **Frontend:**
    - Thymeleaf (con integración para Spring Security)
- **Base de Datos:**
    - PostgreSQL
- **Librerías Adicionales:**
    - Lombok
    - Jsoup (para Web Scraping)

## ⚙️ Configuración y Puesta en Marcha

### 1. Prerrequisitos
- JDK 21 o superior.
- Una instancia de PostgreSQL corriendo.

### 2. Configuración de la Base de Datos
El proyecto está configurado para conectarse a una base de datos PostgreSQL local.

- **Host:** `localhost`
- **Puerto:** `8180`
- **Nombre de la BD:** `sufrirIntegrador`
- **Usuario:** `postgres`
- **Contraseña:** `conde-lemon`

Puedes ajustar estos valores en el archivo `src/main/resources/application.properties`.

### 3. Ejecutar la Aplicación
1.  Clona el repositorio.
2.  Asegúrate de que tu instancia de PostgreSQL esté activa y configurada como se indica arriba.
3.  Abre el proyecto en tu IDE de preferencia (ej. IntelliJ IDEA, VSCode).
4.  Ejecuta la clase principal `DemoApplication.java`.
5.  La aplicación estará disponible en `http://localhost:8081`.

## 👤 Cuentas de Usuario
Al arrancar por primera vez, la aplicación crea automáticamente una cuenta de administrador para facilitar las pruebas:
- **Usuario:** `admin@travel4u.com`
- **Contraseña:** `1234`

**Importante:** La aplicación está configurada actualmente con `NoOpPasswordEncoder`, lo que significa que las contraseñas se guardan en **texto plano**. Esto es inseguro y solo debe usarse para desarrollo.

## 📁 Estructura del Proyecto
La estructura de paquetes principal es la siguiente:
com.travel4u.demo ├── controller/     # Controladores web (Spring MVC) ├── model/           # Entidades JPA y otros objetos de modelo ├── repository/      # Interfaces de Spring Data JPA (DAOs) ├── security/        # Configuración de Spring Security y UserDetailsService ├── service/         # Lógica de negocio (ej. ScrapingService) └── DemoApplication.java # Punto de entrada de la aplicación


## 🧩 Ramas Activas
- `main` → Producción estable.
- `develop` → Desarrollo principal.
- `darce-dev` → Desarrollo de Darce.
- `integration-crud-admin` → CRUD del panel admin.
- `Reporte-dashboar` / `dashboar-reporte` → Módulos de reportes.
- `backup` → Respaldo antes de merges.
- `delete-trash` → Limpieza y refactorización.
- `user-admin` → Gestión de usuarios admin.
- `webScraping` → Funcionalidad experimental.