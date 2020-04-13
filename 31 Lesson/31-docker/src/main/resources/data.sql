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
select t.name, (select id from authors where name = t.author_name), (select id from genres where name = t.genre_name)
from  (select 'The Hobbit'::text as name, 'J.R.R. Tolkien'::text as author_name, 'Action and Adventure'::text as genre_name
       union all
       select 'The Three Musketeers'::text as name, 'Alexandre Dumas'::text as author_name, 'Action and Adventure'::text as genre_name
       union all
       select 'Life of Pi'::text as name, 'Yann Martel'::text as author_name, 'Action and Adventure'::text as genre_name
       union all
       select 'To Kill a Mockingbird'::text as name, 'Harper Lee'::text as author_name, 'Classic'::text as genre_name
       union all
       select '1984'::text as name, 'George Orwell'::text as author_name, 'Classic'::text as genre_name
       union all
       select 'Romeo and Juliet'::text as name, 'William Shakespeare'::text as author_name, 'Classic'::text as genre_name
       union all
       select 'Hamlet'::text as name, 'William Shakespeare'::text as author_name, 'Drama'::text as genre_name
      ) t;
insert into comments(book_id, text, insert_date) values (1,'Test comment', current_date);