DROP TABLE IF EXISTS COMMENTS;
DROP TABLE IF EXISTS BOOKS;
DROP TABLE IF EXISTS AUTHORS;
DROP TABLE IF EXISTS GENRES;
CREATE TABLE AUTHORS(
  ID INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
  NAME VARCHAR(255) NOT NULL,
  CONSTRAINT AUTHORS_UK UNIQUE(NAME)
);
CREATE TABLE GENRES(
  ID INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
  NAME VARCHAR(255) NOT NULL,
  CONSTRAINT GENRES_UK UNIQUE(NAME)
);
CREATE TABLE BOOKS(
  ID INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
  NAME VARCHAR(255) NOT NULL,
  AUTHOR_ID INTEGER NOT NULL,
  GENRE_ID INTEGER NOT NULL,
  CONSTRAINT BOOKS_UK UNIQUE(NAME),
  FOREIGN KEY (AUTHOR_ID) REFERENCES AUTHORS (ID),
  FOREIGN KEY (GENRE_ID) REFERENCES GENRES (ID)
);
CREATE TABLE COMMENTS(
  ID INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
  BOOK_ID INTEGER NOT NULL,
  TEXT VARCHAR(255) NOT NULL,
  INSERT_DATE DATE NOT NULL,
  FOREIGN KEY (BOOK_ID) REFERENCES BOOKS (ID)
);