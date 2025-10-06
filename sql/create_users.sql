USE online_store;

DROP USER IF EXISTS 'admin'@'%';
DROP USER IF EXISTS 'staff'@'%';
DROP USER IF EXISTS 'customer'@'%';
DROP USER IF EXISTS 'guest'@'%';

CREATE USER 'admin'@'%'     IDENTIFIED BY 'admin';
CREATE USER 'staff'@'%'     IDENTIFIED BY 'staff';
CREATE USER 'customer'@'%'  IDENTIFIED BY 'customer';
CREATE USER 'guest'@'%'     IDENTIFIED BY 'guest';

-- ADMIN
GRANT SELECT, INSERT, UPDATE, DELETE, CREATE, DROP
ON online_store.*
TO 'admin'@'%';

-- STAFF
GRANT UPDATE
ON online_store.Customer_order
TO 'staff'@'%';

GRANT INSERT, UPDATE, DELETE
ON online_store.Item
TO 'staff'@'%';

GRANT INSERT, UPDATE, DELETE
ON online_store.Item_category_mapping
TO 'staff'@'%';

GRANT SELECT
ON online_store.*
TO 'staff'@'%';

-- CUSTOMER
GRANT UPDATE
ON online_store.User
TO 'customer'@'%';

GRANT UPDATE, INSERT, DELETE
ON online_store.Shopping_cart
TO 'customer'@'%';

GRANT INSERT
ON online_store.Customer_order
TO 'customer'@'%';

GRANT INSERT
ON online_store.Order_item_mapping
TO 'customer'@'%';

GRANT SELECT
ON online_store.*
TO 'staff'@'%';


-- GUEST
GRANT INSERT
ON online_store.User
TO 'guest'@'%';

GRANT SELECT
ON online_store.*
TO 'guest'@'%';

FLUSH PRIVILEGES;
