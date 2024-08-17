## Access Control Service

## Overview
The access_control_service is a microservice within the InfoVault application responsible 
for managing access control to various resources. This service ensures that users have 
the correct permissions to perform actions on specific documents or resources.

## Features
Grant access to resources based on user roles and permissions.
Revoke access to resources.
Manage access expiration.
Provide role-based access control (RBAC) features.

## Project Structure thus far
access_control_service/
│
├── src/
│   ├── index.ts              # Main entry point of the service
│
├── node_modules/             # Node.js dependencies
├── .env                      # Environment variables file
├── .gitignore                # Git ignore file
├── dependencies.txt          # List of installed dependencies
├── package-lock.json         # Exact versions of dependencies
├── package.json              # Project metadata and dependencies
└── tsconfig.json             # TypeScript configuration file

## Prerequisites for Getting Started
Node.js (version 14 or higher recommended)
TypeScript (installed globally)
npm (Node Package Manager)


