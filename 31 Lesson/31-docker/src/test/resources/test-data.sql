insert into genres(name) values ('GENRE1');

insert into authors(name) values ('AUTHOR1');

insert into books(name, author_id, genre_id)
select 'BOOK1',
       (select id from authors where name = 'AUTHOR1'),
       (select id from genres where name = 'GENRE1');