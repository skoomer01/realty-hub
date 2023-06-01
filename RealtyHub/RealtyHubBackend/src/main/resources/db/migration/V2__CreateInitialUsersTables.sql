CREATE TABLE user_info
(
    id         int NOT NULL AUTO_INCREMENT,
    surname    varchar(50),
    name       varchar(50),
    username   varchar(50),
    PRIMARY KEY (id),
    UNIQUE (username)
);

CREATE TABLE user
(
    id         int          NOT NULL AUTO_INCREMENT,
    username   varchar(50)  NOT NULL,
    password   varchar(100) NOT NULL,
    userinfo_id int NULL,
    PRIMARY KEY (id),
    UNIQUE (username),
    FOREIGN KEY (userinfo_id) REFERENCES user_info (id)
);

CREATE TABLE user_role
(
    id        int         NOT NULL AUTO_INCREMENT,
    user_id   int         NOT NULL,
    role_name varchar(50) NOT NULL,
    PRIMARY KEY (id),
    UNIQUE (user_id, role_name),
    FOREIGN KEY (user_id) REFERENCES user (id)
);