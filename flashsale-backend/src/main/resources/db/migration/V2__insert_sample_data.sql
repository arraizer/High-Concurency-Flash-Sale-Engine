-- Sample Users
INSERT INTO users (username, email, password_hash, role) VALUES 
('john_doe', 'john@example.com', '$2a$10$e8.10uY1...samplehash', 'CUSTOMER'),
('admin_user', 'admin@example.com', '$2a$10$e8.10uY1...samplehash', 'ADMIN');

-- Sample Products
INSERT INTO products (name, description, price) VALUES 
('Vé Concert Anh Trai Vượt Ngàn Chông Gai', 'Vé VIP tham dự đêm nhạc hoành tráng', 1500000.00),
('Tai nghe Bluetooth Sony WH-1000XM5', 'Chống ồn chủ động đỉnh cao', 8490000.00);

-- Sample Flash Sale Event
INSERT INTO flash_sale_events (name, start_time, end_time, status) VALUES 
('Săn Vé Concert 0Đ & Siêu Sale Công Nghệ', NOW(), NOW() + INTERVAL '2 hours', 'ACTIVE');

-- Sample Flash Sale Items
INSERT INTO flash_sale_items (event_id, product_id, flash_price, stock, sold_count, status) VALUES 
(1, 1, 990000.00, 100, 0, 'IN_STOCK'),
(1, 2, 4990000.00, 10, 0, 'IN_STOCK');