DELETE FROM user_votes;
DELETE FROM meals;
DELETE FROM restaurants;
DELETE FROM user_roles;
DELETE FROM users;
ALTER SEQUENCE GLOBAL_SEQ RESTART WITH 100;

INSERT INTO users (name, email, password) VALUES
    ('Admin', 'admin@gmail.com', '{noop}admin'),
    ('User', 'user@gmail.com', '{noop}user');

INSERT INTO user_roles (user_id, role) VALUES
    (100, 'ROLE_ADMIN'),
    (101, 'ROLE_USER');

INSERT INTO restaurants (name) VALUES
    ('McDonalds'),
    ('KFC');

INSERT INTO meals (restaurant_id, name, price, date) VALUES
    (102, 'Burger', 50, '2019-08-06'),
    (102, 'French Fries', 20, '2019-08-06'),
    (103, 'Burger', 30, '2019-08-06'),
    (102, 'Chicken', 35, '2019-08-07');

INSERT INTO user_votes (user_id, restaurant_id, date) VALUES
    (100, 103, '2019-07-01'),
    (100, 102, '2019-07-02'),
    (101, 102, '2019-07-01');