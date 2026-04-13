FROM eclipse-temurin:8-jdk

WORKDIR /app

COPY . .

RUN chmod +x mvnw
RUN ./mvnw clean package -DskipTests

# DEBUG (clave para validar)
RUN ls -l target

# Copia segura del jar (sin wildcard directo)
RUN cp $(ls target/*.jar | head -n 1) /app/app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/app/app.jar"]