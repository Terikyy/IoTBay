-- noinspection SqlNoDataSourceInspectionForFile
DROP TABLE User;
DROP TABLE Admin;
DROP TABLE Staff;
DROP TABLE Product;
DROP TABLE Payment;
DROP TABLE `Order`;
DROP TABLE CartItem;
DROP TABLE OrderItem;
DROP TABLE Address;
DROP TABLE ShippingManagement;
DROP TABLE Log;

-- 01 User
CREATE TABLE User
(
    UserID      INT PRIMARY KEY,
    Name        VARCHAR(50) NOT NULL,
    Email       VARCHAR(50) UNIQUE,
    Password    VARCHAR(50),
    Active      BOOLEAN DEFAULT TRUE,
    PhoneNumber VARCHAR(15)
);

-- 02 Admin
CREATE TABLE Admin
(
    UserID INT PRIMARY KEY,
    FOREIGN KEY (UserID) REFERENCES User (UserID)
);

-- 03 Staff
CREATE TABLE Staff
(
    UserID INT PRIMARY KEY,
    FOREIGN KEY (UserID) REFERENCES User (UserID)
);

-- 04 Product
CREATE TABLE Product
(
    ProductID   INT PRIMARY KEY,
    Name        VARCHAR(150),
    Description VARCHAR(1000),
    Price       DOUBLE(10, 2),
    Stock       INT,
    ReleaseDate DATE,
    Category    VARCHAR(50)
);

-- 05 Order
CREATE TABLE `Order`
(
    OrderID     INT PRIMARY KEY,
    UserID      INT,
    OrderStatus VARCHAR(20),
    OrderDate   DATE DEFAULT CURRENT_TIMESTAMP,
    TotalPrice  NUMERIC(10, 2),
    FOREIGN KEY (UserID) REFERENCES User (UserID)
);

-- 06 Payment
CREATE TABLE Payment
(
    PaymentID     INT PRIMARY KEY,
    OrderID       INT,
    PaymentMethod VARCHAR(50),
    AmountPaid    NUMERIC(10, 2),
    PaymentDate   DATE,
    PaymentStatus VARCHAR(100),
    FOREIGN KEY (OrderID) REFERENCES `Order` (OrderID)
);

-- 07 OrderItem
CREATE TABLE OrderItem
(
    ProductID    INT,
    OrderID      INT,
    Quantity     INT,
    PriceOnOrder NUMERIC(10, 2),
    PRIMARY KEY (ProductID, OrderID),
    FOREIGN KEY (ProductID) REFERENCES Product (ProductID),
    FOREIGN KEY (OrderID) REFERENCES `Order` (OrderID)
);

-- 08 Address
CREATE TABLE Address
(
    AddressID    INT PRIMARY KEY,
    Name         VARCHAR(50) NOT NULL,
    StreetNumber VARCHAR(10),
    StreetName   VARCHAR(50),
    Postcode     INT CHECK (Postcode BETWEEN 1000 AND 9999),
    Suburb       VARCHAR(50),
    City         VARCHAR(30),
    State        VARCHAR(10)
);


-- 09 CartItem
CREATE TABLE CartItem
(
    ProductID INT,
    UserID    INT,
    Quantity  INT,
    PRIMARY KEY (ProductID, UserID),
    FOREIGN KEY (ProductID) REFERENCES Product (ProductID),
    FOREIGN KEY (UserID) REFERENCES User (UserID)
);

CREATE TABLE ShippingManagement
(
    ShipmentID     INTEGER PRIMARY KEY AUTOINCREMENT,
    OrderID        INT  NOT NULL,
    ShipmentDate   DATE NOT NULL,
    Address        TEXT NOT NULL,
    ShippingMethod TEXT NOT NULL,
    IsFinalised    BOOLEAN DEFAULT FALSE
);

CREATE TABLE Log
(
    LogID      INT PRIMARY KEY,
    UserID     INT,
    LogMessage VARCHAR(100),
    Timestamp  DATE NOT NULL DEFAULT CURRENT_TIMESTAMP
);
