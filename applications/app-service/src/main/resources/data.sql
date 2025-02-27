
INSERT INTO role (id, name) VALUES  (1, 'Admin');
INSERT INTO role (id, name) VALUES  (2, 'Client');

INSERT INTO users (name, email, password, role_id) VALUES ('user1', 'admin@mail.com', '$2a$10$0Ir816uoKvhM.IYrQPZqqeZGf51OxweQYF5R9uXDf91IxQ1vuLoxK', 1);
INSERT INTO users (name, email, password, role_id) VALUES ('user2', 'client@mail.com', '$2a$10$0Ir816uoKvhM.IYrQPZqqeZGf51OxweQYF5R9uXDf91IxQ1vuLoxK', 2);

