# ==========================================
# STAGE 1: Build
# ==========================================
FROM eclipse-temurin:21-jdk AS build
WORKDIR /app

# Copiar archivos de configuración de Gradle
COPY demo/gradlew .
COPY demo/gradle gradle
COPY demo/build.gradle .
COPY demo/settings.gradle .

# Copiar código fuente
COPY demo/src src

# Dar permisos de ejecución y construir
RUN chmod +x gradlew
RUN ./gradlew clean build -x test --no-daemon

# ==========================================
# STAGE 2: Runtime
# ==========================================
FROM eclipse-temurin:21-jre
WORKDIR /app

# Copiar JAR construido
COPY --from=build /app/build/libs/*.jar app.jar

# Heroku asigna el puerto dinámicamente
EXPOSE $PORT

# Configuración de JVM optimizada para Heroku
ENV JAVA_OPTS="-Xmx512m -Xms256m -XX:+UseContainerSupport -XX:MaxRAMPercentage=75.0"

# Comando de inicio - Heroku provee $PORT
CMD ["sh", "-c", "java $JAVA_OPTS -Dserver.port=$PORT -jar app.jar"]
