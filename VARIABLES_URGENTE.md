# 🔴 VARIABLES DE ENTORNO - CONFIGURACIÓN URGENTE

## ⚠️ SI VES ESTE ERROR EN RAILWAY:

```
The connection attempt failed
SQL Error: 0, SQLState: 08001
Could not obtain connection to query metadata
JDBCConnectionException: unable to obtain isolated JDBC connection
```

## 🔴 SIGNIFICA QUE FALTAN LAS VARIABLES DE ENTORNO

---

## ✅ SOLUCIÓN INMEDIATA (5 minutos)

### 1. Ve a Railway Dashboard
https://railway.app/dashboard

### 2. Selecciona tu proyecto

### 3. Click en tu servicio (el que tiene el nombre de tu repo)

### 4. Ve a la pestaña "Variables"

### 5. Añade estas 5 variables (click "+ New Variable" para cada una):

---

## 📋 VARIABLES REQUERIDAS

### Variable 1:
```
Name:  SPRING_PROFILES_ACTIVE
Value: heroku
```

### Variable 2:
```
Name:  SPRING_DATASOURCE_URL
Value: jdbc:postgresql://db.tiifltprjgtyfimhnezi.supabase.co:5432/postgres
```
⚠️ Si tu proyecto de Supabase tiene otra URL, usa la tuya.

### Variable 3:
```
Name:  SPRING_DATASOURCE_USERNAME
Value: postgres
```

### Variable 4: 🔴 LA MÁS IMPORTANTE
```
Name:  SPRING_DATASOURCE_PASSWORD
Value: [TU_CONTRASEÑA_REAL_DE_SUPABASE]
```

**¿Dónde encontrar la contraseña?**
1. Ve a: https://supabase.com/dashboard
2. Click en tu proyecto
3. Click en el ícono de Settings (⚙️) en el sidebar
4. Click en "Database"
5. Busca "Database Password"
   - Si la ves, cópiala
   - Si dice "Hidden", necesitas resetearla:
     - Click en "Reset Database Password"
     - Copia la nueva contraseña
     - ⚠️ Esto afectará otras apps conectadas a esta BD

### Variable 5:
```
Name:  PORT
Value: 8080
```

---

## 🔄 DESPUÉS DE AÑADIR LAS VARIABLES

1. **Railway redesplegarás automáticamente** (verás el proceso)
2. Espera **2-3 minutos**
3. Ve a la pestaña **"Deployments"**
4. Click en el deployment más reciente
5. Click en **"View Logs"**

---

## ✅ LOGS DE ÉXITO (lo que debes ver):

```
BUILD SUCCESSFUL in Xs Ym
```

```
HikariPool-1 - Starting...
HikariPool-1 - Added connection org.postgresql.jdbc.PgConnection@xxxxx
HikariPool-1 - Start completed.
```

```
Started DemoApplication in X.XXX seconds
```

```
Tomcat started on port 8080
```

---

## ❌ SI AÚN FALLA

### Verifica cada variable:

1. **SPRING_DATASOURCE_PASSWORD**
   - ¿Es la correcta?
   - ¿No tiene espacios al inicio o final?
   - ¿Copiaste toda la contraseña?

2. **SPRING_DATASOURCE_URL**
   - ¿Tiene el formato correcto?
   - ¿Incluye `:5432`?
   - ¿Es la URL de TU proyecto de Supabase?

3. **SPRING_PROFILES_ACTIVE**
   - ¿Dice exactamente `heroku` (minúsculas)?

---

## 🔍 VERIFICAR CONEXIÓN A SUPABASE

### Opción 1: Desde Railway Logs
Después del redeploy, si ves:
```
HikariPool-1 - Start completed
```
✅ La conexión funciona

### Opción 2: Desde Supabase Dashboard
1. Ve a tu proyecto en Supabase
2. Settings → Database
3. Verifica que "Connection Pooling" esté habilitado
4. Verifica que no haya restricciones de IP (Railway usa IPs dinámicas)

---

## 📊 TABLA DE VERIFICACIÓN

| Variable | Valor Correcto | ¿Configurada? |
|----------|----------------|---------------|
| SPRING_PROFILES_ACTIVE | `heroku` | [ ] |
| SPRING_DATASOURCE_URL | `jdbc:postgresql://db....:5432/postgres` | [ ] |
| SPRING_DATASOURCE_USERNAME | `postgres` | [ ] |
| SPRING_DATASOURCE_PASSWORD | Tu contraseña real | [ ] |
| PORT | `8080` | [ ] |

---

## 🎯 RESUMEN RÁPIDO

```
1. Railway Dashboard → Tu proyecto → Variables
2. Añadir las 5 variables (especialmente la contraseña)
3. Esperar redeploy automático (2-3 min)
4. Verificar logs: "Started DemoApplication"
5. Generar dominio: Settings → Domains → Generate Domain
```

---

## 💡 CONSEJOS

- **Guarda tu contraseña** en un lugar seguro
- No compartas las variables de entorno públicamente
- Si reseteas la contraseña de Supabase, actualízala también en Railway
- Las variables son case-sensitive (respetan mayúsculas/minúsculas)

---

## ✅ TODO CONFIGURADO

Una vez que veas en los logs:
```
Started DemoApplication in X.XXX seconds
```

Tu app está funcionando. Ve a:
- Railway → Settings → Domains → Generate Domain
- Obtén tu URL pública
- Prueba tu aplicación

---

**¿Configuraste las 5 variables? Railway redesplegarás automáticamente.** 🚀

