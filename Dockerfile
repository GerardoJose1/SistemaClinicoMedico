# Multi-stage build para optimizar el tamaño de la imagen
FROM gradle:8.10-jdk21 AS build

# Establecer directorio de trabajo
WORKDIR /app

# Copiar archivos de configuración de Gradle
COPY build.gradle settings.gradle gradlew ./

# Copiar el wrapper de Gradle
COPY gradle ./gradle

# Descargar dependencias (cache layer)
RUN ./gradlew dependencies --no-daemon

# Copiar el código fuente
COPY src ./src

# Construir la aplicación
RUN ./gradlew bootJar --no-daemon

# Imagen final más ligera
FROM eclipse-temurin:21-jre-alpine

# Instalar tzdata para manejo de zonas horarias
RUN apk add --no-cache tzdata

# Establecer directorio de trabajo
WORKDIR /app

# Copiar el JAR desde la etapa de build
COPY --from=build /app/build/libs/*.jar app.jar

# Exponer puerto 8080
EXPOSE 8080

# Variables de entorno por defecto
ENV SPRING_PROFILES_ACTIVE=prod
ENV JAVA_OPTS="-Xmx512m -Xms256m"

# Ejecutar la aplicación
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar /app/app.jar"]
