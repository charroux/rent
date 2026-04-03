FROM gradle:8.7-jdk21 as builder
WORKDIR /app
COPY . .
RUN ./gradle clean build -x test

# Runtime stage
FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=builder /app/build/libs/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
