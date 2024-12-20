# Use an official OpenJDK 21 runtime as a parent image
FROM openjdk:21-slim

# Set the working directory in the container
WORKDIR /app

# Copy the JAR file from the target directory of your Maven project to the container
COPY target/payment-server-1.0-SNAPSHOT.jar payment-server.jar

# Expose the port that your application will run on
EXPOSE 8080

# Command to run the JAR file
ENTRYPOINT ["java", "-jar", "payment-server.jar"]
