# Use a Java base image
FROM openjdk:17

# Set the working directory
WORKDIR /

# Copy the application jar to the container
COPY target/gateway.war .
EXPOSE 8000
# Set the environment variables
ENV JAVA_OPTS=""

# Define the command to run the application
CMD ["java", "-jar", "gateway.war"]