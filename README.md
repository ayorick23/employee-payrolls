# Employee Payrolls

## Tipo de aplicación

### ✅ Aplicación de escritorio con JavaFX + MySQL

Por qué?

- JavaFX te permite hacer interfaces modernas y amigables (más vistosas que Swing).
- JDBC con MySQL es relativamente sencillo de aprender y hay bastante documentación.
- Puedes hacer un sistema que se vea “profesional” sin entrar en la complejidad de Spring Boot (que es más para equipos y proyectos grandes).

## Funcionalidades recomendadas (para que sea completo pero alcanzable)

### 📌 Nivel básico (obligatorio para que tenga sentido)

- [ ] Login con roles (admin y usuario normal).
- [ ] Gestión de empleados: CRUD (crear, leer, actualizar, eliminar).
- [ ] Cálculo de nómina: sueldo neto con descuentos de ISSS, AFP, ISR, etc.

### 📌 Nivel intermedio (para que destaque un poco)

- [ ] Historial de pagos: que guarde la nómina de cada mes.
- [ ] Generación de reportes PDF/Excel (con iText o Apache POI).
- [ ] Búsqueda avanzada: empleados por nombre, departamento o rango salarial.

### 📌 Nivel avanzado (si tienes tiempo y quieres brillar)

- [ ] Gráficos con JavaFX Charts:

Ejemplo: mostrar en pastel el gasto por departamento o en barras los descuentos de ISSS/AFP.

- [ ] Exportación/Importación de datos: CSV o Excel.
- [ ] Notificaciones simples: por ejemplo, mostrar qué empleados cumplen aniversario o requieren actualización de contrato.

## Tecnologías sugeridas (fáciles de manejar con tu nivel actual)

- JavaFX → para la interfaz (mejor que Swing en diseño).
- JDBC con MySQL → para la base de datos.
- Apache POI → si quieres exportar a Excel.
- iText → si quieres exportar a PDF.
- JavaFX Charts → para gráficos simples (barras, pastel, etc.).

👉 Todo esto lo puedes implementar con ayuda de IA y luego ajustar poco a poco.

## Cómo hacer que se vea “formal”

### Arquitectura en capas

- models → clases Empleado, Nomina, Usuario.
- dao → conexión a BD (EmpleadosDAO, NominaDAO).
- services → cálculos de nómina.
- ui → pantallas JavaFX.
- Base de datos bien diseñada
- Tablas: empleados, usuarios, nominas, detalle_nomina.
- Relaciones con claves foráneas.
- Manual de usuario + capturas
- Explicar cómo usar el sistema (esto siempre da puntos extra).

## Base de Datos

📌 Tablas principales

1. usuarios

Para controlar acceso y roles.

```bash
## usuarios

id_usuario (PK)
nombre_usuario (UNIQUE)
contraseña
rol (ENUM: 'admin', 'usuario')
estado (activo/inactivo)
```

2. empleados

Registro de la información de cada empleado.

## empleados

id_empleado (PK)
nombres
apellidos
dui (único)
nit (único)
fecha_nacimiento
fecha_ingreso
departamento
cargo
salario_base
estado (activo/inactivo)

3. nominas

Encabezado de la nómina (por mes o por periodo).

## nominas

id_nomina (PK)
fecha_generacion
periodo_inicio
periodo_fin
total_planilla
generada_por (FK → usuarios.id_usuario)

4. detalle_nomina

Detalle de pago por empleado (relación entre nómina y empleados).

## detalle_nomina

id_detalle (PK)
id_nomina (FK → nominas.id_nomina)
id_empleado (FK → empleados.id_empleado)
salario_base
horas_extra
bonificaciones
descuentos
isss
afp
isr
salario_neto

📌 Tablas opcionales para funcionalidades intermedias/avanzadas 5. bonificaciones

Si quieres manejar distintos tipos de bonos (ejemplo: bono por productividad, bono navideño).

## bonificaciones

id_bono (PK)
id_empleado (FK → empleados.id_empleado)
tipo_bono
monto
fecha

6. descuentos

Permite manejar descuentos personalizados (ejemplo: préstamos, atrasos).

## descuentos

id_descuento (PK)
id_empleado (FK → empleados.id_empleado)
tipo_descuento
monto
fecha

7. departamentos

Si quieres tener catálogos de departamentos en lugar de un simple campo texto.

## departamentos

id_departamento (PK)
nombre
descripcion

8. logs_sistema

Para registrar actividad importante (ejemplo: quién generó una nómina, quién eliminó un empleado).

## logs_sistema

id_log (PK)
id_usuario (FK → usuarios.id_usuario)
accion
fecha_hora
descripcion

## 📌 Relaciones principales (diagrama lógico simplificado)

usuarios 1 ───< nominas (un usuario genera varias nóminas).
empleados 1 ───< detalle_nomina >─── 1 nominas (relación N:N entre empleados y nóminas).
empleados 1 ───< bonificaciones.
empleados 1 ───< descuentos.
departamentos 1 ───< empleados.
usuarios 1 ───< logs_sistema.

## 📌 Arquitectura en Capas

Capa de presentación (UI) → JavaFX (pantallas: login, gestión empleados, generación nómina, reportes).

Capa de negocio (services) → Contiene la lógica de cálculos (ISSS, AFP, ISR, salario neto).

Capa de datos (dao) → Maneja las consultas SQL y conexión a la base de datos.

Capa de modelo (models) → Clases que representan las entidades (Empleado, Usuario, Nómina, etc.).
