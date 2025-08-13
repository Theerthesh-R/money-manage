# Use Maven + JDK image to build the project
FROM maven:3.9.2-eclipse-temurin-21 AS build

WORKDIR /app

# Copy pom.xml first to leverage Docker cache
COPY pom.xml .
# Download dependencies
RUN mvn dependency:go-offline

# Copy source code
COPY src ./src

# Build the JAR
RUN mvn clean package -DskipTests

# Use a smaller JRE image to run the app
FROM eclipse-temurin:21-jre

WORKDIR /app

# Copy the JAR from the build stage
COPY --from=build /app/target/moneyManager-0.0.1-SNAPSHOT.jar moneyManager.jar

EXPOSE 9090

ENTRYPOINT ["java", "-jar", "moneyManager.jar"]
