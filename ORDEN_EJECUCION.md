# 🎯 ORDEN DE EJECUCIÓN - DEPLOY A RAILWAY

## PASO 1: LIMPIAR ARCHIVOS OBSOLETOS

```powershell
cd "C:\Users\LENOVO\Documents\utp\ciclo7\integrador\demo (1)"
.\limpiar.ps1
```

**Esto eliminará:**
- ~35 archivos obsoletos
- Documentación duplicada
- Scripts antiguos
- SQL temporales
- Properties no usados

**Resultado:** Proyecto limpio y organizado

---

## PASO 2: DESPLEGAR A RAILWAY

```powershell
.\deploy.ps1
```

**Esto hará:**
- ✅ Verificar que todo esté correcto
- ✅ Subir código a GitHub
- ✅ Mostrarte qué hacer en Railway

---

## PASO 3: CONFIGURAR EN RAILWAY

1. Ve a: https://railway.app/dashboard
2. Si es primera vez: **New Project** → **Deploy from GitHub repo**
3. Configurar 5 variables de entorno (ver RAILWAY_DEPLOY.md)
4. Generar dominio

---

## 🕐 TIEMPO ESTIMADO

- Limpieza: **30 segundos**
- Deploy (Git push): **1-2 minutos**
- Railway build: **5-8 minutos**
- **TOTAL: ~10 minutos**

---

## ✅ VERIFICACIÓN FINAL

Después del deploy, verifica:
- [ ] Build completado en Railway (logs: "BUILD SUCCESSFUL")
- [ ] App iniciada (logs: "Started DemoApplication")
- [ ] Dominio generado y funcionando
- [ ] Conexión a Supabase exitosa

---

**¡Comienza ejecutando `.\limpiar.ps1` ahora!** 🚀

