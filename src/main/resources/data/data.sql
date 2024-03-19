INSERT INTO user_group (name)
VALUES ('ADMIN'),
    ('USER');
INSERT INTO user (username, hash, email, group_id)
VALUES (
        'darknight',
        -- Actual password: password
        '5e884898da28047151d0e56f8dc6292773603d0d6aabbdd62a11ef721d1542d8',
        'user1@example.com',
        1
    )