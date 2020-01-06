DROP TABLE IF EXISTS BOOKS;
DROP TABLE IF EXISTS AUTHORS;
DROP TABLE IF EXISTS GENRES;
CREATE TABLE AUTHORS(
  ID IDENTITY PRIMARY KEY,
  NAME VARCHAR(255) NOT NULL,
  UNIQUE KEY AUTHORS_UK (NAME)
);
CREATE TABLE GENRES(
  ID IDENTITY PRIMARY KEY,
  NAME VARCHAR(255) NOT NULL,
  UNIQUE KEY GENRES_UK (NAME)
);
CREATE TABLE BOOKS(
  ID IDENTITY PRIMARY KEY,
  NAME VARCHAR(255) NOT NULL,
  AUTHOR_ID BIGINT NOT NULL,
  GENRE_ID BIGINT NOT NULL,
  UNIQUE KEY BOOKS_UK (NAME),
  FOREIGN KEY (AUTHOR_ID) REFERENCES AUTHORS (ID),
  FOREIGN KEY (GENRE_ID) REFERENCES GENRES (ID)
);