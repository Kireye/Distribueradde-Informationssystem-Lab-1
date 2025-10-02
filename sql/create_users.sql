USE online_store;

DROP USER IF EXISTS 'admin'@'%';
DROP USER IF EXISTS 'staff'@'%';
DROP USER IF EXISTS 'customer'@'%';
DROP USER IF EXISTS 'guest'@'%';

CREATE USER 'admin'@'%' IDENTIFIED BY 'admin';
CREATE USER 'staff'@'%' IDENTIFIED BY 'staff';
CREATE USER 'customer'@'%' IDENTIFIED BY 'customer';
CREATE USER 'guest'@'%' IDENTIFIED BY 'guest';

-- ADMIN
GRANT SELECT, INSERT, UPDATE, DELETE, CREATE, DROP
ON online_store.*
TO 'admin'@'%';

-- STAFF
GRANT SELECT, UPDATE
ON online_store.Customer_order
TO 'staff'@'%';

GRANT SELECT, INSERT, UPDATE, DELETE
ON online_store.Item
TO 'staff'@'%';

GRANT SELECT, INSERT, UPDATE, DELETE
ON online_store.Item_category_mapping
TO 'staff'@'%';

-- CUSTOMER
GRANT SELECT, UPDATE
ON online_store.User
TO 'customer'@'%';

GRANT SELECT, UPDATE, INSERT, DELETE
ON online_store.Shopping_cart
TO 'customer'@'%';

GRANT SELECT
ON online_store.Item
TO 'customer'@'%';

GRANT SELECT
ON online_store.Item_category
TO 'customer'@'%';

GRANT SELECT
ON online_store.Item_category_mapping
TO 'customer'@'%';

GRANT SELECT, INSERT
ON online_store.Customer_order
TO 'customer'@'%';

GRANT SELECT, INSERT
ON online_store.Order_item_mapping
TO 'customer'@'%';

-- GUEST
GRANT SELECT, INSERT
ON online_store.User
TO 'guest'@'%';

GRANT SELECT
ON online_store.Item
TO 'guest'@'%';

GRANT SELECT
ON online_store.Item_category
TO 'guest'@'%';

GRANT SELECT
ON online_store.Item_category_mapping
TO 'guest'@'%';

FLUSH PRIVILEGES;
