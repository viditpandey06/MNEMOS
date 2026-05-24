# Stage 1: Build the Java application
FROM maven:3.9-eclipse-temurin-17 AS builder
WORKDIR /app
COPY pom.xml .
RUN mvn dependency:go-offline
COPY src ./src
RUN mvn clean package

# Stage 2: Minimal runtime environment
FROM eclipse-temurin:17-jre-jammy
WORKDIR /app
COPY --from=builder /app/target/mnemos-1.0.jar ./mnemos.jar

EXPOSE 8080

CMD ["java", "-jar", "mnemos.jar"]
