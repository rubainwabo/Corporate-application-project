DROP SCHEMA IF EXISTS projet CASCADE;
CREATE SCHEMA projet;
CREATE TABLE projet.personnes
(
    id_personne       SERIAL PRIMARY KEY,
    pseudo            VARCHAR(100) NOT NULL,
    nom               VARCHAR(100) NOT NULL,
    prenom            VARCHAR(100) NOT NULL,
    boite_postale     INTEGER,
    etat              VARCHAR(100) NOT NULL,
    _role             VARCHAR(100) NOT NULL,
    texte_refus       VARCHAR(100),
    mot_de_passe      VARCHAR(100) NOT NULL,
    num_tel           VARCHAR(100),
    rue               VARCHAR(100) NOT NULL,
    code_postale      INTEGER      NOT NULL,
    no_maison         INTEGER      NOT NULL,
    ville             VARCHAR(100) NOT NULL,
    url_photo         VARCHAR(100),
    nb_objet_non_pris INTEGER      NOT NULL
);

CREATE TABLE projet.types_objet
(
    id_type_objet SERIAL PRIMARY KEY,
    libelle       VARCHAR(100) NOT NULL
);

CREATE TABLE projet.objets
(
    id_objet      SERIAL PRIMARY KEY,
    description   VARCHAR(100)                                          NOT NULL,
    url_photo     VARCHAR(100),
    note          INTEGER,
    commentaire   VARCHAR(200),
    etat          VARCHAR(20)                                           NOT NULL,
    plage_horaire VARCHAR(200)                                          NOT NULL,
    offreur       INTEGER REFERENCES projet.personnes (id_personne)     NOT NULL,
    type_objet    INTEGER REFERENCES projet.types_objet (id_type_objet) NOT NULL,
    receveur      INTEGER REFERENCES projet.personnes (id_personne)
);

CREATE TABLE projet.interets
(
    _date  DATE                                              NOT NULL,
    membre INTEGER REFERENCES projet.personnes (id_personne) NOT NULL,
    objet  INTEGER REFERENCES projet.objets (id_objet)       NOT NULL,
    PRIMARY KEY (membre, objet)
);



CREATE TABLE projet.dates
(
    id_date SERIAL PRIMARY KEY,
    _date   DATE                                        NOT NULL,
    objet   INTEGER REFERENCES projet.objets (id_objet) NOT NULL
);

CREATE TABLE projet.notifications
(
    id_notification SERIAL PRIMARY KEY,
    estVue          BOOLEAN                                           NOT NULL,
    texte           VARCHAR(200)                                      NOT NULL,
    personne        INTEGER REFERENCES projet.personnes (id_personne) NOT NULL
);

INSERT INTO projet.personnes
VALUES (DEFAULT, 'test', 'test', 'test', 2, 'validé', 'admin', DEFAULT,
        '$2a$10$rRmgL91HV9iLQgzhelBWwOopa5sK1ZrAXAASk5G0kXKJZqbZfN6b6', DEFAULT, 'test', 1234, 1,
        'test', DEFAULT, 0);
INSERT INTO projet.types_objet
VALUES (DEFAULT, 'test');
INSERT INTO projet.objets
VALUES (DEFAULT, 'test', DEFAULT, DEFAULT, DEFAULT, 'test', 'test', 1, 1, DEFAULT);
INSERT INTO projet.dates
VALUES (DEFAULT, '1-1-1', 1);
INSERT INTO projet.interets
VALUES ('1-1-1', 1, 1);
INSERT INTO projet.notifications
VALUES (DEFAULT, false, 'test', 1);