-- Insertar estados de tarea
INSERT INTO estados_tarea (id, nombre, descripcion) VALUES 
(1, 'Pendiente', 'Tarea que aun no ha sido iniciada'),
(2, 'En Progreso', 'Tarea que esta siendo trabajada actualmente'),
(3, 'Completada', 'Tarea que ha sido finalizada'),
(4, 'Cancelada', 'Tarea que ha sido cancelada');

-- Insertar usuarios (contraseña: password123)
INSERT INTO usuarios (id, username, password, nombre, apellido, email) VALUES 
(1, 'usuario1', '$2a$12$iZwgH3KPw9bkL3WTY6CaxO0BSclVfVZ3lca5PIOazzz86I/W57DdS', 'Matias', 'Alarcon', 'matias.alarcon@ejemplo.com'),
(2, 'usuario2', '$2a$12$iZwgH3KPw9bkL3WTY6CaxO0BSclVfVZ3lca5PIOazzz86I/W57DdS', 'Usuario', 'X', 'usuario.x@ejemplo.com');

-- Insertar algunas tareas de ejemplo
INSERT INTO tareas (id, titulo, descripcion, fecha_creacion, fecha_vencimiento, estado_id, usuario_id, prioridad) VALUES 
(1, 'Preparar presentacion trimestral', 'Elaborar presentacion con resultados financieros del trimestre', '2025-03-10T10:00:00', '2025-03-20T18:00:00', 2, 1, 3),
(2, 'Coordinar reunion con clientes', 'Organizar reunion con clientes potenciales para presentar nuevos productos', '2025-03-11T09:00:00', '2025-03-15T18:00:00', 1, 2, 2),
(3, 'Actualizar inventario mensual', 'Realizar conteo fisico y actualizar sistema de inventario', '2025-03-12T11:00:00', '2025-03-18T18:00:00', 3, 1, 1),
(4, 'Revisar contratos vigentes', 'Analizar terminos y condiciones de contratos actuales con proveedores', '2025-03-10T11:00:00', '2025-03-18T18:00:00', 1, 2, 2),
(5, 'Organizar evento corporativo', 'Coordinar logistica para evento anual de la empresa', '2025-03-11T13:00:00', '2025-03-16T18:00:00', 2, 2, 3);

-- Reiniciar las secuencias de autoincremento para que comiencen después de los valores existentes
ALTER TABLE estados_tarea ALTER COLUMN id RESTART WITH 5;
ALTER TABLE usuarios ALTER COLUMN id RESTART WITH 3;
ALTER TABLE tareas ALTER COLUMN id RESTART WITH 6;
