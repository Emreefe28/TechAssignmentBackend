# Spring Boot Project

A RESTful API built with Spring Boot and Maven, configured to run with Docker Compose.

## Prerequisites

Before running this project, make sure you have the following installed:

- Java 21 or higher
- Docker and Docker Compose
- Maven

## Quick Start

Follow these steps to get the application up and running:

### 1. Build the Application

Clean and package the Spring Boot application using Maven Wrapper:

```bash
./mvnw clean package -DskipTests
```

This command will:
- Clean any previous builds
- Compile the source code
- Package the application into a JAR file
- Skip running tests for faster build times

### 2. Start the Application

Launch the application and database using Docker Compose:

```bash
docker compose up
```

This will:
- Start the PostgreSQL database container
- Build and start the Spring Boot application container
- Set up the necessary networking between containers

### 3. Access the Application

Once the containers are running, the server will be live and accessible at:

```
http://localhost:8080
```

The API is now ready to receive requests from your frontend application.

## Stopping the Application

To stop the application and all containers:

```bash
docker compose down
```

To stop and remove all data (including database volumes):

```bash
docker compose down -v
```

## Development Notes

- The application uses PostgreSQL as the database
- Database tables are automatically created based on JPA entities
- The application runs on port 8080 by default
- Database data persists between container restarts

