# 📂 ¿DÓNDE EJECUTAR LOS COMANDOS?

## 🎯 UBICACIÓN EXACTA

Todos los comandos deben ejecutarse en la **carpeta raíz del proyecto**:

```
C:\Users\LENOVO\Documents\utp\ciclo7\integrador\demo (1)
```

**⚠️ NO EN LA CARPETA `demo`** (esa es una subcarpeta)

---

## 📋 ESTRUCTURA DEL PROYECTO

```
C:\Users\LENOVO\Documents\utp\ciclo7\integrador\
│
└── demo (1)\                           ← 👈 EJECUTA AQUÍ
    ├── setup-railway.ps1               ← El script está aquí
    ├── Dockerfile                      ← Configuración Docker
    ├── RAILWAY_QUICKSTART.md           ← Guía rápida
    ├── GUIA_DESPLIEGUE_RAILWAY.md      ← Guía completa
    │
    └── demo\                           ← Subcarpeta (NO ejecutes aquí)
        ├── src\
        ├── build.gradle
        └── ...
```

---

## 🖥️ MÉTODO 1: PowerShell Manual

### Paso 1: Abrir PowerShell
1. Presiona `Windows + X`
2. Selecciona **"Windows PowerShell"** o **"Terminal"**

### Paso 2: Navegar a la carpeta
```powershell
cd "C:\Users\LENOVO\Documents\utp\ciclo7\integrador\demo (1)"
```

### Paso 3: Verificar que estás en el lugar correcto
```powershell
ls
```

Deberías ver estos archivos:
- `setup-railway.ps1` ✅
- `Dockerfile` ✅
- `demo/` (carpeta) ✅
- `RAILWAY_QUICKSTART.md` ✅

### Paso 4: Ejecutar el script
```powershell
.\setup-railway.ps1
```

---

## 🖱️ MÉTODO 2: Click Derecho (MÁS FÁCIL)

### En Windows 11:
1. Abre el Explorador de Archivos
2. Navega a: `C:\Users\LENOVO\Documents\utp\ciclo7\integrador\demo (1)`
3. Click derecho en un espacio vacío de la carpeta
4. Selecciona **"Abrir en Terminal"**
5. Ejecuta: `.\setup-railway.ps1`

### En Windows 10:
1. Abre el Explorador de Archivos
2. Navega a: `C:\Users\LENOVO\Documents\utp\ciclo7\integrador\demo (1)`
3. Mantén presionado `Shift` + Click derecho en espacio vacío
4. Selecciona **"Abrir ventana de PowerShell aquí"**
5. Ejecuta: `.\setup-railway.ps1`

---

## 🖥️ MÉTODO 3: Desde VS Code / IntelliJ

### VS Code:
1. Abre la carpeta `demo (1)` en VS Code
2. Presiona `` Ctrl + ` `` (backtick) para abrir terminal
3. Ejecuta: `.\setup-railway.ps1`

### IntelliJ IDEA:
1. Abre el proyecto
2. Ve a **View → Tool Windows → Terminal**
3. Verifica que estés en la raíz (deberías ver `demo (1)` en el path)
4. Ejecuta: `.\setup-railway.ps1`

---

## ✅ VERIFICACIÓN: ¿Estoy en el lugar correcto?

Ejecuta este comando para verificar:

```powershell
Get-Location
```

**Debe mostrar:**
```
Path
----
C:\Users\LENOVO\Documents\utp\ciclo7\integrador\demo (1)
```

**Y al ejecutar:**
```powershell
Test-Path .\setup-railway.ps1
```

**Debe mostrar:**
```
True
```

Si muestra `False`, estás en la carpeta incorrecta.

---

## ❌ ERRORES COMUNES

### Error 1: "No se encuentra el script"
```
.\setup-railway.ps1 : El término '.\setup-railway.ps1' no se reconoce...
```

**Causa:** Estás en la carpeta incorrecta

**Solución:**
```powershell
cd "C:\Users\LENOVO\Documents\utp\ciclo7\integrador\demo (1)"
```

---

### Error 2: "No se puede cargar porque la ejecución de scripts está deshabilitada"
```
no se puede cargar porque running scripts is disabled on this system
```

**Solución:**
```powershell
Set-ExecutionPolicy -Scope Process -ExecutionPolicy Bypass
.\setup-railway.ps1
```

---

### Error 3: "Debes ejecutar este script desde la raíz del proyecto"
```
ERROR: Debes ejecutar este script desde la raíz del proyecto
```

**Causa:** Estás dentro de la carpeta `demo/` en lugar de `demo (1)/`

**Solución:**
```powershell
cd ..
.\setup-railway.ps1
```

---

## 🎯 RESUMEN RÁPIDO

### La forma más fácil:

1. **Copia esta ruta completa:**
   ```
   C:\Users\LENOVO\Documents\utp\ciclo7\integrador\demo (1)
   ```

2. **Abre PowerShell** (Windows + X → PowerShell)

3. **Escribe `cd` + espacio + pega la ruta + Enter:**
   ```powershell
   cd "C:\Users\LENOVO\Documents\utp\ciclo7\integrador\demo (1)"
   ```

4. **Ejecuta:**
   ```powershell
   .\setup-railway.ps1
   ```

---

## 📝 COMANDOS ALTERNATIVOS (Sin Script)

Si prefieres no usar el script, ejecuta estos comandos manualmente:

```powershell
# 1. Navegar a la carpeta correcta
cd "C:\Users\LENOVO\Documents\utp\ciclo7\integrador\demo (1)"

# 2. Verificar estado de Git
git status

# 3. Si no tienes remote de GitHub, añádelo:
git remote add origin https://github.com/TU_USUARIO/travel4u-app.git

# 4. Cambiar a rama main
git branch -M main

# 5. Añadir archivos
git add .

# 6. Hacer commit
git commit -m "Preparar para Railway"

# 7. Push a GitHub
git push -u origin main
```

---

## 💡 TIPS

- **Usa Tab para autocompletar:** Escribe `cd "C:\Users\` y presiona Tab
- **Copia la ruta desde el Explorador:** Click en la barra de direcciones → Ctrl+C
- **Usa el historial de PowerShell:** Presiona ↑ para comandos anteriores

---

**¿Todo claro? ¡Adelante con el deploy! 🚀**

