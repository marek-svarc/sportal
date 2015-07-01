drop table if exists test_large_table;
create table test_large_table (
	id serial primary key,
	str_col_1 character varying (100),
	int_col_1 int,
	str_col_2 character varying (100),
	int_col_2 int,
	str_col_3 character varying (100),
	int_col_3 int,
	str_col_4 character varying (100),
	int_col_4 int,
	str_col_5 character varying (100),
	int_col_5 int,
	str_col_6 character varying (100),
	int_col_6 int,
	str_col_7 character varying (100),
	int_col_7 int,
	str_col_8 character varying (100),
	int_col_8 int,
	str_col_9 character varying (100),
	int_col_9 int,
	str_col_10 character varying (100),
	int_col_10 int	
);