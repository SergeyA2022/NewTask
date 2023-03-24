CREATE TABLE GROUPS
(
    ID   INTEGER IDENTITY NOT NULL PRIMARY KEY,
    NUMBER VARCHAR(255) NOT NULL,
    FACULTY VARCHAR(255) NOT NULL,

    UNIQUE (NUMBER)
);

INSERT INTO GROUPS (NUMBER, FACULTY) VALUES (127, 'GFGGF');

SELECT * FROM GROUPS;

CREATE TABLE STUDENTS
(
    ID   INTEGER IDENTITY NOT NULL PRIMARY KEY,
    LAST_NAME VARCHAR(255) NOT NULL,
    FIST_NAME VARCHAR(255) NOT NULL,
    PATRONYMIC VARCHAR(255),
    BIRTHDAY DATE NOT NULL,
    GROUP_fk INTEGER

);

