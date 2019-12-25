insert into genres(name) values ('GENRE1');

insert into authors(name) values ('AUTHOR1');

insert into books(name, author_id, genre_id)
select name, (select id from authors where name = author_name), (select id from genres where name = genre_name)
from  (select 'BOOK1' name, 'AUTHOR1' author_name, 'GENRE1' genre_name
       from dual
      );
