# Use a Java base image
FROM openjdk:17

# Set the working directory
WORKDIR /

# Copy the application jar to the container
COPY target/eureka.war .
EXPOSE 8761
# Set the environment variables
ENV JAVA_OPTS=""

# Define the command to run the application
CMD ["java", "-jar", "eureka.war"]