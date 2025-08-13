# Build stage
FROM maven:3.9.2-eclipse-temurin-17 AS build
WORKDIR /app
COPY pom.xml .
RUN mvn dependency:go-offline
COPY src ./src
RUN mvn clean package -DskipTests

# Runtime stage
FROM eclipse-temurin:17-jre
WORKDIR /app
COPY --from=build /app/target/moneyManager-0.0.1-SNAPSHOT.jar moneyManager.jar
EXPOSE 9090
ENTRYPOINT ["java","-jar","moneyManager.jar"]
