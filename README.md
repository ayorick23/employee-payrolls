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
