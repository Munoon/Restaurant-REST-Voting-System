DELETE FROM user_votes;
DELETE FROM meals;
DELETE FROM restaurants;
DELETE FROM user_roles;
DELETE FROM users;
ALTER SEQUENCE GLOBAL_SEQ RESTART WITH 100;

INSERT INTO users (name, email, password) VALUES
    ('Nikita', 'munoon@yandex.ru', 'easyPass'),
    ('Vasya', 'vasya@gmail.com', 'VasyaTheBest'),
    ('Petr', 'petr@gmail.com', 'PertHasStrongPass123');

INSERT INTO user_roles (user_id, role) VALUES
    (100, 'ROLE_ADMIN'),
    (101, 'ROLE_USER'),
    (102, 'ROLE_USER');

INSERT INTO restaurants (name) VALUES
    ('McDonalds'),
    ('KFC');

INSERT INTO meals (restaurant_id, name, price) VALUES
    (103, 'Burger', 50),
    (103, 'French Fries', 20),
    (104, 'Burger', 30),
    (104, 'Chicken', 35);

INSERT INTO user_votes (user_id, restaurant_id) VALUES
    (100, 104),
    (101, 103);
