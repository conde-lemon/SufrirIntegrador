# 🚀 Despliegue Rápido en Railway

## ⚡ INICIO RÁPIDO (3 pasos)

### 1️⃣ Subir código a GitHub

**📁 UBICACIÓN:** Debes ejecutar este comando en la carpeta raíz del proyecto:
```
C:\Users\LENOVO\Documents\utp\ciclo7\integrador\demo (1)
```

**Pasos:**
1. Abre **PowerShell**
2. Navega a la carpeta:
```powershell
cd "C:\Users\LENOVO\Documents\utp\ciclo7\integrador\demo (1)"
```
3. Ejecuta el script:
```powershell
.\setup-railway.ps1
```

**O más rápido:**
- Click derecho en la carpeta `demo (1)` en el explorador de archivos
- Selecciona "Abrir en Terminal" o "Open PowerShell window here"
- Ejecuta: `.\setup-railway.ps1`

### 2️⃣ Crear proyecto en Railway
1. Ve a https://railway.app
2. Login con GitHub
3. New Project → Deploy from GitHub repo
4. Selecciona tu repositorio

### 3️⃣ Configurar variables de entorno
En Railway, añade estas variables:

| Variable | Valor |
|----------|-------|
| `SPRING_PROFILES_ACTIVE` | `heroku` |
| `SPRING_DATASOURCE_URL` | `jdbc:postgresql://db.tiifltprjgtyfimhnezi.supabase.co:5432/postgres` |
| `SPRING_DATASOURCE_USERNAME` | `postgres` |
| `SPRING_DATASOURCE_PASSWORD` | `[tu_password_de_supabase]` |
| `PORT` | `8080` |

### ✅ ¡Listo!
Tu app estará disponible en: `https://tu-app.up.railway.app`

---

## 📚 Documentación Completa
Ver: **[GUIA_DESPLIEGUE_RAILWAY.md](./GUIA_DESPLIEGUE_RAILWAY.md)**

---

## 🆓 Plan Gratuito
- ✅ 500 horas/mes gratis
- ✅ NO requiere tarjeta de crédito
- ✅ Deploy automático desde GitHub
- ✅ SSL/HTTPS incluido

---

## 🆘 Problemas Comunes

### Build Failed
- Verifica que el Dockerfile esté en la raíz
- Revisa los logs en Railway

### Cannot connect to database
- Verifica la contraseña de Supabase
- Asegúrate que Supabase permite conexiones externas

### Out of Memory
- Reduce JAVA_OPTS a `-Xmx400m`
- Railway free tier: 512MB RAM

---

## 📞 Soporte
- Documentación Railway: https://docs.railway.app
- Discord Railway: https://discord.gg/railway

---

**¿Listo para desplegar? Ejecuta `.\setup-railway.ps1` 🚀**

