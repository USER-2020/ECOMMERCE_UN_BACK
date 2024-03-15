# Etapa de construcción
# Usamos una imagen base que tiene Maven y JDK 11 instalados
FROM maven:3.6.3-jdk-11 as build

# Copiamos el código fuente de nuestra aplicación al contenedor
COPY src /home/app/src
COPY pom.xml /home/app

# Construimos nuestra aplicación Spring Boot
# Esto producirá un archivo JAR en el directorio target/
RUN mvn -f /home/app/pom.xml clean package

# Etapa de ejecución
# Usamos una imagen base con OpenJDK 11 para ejecutar nuestra aplicación
FROM openjdk:11.0.2-jre-slim

# Copiamos el archivo JAR desde la etapa de construcción
COPY --from=build /home/app/target/*.jar /usr/local/lib/app.jar

# Exponemos el puerto en el que se ejecutará nuestra aplicación
EXPOSE 8080

# Comando para ejecutar nuestra aplicación
ENTRYPOINT ["java","-jar","/usr/local/lib/app.jar"]
