# Sistema de Gestión para Gimnasio 🏋️

Proyecto académico que digitaliza el registro manual (cuaderno) de un gimnasio,
aplicando la arquitectura por capas de Spring Web MVC y Spring Data JPA vistas en clase.

## Problema

El gimnasio utiliza cuadernos para registrar clientes, pagos, asistencias y membresías,
lo que puede ocasionar pérdida de información, errores de registro y dificultad para
conocer rápidamente el estado de los clientes.

## Solución

Sistema web con arquitectura en capas:

```
Entity → Repository → Service → Controller → Vista (Thymeleaf)
```

### Módulos incluidos
- **Clientes**: alta, edición, baja y búsqueda por nombre/apellido.
- **Membresías**: tipo (diario/semanal/mensual/personalizada), fechas y estado (activa/vencida, calculado automáticamente).
- **Pagos**: registro de pagos por cliente, método de pago y próxima fecha de pago; total recaudado del mes.
- **Asistencias**: registro de entrada por cliente con fecha y hora, listado del día.
- **Dashboard**: indicadores generales (clientes, membresías activas/por vencer/vencidas, pagos del mes, asistencias de hoy).

## Tecnologías

- Java 17
- Spring Boot 3.3 (Spring Web MVC, Spring Data JPA, Thymeleaf, Validation)
- MySQL
- Maven
- Lombok

## Configuración y ejecución local

1. Crear la base de datos (o dejar que se cree sola, ver `application.properties`):
   ```sql
   CREATE DATABASE gym_db;
   ```
2. Editar `src/main/resources/application.properties` con tu usuario y contraseña de MySQL:
   ```properties
   spring.datasource.username=root
   spring.datasource.password=tu_password
   ```
3. Ejecutar el proyecto:
   ```bash
   mvn spring-boot:run
   ```
4. Abrir en el navegador: [http://localhost:8080](http://localhost:8080)

Las tablas se crean automáticamente gracias a `spring.jpa.hibernate.ddl-auto=update`.

## Estructura del proyecto

```
src/main/java/com/pontificia/gym/
 ├── entity/       -> Cliente, Membresia, Pago, Asistencia (Modelo)
 ├── repository/   -> Interfaces JpaRepository (acceso a datos)
 ├── service/      -> Interfaces + impl/ (lógica de negocio)
 ├── controller/   -> Controladores Spring MVC
 └── config/       -> Manejo global de errores
src/main/resources/
 ├── templates/    -> Vistas Thymeleaf (una carpeta por módulo)
 ├── static/css/   -> Estilos
 └── application.properties
```

## Subir el proyecto a GitHub (desde cero)

Sigue estos pasos en la terminal, dentro de la carpeta del proyecto:

```bash
# 1. Inicializar el repositorio local
git init

# 2. Agregar todos los archivos al área de preparación
git add .

# 3. Crear el primer commit
git commit -m "Primer commit: estructura inicial del sistema de gestión de gimnasio"

# 4. Crear el repositorio en GitHub (desde la web github.com -> New repository)
#    NO marques "Add a README" para evitar conflictos con este proyecto.

# 5. Conectar tu repositorio local con el remoto de GitHub
git remote add origin https://github.com/TU-USUARIO/gym-sistema.git

# 6. Renombrar la rama principal (si es necesario)
git branch -M main

# 7. Subir el código
git push -u origin main
```

### Flujo de trabajo recomendado para siguientes cambios

```bash
git add .
git commit -m "Descripción breve del cambio"
git push
```

Si trabajas con tu compañero(a), usen ramas para no pisarse el trabajo:

```bash
git branch nombre-de-la-funcionalidad
git switch nombre-de-la-funcionalidad
# ... hacen sus cambios y commits ...
git push -u origin nombre-de-la-funcionalidad
# luego crean un Pull Request en GitHub para fusionar a main
```

## Autor
Proyecto desarrollado para la asignatura de Ingeniería de Sistemas de Información —
Escuela de Educación Superior Tecnológica Privada La Pontificia.
