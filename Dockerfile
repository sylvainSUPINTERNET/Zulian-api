# Build stage
FROM maven:3.6.0-jdk-11-slim AS build
WORKDIR /api
COPY . .
RUN mvn clean install -DskipTests

# Run stage
FROM openjdk:11
EXPOSE 9999
RUN ls -al
COPY --from=build /api/target/zg-1.0.0-SNAPSHOT.jar /api/zg-1.0.0-SNAPSHOT.jar
ENTRYPOINT ["java","-jar","/api/zg-1.0.0-SNAPSHOT.jar"]