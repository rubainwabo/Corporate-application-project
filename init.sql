DROP SCHEMA IF EXISTS projet CASCADE;
CREATE SCHEMA projet;
CREATE TABLE projet.members
(
    user_id                       SERIAL PRIMARY KEY,
    username                      VARCHAR(100) NOT NULL,
    last_name                     VARCHAR(100) NOT NULL,
    first_name                    VARCHAR(100) NOT NULL,
    unit_number                   INTEGER,
    state                         VARCHAR(100) NOT NULL,
    _role                         VARCHAR(100) NOT NULL,
    reason_for_connection_refusal VARCHAR(100),
    password                      VARCHAR(100) NOT NULL,
    phone_number                  VARCHAR(100),
    street                        VARCHAR(100) NOT NULL,
    postCode                      INTEGER      NOT NULL,
    building_number               INTEGER      NOT NULL,
    city                          VARCHAR(100) NOT NULL,
    url_picture                   VARCHAR(100),
    nb_of_item_not_taken          INTEGER      NOT NULL
);

CREATE TABLE projet.item_type
(
    id_item_type   SERIAL PRIMARY KEY,
    item_type_name VARCHAR(100) NOT NULL
);

CREATE TABLE projet.items
(
    id_item                     SERIAL PRIMARY KEY,
    description                 VARCHAR(100)                                       NOT NULL,
    url_picture                 VARCHAR(100),
    rating                      INTEGER,
    comment                     VARCHAR(200),
    item_condition              VARCHAR(20)                                        NOT NULL,
    time_slot                   VARCHAR(200)                                       NOT NULL,
    offeror                     INTEGER REFERENCES projet.members (user_id)        NOT NULL,
    item_type                   INTEGER REFERENCES projet.item_type (id_item_type) NOT NULL,
    recipient                   INTEGER REFERENCES projet.members (user_id),
    number_of_people_interested INTEGER
);

CREATE TABLE projet.interests
(
    _date  varchar(100) NOT NULL,
    member INTEGER REFERENCES projet.members (user_id) NOT NULL,
    item   INTEGER REFERENCES projet.items (id_item)   NOT NULL,
    PRIMARY KEY (member, item)
);

CREATE TABLE projet.dates
(
    id_date SERIAL PRIMARY KEY,
    _date   DATE                                      NOT NULL,
    item    INTEGER REFERENCES projet.items (id_item) NOT NULL
);

CREATE TABLE projet.notifications
(
    id_notification SERIAL PRIMARY KEY,
    is_viewed       BOOLEAN                                     NOT NULL,
    text            VARCHAR(200)                                NOT NULL,
    person          INTEGER REFERENCES projet.members (user_id) NOT NULL,
    item            INTEGER REFERENCES projet.items (id_item)   NOT NULL
);


INSERT INTO projet.members
VALUES (DEFAULT, 'test', 'test', 'test', 2, 'valid', 'admin', DEFAULT,
        '$2a$10$rRmgL91HV9iLQgzhelBWwOopa5sK1ZrAXAASk5G0kXKJZqbZfN6b6', DEFAULT, 'test', 1234, 1,
        'test', DEFAULT, 0);
INSERT INTO projet.members
VALUES (DEFAULT, 'test', 'numero1', 'test', 2, 'denied', 'admin', DEFAULT,
        '$2a$10$rRmgL91HV9iLQgzhelBWwOopa5sK1ZrAXAASk5G0kXKJZqbZfN6b6', DEFAULT, 'test', 1234, 1,
        'test', DEFAULT, 0);
INSERT INTO projet.members
VALUES (DEFAULT, 'test', 'test1', 'test', 2, 'waiting', 'admin', DEFAULT,
        '$2a$10$rRmgL91HV9iLQgzhelBWwOopa5sK1ZrAXAASk5G0kXKJZqbZfN6b6', DEFAULT, 'test', 1234, 1,
        'test', DEFAULT, 0);
INSERT INTO projet.members
VALUES (DEFAULT, 'test', 'numero2', 'test', 2, 'waiting', 'admin', DEFAULT,
        '$2a$10$rRmgL91HV9iLQgzhelBWwOopa5sK1ZrAXAASk5G0kXKJZqbZfN6b6', DEFAULT, 'test', 1234, 1,
        'test', DEFAULT, 0);

INSERT INTO projet.item_type
VALUES (DEFAULT, 'test');
INSERT INTO projet.item_type
VALUES (DEFAULT, 'Jouets');
INSERT INTO projet.items
VALUES (DEFAULT, 'test', DEFAULT, DEFAULT, DEFAULT, 'test', 'test', 2, 1, DEFAULT);
INSERT INTO projet.dates
VALUES (DEFAULT, '1-1-1', 1);
