drop table if exists indexed_table;

create table indexed_table (
	id serial,
	string_col character varying(100),
	string_col_indexed character varying(100),
	int_col int,
	int_col_indexed int,
	my_enum int,
	my_enum_indexed int
);

create unique index idx_int_col_indexed on indexed_table using btree (int_col_indexed);
create index idx_my_enum_indexed on indexed_table using gin (my_enum_indexed);