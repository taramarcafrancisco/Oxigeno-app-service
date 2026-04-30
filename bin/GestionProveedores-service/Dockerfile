FROM eclipse-temurin:8-jdk

WORKDIR /app

COPY . .

RUN chmod +x mvnw
RUN ./mvnw clean package -DskipTests
RUN ls -l target
RUN cp target/*.jar /app/app.jar

EXPOSE 5388

ENTRYPOINT ["java", "-jar", "/app/app.jar"]
