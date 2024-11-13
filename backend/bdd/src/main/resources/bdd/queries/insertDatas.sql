-- Insert MenuItems for "Le Petit Nice"
-- Assuming menuId for "Le Petit Nice" is 1
INSERT INTO MenuItem (menuId, name, prepTime, price) VALUES (1, 'Salade Nicoise', 60, 12.50);
INSERT INTO MenuItem (menuId, name, prepTime, price) VALUES (1, 'Bouillabaisse', 500, 25.00);
INSERT INTO MenuItem (menuId, name, prepTime, price) VALUES (1, 'Tarte Tatin', 1800, 8.00);

-- Insert Menu for "Le Petit Nice"
INSERT INTO Menu (restaurantId) VALUES ('Le Petit Nice');

-- Insert MenuItems for "Le Petit Jardin"
-- Assuming menuId for "Le Petit Jardin" is 2
INSERT INTO MenuItem (menuId, name, prepTime, price) VALUES (2, 'Salade de chèvre chaud', 400, 10.00);
INSERT INTO MenuItem (menuId, name, prepTime, price) VALUES (2, 'Magret de canard', 1800, 20.00);
INSERT INTO MenuItem (menuId, name, prepTime, price) VALUES (2, 'Crème brûlée', 600, 7.00);

-- Insert Menu for "Le Petit Jardin"
INSERT INTO Menu (restaurantId) VALUES ('Le Petit Jardin');

-- Insert MenuItems for "Le Petit Chateau"
-- Assuming menuId for "Le Petit Chateau" is 3
INSERT INTO MenuItem (menuId, name, prepTime, price) VALUES (3, 'Escargots', 1800, 15.00);
INSERT INTO MenuItem (menuId, name, prepTime, price) VALUES (3, 'Coq au vin', 1800, 22.00);
INSERT INTO MenuItem (menuId, name, prepTime, price) VALUES (3, 'Mousse au chocolat', 1800, 6.00);

-- Insert Menu for "Le Petit Chateau"
INSERT INTO Menu (restaurantId) VALUES ('Le Petit Chateau');

-- Insert Restaurants
INSERT INTO Restaurant (restaurantId, restaurantName, openingTime, closingTime, orderPriceStrategy, menuId)
VALUES ('Le Petit Nice', 'Le Petit Nice', '2024-10-18T12:00:00', '2024-10-18T14:00:00', 'STANDARD', 1);

INSERT INTO Restaurant (restaurantId, restaurantName, openingTime, closingTime, orderPriceStrategy, menuId)
VALUES ('Le Petit Jardin', 'Le Petit Jardin', '2024-10-18T12:00:00', '2024-10-18T14:00:00', 'STANDARD', 2);

INSERT INTO Restaurant (restaurantId, restaurantName, openingTime, closingTime, orderPriceStrategy, menuId)
VALUES ('Le Petit Chateau', 'Le Petit Chateau', '2024-10-18T12:00:00', '2024-10-18T14:00:00', 'STANDARD', 3);

-- For restaurant "Le Gourmet D'Or" (closed)
INSERT INTO Restaurant (restaurantId, restaurantName, openingTime, closingTime, orderPriceStrategy, menuId)
VALUES ('Le Gourmet D\'Or', 'Le Gourmet D\'Or', NULL, NULL, 'STANDARD', 4);

-- For restaurant "Bistro de la Plage"
INSERT INTO Restaurant (restaurantId, restaurantName, openingTime, closingTime, orderPriceStrategy, menuId)
VALUES ('Bistro de la Plage', 'Bistro de la Plage', '2024-10-18T12:00:00', '2024-10-18T14:00:00', 'STANDARD', 5);

-- For restaurant "Café de l'Aube"
INSERT INTO Restaurant (restaurantId, restaurantName, openingTime, closingTime, orderPriceStrategy, menuId)
VALUES ('Café de l\'Aube', 'Café de l\'Aube', '2024-10-18T12:00:00', '2024-10-18T14:00:00', 'STANDARD', 6);

-- Insert Restaurant Managers
INSERT INTO RestaurantManager (managerId, restaurantId, email, password, name)
VALUES ('manager1@test.com', 'Le Petit Nice', 'manager@test.com', 'password', 'Manager');

INSERT INTO RestaurantManager (managerId, restaurantId, email, password, name)
VALUES ('manager2@test.com', 'Le Petit Jardin', 'manager2@test.com', 'password', 'Manager2');

INSERT INTO RestaurantManager (managerId, restaurantId, email, password, name)
VALUES ('manager3@test.com', 'Le Petit Chateau', 'manager3@test.com', 'password', 'Manager3');

INSERT INTO RestaurantManager (managerId, restaurantId, email, password, name)
VALUES ('managerA@test.com', 'Le Gourmet D"Or', 'managerA@test.com', 'password', 'ManagerA');

INSERT INTO RestaurantManager (managerId, restaurantId, email, password, name)
VALUES ('managerB@test.com', 'Bistro de la Plage', 'managerB@test.com', 'password', 'ManagerB');

INSERT INTO RestaurantManager (managerId, restaurantId, email, password, name)
VALUES ('managerC@test.com', 'Café de l"Aube', 'managerC@test.com', 'mypassword', 'ManagerC');

-- Insert Slots for "Le Petit Nice" (restaurantId = 'Le Petit Nice')
INSERT INTO Slot (currentCapacity, maxCapacity, openingDate, durationTime, numberOfPersonnel)
VALUES (5, 10, '2024-10-18T12:00:00', 'PT1H', 5);

INSERT INTO Slot (currentCapacity, maxCapacity, openingDate, durationTime, numberOfPersonnel)
VALUES (10, 15, '2024-10-18T13:00:00', 'PT1H', 10);

INSERT INTO Slot (currentCapacity, maxCapacity, openingDate, durationTime, numberOfPersonnel)
VALUES (3, 10, '2024-10-18T14:00:00', 'PT1H', 3);

INSERT INTO Slot (currentCapacity, maxCapacity, openingDate, durationTime, numberOfPersonnel)
VALUES (2, 5, '2024-10-18T15:00:00', 'PT1H', 2);

-- Insert Orders
INSERT INTO "Order" (orderUUID, restaurantId, userId, status)
VALUES ('orderUUID1', 'Le Petit Nice', 'user1', 'PENDING');

-- Insert Transactions
INSERT INTO Transaction (amount, paymentMethod, timestamp, orderUUID)
VALUES (25.00, 'CREDIT_CARD', '2024-10-18T12:15:00', 'orderUUID1');
