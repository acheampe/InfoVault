# Document Management Service

## Overview
The `doc_mgmt_service` is a microservice responsible for handling document management tasks within the InfoVault application. It is built using Python and the Flask framework.

## Features
- Document Upload
- Document Retrieval
- Document Update
- Document Deletion
- Tagging and Categorization
- Document Expiry Notifications

## Project Structure
```plaintext
doc_mgmt_service/
│
├── src/
│   ├── main/
│   │   ├── app.py              # Flask application entry point
│   │   ├── config.py           # Configuration settings
│   │   ├── models.py           # Data models (e.g., SQLAlchemy models)
│   │   ├── routes.py           # API route definitions
│   │   ├── services/           # Business logic and service layer
│   │   ├── static/             # Static assets (e.g., CSS, JS, images)
│   │   └── templates/          # HTML templates (if any)
│   ├── test/                   # Unit and integration tests
│
├── .gitignore                  # Git ignore file
├── requirements.txt            # Python dependencies
└── README.md                   # Project documentation (this file)

## Prerequisites for Getting Started
- Python 3.10 (or compatible version)
- Flask 2.x
- A Git client for version control
