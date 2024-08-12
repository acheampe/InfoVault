-- PostgreSQL
-- Name: Manny Ach
-- Date: 08.02.24
-- Project: InfoVault
-- https://www.geeksforgeeks.org/difference-between-ddl-and-dml-in-dbms/
-- Data Manipulation Language -- DML

-- Insert sample data into Users table
INSERT INTO users (first_name, last_name, email, phone_number, password_hash)
VALUES 
    ('John', 'Doe', 'john.doe@example.com', '+1234567890', 'hashed_password1'),
    ('Jane', 'Smith', 'jane.smith@example.com', '+0987654321', 'hashed_password2'),
    ('Alice', 'Johnson', 'alice.johnson@example.com', '+1122334455', 'hashed_password3');


-- Insert sample data into Documents table
INSERT INTO documents (doc_name, file_path, upload_date, expiration_date, tags, user_id, document_type_id)
VALUES 
    ('Passport', '/files/passport.pdf', '2024-01-01 10:00:00', '2030-01-01', '{"identity", "government"}', 1, 1),
    ('Driver License', '/files/driver_license.pdf', '2024-01-02 11:00:00', '2026-01-01', '{"license", "identity"}', 2, 2),
    ('Utility Bill', '/files/utility_bill.pdf', '2024-01-03 12:00:00', NULL, '{"bill", "utility"}', 3, 3);
    
-- Insert sample data into Document Types table
INSERT INTO document_types (name)
VALUES 
    ('Government ID'),
    ('Utility Bill'),
    ('Medical Record');

-- Insert sample data into Categories table
INSERT INTO categories (name)
VALUES 
    ('Identity Documents'),
    ('Financial Documents'),
    ('Personal Documents');

-- Insert sample data into DocumentCategories table
INSERT INTO document_categories (doc_id, category_id)
VALUES 
    (1, 1), -- Passport in Identity Documents
    (2, 1), -- Driver License in Identity Documents
    (3, 2); -- Utility Bill in Financial Documents

-- Insert sample data into AccessControl table
INSERT INTO access_controls (permission_level, document_id, user_id, expires_at, max_view_duration)
VALUES 
    ('read', 1, 2, '2025-01-01 00:00:00', '1 day'),
    ('edit', 2, 1, NULL, '2 hours');

-- Insert sample data into ActivityLog table
INSERT INTO activity_logs (action, timestamp, user_id, document_id)
VALUES 
    ('Document Viewed', '2024-08-01 08:00:00', 1, 1),
    ('Document Edited', '2024-08-01 09:00:00', 2, 2);

-- Insert sample data into Notifications table
INSERT INTO notifications (message, status, sent_at, user_id)
VALUES 
    ('Your passport document is about to expire.', 'sent', '2024-07-31 12:00:00', 1),
    ('New login detected.', 'unread', '2024-08-01 13:00:00', 2);

-- -----------------------------------------------------
-- UPDATING DATA
-- -----------------------------------------------------
-- Update a user's email
UPDATE users
SET email = 'john.doe.new@example.com'
WHERE user_id = 1;

-- Delete a document by ID
DELETE FROM documents
WHERE doc_id = 3;

-- -----------------------------------------------------
-- Querying Data
-- -----------------------------------------------------
-- Select all users
SELECT * FROM users;

-- Select documents for a specific user
SELECT * FROM documents
WHERE user_id = 1;

-- Select all access control entries for a specific document
SELECT * FROM access_control
WHERE document_id = 1;