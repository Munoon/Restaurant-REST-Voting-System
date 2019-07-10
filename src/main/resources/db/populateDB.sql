DELETE FROM user_votes;
DELETE FROM meals;
DELETE FROM restaurants;
DELETE FROM user_roles;
DELETE FROM users;
ALTER SEQUENCE GLOBAL_SEQ RESTART WITH 100;

INSERT INTO users (name, email, password, registered) VALUES
    ('Nikita', 'munoon@yandex.ru', 'easyPass', '2019-07-01 10:00:00.000000'),
    ('Vasya', 'vasya@gmail.com', 'VasyaTheBest', '2019-07-02 10:00:00.000000'),
    ('Petr', 'petr@gmail.com', 'PertHasStrongPass123', '2019-07-03 10:00:00.000000');

INSERT INTO user_roles (user_id, role) VALUES
    (100, 'ROLE_ADMIN'),
    (101, 'ROLE_USER'),
    (102, 'ROLE_USER');

INSERT INTO restaurants (name, date) VALUES
    ('McDonalds', '2019-07-01 10:00:00.000000'),
    ('KFC', '2019-07-02 10:00:00.000000');

INSERT INTO meals (restaurant_id, name, price) VALUES
    (103, 'Burger', 50),
    (103, 'French Fries', 20),
    (104, 'Burger', 30),
    (104, 'Chicken', 35);

INSERT INTO user_votes (user_id, restaurant_id, date) VALUES
    (100, 104, '2019-07-01 10:00:00.000000'),
    (100, 103, '2019-07-02 10:00:00.000000'),
    (101, 103, '2019-07-01 10:00:00.000000');