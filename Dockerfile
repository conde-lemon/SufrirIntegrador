# ==========================================
# STAGE 1: Build
# ==========================================
FROM eclipse-temurin:21-jdk AS build
WORKDIR /app

# Copiar archivos de Gradle wrapper primero (IMPORTANTE: mantener estructura exacta)
COPY demo/gradlew ./
COPY demo/gradlew.bat ./
COPY demo/gradle ./gradle

# Copiar archivos de configuración de Gradle
COPY demo/build.gradle ./
COPY demo/settings.gradle ./

# Copiar código fuente
COPY demo/src ./src

# Dar permisos de ejecución
RUN chmod +x gradlew

# Construir la aplicación
RUN ./gradlew clean build -x test --no-daemon

# ==========================================
# STAGE 2: Runtime
# ==========================================
FROM eclipse-temurin:21-jre
WORKDIR /app

# Copiar JAR construido
COPY --from=build /app/build/libs/*.jar app.jar

# Puerto por defecto (Railway/Heroku lo sobrescriben con $PORT)
EXPOSE 8080

# Configuración de JVM optimizada
ENV JAVA_OPTS="-Xmx512m -Xms256m -XX:+UseContainerSupport -XX:MaxRAMPercentage=75.0"

# Comando de inicio - usa $PORT si existe, sino 8080
CMD ["sh", "-c", "java $JAVA_OPTS -Dserver.port=${PORT:-8080} -jar app.jar"]
