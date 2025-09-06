CREATE TABLE IF NOT EXISTS form_data (
                                         id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                         title VARCHAR(100) NOT NULL,
    description TEXT,
    image_path VARCHAR(500),
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    ip_address VARCHAR(50),
    user_agent VARCHAR(500)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;