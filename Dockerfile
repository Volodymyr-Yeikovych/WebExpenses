# Build
FROM maven:4.0.0-openjdk21 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
LABEL authors="Volodymyr"
RUN mvn clean install

# Run
FROM openjdk:21
WORKDIR /app
COPY --from=build /app/target/webExpenses-0.0.1-SNAPSHOT.jar ./exps-mgr.jar
EXPOSE 8080
CMD ["java", "-jar", "exps-mgr.jar"]