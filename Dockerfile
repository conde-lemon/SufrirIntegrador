# Dockerfile optimizado para Render
FROM eclipse-temurin:21-jdk-alpine

# Instalar dependencias necesarias
RUN apk add --no-cache curl

# Crear directorio de trabajo
WORKDIR /app

# Copiar archivos de Gradle
COPY demo/gradle/ gradle/
COPY demo/gradlew .
COPY demo/gradlew.bat .
COPY demo/build.gradle .
COPY demo/settings.gradle .

# Dar permisos de ejecución
RUN chmod +x ./gradlew

# Copiar código fuente
COPY demo/src/ src/

# Construir la aplicación (sin tests para acelerar)
RUN ./gradlew build -x test --no-daemon

# Crear directorio para logs
RUN mkdir -p /app/logs

# Exponer puerto (Render usa PORT env variable)
EXPOSE $PORT

# Variables de entorno para Render
ENV SPRING_PROFILES_ACTIVE=render
ENV JAVA_OPTS="-Xmx512m -Xms256m"

# Comando de inicio con puerto dinámico de Render
CMD java $JAVA_OPTS -Dserver.port=$PORT -jar build/libs/demo-0.0.1-SNAPSHOT.jar