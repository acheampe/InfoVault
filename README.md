# InfoVault

## Overview
**InfoVault** is a secure, scalable, and modular application designed to manage sensitive documents and information. It features a microservices architecture, leveraging various programming languages and frameworks to deliver an efficient, flexible, and robust solution. The project utilizes serverless components where possible to minimize overhead and ensure scalability.

## Features
- **Authentication Service**: Handles user authentication, registration, and role-based access control.
- **Access Control Service**: Manages permissions, ensuring secure document access.
- **Document Management Service**: Provides CRUD operations for managing and storing documents securely.
- **Search Service**: Facilitates quick and efficient document retrieval using advanced search functionalities.

## Microservices Overview
### 1. **Authentication Service**
   - **Framework**: Java with Spring Boot
   - **Role**: Handles user registration, login, and JWT-based authentication.
   - **Repository**: `/backend/auth_service`

### 2. **Access Control Service**
   - **Framework**: Node.js with TypeScript and Express
   - **Role**: Manages permissions and access controls for documents.
   - **Repository**: `/backend/access_control_service`

### 3. **Document Management Service**
   - **Framework**: Python with Flask
   - **Role**: Provides endpoints for uploading, retrieving, updating, and deleting documents.
   - **Repository**: `/backend/doc_mgmt_service`

### 4. **Search Service**
   - **Framework**: Go with Gin
   - **Role**: Enables fast document searches with efficient indexing and querying.
   - **Repository**: `/backend/search_service`

## Project Structure
```plaintext
InfoVault/
│
├── backend/
│   ├── auth_service/             # Java-based authentication microservice
│   ├── access_control_service/   # Node.js-based access control microservice
│   ├── doc_mgmt_service/         # Python-based document management microservice
│   └── search_service/           # Go-based search microservice
│
├── database/                     # Database scripts and migrations
│   ├── infovault_DDL.sql         # Data Definition Language (DDL) scripts
│   └── infovault_DML.sql         # Data Manipulation Language (DML) scripts
│
├── aws/                          # AWS configurations and infrastructure code
│
├── api_test/                     # API test cases and scripts
│   ├── infovault_api_endpoints.json # API endpoint definitions for testing
│   └── main.py                   # API test script
│
└── README.md                     # General project documentation (this file)

## License
Private Use License

Version 1.0

2024, Emmanuel Acheampong

## Terms and Conditions

## Private Use Only:
The software may be used, copied, modified, and distributed solely for personal, educational, or non-commercial purposes. Commercial use of this software is strictly prohibited.

## No Warranty:
This software is provided "as is," without warranty of any kind, express or implied, including but not limited to the warranties of merchantability, fitness for a particular purpose, and noninfringement. In no event shall the authors or copyright holders be liable for any claim, damages, or other liability, whether in an action of contract, tort, or otherwise, arising from, out of, or in connection with the software or the use or other dealings in the software.

## No Redistribution:
Redistribution of this software, with or without modification, is not permitted unless explicitly authorized by the copyright holder.

## Modification
You may modify the software for your personal use but may not distribute the modified software.
