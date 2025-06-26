FROM eclipse-temurin:17-jdk-alpine
WORKDIR /app
COPY ./api /app
WORKDIR /app
RUN ./mvnw clean package -DskipTests
CMD ["java", "-jar", "target/api-0.0.1-SNAPSHOT.jar"]
