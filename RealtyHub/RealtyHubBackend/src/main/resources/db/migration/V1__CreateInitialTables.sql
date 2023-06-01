CREATE TABLE address
(
    id         int NOT NULL AUTO_INCREMENT,
    number        int NOT NULL,
    street_name       varchar(50),
    postcode varchar(50),
    PRIMARY KEY (id)
);

CREATE TABLE accommodation
(
    id   int     NOT NULL AUTO_INCREMENT,
    name varchar(50),
    starting_date varchar(50),
    price double precision,
    address_id int,
    area int,
    interior varchar(50),
    image varchar(200),
    floors int,
    rooms int,
    PRIMARY KEY (id),
    FOREIGN KEY (address_id) REFERENCES address (id)
);

