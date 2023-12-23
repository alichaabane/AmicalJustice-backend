## PART 1
# Use Maven image as the base image
FROM maven:3.8.4-openjdk-17-slim AS build
# Set the working directory in the container
WORKDIR /justice-backend
ARG CONTAINER_PORT
# Copy the project's pom.xml and download dependencies
COPY pom.xml /justice-backend
#RUN mvn dependency:go-offline -B
RUN mvn dependency:resolve
# Copy the project source code
COPY . /justice-backend
# Clean & Build the application
RUN mvn clean
RUN mvn package -DskipTests -X

## PART 2
# Use AdoptOpenJDK 17 as the base image for running the application
FROM openjdk:17-jdk-alpine
# Copy the JAR file from the build stage to the container
COPY --from=build /justice-backend/target/*.jar justice-backend.jar

# Copy the static files/folders to the container
COPY src/main/resources/static ./src/main/resources/static

# Expose the port the application runs on
EXPOSE ${CONTAINER_PORT}
# Define the entry point to run the application when the container starts
CMD ["java", "-jar", "justice-backend.jar"]
