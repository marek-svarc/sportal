--revoke select, insert, update, delete on all tables in schema public from clubeek;
--revoke usage, select on all sequences in schema public from clubeek;
--drop role if exists clubeek;
create role clubeek noinherit login password 'x5PurEQUVaxathe8' valid until 'infinity';
grant select, insert, update, delete on all tables in schema public to clubeek;
grant usage, select on all sequences in schema public to clubeek;

