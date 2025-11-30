# Use OpenJDK 17 Alpine image
FROM eclipse-temurin:17-jdk-alpine

# Set working directory inside container
WORKDIR /app

# Copy Maven wrapper and pom.xml first to leverage Docker cache
COPY ./pom.xml ./
COPY ./mvnw ./
COPY .mvn .mvn

# Download dependencies (cache layer)
RUN chmod +x mvnw && ./mvnw dependency:go-offline -B

# Copy the rest of the source code
COPY ./src ./src

# Package the Spring Boot application
RUN ./mvnw clean package -DskipTests

# Expose port (Render uses PORT env)
EXPOSE 8080

# Run the jar (replace 'your-app-name' with your actual jar name)
CMD ["sh", "-c", "java -jar target/$(ls target | grep '.jar' | head -n 1)"]
