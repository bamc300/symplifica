# Diseño y Documentación: Backend Symplifica
**Candidato:** Prueba Técnica
**Stack:** Java 25 (21 fallback), Spring Boot 3.4.x, PostgreSQL 18, Ollama (LLaMA 3)

## 1. Estructura General del Proyecto
El proyecto está estructurado bajo principios de arquitectura por capas asegurando separación de responsabilidades:
- `com.symplifica.backend.config`: Configuración global de la aplicación (Seguridad Básica, Cliente REST, Excepciones Globales).
- `com.symplifica.backend.controller`: Capa de exposición de Endpoints REST (Controladores).
- `com.symplifica.backend.entity`: Modelos de dominio mapeados con JPA a la base de datos PostgreSQL.
- `com.symplifica.backend.job`: Orquestación de tareas en segundo plano programadas por JobRunr (El `NewsJob`).
- `com.symplifica.backend.repository`: Interfaces Spring Data JPA para la persistencia.
- `com.symplifica.backend.service`: Lógica de negocio core (Lectura de RSS, Integración LangChain4j, JavaMail).

## 2. Entidades y Modelo de Relaciones
- **`User` (users):** Entidad base exigida por la prueba. Almacena la información de usuarios (id, nombre, email, password_hash, estado y fechas de auditoría).
- **`JobPreference` (job_preferences):** [Bono] Relación `ManyToOne` con `User` (FK `user_id`). Almacena configuraciones preferidas del usuario para la ejecución de sus notificaciones personalizadas.
- **`NewsLog` (news_log):** Entidad transaccional enfocada en la trazabilidad. Almacena la fecha de ejecución, resumen generado en cada ciclo por el LLM y su estado final (SUCCESS, SKIPPED, FAILED).

## 3. Decisiones Técnicas Relevantes
- **Integración con Ollama:** Se optó por la librería *LangChain4j* (`langchain4j-ollama-spring-boot-starter`) en lugar de peticiones crudas REST dado su acoplamiento modular y abstracción fluida de modelos en Java. Por defecto, utilicé `llama3`.
- **Estructura del Job:** Se usó una aproximación "Fail-Safe". Si el RSS arroja noticias vacías o nulas, el servicio no invoca al LLM para ahorrar recursos locales y registra un `SKIPPED`.
- **Plantilla de Correo:** Para no saturar el artefacto de Java compilado con dependencias de Thymeleaf, opté por leer un archivo HTML puro inyectando tags simples como `{{SUMMARY_CONTENT}}`, proporcionando estilo CSS básico incrustado.

## 4. Implementaciones Bonus
- Se añadieron controladores **REST CRUD Bonus** (`UserController` y `NewsLogController`) para observar interacciones internas de la plataforma de JobRunr desde la capa web.
- Se inyectó seguridad mediante **Spring Security**. El panel de endpoints expuestos está protegido, requiriendo un token de Basic Auth por defecto (admin:admin123).
- Se centralizó el control de errores usando el `GlobalExceptionHandler` con soporte a `@ControllerAdvice`.
