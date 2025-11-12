# Trabajo Práctico Microservicios y APIs Escalables - Clínica Odontológica

Requisitos
- Java 17 (o la versión configurada en el pom.xml)
- Maven

Cómo ejecutar
1) Compilar el proyecto:
   mvn clean package

2) Ejecutar con Maven:
   mvn spring-boot:run

   o ejecutar el JAR generado:
   java -jar target/tp-microservicios-clinica-odontologica-0.0.1-SNAPSHOT.jar

El servicio quedará disponible por defecto en http://localhost:8080

Probar con Postman
- Se brinda una [colección de Postman con requests de ejemplo](documentacion/tp-clinica.postman_collection.json)
- Importar esa colección en Postman y probar endpoints como:
  - POST /paciente
  - GET /paciente
  - POST /odontologo
  - GET /odontologo
  - POST /turnos

Notas
- Si cambia el puerto, actualizar las URLs en la colección de Postman.
- Para cualquier error revisar los logs en la consola donde se ejecuta la aplicación.
