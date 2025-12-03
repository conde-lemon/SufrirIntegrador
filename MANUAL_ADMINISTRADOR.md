# 👨‍💼 MANUAL DE ADMINISTRADOR - TRAVEL4U

## 📋 Tabla de Contenidos

1. [Introducción](#introducción)
2. [Acceso al Panel de Administración](#acceso-al-panel)
3. [Gestión de Servicios](#gestión-de-servicios)
4. [Gestión de Proveedores](#gestión-de-proveedores)
5. [Gestión de Usuarios](#gestión-de-usuarios)
6. [Gestión de Reservas](#gestión-de-reservas)
7. [Reportes y Estadísticas](#reportes-y-estadísticas)
8. [Configuración del Sistema](#configuración-del-sistema)
9. [Monitoreo y Logs](#monitoreo-y-logs)
10. [Mantenimiento](#mantenimiento)

---

## 🎯 Introducción

Como administrador de Travel4U, tienes acceso completo para:
- ✅ Gestionar servicios (vuelos, cruceros, buses)
- ✅ Administrar proveedores
- ✅ Supervisar usuarios y reservas
- ✅ Generar reportes
- ✅ Configurar el sistema
- ✅ Monitorear el rendimiento

---

## 🔐 Acceso al Panel de Administración

### Credenciales de Administrador

**Usuario predeterminado**:
```
Email: admin@travel4u.com
Contraseña: Admin123!
```

> ⚠️ **IMPORTANTE**: Cambia la contraseña predeterminada inmediatamente después del primer acceso.

### Acceder al Panel

1. Ir a `http://localhost:8081/login`
2. Ingresar credenciales de administrador
3. Serás redirigido automáticamente al panel de admin
4. URL del panel: `/admin/dashboard`

### Permisos de Administrador

Los administradores pueden:
- ✅ Ver y modificar TODO el contenido
- ✅ Acceder a funciones restringidas
- ✅ Generar reportes
- ✅ Configurar el sistema
- ❌ Los usuarios regulares NO pueden acceder a estas funciones

---

## 🛫 Gestión de Servicios

### Ver Todos los Servicios

**URL**: `/admin/servicios`

Verás una tabla con todos los servicios:

| ID | Tipo | Nombre | Origen | Destino | Precio | Disponibilidad | Estado | Acciones |
|----|------|--------|--------|---------|--------|----------------|--------|----------|
| 1 | VUELO | Lima-Cusco | Lima | Cusco | S/ 150 | 50 | Activo | [Editar] [Eliminar] |
| 2 | BUS | Lima-Arequipa | Lima | Arequipa | S/ 80 | 40 | Activo | [Editar] [Eliminar] |

### Crear Nuevo Servicio

1. Click en **"Nuevo Servicio"**
2. Completar el formulario:

```
┌─────────────────────────────────────────┐
│ CREAR NUEVO SERVICIO                    │
├─────────────────────────────────────────┤
│ Tipo de Servicio: [VUELO ▼]            │
│ Nombre:          [________________]     │
│ Descripción:     [________________]     │
│                  [________________]     │
│ Origen:          [________________]     │
│ Destino:         [________________]     │
│ Precio Base:     [______] S/            │
│ Disponibilidad:  [___] asientos         │
│ Tags:            [________________]     │
│                  (separados por comas)  │
│ Proveedor:       [Seleccionar ▼]       │
│ Fecha Salida:    [__/__/____]          │
│ Fecha Llegada:   [__/__/____]          │
│                                         │
│        [Cancelar]    [Guardar]         │
└─────────────────────────────────────────┘
```

3. Click en **"Guardar"**

### Tipos de Servicio Disponibles

- **VUELO**: Vuelos aéreos
- **CRUCERO**: Viajes en crucero
- **BUS**: Transporte terrestre
- **HOTEL**: Alojamiento (futuro)

### Editar Servicio Existente

1. Click en **"Editar"** en la fila del servicio
2. Modifica los campos necesarios
3. Click en **"Actualizar"**

**Campos editables**:
- Nombre
- Descripción
- Precio
- Disponibilidad
- Tags
- Fechas
- Estado (Activo/Inactivo)

### Eliminar Servicio

1. Click en **"Eliminar"**
2. Confirmar la acción en el modal
3. El servicio se marca como **inactivo** (soft delete)

> 📝 **Nota**: No se elimina físicamente, solo se desactiva para mantener el historial de reservas.

### Activar/Desactivar Servicio

**Toggle de Estado**:
- ✅ **Activo**: Visible para usuarios
- ❌ **Inactivo**: Oculto para usuarios, solo visible en admin

### Importar Servicios desde CSV

1. Click en **"Importar CSV"**
2. Formato del CSV:
```csv
tipo,nombre,descripcion,origen,destino,precio,disponibilidad,tags,proveedor_id
VUELO,Lima-Cusco,Vuelo directo,Lima,Cusco,150.00,50,"vacaciones,top ventas",1
BUS,Lima-Arequipa,Bus cama,Lima,Arequipa,80.00,40,"económico,nocturno",2
```
3. Seleccionar archivo
4. Click en **"Importar"**
5. Revisar resumen de importación

---

## 🏢 Gestión de Proveedores

### Ver Todos los Proveedores

**URL**: `/admin/proveedores`

Tabla de proveedores:

| ID | Nombre | Tipo | Email | Teléfono | Estado | Acciones |
|----|--------|------|-------|----------|--------|----------|
| 1 | LATAM Airlines | AEROLÍNEA | latam@contact.com | +51 123456 | Activo | [Editar] [Ver Servicios] |
| 2 | Cruz del Sur | BUS | info@cruzdelsur.pe | +51 234567 | Activo | [Editar] [Ver Servicios] |

### Crear Nuevo Proveedor

1. Click en **"Nuevo Proveedor"**
2. Completar formulario:

```
┌─────────────────────────────────────────┐
│ CREAR NUEVO PROVEEDOR                   │
├─────────────────────────────────────────┤
│ Nombre:          [____________________] │
│ Tipo:            [AEROLÍNEA ▼]         │
│ Contacto:        [____________________] │
│ Email:           [____________________] │
│ Teléfono:        [____________________] │
│ Dirección:       [____________________] │
│ RUC:             [___________]          │
│ Comisión (%):    [__]%                  │
│                                         │
│        [Cancelar]    [Guardar]         │
└─────────────────────────────────────────┘
```

### Tipos de Proveedor

- **AEROLÍNEA**: Compañías aéreas
- **NAVIERA**: Líneas de cruceros
- **TRANSPORTE**: Empresas de buses
- **HOTELERA**: Cadenas hoteleras

### Editar Proveedor

1. Click en **"Editar"**
2. Modificar campos
3. Click en **"Actualizar"**

### Ver Servicios del Proveedor

1. Click en **"Ver Servicios"**
2. Muestra todos los servicios asociados al proveedor
3. Permite editar o desactivar servicios

### Configurar Comisión

La comisión es el porcentaje que el proveedor paga a Travel4U por cada venta:

1. En edición de proveedor
2. Campo **"Comisión (%)"**: Ingresar porcentaje (ej: 10%)
3. Se aplicará automáticamente en reportes de ventas

---

## 👥 Gestión de Usuarios

### Ver Todos los Usuarios

**URL**: `/admin/usuarios`

Tabla de usuarios:

| ID | Nombre | Email | DNI | Teléfono | Rol | Reservas | Estado | Acciones |
|----|--------|-------|-----|----------|-----|----------|--------|----------|
| 1 | Juan Pérez | juan@email.com | 12345678 | 987654321 | USER | 5 | Activo | [Ver] [Editar] [Bloquear] |
| 2 | Admin | admin@travel4u.com | - | - | ADMIN | 0 | Activo | [Ver] [Editar] |

### Ver Detalle de Usuario

Click en **"Ver"** para ver:
- Información personal completa
- Historial de reservas
- Pagos realizados
- Actividad reciente

### Editar Usuario

1. Click en **"Editar"**
2. Modificar campos permitidos:
   - Nombre
   - Teléfono
   - Dirección
   - Estado (Activo/Bloqueado)
3. **NO** se puede modificar:
   - Email (es el identificador único)
   - DNI (por seguridad)

### Bloquear/Desbloquear Usuario

**Bloquear**:
1. Click en **"Bloquear"**
2. Motivo: "Fraude" / "Violación términos" / "Otro"
3. Confirmar
4. El usuario no podrá iniciar sesión

**Desbloquear**:
1. Click en **"Desbloquear"**
2. Confirmar
3. El usuario recupera el acceso

### Cambiar Rol de Usuario

1. En edición de usuario
2. Campo **"Rol"**: Seleccionar nuevo rol
   - **USER**: Usuario regular
   - **ADMIN**: Administrador
3. Click en **"Actualizar"**

> ⚠️ **CUIDADO**: Solo asignar rol ADMIN a personal de confianza.

### Restablecer Contraseña de Usuario

1. Click en **"Restablecer Contraseña"**
2. Se genera una contraseña temporal
3. Se envía email al usuario
4. El usuario debe cambiarla en el primer login

### Buscar Usuarios

Filtros disponibles:
- **Por DNI**: Buscar por documento
- **Por Email**: Buscar por correo
- **Por Nombre**: Búsqueda parcial
- **Por Estado**: Activos/Bloqueados
- **Por Rol**: USER/ADMIN

---

## 📋 Gestión de Reservas

### Ver Todas las Reservas

**URL**: `/admin/reservas`

Vista de reservas:

| ID | Usuario | Servicio | Fecha Reserva | Fecha Viaje | Pasajeros | Total | Estado | Acciones |
|----|---------|----------|---------------|-------------|-----------|-------|--------|----------|
| 101 | Juan P. | Lima-Cusco | 01/12/2025 | 15/12/2025 | 2 | S/ 300 | CONFIRMADA | [Ver] [Cancelar] |
| 102 | María G. | Lima-Arequipa | 02/12/2025 | 20/12/2025 | 1 | S/ 80 | PENDIENTE | [Ver] [Recordar Pago] |

### Estados de Reserva

- 🟢 **CONFIRMADA**: Reserva pagada y confirmada
- 🟡 **PENDIENTE**: Esperando pago (24h)
- 🔴 **CANCELADA**: Cancelada por usuario o admin
- ⏰ **EXPIRADA**: No se completó el pago
- ✈️ **COMPLETADA**: Viaje realizado

### Ver Detalle de Reserva

Click en **"Ver"** para mostrar:

```
┌─────────────────────────────────────────┐
│ RESERVA #000101                         │
├─────────────────────────────────────────┤
│ Usuario: Juan Pérez (juan@email.com)   │
│ DNI: 12345678                          │
│                                         │
│ SERVICIO                                │
│ Vuelo: Lima - Cusco                    │
│ Fecha: 15/12/2025 08:00 AM             │
│ Proveedor: LATAM Airlines              │
│                                         │
│ PASAJEROS                               │
│ 1. Juan Pérez García - DNI 12345678   │
│ 2. María López Torres - DNI 87654321  │
│                                         │
│ ASIENTOS                                │
│ 15A, 15B                               │
│                                         │
│ PAGO                                    │
│ Método: Tarjeta de crédito             │
│ Fecha: 01/12/2025 14:30               │
│ Comprobante: TXN-12345678              │
│ Total: S/ 300.00                       │
│                                         │
│ ESTADO: ✅ CONFIRMADA                  │
│                                         │
│ [Descargar Boleta] [Enviar Email]     │
│ [Cancelar Reserva]                     │
└─────────────────────────────────────────┘
```

### Cancelar Reserva (Admin)

1. Click en **"Cancelar Reserva"**
2. Seleccionar motivo:
   - Cancelación por cliente
   - Servicio cancelado
   - Fraude
   - Otro
3. **Política de reembolso**:
   - > 7 días: Reembolso 100%
   - 3-7 días: Reembolso 50%
   - < 3 días: Sin reembolso
4. Confirmar cancelación
5. Se envía email al usuario

### Modificar Reserva

1. Click en **"Modificar"**
2. Cambios permitidos:
   - Asientos/cabinas
   - Fecha (sujeto a disponibilidad)
   - Datos de pasajeros
3. Recalcular precio si hay diferencia
4. Actualizar y notificar usuario

### Recordar Pago Pendiente

Para reservas en estado **PENDIENTE**:
1. Click en **"Recordar Pago"**
2. Se envía email recordatorio
3. Incluye enlace directo al pago

### Filtros de Búsqueda

Filtrar reservas por:
- **Estado**: CONFIRMADA, PENDIENTE, etc.
- **Fecha de viaje**: Rango de fechas
- **Servicio**: Tipo (vuelo, bus, crucero)
- **Usuario**: Por DNI o email
- **Proveedor**: Filtrar por empresa

### Exportar Reservas

1. Aplicar filtros deseados
2. Click en **"Exportar"**
3. Formatos disponibles:
   - **Excel (.xlsx)**: Para análisis
   - **CSV (.csv)**: Para importar
   - **PDF (.pdf)**: Para imprimir

---

## 📊 Reportes y Estadísticas

### Dashboard Principal

**URL**: `/admin/dashboard`

Muestra KPIs principales:

```
┌────────────────┬────────────────┬────────────────┐
│ VENTAS HOY     │ RESERVAS MES   │ USUARIOS       │
│ S/ 12,500.00   │ 156 reservas   │ 1,234 total    │
│ ↑ +15%         │ ↑ +8%          │ ↑ +12%         │
└────────────────┴────────────────┴────────────────┘

┌────────────────────────────────────────────────┐
│ GRÁFICO DE VENTAS (Últimos 7 días)            │
│                                                │
│   ██████                                       │
│   ██████  ████                                 │
│   ██████  ████  ████  ████                     │
│   ██████  ████  ████  ████  ████  ████  ████  │
│   L       M     M     J     V     S     D      │
│                                                │
│ Total: S/ 85,400.00                           │
└────────────────────────────────────────────────┘

┌────────────────────────────────────────────────┐
│ TOP 5 SERVICIOS MÁS VENDIDOS                  │
│ 1. Lima - Cusco.............. 45 ventas       │
│ 2. Lima - Arequipa.......... 32 ventas        │
│ 3. Crucero Caribe........... 18 ventas        │
│ 4. Lima - Trujillo.......... 15 ventas        │
│ 5. Lima - Piura............. 12 ventas        │
└────────────────────────────────────────────────┘
```

### Reporte de Ventas

**URL**: `/admin/reportes/ventas`

Parámetros:
- **Período**: Hoy / Semana / Mes / Rango personalizado
- **Tipo de servicio**: Todos / Vuelos / Buses / Cruceros
- **Proveedor**: Todos / Específico

Información mostrada:
- Total de ventas (S/)
- Número de transacciones
- Ticket promedio
- Gráfico de tendencia
- Desglose por tipo de servicio
- Comparativa con período anterior

**Exportar**:
- PDF con gráficos
- Excel con datos detallados

### Reporte de Reservas

**URL**: `/admin/reportes/reservas`

Estadísticas:
- Total de reservas por estado
- Reservas por tipo de servicio
- Reservas por proveedor
- Distribución por destinos
- Ocupación promedio

### Reporte de Usuarios

**URL**: `/admin/reportes/usuarios`

Métricas:
- Nuevos registros por período
- Usuarios activos vs inactivos
- Distribución geográfica
- Clientes frecuentes (top 10)
- Tasa de conversión (visitas → reservas)

### Reporte de Proveedores

**URL**: `/admin/reportes/proveedores`

Por proveedor:
- Total de servicios ofrecidos
- Servicios vendidos
- Ingresos generados
- Comisión a pagar
- Rating promedio

**Generar Estado de Cuenta**:
1. Seleccionar proveedor
2. Seleccionar período
3. Click en **"Generar Estado de Cuenta"**
4. Se genera PDF con:
   - Detalle de ventas
   - Comisión calculada
   - Total a pagar/cobrar

### Reporte Financiero

**URL**: `/admin/reportes/financiero`

Incluye:
- Ingresos totales
- Comisiones pagadas
- Reembolsos procesados
- Ingresos netos
- Proyección mensual
- Comparativa anual

---

## ⚙️ Configuración del Sistema

### Configuración General

**URL**: `/admin/configuracion`

#### Información de la Empresa

```
Nombre:     [Travel4U                    ]
RUC:        [20123456789]
Dirección:  [Av. Principal 123, Lima    ]
Teléfono:   [+51 1 234 5678             ]
Email:      [info@travel4u.com          ]
Website:    [www.travel4u.com           ]
```

#### Políticas de Reserva

```
Tiempo máximo para pagar:  [24] horas
Cancelación gratuita:      [✓] Habilitada
  - Hasta [7] días antes
Modificación de reservas:  [✓] Permitida
  - Costo: S/ [20.00]
```

#### Comisiones

```
Comisión estándar aerolíneas:  [10]%
Comisión estándar buses:       [8]%
Comisión estándar cruceros:    [15]%
```

#### Pagos

```
Métodos habilitados:
[✓] Tarjeta de crédito/débito
[✓] PayPal
[ ] Transferencia bancaria
[ ] Pago en efectivo
```

### Configuración de Emails

```
Servidor SMTP:     [smtp.gmail.com      ]
Puerto:            [587]
Usuario:           [noreply@travel4u.com]
Contraseña:        [****************    ]
Cifrado:           [TLS ▼]

Templates de Email:
- Confirmación de reserva    [Editar]
- Recordatorio de pago       [Editar]
- Boleta electrónica         [Editar]
- Recordatorio de viaje      [Editar]
```

### Configuración de Notificaciones

```
Notificar al admin cuando:
[✓] Nueva reserva realizada
[✓] Pago recibido
[✓] Reserva cancelada
[ ] Usuario nuevo registrado
[✓] Error en el sistema

Email para notificaciones:
[admin@travel4u.com          ]
```

### Mantenimiento del Sistema

```
Modo mantenimiento: [ ] Activado

Cuando activado:
- Usuarios no podrán acceder
- Se muestra página de mantenimiento
- Admins pueden acceder normalmente

Mensaje personalizado:
[Estamos mejorando nuestro servicio.    ]
[Volveremos pronto.                      ]
```

---

## 📈 Monitoreo y Logs

### Dashboard de Monitoreo

**URL**: `/admin/monitor`

#### Métricas en Tiempo Real

```
┌────────────────────────────────────────┐
│ ESTADO DEL SISTEMA                     │
├────────────────────────────────────────┤
│ Uptime:          99.8%                 │
│ CPU:             [████████░░] 78%     │
│ Memoria:         [██████░░░░] 62%     │
│ Base de datos:   ✅ Conectada         │
│ Usuarios online: 23                    │
└────────────────────────────────────────┘

┌────────────────────────────────────────┐
│ ACTIVIDAD RECIENTE                     │
├────────────────────────────────────────┤
│ 16:45 - Nueva reserva #102345          │
│ 16:43 - Pago recibido S/ 150.00       │
│ 16:40 - Usuario juan@... registrado   │
│ 16:35 - Admin inició sesión            │
└────────────────────────────────────────┘
```

### Logs del Sistema

**URL**: `/admin/logs`

*(Insertar captura: Vista de logs con filtros y niveles)*

Tipos de logs:
- **INFO**: Operaciones normales
- **WARNING**: Advertencias (no críticas)
- **ERROR**: Errores que requieren atención
- **FATAL**: Errores críticos del sistema

Filtros:
- Por nivel (INFO, WARNING, ERROR)
- Por fecha/hora
- Por usuario/sesión
- Por módulo (Auth, Reservas, Pagos, etc.)

**Ejemplo de log**:
```
[2025-12-03 16:45:23] [INFO] [ReservaController] 
Usuario juan@email.com creó reserva #102345 
para servicio Lima-Cusco (ID: 1)

[2025-12-03 16:43:15] [ERROR] [PagoService]
Error al procesar pago con PayPal
TransactionID: TXN-12345678
Error: Insufficient funds
Usuario: maria@email.com
```

### Exportar Logs

1. Aplicar filtros deseados
2. Click en **"Exportar"**
3. Formato: TXT o CSV
4. Útil para auditorías

---

## 🔧 Mantenimiento

### Respaldo de Base de Datos

**URL**: `/admin/mantenimiento/backup`

#### Crear Respaldo Manual

1. Click en **"Crear Respaldo Ahora"**
2. Se genera archivo SQL
3. Se guarda en: `/backups/backup_YYYYMMDD_HHMMSS.sql`
4. Click en **"Descargar"** para guardar localmente

#### Respaldos Automáticos

```
Programación de respaldos:
[✓] Activado

Frecuencia: [Diario ▼]
Hora:       [02:00 AM ▼]
Retener:    [7] respaldos

Ubicación:  [/backups/               ]
```

#### Restaurar desde Respaldo

1. Click en **"Restaurar"**
2. Seleccionar archivo de respaldo
3. **ADVERTENCIA**: Se perderán datos actuales
4. Confirmar restauración
5. Esperar a que finalice

### Limpiar Datos Antiguos

```
Eliminar datos obsoletos:

[Eliminar] reservas expiradas de hace más de [90] días
[Eliminar] sesiones caducadas de hace más de [30] días  
[Eliminar] logs de hace más de [180] días

[ Cancelar ]  [ Limpiar Ahora ]
```

### Optimizar Base de Datos

1. Click en **"Optimizar BD"**
2. Procesos que se ejecutan:
   - VACUUM (liberar espacio)
   - ANALYZE (actualizar estadísticas)
   - REINDEX (reconstruir índices)
3. **Nota**: Puede tardar varios minutos
4. Hacer en horarios de baja actividad

### Verificar Integridad

1. Click en **"Verificar Integridad"**
2. Verifica:
   - Consistencia de datos
   - Referencias huérfanas
   - Índices corruptos
3. Muestra reporte de problemas
4. Ofrece opciones de reparación

---

## 🚨 Procedimientos de Emergencia

### Sistema Caído

1. Verificar logs en `/admin/logs`
2. Revisar estado del servidor
3. Verificar conexión a base de datos
4. Activar **Modo Mantenimiento**
5. Notificar al equipo técnico

### Problema con Pagos

1. Ir a `/admin/pagos-pendientes`
2. Identificar transacciones fallidas
3. Contactar a proveedores de pago
4. Procesar manualmente si es necesario
5. Notificar a usuarios afectados

### Fraude Detectado

1. Bloquear usuario inmediatamente
2. Cancelar reservas sospechosas
3. Marcar transacciones para revisión
4. Documentar el caso
5. Reportar a autoridades si necesario

---

## 📱 Acceso Móvil para Admins

El panel de administración es **responsive** y funciona en:
- 📱 Smartphones
- 📲 Tablets
- 💻 Laptops
- 🖥️ Desktop

**Funciones móviles**:
- Ver reservas
- Confirmar/cancelar reservas
- Ver estadísticas
- Recibir notificaciones
- Acceso a logs

---

## ✅ Checklist Diario del Administrador

### Por la Mañana
- [ ] Revisar dashboard y KPIs
- [ ] Verificar reservas del día
- [ ] Confirmar pagos pendientes
- [ ] Revisar logs de errores
- [ ] Responder consultas de usuarios

### Durante el Día
- [ ] Monitorear nuevas reservas
- [ ] Gestionar cancelaciones
- [ ] Resolver incidencias
- [ ] Actualizar disponibilidad si necesario

### Por la Noche
- [ ] Verificar respaldo automático
- [ ] Revisar ventas del día
- [ ] Preparar reporte para mañana
- [ ] Cerrar tickets resueltos

---

## 🎓 Mejores Prácticas

### Seguridad

1. ✅ Cambia la contraseña regularmente
2. ✅ No compartas credenciales de admin
3. ✅ Cierra sesión en computadoras compartidas
4. ✅ Usa conexión segura (HTTPS)
5. ✅ Habilita autenticación de dos factores

### Gestión de Servicios

1. ✅ Verifica disponibilidad real antes de publicar
2. ✅ Mantén precios actualizados
3. ✅ Desactiva servicios que ya no están disponibles
4. ✅ Actualiza descripciones regularmente

### Atención al Cliente

1. ✅ Responde consultas en menos de 24h
2. ✅ Sé proactivo con problemas
3. ✅ Documenta casos especiales
4. ✅ Mantén comunicación clara

---

## 📞 Soporte Técnico

### Contacto de Emergencia

- 📧 **Email**: tech@travel4u.com
- 📱 **Teléfono**: +51 987 654 321
- 💬 **Slack**: #admin-support
- ⏰ **Disponibilidad**: 24/7 para emergencias

### Documentación Técnica

- [Manual de API](/docs/api)
- [Base de Datos Schema](/docs/database)
- [Guía de Deploy](/docs/deployment)

---

**Versión**: 1.0  
**Fecha**: Diciembre 2025  
**Sistema**: Travel4U - Monolito  
**Nivel de Acceso**: ADMINISTRADOR

*** Este documento contiene información confidencial. No compartir. ***

