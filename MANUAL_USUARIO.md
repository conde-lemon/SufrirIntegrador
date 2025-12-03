# 👤 MANUAL DE USUARIO - TRAVEL4U

## 📋 Tabla de Contenidos

1. [Introducción](#introducción)
2. [Acceso al Sistema](#acceso-al-sistema)
3. [Registro de Usuario](#registro-de-usuario)
4. [Inicio de Sesión](#inicio-de-sesión)
5. [Buscar Vuelos](#buscar-vuelos)
6. [Buscar Cruceros](#buscar-cruceros)
7. [Buscar Buses](#buscar-buses)
8. [Realizar una Reserva](#realizar-una-reserva)
9. [Ver Mis Reservas](#ver-mis-reservas)
10. [Gestionar Perfil](#gestionar-perfil)
11. [Realizar Pagos](#realizar-pagos)
12. [Ver Boletas](#ver-boletas)
13. [Solución de Problemas](#solución-de-problemas)

---

## 🌟 Introducción

**Travel4U** es tu plataforma integral para reservar servicios de viaje:
- ✈️ **Vuelos** nacionales e internacionales
- 🚢 **Cruceros** a destinos increíbles
- 🚌 **Buses** interprovinciales
- 🏨 **Hoteles** (próximamente)

---

## 🔐 Acceso al Sistema

### URL de Acceso

```
http://localhost:8081
```

O si está desplegado:
```
https://travel4u.railway.app
```

### Página Principal

Al ingresar verás:
- Barra de navegación superior
- Buscador de vuelos destacado
- Secciones de servicios disponibles

---

## 📝 Registro de Usuario

### Paso 1: Acceder al Registro

1. Click en el botón **"Registrarse"** en la esquina superior derecha
2. Serás redirigido a `/registrar`

### Paso 2: Completar el Formulario

Ingresa los siguientes datos:

| Campo | Descripción | Ejemplo |
|-------|-------------|---------|
| **Nombre Completo** | Tu nombre y apellidos | Juan Pérez García |
| **Email** | Correo electrónico válido | juan.perez@email.com |
| **Contraseña** | Mínimo 6 caracteres | ******** |
| **Confirmar Contraseña** | Debe coincidir | ******** |
| **Teléfono** | Número de contacto | +51 987654321 |
| **DNI** | Documento de identidad | 12345678 |

### Paso 3: Enviar el Registro

1. Verificar que todos los campos estén correctos
2. Click en **"Registrarse"**
3. Si todo es correcto, serás redirigido al inicio de sesión

### Mensajes de Error Comunes

- **"El email ya está registrado"**: Usa otro email o recupera tu contraseña
- **"Las contraseñas no coinciden"**: Verifica que ambas sean idénticas
- **"DNI inválido"**: Ingresa un DNI válido de 8 dígitos

---

## 🔑 Inicio de Sesión

### Paso 1: Acceder al Login

1. Click en **"Iniciar Sesión"** en la barra superior
2. Serás redirigido a `/login`

### Paso 2: Ingresar Credenciales

1. **Email**: Ingresa tu correo registrado
2. **Contraseña**: Ingresa tu contraseña

### Paso 3: Iniciar Sesión

1. Click en **"Iniciar Sesión"**
2. Si las credenciales son correctas:
   - La barra superior mostrará tu nombre
   - Accederás a todas las funcionalidades

### Cerrar Sesión

1. Click en tu nombre en la barra superior
2. Selecciona **"Cerrar Sesión"**

---

## ✈️ Buscar Vuelos

### Método 1: Desde la Página Principal

1. En la página principal verás el buscador de vuelos
2. Completa los campos:
   - **Origen**: Ciudad de partida (ej: Lima)
   - **Destino**: Ciudad de llegada (ej: Cusco)
3. Click en **"Buscar Vuelos"**

### Método 2: Desde el Menú

1. Click en **"Vuelos"** en la barra de navegación
2. Verás todos los vuelos disponibles
3. Usa el formulario de búsqueda en la parte superior

### Resultados de Búsqueda

La pantalla mostrará:

```
┌─────────────────────────────────────┐
│  Lima - Cusco Directo               │
│  LATAM Airlines                     │
│  Vuelo directo, 1h 30min            │
│  Ruta: Lima → Cusco                 │
│  Tags: vacaciones, top ventas       │
│  S/ 150.00          [Reservar]      │
└─────────────────────────────────────┘
```

### Información de Cada Vuelo

- **Nombre del servicio**: Ruta y tipo
- **Proveedor**: Aerolínea
- **Descripción**: Detalles del vuelo
- **Ruta**: Origen → Destino
- **Tags**: Categorías (vacaciones, familiar, etc.)
- **Precio**: Costo en soles
- **Botón Reservar**: Para proceder con la reserva

### Si No Hay Resultados

El sistema mostrará:
- Mensaje: "No se encontraron vuelos..."
- **Sugerencias**: Vuelos alternativos que podrían interesarte

---

## 🚢 Buscar Cruceros

### Acceso

1. Click en **"Cruceros"** en la barra de navegación
2. URL: `/cruceros`

### Ver Cruceros Disponibles

La pantalla mostrará todos los cruceros disponibles con:
- Nombre del crucero
- Línea naviera
- Destinos incluidos
- Duración
- Precio
- Botón para reservar

### Filtrar Cruceros

Usa el buscador para filtrar por:
- Origen
- Destino
- Precio máximo

---

## 🚌 Buscar Buses

### Acceso

1. Click en **"Buses"** en la barra de navegación
2. URL: `/bus`

### Ver Buses Disponibles

Verás una lista de servicios de bus con:
- Empresa de transporte
- Ruta
- Tipo de servicio (Económico, VIP, etc.)
- Horarios
- Precio
- Disponibilidad

### Buscar Bus Específico

1. Ingresa origen y destino
2. Click en **"Buscar"**
3. Se mostrarán solo los buses que coincidan

---

## 🎫 Realizar una Reserva

### Paso 1: Seleccionar Servicio

1. Busca el servicio deseado (vuelo, crucero o bus)
2. Click en el botón **"Reservar"**

### Paso 2: Seleccionar Asientos (Vuelos y Buses)

Para vuelos:
- URL: `/asientos?servicioId=X`
- Visualizarás un mapa de asientos
- Los asientos ocupados aparecen en rojo
- Los disponibles en verde
- Click en el asiento deseado

Para buses:
- Similar al sistema de vuelos
- URL: `/asientos-bus?servicioId=X`

Para cruceros:
- URL: `/asientos-crucero?servicioId=X`
- Selecciona tipo de cabina

### Paso 3: Confirmar Cantidad de Pasajeros

1. Ingresa la cantidad de pasajeros
2. El precio total se calculará automáticamente
3. Click en **"Continuar"**

### Paso 4: Ingresar Datos de Pasajeros

Para cada pasajero, ingresa:
- Nombre completo
- DNI/Pasaporte
- Fecha de nacimiento
- Nacionalidad

### Paso 5: Confirmar Reserva

1. Revisa todos los datos:
   - Servicio seleccionado
   - Asientos/Cabinas
   - Pasajeros
   - Precio total
2. Click en **"Confirmar Reserva"**

### Paso 6: Realizar el Pago

Serás redirigido a la pasarela de pago (ver sección de pagos)

---

## 📋 Ver Mis Reservas

### Acceder a Reservas

1. Click en tu nombre en la barra superior
2. Selecciona **"Mis Reservas"**
3. URL: `/reservas`

### Lista de Reservas

Verás todas tus reservas con:

```
┌────────────────────────────────────────┐
│ Reserva #12345                         │
│ Lima - Cusco                           │
│ Fecha: 15/12/2025                      │
│ Estado: ✅ CONFIRMADA                  │
│ Precio: S/ 150.00                      │
│ [Ver Detalle] [Descargar Boleta]      │
└────────────────────────────────────────┘
```

### Estados de Reserva

- 🟢 **CONFIRMADA**: Pagada y confirmada
- 🟡 **PENDIENTE**: Esperando pago
- 🔴 **CANCELADA**: Reserva cancelada
- ⏰ **EXPIRADA**: No se completó el pago a tiempo

### Ver Detalle de Reserva

Click en **"Ver Detalle"** para ver:
- Información completa del servicio
- Datos de pasajeros
- Asientos asignados
- Comprobante de pago
- Código de reserva (para check-in)

### Descargar Boleta

1. Click en **"Descargar Boleta"**
2. Se generará un PDF con:
   - Datos de la reserva
   - QR code
   - Detalles del servicio
   - Información de pasajeros

---

## 👤 Gestionar Perfil

### Acceder al Perfil

1. Click en tu nombre en la barra superior
2. Selecciona **"Mi Perfil"**
3. URL: `/perfil`

### Información del Perfil

Verás y podrás editar:
- Nombre completo
- Email (solo lectura)
- Teléfono
- DNI (solo lectura)
- Dirección
- Fecha de nacimiento

### Actualizar Datos

1. Modifica los campos editables
2. Click en **"Guardar Cambios"**
3. Verás un mensaje de confirmación

### Cambiar Contraseña

1. Click en **"Cambiar Contraseña"**
2. Ingresa:
   - Contraseña actual
   - Nueva contraseña
   - Confirmar nueva contraseña
3. Click en **"Actualizar Contraseña"**

---

## 💳 Realizar Pagos

### Métodos de Pago Disponibles

- 💳 **Tarjeta de Crédito/Débito**
- 🌐 **PayPal**
- 💰 **Transferencia Bancaria**

### Proceso de Pago con Tarjeta

1. Después de confirmar la reserva, serás redirigido a `/pasarela-pago`
2. Selecciona **"Tarjeta de Crédito/Débito"**
3. Ingresa los datos:
   ```
   Número de tarjeta:   [____-____-____-____]
   Nombre en tarjeta:   [_________________]
   Fecha expiración:    [MM/AA]
   CVV:                 [___]
   ```
4. Click en **"Pagar S/ XXX.XX"**

### Proceso de Pago con PayPal

1. Selecciona **"PayPal"**
2. Serás redirigido a PayPal
3. Inicia sesión en tu cuenta PayPal
4. Confirma el pago
5. Regresarás a Travel4U con la confirmación

### Confirmación de Pago

Después del pago exitoso:
1. Verás pantalla de confirmación
2. Recibirás email con:
   - Comprobante de pago
   - Código de reserva
   - Boleta electrónica
3. La reserva cambiará a estado **CONFIRMADA**

### Pago Fallido

Si el pago falla:
- Verás mensaje de error
- La reserva quedará en estado **PENDIENTE**
- Tienes 24 horas para completar el pago
- Puedes reintentar desde "Mis Reservas"

---

## 🧾 Ver Boletas

### Acceder a Boletas

1. Click en tu nombre
2. Selecciona **"Mis Boletas"**
3. URL: `/boletas`

### Lista de Boletas

Verás todas las boletas emitidas:

*(Insertar captura: Lista de boletas - /boletas)*

```
┌────────────────────────────────────┐
│ Boleta B001-00012345               │
│ Fecha: 03/12/2025                  │
│ Servicio: Lima - Cusco             │
│ Monto: S/ 150.00                   │
│ [Ver] [Descargar PDF]              │
└────────────────────────────────────┘
```

### Descargar Boleta PDF

1. Click en **"Descargar PDF"**
2. Se abrirá/descargará un PDF con:
   - Logo de Travel4U
   - Número de boleta
   - RUC de la empresa
   - Datos del cliente
   - Detalle del servicio
   - Subtotal, IGV, Total
   - QR code para validación
   - Código de reserva

### Imprimir Boleta

1. Abre el PDF
2. Usa Ctrl+P (Windows) o Cmd+P (Mac)
3. Selecciona impresora
4. Click en **"Imprimir"**

---

## ❓ Solución de Problemas

### No puedo iniciar sesión

**Problema**: "Credenciales incorrectas"

**Soluciones**:
1. Verifica que el email sea correcto
2. Revisa mayúsculas/minúsculas en la contraseña
3. Si olvidaste tu contraseña:
   - Click en "¿Olvidaste tu contraseña?"
   - Ingresa tu email
   - Revisa tu correo para restablecer

### No veo mis reservas

**Problema**: La lista de reservas está vacía

**Soluciones**:
1. Verifica que hayas iniciado sesión
2. Confirma que completaste el proceso de reserva
3. Verifica el estado del pago
4. Refresca la página (F5)

### El pago no se procesó

**Problema**: Error al procesar el pago

**Soluciones**:
1. Verifica los datos de tu tarjeta
2. Confirma que tengas fondos suficientes
3. Intenta con otro método de pago
4. Contacta a tu banco si el problema persiste
5. La reserva se guarda por 24 horas

### No aparecen resultados de búsqueda

**Problema**: "No se encontraron servicios"

**Soluciones**:
1. Verifica que origen y destino sean correctos
2. Intenta con otra fecha
3. Revisa las sugerencias mostradas
4. Prueba con otros destinos cercanos

### Error al seleccionar asientos

**Problema**: No puedo seleccionar asientos

**Soluciones**:
1. Verifica que el asiento esté disponible (verde)
2. Refresca la página
3. Intenta con otro asiento
4. Si persiste, contacta soporte

### No recibí el email de confirmación

**Problema**: No llegó el correo

**Soluciones**:
1. Revisa la carpeta de SPAM
2. Verifica que el email sea correcto en tu perfil
3. Espera 10 minutos (puede haber retraso)
4. Puedes descargar la boleta desde "Mis Reservas"

---

## 📱 Consejos de Uso

### ✅ Mejores Prácticas

1. **Reserva con anticipación**: Mejores precios y disponibilidad
2. **Compara opciones**: Revisa diferentes vuelos/buses antes de reservar
3. **Guarda tus boletas**: Descarga y guarda los PDFs
4. **Verifica datos**: Revisa bien los datos de pasajeros antes de confirmar
5. **Completa el pago rápido**: Las reservas sin pagar expiran en 24h

### ⚡ Atajos de Teclado

- `Ctrl + F`: Buscar en la página
- `F5`: Refrescar página
- `Ctrl + P`: Imprimir boleta
- `Esc`: Cerrar modales

### 📧 Notificaciones por Email

Recibirás emails cuando:
- ✅ Completes una reserva
- 💳 Se procese un pago
- 🎫 Se emita una boleta
- ⏰ Recordatorio de viaje (24h antes)
- ❌ Se cancele una reserva

---

## 📞 Soporte y Contacto

### Contactar Soporte

- 📧 **Email**: soporte@travel4u.com
- 📱 **WhatsApp**: +51 987 654 321
- ⏰ **Horario**: Lun-Dom 8:00 AM - 10:00 PM

### Preguntas Frecuentes

**¿Puedo cancelar una reserva?**
- Sí, desde "Mis Reservas" → "Ver Detalle" → "Cancelar"
- Aplican políticas de cancelación según el proveedor

**¿Puedo cambiar la fecha de mi viaje?**
- Contacta a soporte con tu código de reserva
- Sujeto a disponibilidad y diferencia de tarifa

**¿Los precios incluyen impuestos?**
- Sí, los precios mostrados son finales
- Ya incluyen todos los impuestos

**¿Puedo reservar para otra persona?**
- Sí, ingresa sus datos en la sección de pasajeros
- Asegúrate de tener sus documentos correctos

---

## 🎯 Flujo Completo de Uso

```
1. Registrarse/Iniciar Sesión
        ↓
2. Buscar Servicio (Vuelo/Crucero/Bus)
        ↓
3. Seleccionar Servicio Deseado
        ↓
4. Elegir Asientos/Cabinas
        ↓
5. Ingresar Datos de Pasajeros
        ↓
6. Confirmar Reserva
        ↓
7. Realizar Pago
        ↓
8. Recibir Confirmación
        ↓
9. Descargar Boleta
        ↓
10. ¡Disfrutar del Viaje! ✈️
```

---

**Versión**: 1.0  
**Fecha**: Diciembre 2025  
**Sistema**: Travel4U - Monolito

*** ¡Feliz Viaje con Travel4U! ***

