# Auth Service

## Overview
The `auth_service` is a microservice responsible for handling authentication and 
authorization tasks within the InfoVault application. It is built using Java and the Spring Boot framework.

## Features
- User Registration
- User Login with JWT Authentication
- Password Management
- Role-based Access Control (RBAC)
- Token Validation

## Project Structure
```plaintext
auth_service/
│
├── src/
│   ├── main/
│   │   ├── java/               # Java source files
│   │   │   └── com/
│   │   │       └── infovault/
│   │   │           ├── InfoVaultApplication.java  # Main application entry point
│   │   │           ├── config/                    # Configuration classes
│   │   │           │   └── SecurityConfig.java    # Spring Security configuration
│   │   │           └── service/                   # Service classes
│   │   │               └── CognitoService.java    # AWS Cognito integration service
│   │   ├── resources/
│   │   │   ├── static/         # Static assets (if any)
│   │   │   ├── templates/      # Template files (if using any templating engine)
│   │   │   └── application.properties # Main configuration file
│   ├── test/                   # Unit and integration tests
│   │   └── java/
│   │       └── com/
│   │           └── infovault/
│   │               └── InfoVaultApplicationTests.java # Test class for the main application
│
├── .gitignore                  # Git ignore file
├── HELP.md                     # Spring Boot help file
├── mvnw                        # Maven wrapper script for Unix
├── mvnw.cmd                    # Maven wrapper script for Windows
├── pom.xml                     # Maven configuration file
├── dependencies.txt            # Declares dependencies
└── README.md                   # Project documentation (this file)

## Prerequisites for Getting Started
Java 17 (or compatible version)
Maven 3.8.x
A Git client for version control

