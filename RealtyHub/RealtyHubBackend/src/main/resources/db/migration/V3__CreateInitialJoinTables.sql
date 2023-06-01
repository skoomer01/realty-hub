CREATE TABLE user_accommodation
(
    id        int         NOT NULL AUTO_INCREMENT,
    user_id   int         NOT NULL,
    accommodation_id   int      NOT NULL,
    status varchar(10) NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (user_id) REFERENCES user (id),
    FOREIGN KEY (accommodation_id) REFERENCES accommodation (id)
);