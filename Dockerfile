# Build
FROM maven:3.9.6-amazoncorretto-21 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
COPY src/main/resources/db/exps-db.mv.db ./src/main/resources/db/exps-db.mv.db
RUN mvn clean install
LABEL authors="Volodymyr"

# Run
FROM openjdk:21
WORKDIR /app
COPY --from=build /app/target/webExpenses-0.0.1-SNAPSHOT.jar ./exps-mgr.jar
EXPOSE 8080
CMD ["java", "-jar", "exps-mgr.jar"]