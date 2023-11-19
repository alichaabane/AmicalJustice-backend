# Use Maven image as the base image
FROM maven:3.8.4-openjdk-17-slim AS build

# Set the working directory in the container
WORKDIR /justice

# Copy the project's pom.xml and download dependencies
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copy the project source code
COPY src ./src

# Build the application
RUN mvn package -DskipTests

# Use AdoptOpenJDK 17 as the base image for running the application
FROM openjdk:17-jdk-alpine

# Set the working directory in the container
WORKDIR /justice

# Copy the JAR file from the build stage to the container
COPY --from=build /justice/target/justice-0.0.1-SNAPSHOT.jar ./justice.jar

# Expose the port the application runs on
EXPOSE 8083

# Define the entry point to run the application when the container starts
ENTRYPOINT ["java", "-jar", "justice.jar"]
