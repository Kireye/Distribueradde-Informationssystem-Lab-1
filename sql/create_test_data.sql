USE online_store;

INSERT INTO User (user_role, name, email, password_hash)
VALUES
    ('admin', 'Hel', 'hel@helheim.se', '7766BDB93180F4C2DD13B54EDC835A3A355D764FFB3DD2EBC1447A9BDE99D03C'),             -- password: garm,      id: 1
    ('staff', 'Balder', 'balder@helheim.se', 'D2645F062945F7B268F27D54F30EBD35005EFC4FA71CD56A73D26F40ECBF01B8'),       -- password: mistel,    id: 2
    ('customer', 'Hermodr', 'hermodr@asgard.se', 'CD12D578CABA798A5AFCFA99A7E38CD31053E65FA4668462D37E70D8ED431566');   -- password: balder,    id: 3

INSERT INTO Item_category (name)
VALUES
    ('Electronics'),
    ('Food'),
    ('Outdoors');

INSERT INTO Item (name, description, price, stock)
VALUES
    -- Electronics
    ('RTX 5090', 'Very expensive GPU', 29999.99, 0),    -- id: 1
    ('9800X3D', '8-Core CPU', 7000.00,24),              -- id: 2
    -- Food
    ('Frying pan', 'Pan of frying', 129.99, 50),        -- id: 3
    ('Cucumber', 'Water stick', 12.90, 150),            -- id: 4
    -- Outdoors
    ('Tent', 'Wind resistant tent', 2599.99, 12),       -- id: 5
    ('Sleeping bag', 'Warm sleeping bag', 1399.99, 18); -- id: 6

INSERT INTO Item_category_mapping (item_id, item_category_id)
VALUES
    (1, 1), -- 5090         ->  Electronics
    (2, 1), -- 9800X3D      ->  Electronics
    (3, 2), -- Frying pan   ->  Food
    (3, 3), -- Frying pan   ->  Outdoors
    (4, 2), -- Cucumber     ->  Food
    (5, 3), -- Tent         ->  Outdoors
    (6, 3); -- Sleeping bag ->  Outdoors

INSERT INTO Shopping_cart (user_id, item_id, quantity)
VALUES
    (3, 4, 5),
    (3, 3, 1);

INSERT INTO Customer_order (user_id, status, order_date)
VALUES (3, 'ordered', '2025-10-04 12:00:00'); -- id: 1

INSERT INTO Customer_order (user_id, status)
VALUES (3, 'packaged');                                 -- id: 2

INSERT INTO Order_item_mapping (order_id, item_id, quantity)
VALUES
    (1, 1, 1), -- 5090         -> Order 1   x 1
    (1, 2, 2), -- 9800X3D      -> Order 1   x 2
    (2, 5, 3), -- Tent         -> Order 2   x 3
    (2, 6, 4); -- Sleeping bag -> Order 2   x 4