# ==========================================
# STAGE 1: Build
# ==========================================
FROM eclipse-temurin:21-jdk AS build
WORKDIR /app

# Instalar Gradle directamente (evitar problemas con el wrapper)
RUN apt-get update && \
    apt-get install -y wget unzip && \
    wget https://services.gradle.org/distributions/gradle-8.5-bin.zip && \
    unzip gradle-8.5-bin.zip && \
    mv gradle-8.5 /opt/gradle && \
    rm gradle-8.5-bin.zip && \
    apt-get clean

# Agregar Gradle al PATH
ENV PATH="/opt/gradle/bin:${PATH}"

# Copiar archivos del proyecto
COPY demo/build.gradle ./
COPY demo/settings.gradle ./
COPY demo/src ./src

# Construir la aplicación con Gradle instalado
RUN gradle clean build -x test --no-daemon

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
