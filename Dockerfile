# ---------- Stage 1: Build ----------
FROM maven:3.9.3-eclipse-temurin-17 AS build

WORKDIR /app

COPY tp2-devops/pom.xml ./
RUN mvn dependency:go-offline -B
COPY tp2-devops/src ./src
RUN mvn clean package -DskipTests

# ---------- Stage 2: Run ----------
FROM eclipse-temurin:17-jdk-alpine

WORKDIR /app

COPY --from=build /app/target/*.jar app.jar

EXPOSE 4040

ENTRYPOINT ["java", "-jar", "app.jar"]
