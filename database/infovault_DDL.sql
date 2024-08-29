-- PostgreSQL
-- Name: Manny Ach
-- Date: 08.01.24
-- Project: InfoVault
-- https://www.geeksforgeeks.org/difference-between-ddl-and-dml-in-dbms/
-- Data Definition Language (DDL)

-- -----------------------------------------------------
-- Entity: Users
-- -----------------------------------------------------
CREATE TABLE users (
    -- Purpose: Store relevant user info for authentication and profile management.
    user_id SERIAL PRIMARY KEY,
    first_name VARCHAR(255) NOT NULL,
    last_name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    phone_number VARCHAR(20) NOT NULL CHECK (phone_number ~ '^[0-9\+\-]+$'), -- Allow only digits, +, and -
    password_hash VARCHAR(255) NOT NULL,
    created_at TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP,
    last_login TIMESTAMPTZ
);

-- -----------------------------------------------------
-- Entity: Document Types
-- -----------------------------------------------------
CREATE TABLE document_types (
    -- Purpose: Defines type of documents.
    document_type_id SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL
);

-- -----------------------------------------------------
-- Entity: Categories
-- -----------------------------------------------------
CREATE TABLE categories (
    -- Purpose: Used to allow users to categorize their documents.
    category_id SERIAL PRIMARY KEY,
    name VARCHAR(100)
);

-- -----------------------------------------------------
-- Entity: Documents
-- -----------------------------------------------------
CREATE TABLE documents (
    -- Purpose: Store metadata about user documents.
    doc_id SERIAL PRIMARY KEY,
    doc_name VARCHAR(100) NOT NULL,
    file_path TEXT NOT NULL,
    upload_date TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP,
    expiration_date DATE,
    tags TEXT[], -- Array of tags for categorization and search
    user_id INTEGER REFERENCES users(user_id) ON DELETE CASCADE,
    document_type_id INTEGER REFERENCES document_types(document_type_id) ON DELETE CASCADE
);

-- -----------------------------------------------------
-- Entity: Document Categories
-- -----------------------------------------------------
CREATE TABLE document_categories (
    -- Purpose: Creates a link between categories for better organization.
    doc_id INTEGER REFERENCES documents(doc_id) ON DELETE CASCADE,
    category_id INTEGER REFERENCES categories(category_id) ON DELETE CASCADE,
    PRIMARY KEY (doc_id, category_id)
);

-- -----------------------------------------------------
-- Entity: Access Control
-- -----------------------------------------------------
CREATE TABLE access_controls (
    -- Purpose: Manages access permissions for documents, allows sharing between users.
    access_control_id SERIAL PRIMARY KEY,
    permission_level VARCHAR(50) NOT NULL,
    doc_id INTEGER REFERENCES documents(doc_id) ON DELETE CASCADE,
    user_id INTEGER REFERENCES users(user_id) ON DELETE CASCADE,
    expires_at TIMESTAMPTZ, -- Optional expiration time
    max_view_duration INTERVAL -- Optional view duration limit
);

-- -----------------------------------------------------
-- Entity: Activity Log
-- -----------------------------------------------------
CREATE TABLE activity_logs (
    -- Purpose: Tracks user actions for auditing and monitoring.
    log_id SERIAL PRIMARY KEY,
    action TEXT NOT NULL,
    timestamp TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP,
    user_id INTEGER REFERENCES users(user_id) ON DELETE SET NULL,
    doc_id INTEGER REFERENCES documents(doc_id) ON DELETE SET NULL
);

-- -----------------------------------------------------
-- Entity: Notifications
-- -----------------------------------------------------
CREATE TABLE notifications (
    -- Purpose: Send notifications to users, such as document expiration updates.
    notification_id SERIAL PRIMARY KEY,
    message TEXT NOT NULL,
    status VARCHAR(50) NOT NULL,
    sent_at TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP,
    user_id INTEGER REFERENCES users(user_id) ON DELETE CASCADE
);

-- -----------------------------------------------------
-- Entity Relationships
-- -----------------------------------------------------

-- users to documents – 1 to M Relationship:
-- A user can have multiple documents, but each document is associated with only one user.

-- documents to document_types – 1 to M Relationship:
-- A document type can have many documents, but each document has only one type.

-- documents to categories: M to M Relationship:
-- A document can belong to multiple categories, and a category can include multiple documents.

-- users to activity_log: 1 to M Relationship:
-- A user can have multiple activity logs to track their actions, but each activity log can only have one user.

-- documents to activity_log: 1 to M Relationship:
-- A document can have many activity logs, but each activity log can have only one document.

-- users to access_control: 1 to M Relationship:
-- A user can have multiple access controls, but each access control can only have one user.

-- documents to access_control: 1 to M Relationship:
-- A document can have multiple access controls, but each access control can only have one document.

-- users to notifications: 1 to M Relationship:
-- A user can have multiple notifications, but each notification can only have one user.
