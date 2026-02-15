# ---------- BUILD STAGE ----------
FROM maven:3.9.6-eclipse-temurin-17 AS build

WORKDIR /app

# Copy only backend folder content
COPY backend/pom.xml .
COPY backend/src ./src

RUN mvn clean package -DskipTests

# ---------- RUNTIME STAGE ----------
FROM eclipse-temurin:17-jdk-alpine

WORKDIR /app

COPY --from=build /app/target/parking-booking-backend-1.0.0.jar app.jar

EXPOSE 8080

CMD ["java", "-jar", "app.jar"]
