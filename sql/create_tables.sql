DROP DATABASE IF EXISTS online_store;
CREATE DATABASE online_store;
USE online_store;

CREATE TABLE User (
                        user_id INT PRIMARY KEY AUTO_INCREMENT,
                        user_role ENUM('admin', 'staff', 'customer') NOT NULL,
                        name VARCHAR(64) NOT NULL,
                        email VARCHAR(254) NOT NULL UNIQUE,
                        password_hash VARCHAR(64) NOT NULL
);

CREATE TABLE Item (
                        item_id INT PRIMARY KEY AUTO_INCREMENT,
                        name VARCHAR(128) NOT NULL,
                        description VARCHAR(1024) NOT NULL,
                        price DECIMAL(9, 2) NOT NULL CHECK ( price >= 0 ),
                        stock INT NOT NULL CHECK ( stock >= 0 )
);

CREATE TABLE Item_category (
                        item_category_id INT PRIMARY KEY AUTO_INCREMENT,
                        name VARCHAR(32) NOT NULL
);

CREATE TABLE Item_category_mapping (
                        item_id INT,
                        FOREIGN KEY (item_id) REFERENCES Item(item_id) ON DELETE CASCADE,
                        item_category_id INT,
                        FOREIGN KEY (item_category_id) REFERENCES Item_category(item_category_id) ON DELETE CASCADE,
                        PRIMARY KEY (item_id, item_category_id)
);

CREATE TABLE Shopping_cart (
                        user_id INT,
                        FOREIGN KEY (user_id) REFERENCES User(user_id) ON DELETE CASCADE,
                        item_id INT,
                        FOREIGN KEY (item_id) REFERENCES Item(item_id) ON DELETE CASCADE,
                        quantity INT NOT NULL CHECK ( quantity > 0 ),
                        PRIMARY KEY (user_id, item_id)
);

CREATE TABLE Customer_order (
                        order_id INT PRIMARY KEY AUTO_INCREMENT,
                        user_id INT NOT NULL,
                        FOREIGN KEY (user_id) REFERENCES User(user_id) ON DELETE CASCADE,
                        status ENUM('ordered', 'packaged', 'shipped', 'delivered') NOT NULL,
                        order_date TIMESTAMP DEFAULT NOW()
);

CREATE TABLE Order_item_mapping (
                        order_id INT,
                        FOREIGN KEY (order_id) REFERENCES Customer_order(order_id) ON DELETE CASCADE,
                        item_id INT,
                        FOREIGN KEY (item_id) REFERENCES Item(item_id) ON DELETE CASCADE,
                        quantity INT NOT NULL CHECK ( quantity > 0 ),
                        PRIMARY KEY (order_id, item_id)
);
