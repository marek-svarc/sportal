drop table if exists test_enum_value;
create table test_enum_value (
	id serial not null primary key,
	enum_value character varying
);

drop table if exists test_enum_number;
create table test_enum_number (
	id serial not null primary key,
	enum_number int
);

-- Creates GIN index (similar functionality to bitmap index)
--create extension if not exists btree_gin;
--create index test_enum_value_idx on test_enum_value using gin (enum_value);
--create index test_enum_number_idx on test_enum_number using gin (enum_number);