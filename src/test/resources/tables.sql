DROP TABLE IF EXISTS products;
CREATE TABLE products (
    id IDENTITY NOT NULL PRIMARY KEY,
    name VARCHAR(255)
);

INSERT INTO products(name) VALUES ('Orange');