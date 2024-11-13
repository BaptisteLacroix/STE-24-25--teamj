CREATE TABLE Restaurant
(
    restaurantId       TEXT PRIMARY KEY,
    restaurantName     TEXT NOT NULL,
    openingTime        TEXT,    -- Store as ISO 8601 string (e.g., '2024-11-13T12:00:00')
    closingTime        TEXT,    -- Store as ISO 8601 string
    orderPriceStrategy TEXT,    -- You can store the strategy as a string or reference another table
    menuId             INTEGER, -- Foreign key to the Menu table
    FOREIGN KEY (menuId) REFERENCES Menu (menuId)
);

CREATE TABLE Slot
(
    slotId            INTEGER PRIMARY KEY AUTOINCREMENT,
    currentCapacity   INTEGER NOT NULL,
    maxCapacity       INTEGER NOT NULL,
    openingDate       TEXT    NOT NULL, -- Store as ISO 8601 string (e.g., '2024-11-13T12:00:00')
    durationTime      TEXT    NOT NULL, -- Store as duration string (e.g., 'PT1H' for 1 hour)
    numberOfPersonnel INTEGER NOT NULL
);

CREATE TABLE Menu
(
    menuId       INTEGER PRIMARY KEY AUTOINCREMENT,
    restaurantId TEXT, -- Foreign key to Restaurant table
    FOREIGN KEY (restaurantId) REFERENCES Restaurant (restaurantId)
);

CREATE TABLE MenuItem
(
    menuItemId INTEGER PRIMARY KEY AUTOINCREMENT,
    menuId     INTEGER,          -- Foreign key to Menu table
    name       TEXT    NOT NULL,
    prepTime   INTEGER NOT NULL, -- Preparation time in minutes
    price      REAL    NOT NULL, -- Price of the item
    FOREIGN KEY (menuId) REFERENCES Menu (menuId)
);

CREATE TABLE CampusUser
(
    userId                 TEXT PRIMARY KEY, -- Unique identifier for user (e.g., UUID)
    name                   TEXT NOT NULL,
    balance                REAL NOT NULL,    -- User's balance
    preferredPaymentMethod TEXT,             -- Payment method (e.g., 'CREDIT_CARD', 'PAYPAL')
    defaultPaymentMethod   TEXT,             -- Default payment method (e.g., 'CREDIT_CARD')
    transactions           INTEGER,          -- Foreign key to Transaction table (you can store history separately)
    ordersHistory          INTEGER           -- Foreign key to Order table
);

CREATE TABLE "Order"
(
    orderUUID    TEXT PRIMARY KEY, -- Unique identifier for each order (UUID)
    restaurantId TEXT NOT NULL,    -- Foreign key to the Restaurant table
    userId       TEXT NOT NULL,    -- Foreign key to CampusUser table
    status       TEXT,             -- Order status (e.g., 'PENDING', 'CONFIRMED', 'COMPLETED')
    FOREIGN KEY (restaurantId) REFERENCES Restaurant (restaurantId),
    FOREIGN KEY (userId) REFERENCES CampusUser (userId)
);

CREATE TABLE "Transaction"
(
    transactionId INTEGER PRIMARY KEY AUTOINCREMENT,
    amount        REAL NOT NULL, -- Transaction amount
    paymentMethod TEXT NOT NULL, -- Payment method used (e.g., 'CREDIT_CARD')
    timestamp     TEXT NOT NULL, -- Timestamp of the transaction (ISO 8601 string)
    orderUUID     TEXT,          -- Foreign key to Order table
    FOREIGN KEY (orderUUID) REFERENCES "Order" (orderUUID)
);

CREATE TABLE GroupOrder
(
    groupOrderId     TEXT PRIMARY KEY, -- Unique identifier for the group order
    deliveryLocation TEXT,             -- Delivery location for the group order (e.g., 'Campus Dorm')
    deliveryTime     TEXT,             -- Optional delivery time (ISO 8601 string)
    status           TEXT              -- Status (e.g., 'PENDING', 'COMPLETED')
);

CREATE TABLE GroupOrder_Orders
(
    groupOrderId TEXT, -- Foreign key to GroupOrder
    orderUUID    TEXT, -- Foreign key to Order
    PRIMARY KEY (groupOrderId, orderUUID),
    FOREIGN KEY (groupOrderId) REFERENCES GroupOrder (groupOrderId),
    FOREIGN KEY (orderUUID) REFERENCES "Order" (orderUUID)
);

CREATE TABLE DeliveryDetails
(
    deliveryId       INTEGER PRIMARY KEY AUTOINCREMENT,
    orderUUID        TEXT,          -- Foreign key to Order table
    deliveryLocation TEXT NOT NULL, -- Delivery location (e.g., 'Campus Dorm, 123 Campus St.')
    deliveryTime     TEXT,          -- Optional delivery time (ISO 8601 string)
    FOREIGN KEY (orderUUID) REFERENCES "Order" (orderUUID)
);

CREATE TABLE Slot_Orders
(
    slotId    INTEGER, -- Foreign key to Slot
    orderUUID TEXT,    -- Foreign key to Order
    PRIMARY KEY (slotId, orderUUID),
    FOREIGN KEY (slotId) REFERENCES Slot (slotId),
    FOREIGN KEY (orderUUID) REFERENCES "Order" (orderUUID)
);

CREATE TABLE RestaurantManager
(
    managerId    TEXT PRIMARY KEY, -- Unique identifier for the manager (e.g., UUID)
    restaurantId TEXT NOT NULL,    -- Foreign key to the Restaurant table
    email        TEXT NOT NULL,    -- Email for authentication
    password     TEXT NOT NULL,    -- Encrypted password
    name         TEXT,             -- Name of the manager
    FOREIGN KEY (restaurantId) REFERENCES Restaurant (restaurantId)
);
