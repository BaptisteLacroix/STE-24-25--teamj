-- Create the Menu that will be added to the resrauratn
INSERT INTO MENUS (ID)
VALUES (X'9c51a083970b4742a8ea02565835f3b0');

-- Insert into menu_items
INSERT INTO menu_items (id, name, PREPTIME, PRICE, MENU_ID, TYPE)
VALUES (X'cdaa1fc4621b4b1889df1fafd39aadd0', 'TEST1', 30, 12.50, X'9c51a083970b4742a8ea02565835f3b0', 'Appetizer'),
       (X'91580f0b83ab4edab92002a4444ddd59', 'TEST2', 180, 25.00, X'9c51a083970b4742a8ea02565835f3b0', 'Main Course'),
       (X'c36e8bf2d7fe4b3a8e7af66b56286b0f', 'TEST3', 1200, 8.00, X'9c51a083970b4742a8ea02565835f3b0', 'Dessert');


-- Insert into restaurants
insert into RESTAURANTS (ID, CLOSINGTIME, NAME, OPENINGTIME, MENUENTITY_ID)
VALUES (X'3183fa1cecd549a9935192f75d33fea4', null, 'TEST', null, X'9c51a083970b4742a8ea02565835f3b0');

-- Insert into delivery_details
INSERT INTO delivery_details (id, location_id, delivery_time)
VALUES (X'774fcc38ff404cc88722a20bca38338d', X'10a834131b4a4184b082238d073e6126', '2024-12-31 11:00:00');

-- Insert into delivery_details
INSERT INTO delivery_details (id, location_id, delivery_time)
VALUES
    (X'a63b529e7aee41dd9c663ed6cd621506', X'09d4e91cd38d49b19ed3297a88b5492c', '2024-12-31 12:00:00');

-- Insert into orders
INSERT INTO orders (id, restaurant_id, user_id, status)
VALUES (X'178225f29f084a7fadd23783e89ffa6b', X'3183fa1cecd549a9935192f75d33fea4', X'1aeb4480305a499d885c7d2d9f99153b',
        'PENDING');

-- Insert into individual_orders
INSERT INTO orders (id, restaurant_id, user_id, status)
VALUES (X'f78ed5e2face43c3a60e7f703bd995c3', X'3183fa1cecd549a9935192f75d33fea4', X'1aeb4480305a499d885c7d2d9f99153b',
        'PENDING');

-- Insert into INDIVIDUAL_ORDERS (ensure the correct ID and DELIVERYDETAILS_ID are referenced)
INSERT INTO INDIVIDUAL_ORDERS (ID, DELIVERYDETAILS_ID)
VALUES (X'f78ed5e2face43c3a60e7f703bd995c3', X'774fcc38ff404cc88722a20bca38338d');

-- Create an TEST Manager that have the restaurant TEST
INSERT INTO restaurant_managers (id, email, name, restaurant_id)
VALUES (X'5926a1d4183148ea9106b28cc16c9da3', 'TEST', 'TEST', X'3183fa1cecd549a9935192f75d33fea4');



-- Insert a test GROUP_ORDERS entry
INSERT INTO GROUP_ORDERS (id, status, deliverydetails_id)
VALUES
    (X'ef254832dba444f88fa488720ee3e0a7', 'OPEN', X'a63b529e7aee41dd9c663ed6cd621506');

-- Insert a test entry in GROUP_ORDERS_ORDERS
INSERT INTO GROUP_ORDERS_ORDERS (GROUPORDERENTITY_ID, ORDERS_ID)
VALUES
    (X'ef254832dba444f88fa488720ee3e0a7', X'178225f29f084a7fadd23783e89ffa6b');

-- Insert a test entry in GROUP_ORDERS_CAMPUS_USERS
INSERT INTO GROUP_ORDERS_CAMPUS_USERS (GROUPORDERENTITY_ID, USERS_ID)
VALUES
    (X'ef254832dba444f88fa488720ee3e0a7', X'1aeb4480305a499d885c7d2d9f99153b');
