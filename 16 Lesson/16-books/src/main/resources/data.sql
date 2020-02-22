insert into genres(name) values ('Action and Adventure');
insert into genres(name) values ('Classic');
insert into genres(name) values ('Drama');

insert into authors(name) values ('J.R.R. Tolkien');
insert into authors(name) values ('Alexandre Dumas');
insert into authors(name) values ('Yann Martel');
insert into authors(name) values ('Harper Lee');
insert into authors(name) values ('George Orwell');
insert into authors(name) values ('William Shakespeare');

insert into books(name, author_id, genre_id)
select name, (select id from authors where name = author_name), (select id from genres where name = genre_name)
from  (select 'The Hobbit' name, 'J.R.R. Tolkien' author_name, 'Action and Adventure' genre_name
       from dual
       union all
       select 'The Three Musketeers' name, 'Alexandre Dumas' author_name, 'Action and Adventure' genre_name
       from dual
       union all
       select 'Life of Pi' name, 'Yann Martel' author_name, 'Action and Adventure' genre_name
       from dual
       union all
       select 'To Kill a Mockingbird' name, 'Harper Lee' author_name, 'Classic' genre_name
       from dual
       union all
       select '1984' name, 'George Orwell' author_name, 'Classic' genre_name
       from dual
       union all
       select 'Romeo and Juliet' name, 'William Shakespeare' author_name, 'Classic' genre_name
       from dual
       union all
       select 'Hamlet' name, 'William Shakespeare' author_name, 'Drama' genre_name
       from dual
      );
