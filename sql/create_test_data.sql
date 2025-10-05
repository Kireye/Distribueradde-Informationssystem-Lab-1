USE online_store;

INSERT INTO User (user_role, name, email, password_hash)
VALUES
    ('admin', 'Hel', 'hel@helheim.se', '7766BDB93180F4C2DD13B54EDC835A3A355D764FFB3DD2EBC1447A9BDE99D03C'),             -- password: garm,      id: 1
    ('staff', 'Balder', 'balder@helheim.se', 'D2645F062945F7B268F27D54F30EBD35005EFC4FA71CD56A73D26F40ECBF01B8'),       -- password: mistel,    id: 2
    ('customer', 'Hermodr', 'hermodr@asgard.se', 'CD12D578CABA798A5AFCFA99A7E38CD31053E65FA4668462D37E70D8ED431566');   -- password: balder,    id: 3

INSERT INTO Item_category (name)
VALUES
    ('Electronics'),
    ('Home & Kitchen'),
    ('Video Games & Consoles'),
    ('Books'),
    ('Toys & Games'),
    ('Sport & Outdoor'),
    ('Tools');

INSERT INTO Item (name, description, price, stock)
VALUES
  -- Electronics
    ('RTX 5090', 'Very expensive GPU', 29999.99, 0),                      -- id: 1
    ('9800X3D', '8-Core CPU', 7000.00, 24),                               -- id: 2
    ('PlayStation 5 Slim', 'Latest-gen console with ultra-fast SSD', 6990.00, 12),   -- id: 3
    ('Nintendo Switch OLED', 'Handheld/TV hybrid with vibrant OLED screen', 4490.00, 20), -- id: 4
    ('Samsung 55\" 4K TV', 'Crystal UHD 4K Smart TV, 55-inch', 6490.00, 15),          -- id: 5

    -- Home & Kitchen
    ('Frying pan', 'Pan of frying', 129.99, 50),                          -- id: 6
    ('Chef''s Knife', 'Stainless steel kitchen knife', 349.00, 80),       -- id: 7
    ('Espresso Machine', '13-bar pump, milk frother, stainless steel', 1890.00, 9), -- id: 8
    ('Air Fryer XL', 'Large basket, easy-clean, low-oil frying', 1290.00, 25),      -- id: 9
    ('Cutting Board Bamboo', 'Durable bamboo cutting board', 199.00, 60), -- id: 10

    -- Video Games & Consoles
    ('DualSense Wireless Controller', 'PS5 controller with haptic feedback', 849.00, 30), -- id: 11
    ('The Legend of Zelda: TotK', 'Open-world adventure game (Switch)', 699.00, 22),      -- id: 12

    -- Books
    ('Clean Code', 'A Handbook of Agile Software Craftsmanship', 499.00, 14),             -- id: 13
    ('The Pragmatic Programmer', 'Your Journey to Mastery (20th Anniversary)', 529.00, 12), -- id: 14

    -- Toys & Games
    ('LEGO Starter Set', 'Creative brick set for kids', 299.00, 40),          -- id: 15
    ('UNO Card Game', 'Classic family card game', 129.00, 70),                -- id: 16

    -- Sport & Outdoor
    ('Tent', 'Wind resistant tent', 2599.99, 12),                              -- id: 17
    ('Sleeping bag', 'Warm sleeping bag', 1399.99, 18),                        -- id: 18
    ('Trail Running Shoes', 'Lightweight trail runners, neutral', 1299.00, 18), -- id: 19
    ('Hiking Backpack 30L', 'Comfortable 30L daypack', 899.00, 3),             -- id: 20

    -- Tools
    ('Cordless Drill 18V', 'Brushless drill with 2 batteries', 1699.00, 10),    -- id: 21
    ('Precision Screwdriver Set', '25-in-1 magnetic toolkit', 249.00, 80);      -- id: 22

INSERT INTO Item_category_mapping (item_id, item_category_id)
VALUES
    -- Electronics (cat 1)
    (1, 1),  -- RTX 5090
    (2, 1),  -- 9800X3D
    (3, 1),  -- PlayStation 5 Slim
    (4, 1),  -- Nintendo Switch OLED
    (5, 1),  -- Samsung 55" 4K TV

    -- Home & Kitchen (cat 2)
    (6, 2),  -- Frying pan
    (7, 2),  -- Chef's Knife
    (8, 2),  -- Espresso Machine
    (9, 2),  -- Air Fryer XL
    (10, 2), -- Cutting Board Bamboo

    -- Video Games & Consoles (cat 3)
    (11, 3), -- DualSense
    (12, 3), -- Zelda TotK

    -- Books (cat 4)
    (13, 4), -- Clean Code
    (14, 4), -- Pragmatic Programmer

    -- Toys & Games (cat 5)
    (15, 5), -- LEGO Starter Set
    (16, 5), -- UNO

    -- Sport & Outdoor (cat 6)
    (17, 6), -- Tent
    (18, 6), -- Sleeping bag
    (19, 6), -- Trail Running Shoes
    (20, 6), -- Hiking Backpack 30L

    -- Tools (cat 7)
    (21, 7), -- Cordless Drill 18V
    (22, 7), -- Precision Screwdriver Set

    -- Extra: Frying pan passar även för utomhusbruk (dubbel-kategori)
    (6, 6);  -- Frying pan -> Sport & Outdoor

INSERT INTO Shopping_cart (user_id, item_id, quantity)
VALUES
    (3, 4, 5),
    (3, 3, 1);

INSERT INTO Customer_order (user_id, status, order_date)
VALUES (3, 'ordered', '2025-10-04 12:00:00'); -- id: 1

INSERT INTO Customer_order (user_id, status)
VALUES (3, 'packaged');                                 -- id: 2

INSERT INTO Order_item_mapping (order_id, item_id)
VALUES
    (1, 1), -- 5090         -> Order 1
    (1, 2), -- 9800X3D      -> Order 1
    (2, 5), -- Tent         -> Order 2
    (2, 6); -- Sleeping bag -> Order 2
