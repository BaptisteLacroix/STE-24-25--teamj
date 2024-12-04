-- Generate ORDERS and INDIVIDUAL_ORDERS
-- Insert into campus_users
INSERT INTO campus_users (id, name, balance, defaultPaymentMethod)
VALUES
    ('1aeb4480-305a-499d-885c-7d2d9f99153b', 'John Doe', 100.0, 'CREDIT_CARD');

-- Insert into restaurants
INSERT INTO restaurants (id, name)
VALUES
    ('3183fa1c-ecd5-49a9-9351-92f75d33fea4', 'TEST');

-- Insert into delivery_locations
INSERT INTO delivery_locations (id, location_name, address)
VALUES
    (X'10a834131b4a4184b082238d073e6126', 'Campus Main Gate', '123 Campus Street');

-- Insert into delivery_details
INSERT INTO delivery_details (id, location_id, delivery_time)
VALUES
    (X'774fcc38ff404cc88722a20bca38338d', X'10a834131b4a4184b082238d073e6126', '2024-12-31T11:00:00');

-- Insert into menu_items
INSERT INTO menu_items (id, name, category, price)
VALUES
    (X'cdaa1fc4621b4b1889df1fafd39aadd0', 'TEST', 0, 12.50),
    (X'91580f0b83ab4edab92002a4444ddd59', 'TEST', 1, 25.00),
    (X'c36e8bf2d7fe4b3a8e7af66b56286b0f', 'TEST', 2, 8.00);

-- Insert into orders
INSERT INTO orders (id, restaurant_id, user_id, status)
VALUES
    (X'178225f29f084a7fadd23783e89ffa6b', X'3183fa1cecd549a9935192f75d33fea4', X'1aeb4480305a499d885c7d2d9f99153b', 'PENDING');

-- Insert into individual_orders
INSERT INTO orders (id, restaurant_id, user_id, status)
VALUES
    (X'f78ed5e2face43c3a60e7f703bd995c3', X'3183fa1cecd549a9935192f75d33fea4', X'1aeb4480305a499d885c7d2d9f99153b', 'PENDING');

-- Insert into INDIVIDUAL_ORDERS (ensure the correct ID and DELIVERYDETAILS_ID are referenced)
INSERT INTO INDIVIDUAL_ORDERS (ID, DELIVERYDETAILS_ID)
VALUES
    (X'f78ed5e2face43c3a60e7f703bd995c3', X'774fcc38ff404cc88722a20bca38338d');

-- Create an TEST Manager that have the restaurant TEST
INSERT INTO restaurant_managers (id, email, name, restaurant_id)
VALUES
    (X'5926a1d4183148ea9106b28cc16c9da3', 'TEST', 'TEST', X'3183fa1cecd549a9935192f75d33fea4');
