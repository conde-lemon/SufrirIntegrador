# 🔐 Variables de Entorno para Railway

## ⚠️ IMPORTANTE: REEMPLAZA CON TUS VALORES REALES

Copia y pega estas variables en Railway (Settings → Variables):

---

## Variable 1: Spring Profile
```
SPRING_PROFILES_ACTIVE=heroku
```

---

## Variable 2: URL Base de Datos
```
SPRING_DATASOURCE_URL=jdbc:postgresql://db.tiifltprjgtyfimhnezi.supabase.co:5432/postgres
```

**⚠️ Nota:** Si tu URL de Supabase es diferente, cámbiala aquí.
Puedes encontrarla en: Supabase Dashboard → Settings → Database → Connection String (Direct)

---

## Variable 3: Usuario Base de Datos
```
SPRING_DATASOURCE_USERNAME=postgres
```

---

## Variable 4: Contraseña Base de Datos
```
SPRING_DATASOURCE_PASSWORD=TU_PASSWORD_AQUI
```

**🔴 IMPORTANTE:** Reemplaza `TU_PASSWORD_AQUI` con tu contraseña real de Supabase.

La encuentras en: Supabase Dashboard → Settings → Database → Database Password

**Nunca compartas esta contraseña públicamente.**

---

## Variable 5: Puerto
```
PORT=8080
```

---

## 📋 Resumen de Variables

| Variable | Valor | ¿Requiere Cambio? |
|----------|-------|-------------------|
| `SPRING_PROFILES_ACTIVE` | `heroku` | ❌ No |
| `SPRING_DATASOURCE_URL` | `jdbc:postgresql://db.tiifltprjgtyfimhnezi...` | ⚠️ Verifica tu URL |
| `SPRING_DATASOURCE_USERNAME` | `postgres` | ❌ No (usualmente) |
| `SPRING_DATASOURCE_PASSWORD` | `TU_PASSWORD_AQUI` | ✅ **SÍ - REQUERIDO** |
| `PORT` | `8080` | ❌ No |

---

## 🔍 Cómo Obtener tus Credenciales de Supabase

### URL de Conexión:
1. Ve a: https://supabase.com/dashboard
2. Selecciona tu proyecto
3. Settings → Database
4. Copia "Connection String" (Mode: Direct)
5. Reemplaza `[YOUR-PASSWORD]` con tu contraseña

### Contraseña:
1. En Settings → Database
2. "Database Password"
3. Si no la recuerdas, puedes resetearla (⚠️ afectará otras conexiones)

---

## ✅ Verificación

Después de configurar las variables en Railway:

1. Railway redesplegar automáticamente
2. Ve a la pestaña "Deployments" para ver el progreso
3. Busca en los logs:
   ```
   Started DemoApplication in X.XXX seconds
   ```
4. Si ves este mensaje, ¡tu app está funcionando! 🎉

---

## 🐛 Troubleshooting

### Error: "Connection refused"
- ✅ Verifica que la URL de Supabase sea correcta
- ✅ Asegúrate de usar el puerto 5432
- ✅ Usa "Direct Connection" no "Connection Pooler"

### Error: "Authentication failed"
- ✅ Verifica la contraseña
- ✅ Asegúrate de no tener espacios extra
- ✅ La contraseña es case-sensitive

### Error: "Timeout"
- ✅ Verifica que Supabase permita conexiones externas
- ✅ Por defecto está permitido, pero revísalo en Settings

---

## 🔒 Seguridad

- ❌ **NUNCA** subas estas credenciales a Git
- ❌ **NUNCA** compartas tu contraseña
- ✅ Usa variables de entorno (como estás haciendo)
- ✅ Railway las encripta automáticamente

---

**¿Todo configurado? Ve a Settings → Domains → Generate Domain para obtener tu URL pública 🌐**

