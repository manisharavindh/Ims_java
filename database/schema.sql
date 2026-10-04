CREATE DATABASE IF NOT EXISTS lms_db;
USE lms_db;

CREATE TABLE IF NOT EXISTS users (
    user_id INT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL
);

CREATE TABLE IF NOT EXISTS books (
    book_id INT PRIMARY KEY AUTO_INCREMENT,
    title VARCHAR(150) NOT NULL,
    author VARCHAR(100) NOT NULL,
    category VARCHAR(100),
    isbn VARCHAR(30) NOT NULL UNIQUE,
    total_copies INT NOT NULL,
    available_copies INT NOT NULL
);

CREATE TABLE IF NOT EXISTS members (
    member_id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(150) NOT NULL UNIQUE,
    phone VARCHAR(20),
    department VARCHAR(100)
);

CREATE TABLE IF NOT EXISTS transactions (
    transaction_id INT PRIMARY KEY AUTO_INCREMENT,
    book_id INT NOT NULL,
    member_id INT NOT NULL,
    issue_date DATE NOT NULL,
    due_date DATE NOT NULL,
    return_date DATE,
    status VARCHAR(20) NOT NULL,
    FOREIGN KEY (book_id) REFERENCES books(book_id),
    FOREIGN KEY (member_id) REFERENCES members(member_id)
);

-- Insert Sample Data
INSERT IGNORE INTO users (username, password) VALUES ('admin', 'admin123');

INSERT IGNORE INTO books (title, author, category, isbn, total_copies, available_copies) VALUES
('Java Programming', 'James Gosling', 'Computer Science', '978-0-13-468599-1', 5, 5),
('Effective Java', 'Joshua Bloch', 'Computer Science', '978-0-13-468609-7', 3, 3),
('Clean Code', 'Robert C. Martin', 'Software Engineering', '978-0-13-235088-4', 4, 3);

INSERT IGNORE INTO members (name, email, phone, department) VALUES
('John Doe', 'john.doe@example.com', '1234567890', 'Computer Science'),
('Jane Smith', 'jane.smith@example.com', '9876543210', 'Information Technology');

-- A sample transaction (Book 3 issued to Member 1)
INSERT IGNORE INTO transactions (book_id, member_id, issue_date, due_date, status) VALUES
(3, 1, CURDATE(), DATE_ADD(CURDATE(), INTERVAL 14 DAY), 'ISSUED');
