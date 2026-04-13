# Etapa de compilación: Maven 3.9+ y JDK 17
FROM maven:3.9.9-eclipse-temurin-17 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn -B -DskipTests package

# Imagen de ejecución: JRE 17 (Ubuntu para evitar problemas de usuario en Alpine)
FROM eclipse-temurin:17-jre-jammy
WORKDIR /app
COPY --from=build /app/target/servicio-precios-1.0.0-SNAPSHOT.jar /app/app.jar
RUN chown nobody:nogroup /app/app.jar
USER nobody
EXPOSE 8080
# Perfiles: Spring Boot lee `SPRING_PROFILES_ACTIVE` del entorno en runtime (p. ej. `docker run -e SPRING_PROFILES_ACTIVE=prod`).
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
