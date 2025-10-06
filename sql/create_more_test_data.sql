USE online_store;

INSERT INTO Item_category (name)
VALUES
    ('Home & Kitchen'),
    ('Video Games & Consoles'),
    ('Books'),
    ('Toys & Games'),
    ('Sport & Outdoor'),
    ('Tools');

INSERT INTO Item (name, description, price, stock)
VALUES
    -- Electronics
    ('PlayStation 5 Slim', 'Latest-gen console with ultra-fast SSD', 6990.00, 12),   -- id: 7
    ('Nintendo Switch OLED', 'Handheld/TV hybrid with vibrant OLED screen', 4490.00, 20), -- id: 8
    ('Samsung 55\" 4K TV', 'Crystal UHD 4K Smart TV, 55-inch', 6490.00, 15),          -- id: 9

    -- Food
    ('Chef''s Knife', 'Stainless steel kitchen knife', 349.00, 80),       -- id: 10
    ('Espresso Machine', '13-bar pump, milk frother, stainless steel', 1890.00, 9), -- id: 11
    ('Air Fryer XL', 'Large basket, easy-clean, low-oil frying', 1290.00, 25),      -- id: 12
    ('Cutting Board Bamboo', 'Durable bamboo cutting board', 199.00, 60), -- id: 13

    -- Video Games & Consoles
    ('DualSense Wireless Controller', 'PS5 controller with haptic feedback', 849.00, 30), -- id: 14
    ('The Legend of Zelda: TotK', 'Open-world adventure game (Switch)', 699.00, 22),      -- id: 15

    -- Books
    ('Clean Code', 'A Handbook of Agile Software Craftsmanship', 499.00, 14),             -- id: 16
    ('The Pragmatic Programmer', 'Your Journey to Mastery (20th Anniversary)', 529.00, 12), -- id: 17

    -- Toys & Games
    ('LEGO Starter Set', 'Creative brick set for kids', 299.00, 40),          -- id: 18
    ('UNO Card Game', 'Classic family card game', 129.00, 70),                -- id: 19

    -- Outdoors
    ('Trail Running Shoes', 'Lightweight trail runners, neutral', 1299.00, 18), -- id: 20
    ('Hiking Backpack 30L', 'Comfortable 30L daypack', 899.00, 3),             -- id: 21

    -- Tools
    ('Cordless Drill 18V', 'Brushless drill with 2 batteries', 1699.00, 10),    -- id: 22
    ('Precision Screwdriver Set', '25-in-1 magnetic toolkit', 249.00, 80);      -- id: 23

INSERT INTO Item_category_mapping (item_id, item_category_id)
VALUES
    -- Electronics (cat 1)
    (7, 1),  -- PlayStation 5 Slim
    (8, 1),  -- Nintendo Switch OLED
    (9, 1),  -- Samsung 55" 4K TV

    -- Food (cat 2)
    (10, 2),  -- Chef's Knife
    (11, 2),  -- Espresso Machine
    (12, 2),  -- Air Fryer XL
    (13, 2),  -- Cutting Board Bamboo

    -- Video Games & Consoles (cat 3)
    (14, 5), -- DualSense
    (15, 5), -- Zelda TotK

    -- Books (cat 4)
    (16, 6), -- Clean Code
    (17, 6), -- Pragmatic Programmer

    -- Toys & Games (cat 5)
    (18, 7), -- LEGO Starter Set
    (19, 7), -- UNO

    -- Outdoors (cat 6)
    (20, 3), -- Trail Running Shoes
    (21, 3), -- Hiking Backpack 30L

    -- Tools (cat 7)
    (22, 9), -- Cordless Drill 18V
    (23, 9); -- Precision Screwdriver Set