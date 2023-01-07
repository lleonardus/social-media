-- users

INSERT INTO users(name, email, created_at, updated_at)
VALUES ('Leonardo', 'leonardo@gmail.com', '2022-1-1', '2022-8-12'),
       ('Maria', 'maria@gmail.com', '2022-1-1', '2023-9-12'),
       ('Gabriel', 'gabriel@gmail.com', '2022-1-7', '2023-11-12'),
       ('Marcos', 'marcos@gmail.com', '2022-1-8', '2023-11-12'),
       ('Felipe', 'felipe@gmail.com', '2022-1-7', '2023-1-12');


--posts

INSERT INTO posts(user_id, title, content, created_at, updated_at)
VALUES (1, 'My first post', 'Hello World!', '2022-10-10', '2022-12-22'),
       (1, 'My favorite language', 'Hello Java!', '2022-8-10', '2022-9-10'),
       (2, 'My first language', 'Hello Python!', '2022-5-10', '2022-7-19'),
       (3, 'My first post', 'Hello C++!', '2022-2-10', '2022-3-18'),
       (4, 'My first post', 'Hello Rust!', '2022-1-10', '2022-2-12');


--comments
INSERT INTO comments(user_id, post_id, content, created_at, updated_at)
VALUES(5, 1, 'Nice', '2022-10-10', '2022-11-12'),
      (4, 2, 'Java > JS', '2022-10-10', '2022-11-23'),
      (3, 2, 'I Love Java', '2022-8-10', '2022-12-22'),
      (2, 3, 'Python is easy', '2022-5-10', '2022-12-17'),
      (1, 5, 'C++ killer', '2022-1-10', '2022-11-13');