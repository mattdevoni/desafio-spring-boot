# Sistema de Gestión de Tareas

Sistema de gestión de tareas desarrollado con Spring Boot 3.4.3 y Java 17, que permite a los usuarios crear, actualizar, eliminar y listar tareas. Incluye autenticación mediante JWT y documentación de la API utilizando OpenAPI y Swagger.

## Requisitos Previos

- Java 17
- Maven 3.8+

## Tecnologías Utilizadas

- Spring Boot 3.4.3
- Spring Security con JWT
- Spring Data JPA
- Base de datos H2 (en memoria)
- OpenAPI/Swagger para documentación
- Lombok

## Configuración y Ejecución

### Compilar el Proyecto

```bash
mvn clean package
```

### Ejecutar la Aplicación

```bash
mvn spring-boot:run
```

La aplicación estará disponible en: http://localhost:8080

### Acceder a la Documentación de la API

Una vez que la aplicación esté en ejecución, puedes acceder a la documentación Swagger en:

```
http://localhost:8080/swagger-ui.html
```

También puedes acceder directamente a la especificación OpenAPI en formato JSON:

```
http://localhost:8080/v3/api-docs
```

### Acceder a la Consola H2

La consola H2 está disponible en:

```
http://localhost:8080/h2-console
```

Configuración de conexión:
- JDBC URL: `jdbc:h2:mem:taskdb`
- Usuario: `sa`
- Contraseña: `password`

## Pruebas de la API

### Colección de Postman

Se incluye una colección de Postman completa (`Gestion_Tareas_API.postman_collection.json`) en la raíz del proyecto para facilitar las pruebas de la API. Para utilizarla:

1. Importa la colección en Postman
2. Configura la variable de entorno `base_url` con el valor `http://localhost:8080`
3. Ejecuta primero la solicitud "Login" para obtener un token JWT
4. El token se guardará automáticamente y se utilizará en las demás solicitudes

### Orden Recomendado para Probar los Endpoints

Para probar correctamente la API, se recomienda seguir este orden:

1. **Autenticación**: 
   - `POST /api/auth/login` - Iniciar sesión con un usuario existente (ej: usuario1/password123)
   - El token JWT se guardará automáticamente en la variable de entorno

2. **Consultas**:
   - `GET /api/tareas` - Listar todas las tareas del usuario
   - `GET /api/tareas/{id}` - Obtener una tarea específica
   - `GET /api/estados` - Listar todos los estados de tareas
   - `GET /api/estados/{id}` - Obtener un estado específico

3. **Operaciones de Escritura**:
   - `POST /api/tareas` - Crear una nueva tarea
   - `PUT /api/tareas/{id}` - Actualizar una tarea existente
   - `DELETE /api/tareas/{id}` - Eliminar una tarea

Nota: Asegúrate de usar IDs válidos al probar los endpoints que requieren un ID. Puedes obtener IDs válidos listando primero todas las tareas.

### Ejemplos de JSON para Pruebas

#### Autenticación (Login)

```json
{
  "username": "usuario1",
  "password": "password123"
}
```

#### Crear Tarea

```json
{
  "titulo": "Nueva tarea de prueba",
  "descripcion": "Descripción detallada de la tarea de prueba",
  "fechaVencimiento": "2025-04-15T18:00:00",
  "estadoId": 1,
  "prioridad": 2
}
```

#### Actualizar Tarea

```json
{
  "titulo": "Tarea actualizada",
  "descripcion": "Descripción actualizada de la tarea",
  "fechaVencimiento": "2025-04-20T18:00:00",
  "estadoId": 2,
  "prioridad": 1
}
```

### Ejecutar Pruebas Unitarias

Para ejecutar las pruebas unitarias del proyecto:

```bash
mvn test
```

Para generar un informe de cobertura de código con JaCoCo:

```bash
mvn test jacoco:report
```

El informe estará disponible en: `target/site/jacoco/index.html`

## Endpoints de la API

### Autenticación

- **Registro de Usuario**: `POST /api/auth/signup`
- **Inicio de Sesión**: `POST /api/auth/login`

### Gestión de Tareas

- **Listar Tareas**: `GET /api/tareas`
- **Obtener Tarea por ID**: `GET /api/tareas/{id}`
- **Crear Tarea**: `POST /api/tareas`
- **Actualizar Tarea**: `PUT /api/tareas/{id}`
- **Eliminar Tarea**: `DELETE /api/tareas/{id}`

## Datos Precargados

La aplicación incluye los siguientes datos precargados:

### Usuarios
- Usuario: usuario1 / Contraseña: password123 (Matías Alarcón)
- Usuario: usuario2 / Contraseña: password123 (Usuario X)

### Estados de Tarea
- Pendiente (ID: 1)
- En Progreso (ID: 2)
- Completada (ID: 3)
- Cancelada (ID: 4)

### Tareas Existentes
- ID 1: "Preparar presentación trimestral" (Usuario: Matías Alarcón, Estado: En Progreso)
- ID 2: "Coordinar reunión con clientes" (Usuario: Usuario X, Estado: Pendiente)
- ID 3: "Actualizar inventario mensual" (Usuario: Matías Alarcón, Estado: Completada)
- ID 4: "Revisar contratos vigentes" (Usuario: Usuario X, Estado: Pendiente)
- ID 5: "Organizar evento corporativo" (Usuario: Usuario X, Estado: En Progreso)

## Ejemplos de Uso con cURL

### Inicio de Sesión (con usuario existente)

```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "username": "usuario1",
    "password": "password123"
  }'
```

Esto devolverá un token JWT que deberás usar en las siguientes peticiones.

### Registro de Usuario Nuevo

```bash
curl -X POST http://localhost:8080/api/auth/signup \
  -H "Content-Type: application/json" \
  -d '{
    "username": "usuario3",
    "password": "password123",
    "nombre": "Juan",
    "apellido": "Pérez",
    "email": "juan.perez@ejemplo.com"
  }'
```

### Crear una Tarea Nueva

```bash
curl -X POST http://localhost:8080/api/tareas \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer {tu_token_jwt}" \
  -d '{
    "titulo": "Gestionar proveedores",
    "descripcion": "Contactar nuevos proveedores para mejorar costos",
    "fechaVencimiento": "2025-04-15T18:00:00",
    "estadoId": 1,
    "prioridad": 2
  }'
```

### Listar Tareas

```bash
curl -X GET http://localhost:8080/api/tareas \
  -H "Authorization: Bearer {tu_token_jwt}"
```

### Obtener Tarea por ID

```bash
curl -X GET http://localhost:8080/api/tareas/1 \
  -H "Authorization: Bearer {tu_token_jwt}"
```

### Actualizar Tarea Existente

```bash
curl -X PUT http://localhost:8080/api/tareas/1 \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer {tu_token_jwt}" \
  -d '{
    "titulo": "Preparar presentación trimestral",
    "descripcion": "Incluir análisis financiero y proyecciones para el próximo trimestre",
    "fechaVencimiento": "2025-03-25T18:00:00",
    "estadoId": 3,
    "prioridad": 1
  }'
```

### Eliminar Tarea

```bash
curl -X DELETE http://localhost:8080/api/tareas/5 \
  -H "Authorization: Bearer {tu_token_jwt}"
```

## Colección de Postman

Para facilitar las pruebas, se incluye una colección de Postman en el archivo `Gestion_Tareas_API.postman_collection.json` en la raíz del proyecto.

## Autor

Matías Alarcón - [matias.alarcon.pob@gmail.com](mailto:matias.alarcon.pob@gmail.com)
