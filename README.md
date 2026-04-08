# Backend Prueba Técnica - Symplifica

Proyecto backend en Java Spring Boot que consume un feed de RSS, resume las noticias relevantes usando un LLM local (Ollama) mediante LangChain4j, envía un boletín por e-mail y guarda un historial de ejecuciones, todo orquestado proactivamente con JobRunr.

## Requisitos Previos
* **Java 25** (o mínimo Java 21 para compatibilidad garantizada).
* **PostgreSQL 18** (desplegable fácilmente vía Docker).
* **Ollama** instalado localmente y ejecutándose en el puerto `11434`.
* Además de tener Ollama, es obligatorio haber descargado el modelo objetivo. Por defecto usamos LLaMA 3. (Ejecuta `ollama run llama3` previamente).
* **Docker y Docker Compose** (para levantar base de datos y MailHog).

## Variables de Entorno / Propiedades
Las configuraciones pueden editarse en `src/main/resources/application.yml`:
* `spring.datasource.url`: `jdbc:postgresql://localhost:5432/symplifica_db`
* `spring.datasource.username`: `user`
* `spring.datasource.password`: `password`
* `langchain4j.ollama.chat-model.base-url`: Ruta base de Ollama (por defecto `http://localhost:11434`).
* `langchain4j.ollama.chat-model.model-name`: Nombre del modelo a usar (ej: `llama3`, `phi`, etc.).

## Pasos para ejecutar la aplicación localmente
1. **Clonar repositorio** y ubicarse en el directorio principal.
2. **Levantar base de datos y SMTP de pruebas**:
   ```bash
   docker-compose up -d
   ```
   *Esto habilitará PostgreSQL en `localhost:5432` y MailHog (SMTP en `localhost:1025`, Interfaz Web en `localhost:8025`).*
3. **Asegurar la ejecución de Ollama**.
4. **Ejecutar la aplicación**:
   Puedes ejecutarlo directamente usando el wrapper de Maven (o Maven local):
   ```bash
   mvn spring-boot:run
   ```
   *La base de datos será automáticamente poblada con migraciones controladas por Flyway.*

## Cómo ejecutar las pruebas unitarias
Las pruebas cubren los servicios principales mediante JUnit 5 y se auxilian de Mockito para desconectarse de la necesidad de una BD o internet activo:
```bash
mvn test
```

## Dashboard y Monitorización
* **JobRunr Dashboard:** Visible en `http://localhost:8000/` tras arrancar la aplicación.
* **Mails enviados:** Pueden visualizarse abriendo MailHog en `http://localhost:8025/`.
* **Endpoints:** Existen llamadas REST accesibles usando Authentication Basic (`admin:admin123`):
  * `GET http://localhost:8080/api/users`
  * `GET http://localhost:8080/api/logs`
