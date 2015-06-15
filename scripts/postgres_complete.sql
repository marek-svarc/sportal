------------------------------
-- Dropping tables if exist --
------------------------------

drop table if exists T_CLUB_SETTING;
drop table if exists T_CONTACT;
drop table if exists T_APPLICANT_FOR_ACTION;
drop table if exists T_APPLICANT_FOR_TEAM_TRAINING;
drop table if exists T_APPLICANT_FOR_TEAM_MATCH;
drop table if exists T_USER;
drop table if exists T_PARTICIPANT_OF_TRAINING;
drop table if exists T_PARTICIPANT_OF_MATCH;
drop table if exists T_ACTION;
drop table if exists T_TEAM_TRAINING;

drop view if exists club_member_by_team;

drop table if exists T_TEAM_MEMBER;
drop table if exists T_CLUB_MEMBER;
drop table if exists T_TEAM_MATCH;
drop table if exists T_CLUB_RIVAL;
drop table if exists T_ARTICLE;
drop table if exists T_CLUB_TEAM;
drop table if exists T_CATEGORY;


---------------------
-- Creating tables --
---------------------

CREATE TABLE T_CLUB_SETTING
(
    id 		serial,
    title	varchar(200),
    comment	varchar(200),
    logo	bytea,

    primary key (id)
);

CREATE TABLE T_CATEGORY
(
    id			serial,
    description		varchar(100)	not null,
    active		boolean	default 'true',
    sorting		integer		default 0,

    primary key (id)
);

CREATE TABLE T_CLUB_MEMBER
(
    id			serial,
    id_personal		varchar(20),
    id_registration	varchar(20),
    name		varchar(100)		not null,
    surname		varchar(100)		not null,
    birthdate		date,
    street		varchar(200),
    city		varchar(100),
    code		varchar(20),
    photo		bytea,

    primary key (id)
);

CREATE TABLE T_CLUB_RIVAL
(
    id			serial,
    name		varchar(100)		not null,
    web			varchar(100),
    gps			varchar(100),
    street		varchar(200),
    city		varchar(100),
    code		varchar(20),
    icon		bytea,

    primary key (id)
);

CREATE TABLE T_CLUB_TEAM
(
    id			serial,
    name		varchar(100)		not null,
    active		boolean,
    sorting		integer 		not null  default 0,
    category_id		integer	        	default null,

    primary key (id),
    foreign key (category_id) references T_CATEGORY (id)
        ON DELETE SET NULL
);

CREATE TABLE T_CONTACT
(
    id 			serial,
    contact		varchar(100)		not null,
    description		varchar(100),
    type		smallint		not null,
    notification	smallint		not null,
    club_member_id	integer			not null,

    primary key (id),
    foreign key (club_member_id) references T_CLUB_MEMBER(id)
        ON DELETE CASCADE,
    unique (contact, club_member_id)

);

CREATE TABLE T_TEAM_MEMBER
(
    id			serial,
    functions		integer		not null  default 0,
    club_member_id	integer		not null,
    club_team_id	integer		not null,

    primary key (id),
    foreign key (club_member_id) references T_CLUB_MEMBER (id)
        ON DELETE CASCADE,
    foreign key ( club_team_id ) references T_CLUB_TEAM ( id )
        ON DELETE CASCADE,
    unique(club_member_id, club_team_id)


);

CREATE TABLE T_TEAM_TRAINING
(
    id			serial,
    start		timestamp	not null,
    finish		timestamp	not null,
    place		varchar(500),
    comment		TEXT,
    club_team_id	integer		not null,

    primary key (id),
    foreign key (club_team_id) references T_CLUB_TEAM (id)
        ON DELETE CASCADE
);

CREATE TABLE T_TEAM_MATCH
(
    id			serial,
    start		timestamp	not null,
    score_pos		smallint,
    score_neg		smallint,
    home_court		boolean 	not null,
    comment		TEXT,
    publish		boolean 	not null default 'false',
    club_team_id	integer		not null,
    club_rival_id	integer,
    club_rival_comment	varchar(200),

    primary key (id),
    foreign key (club_rival_id) references T_CLUB_RIVAL (id)
        ON DELETE SET NULL,
    foreign key (club_team_id) references T_CLUB_TEAM (id)
        ON DELETE CASCADE

);

CREATE TABLE T_ACTION
(
    id			serial,
    start		timestamp		not null,
    finish		timestamp,
    caption		varchar(500)		not null,
    place		varchar(500),
    description		TEXT,
    sign_participation	boolean,
    club_team_id	integer,
    category_id		integer,

    primary key (id),
    foreign key ( club_team_id ) references T_CLUB_TEAM ( id )
        ON DELETE CASCADE,
    foreign key ( category_id ) references T_CATEGORY ( id )
        ON DELETE CASCADE,

    check ((category_id is not null AND club_team_id is null) OR (club_team_id is not null AND category_id is null) OR (club_team_id is null AND category_id is null))

);

CREATE TABLE T_APPLICANT_FOR_ACTION
(
    attend		boolean	not null default 'false',
    club_member_id	integer		not null,
    action_id		integer		not null,

    primary key ( club_member_id, action_id ),
    foreign key ( club_member_id ) references T_CLUB_MEMBER( id )
	ON DELETE CASCADE,
    foreign key ( action_id ) references T_ACTION ( id )
	ON DELETE CASCADE
);

CREATE TABLE T_APPLICANT_FOR_TEAM_TRAINING
(
    attend		boolean	not null default 'false',
    club_member_id	integer		not null,
    team_training_id	integer		not null,

    primary key ( club_member_id, team_training_id ),
    foreign key ( club_member_id ) references T_CLUB_MEMBER( id )
	ON DELETE CASCADE,
    foreign key ( team_training_id ) references T_TEAM_TRAINING ( id )
	ON DELETE CASCADE
);

CREATE TABLE T_APPLICANT_FOR_TEAM_MATCH
(
    attend		boolean	not null default 'false',
    club_member_id	integer		not null,
    team_match_id	integer		not null,

    primary key (club_member_id, team_match_id),
    foreign key (club_member_id) references T_CLUB_MEMBER(id)
	ON DELETE CASCADE,
    foreign key (team_match_id) references T_TEAM_MATCH (id)
	ON DELETE CASCADE
);

CREATE TABLE T_ARTICLE
(
    id			serial,
    location		smallint	not null default 1,
    priority		boolean		not null default 'false',
    caption		varchar(2000)	not null,
    summary		TEXT,
    content		TEXT,
    creation_date	timestamp	not null,
    expiration_date	DATE,
    owner_type		smallint	not null default 0,
    club_team_id	integer,
    category_id		integer,

    primary key (id),
    foreign key (club_team_id) references T_CLUB_TEAM (id)
        ON DELETE CASCADE,
    foreign key (category_id) references T_CATEGORY (id)
        ON DELETE CASCADE,
    check (((owner_type = 1) AND category_id is not null) OR ((owner_type = 2) AND club_team_id is not null) OR (owner_type <> 1 AND owner_type <> 2))
);

CREATE TABLE T_USER
(
    id			serial,
    name		varchar(100)	not null,
    password		varchar(100)	not null,
    permissions		integer		not null default 0,
    club_member_id	integer,

    primary key (id),
    foreign key ( club_member_id ) references T_CLUB_MEMBER ( id )
        ON DELETE CASCADE,
    unique (name),
    unique (id, club_member_id)

);

CREATE TABLE T_PARTICIPANT_OF_MATCH
(
    points		smallint	not null default 0,
    yellow_cards	smallint	not null default 0,
    red_cards		smallint	not null default 0,
    club_member_id	integer		not null,
    team_match_id	integer		not null,

    primary key (club_member_id, team_match_id),
    foreign key ( club_member_id ) references T_CLUB_MEMBER ( id )
        ON DELETE CASCADE,
    foreign key ( team_match_id ) references T_TEAM_MATCH ( id )
        ON DELETE CASCADE
);

CREATE TABLE T_PARTICIPANT_OF_TRAINING
(
    club_member_id	integer		not null,
    team_training_id	integer		not null,

    primary key (club_member_id, team_training_id),
    foreign key ( club_member_id ) references T_CLUB_MEMBER ( id )
        ON DELETE CASCADE,

    foreign key ( team_training_id ) references T_TEAM_TRAINING ( id )
        ON DELETE CASCADE
);

CREATE OR REPLACE VIEW club_member_by_team AS
SELECT  cm.id club_member_id,
        t.id club_team_id,
        cm.id_personal id_personal,
        cm.id_registration id_registration,
        cm.name first_name,
        cm.surname surname,
        cm.birthdate birthdate,
        cm.street street,
        cm.city city,
        cm.code code,
        cm.photo photo
FROM    T_TEAM_MEMBER tm
	JOIN T_CLUB_TEAM t ON tm.club_team_id = t.id
	JOIN T_CLUB_MEMBER cm ON tm.club_member_id = cm.id
;





--------------------------------------------------------------------------------
---------------------
-- Test data --
---------------------
--------------------------------------------------------------------------------
INSERT INTO T_CLUB_SETTING (id, title, comment) VALUES
	(1, 'FC Chvojkovice Brod', 'Zřejmě dobrý oddíl ...'),
	(2, 'Testovací superklub', 'Komentář testovacího klubu, který vyhraje superpohár.');


-- user name, password (the same as user name)
-- administrator is the same as admin (admin is shorter to write)
insert into T_USER (name, password, permissions) values
	('member', 'ddc721d12cbb3aa9850be6b6b801231f2683b221', 0),
	('editor', '97475902e4ce16c0d1b96bbe1e9ad6bb2d41c54e', 1),
	('sportManager', 'a4ae0dc42860d467da627ee8313628f8cc4cc600', 2),
	('clubManager', '76e8fbd3aa79a320cf8e81b436c8feff81612bde', 3),
	('administrator', 'da175474680e4b688737821fae09ddd3add7401e', 4),
	('admin', '45f98c2702661d67f53c5c46d0a3f73083c613f5', 4);

insert into T_CATEGORY (description, active) values
	('Fanoušci [kategorie]', true),
	('Historie [kategorie]', true),
	('Oddíly [kategorie]', true),
	('Pokus', false);

insert into T_CLUB_TEAM (name, active, category_id) values
	('Senioři [týmy]', true, 3),
	('Mládež [týmy]', true, 3),
	('Dospělí [týmy]', true, 3);

insert into T_CLUB_TEAM (name, active) values
	('Pokus [týmy]', false);

insert into T_CLUB_MEMBER (name, surname, birthdate, street, city, code) values
	('Kryštof', 'Staněk', null, null, null, null),
	('Marian', 'Pavlík', to_date('1968-04-04', 'YYYY-MM-DD'), 'Dr. Slabihoudka 988', 'Mladá Boleslav', '29301'),
	('Kristýna', 'Sedláčková', to_date('1953-10-04', 'YYYY-MM-DD'), 'Wattova 172', 'Cheb', '35002'),
	('Dana', 'Kolářová', to_date('2002-11-08', 'YYYY-MM-DD'), 'Nálepkovo náměstí 573', 'Jablonec nad Nisou', '46601'),
	('Božena', 'Jandová', to_date('1922-10-26', 'YYYY-MM-DD'), 'Žofínská 0', 'Znojmo', '67181'),
	('Václav', 'Vávra', to_date('2004-11-07', 'YYYY-MM-DD'), 'Hlubinská 184', 'Olomouc', '77200'),
	('Patrik', 'Hájek', to_date('1975-06-02', 'YYYY-MM-DD'), 'Porážková 331', 'Karlovy Vary', '36001'),
	('Jitka', 'Pokorná', to_date('1941-06-20', 'YYYY-MM-DD'), 'Horní 572', 'Valašské Meziříčí', '75701'),
	('Ludmila', 'Králová', to_date('1938-05-18', 'YYYY-MM-DD'), 'Františka a Anny Ryšových 18', 'Český Těšín', '73701'),
	('Martina', 'Kadlecová', to_date('1975-03-16', 'YYYY-MM-DD'), 'nám. Boženy Němcové 534', 'České Budějovice', '37001'),
	('Marcela', 'Poláková', to_date('1940-04-21', 'YYYY-MM-DD'), 'Žerotínova 998', 'Liberec', '46008'),
	('Ivan', 'Sedláček', null, 'Lechowiczova 761', 'Pardubice', '53012'),
	('Eliška', 'Šimková', to_date('1920-07-20', 'YYYY-MM-DD'), '1. května 676', 'Ostrava', '70900'),
	('Otakar', 'Doležal', to_date('1981-06-10', 'YYYY-MM-DD'), null, null, null),
	('Jarmila', 'Kučerová', null, 'Pod Tratí 750', 'Kladno', '27201'),
	('Radovan', 'Staněk', to_date('1972-12-30', 'YYYY-MM-DD'), 'Dolní 111', 'Jihlava', '58601'),
	('Vlasta', 'Mašková', to_date('1954-09-28', 'YYYY-MM-DD'), 'Edisonova 767', 'Valašské Meziříčí', '75701'),
	('Richard', 'Pavlík', null, 'Čapkova 264', 'Pardubice', '53012'),
	('Alexandr', 'Doležal', to_date('1930-10-25', 'YYYY-MM-DD'), 'Soukenická 448', 'Havířov', '73601'),
	('Jaroslava', 'Musilová', to_date('1944-07-04', 'YYYY-MM-DD'), 'Hornopolní 477', 'Hradec Králové', '50008'),
	('Zuzana', 'Malá', to_date('1978-03-06', 'YYYY-MM-DD'), 'Výškovická 827', 'Znojmo', '67181'),
	('Ludmila', 'Dostálová', to_date('1959-03-28', 'YYYY-MM-DD'), 'Hollarova 509', 'Písek', '39701'),
	('Petra', 'Fialová', to_date('2013-08-06', 'YYYY-MM-DD'), 'Palackého 511', 'Karlovy Vary', '36001'),
	('Eduard', 'Dostál', null, 'Opavská 956', 'Příbram', '26101'),
	('Eliška', 'Benešová', to_date('1918-06-10', 'YYYY-MM-DD'), '17. listopadu 488', 'Český Těšín', '73701'),
	('Vratislav', 'Bláha', null, 'Harantova 629', 'Karviná', '73506'),
	('Dana', 'Ševčíková', to_date('1970-04-06', 'YYYY-MM-DD'), 'Husovo náměstí 73', 'Mladá Boleslav', '29301'),
	('Jaroslava', 'Machová', to_date('2000-12-19', 'YYYY-MM-DD'), 'Dr. Slabihoudka 119', 'Nový Jičín', '74101'),
	('Martina', 'Procházková', to_date('1957-08-22', 'YYYY-MM-DD'), 'nám. Dr. E. Beneše 257', 'Třinec', '73961'),
	('Marta', 'Ševčíková', to_date('1944-03-15', 'YYYY-MM-DD'), 'Jurečkova 747', 'Praha', '11800'),
	('Andrea', 'Vávrová', to_date('1977-03-18', 'YYYY-MM-DD'), 'Zkrácená 525', 'Teplice', '41501'),
	('Stanislav', 'Kučera', to_date('1921-04-17', 'YYYY-MM-DD'), 'Záblatská 210', 'Kolín', '28123'),
	('Lenka', 'Svobodová', null, null, null, null),
	('Božena', 'Sýkorová', to_date('2012-04-25', 'YYYY-MM-DD'), null, null, null),
	('Alena', 'Jelínková', to_date('1971-10-25', 'YYYY-MM-DD'), 'Koněvova 972', 'Opava', '74601'),
	('Dalibor', 'Mareš', to_date('1988-11-06', 'YYYY-MM-DD'), 'Bieblova 200', 'Jihlava', '58601'),
	('Barbora', 'Růžičková', to_date('1942-07-15', 'YYYY-MM-DD'), null, null, null),
	('Aleš', 'Marek', to_date('1976-02-20', 'YYYY-MM-DD'), 'Radvanická 394', 'Tábor', '39003'),
	('Markéta', 'Hrubá', to_date('1942-01-29', 'YYYY-MM-DD'), 'Španihelova 632', 'Frýdek-Místek', '73801'),
	('Roman', 'Kříž', to_date('1969-02-21', 'YYYY-MM-DD'), 'K Myslivně 964', 'Vsetín', '75501'),
	('Libuše', 'Kopecká', null, 'Pod Landekem 657', 'Ústí nad Labem', '40011'),
	('Petra', 'Ševčíková', to_date('1957-08-10', 'YYYY-MM-DD'), null, null, null),
	('Hana', 'Staňková', to_date('1972-05-01', 'YYYY-MM-DD'), 'Balcarova 999', 'Litvínov', '43601'),
	('Lucie', 'Kopecká', to_date('1937-01-01', 'YYYY-MM-DD'), 'Muzejní 186', 'Litvínov', '43601'),
	('Radovan', 'Bureš', to_date('1955-01-06', 'YYYY-MM-DD'), 'Heřmanická 236', 'Přerov', '75003'),
	('Jindřich', 'Polák', null, 'Provaznická 640', 'Most', '43401'),
	('Tereza', 'Nguyen', to_date('1920-06-07', 'YYYY-MM-DD'), 'Gregorova 167', 'Uherské Hradiště', '68606'),
	('Jozef', 'Novotný', null, 'Umělecká 586', 'Nový Jičín', '74101'),
	('Veronika', 'Tichá', to_date('1922-06-10', 'YYYY-MM-DD'), 'Bartovická 312', 'Plzeň', '31700'),
	('Veronika', 'Marková', to_date('1996-02-20', 'YYYY-MM-DD'), 'U Koupaliště 658', 'Valašské Meziříčí', '75701'),
	('Marta', 'Kučerová', null, 'Fišerova 315', 'Frýdek-Místek', '73801'),
	('Luboš', 'Sýkora', to_date('2005-06-25', 'YYYY-MM-DD'), 'Lechowiczova 307', 'Prostějov', '79604'),
	('Radovan', 'Kříž', to_date('1991-03-24', 'YYYY-MM-DD'), null, null, null),
	('Radek', 'Bartoš', to_date('1973-08-22', 'YYYY-MM-DD'), 'Horova 285', 'Vsetín', '75501'),
	('Martin', 'Nguyen', to_date('1932-09-25', 'YYYY-MM-DD'), null, null, null),
	('Vlastimil', 'Kovář', to_date('1971-02-26', 'YYYY-MM-DD'), null, null, null),
	('Božena', 'Svobodová', to_date('1988-07-24', 'YYYY-MM-DD'), 'Fryštátská 803', 'Mladá Boleslav', '29301'),
	('Ján', 'Horák', null, 'Karla Svobody 731', 'Krnov', '79401'),
	('Eduard', 'Čermák', to_date('1924-07-12', 'YYYY-MM-DD'), 'Mongolská 179', 'Brno', '63800'),
	('Jindřich', 'Hrubý', to_date('2011-05-29', 'YYYY-MM-DD'), 'Slavíčkova 869', 'Hradec Králové', '50002'),
	('Andrea', 'Pavlíková', null, 'Dr. Slabihoudka 263', 'Frýdek-Místek', '73801'),
	('Miloš', 'Sedláček', to_date('1988-03-21', 'YYYY-MM-DD'), 'Zelená 3', 'Brno', '60200'),
	('Anna', 'Bartošová', to_date('2010-11-12', 'YYYY-MM-DD'), 'Dostojevského 383', 'Liberec', '46008'),
	('Eliška', 'Nguyenová', to_date('1922-12-27', 'YYYY-MM-DD'), 'Michalské náměstí 709', 'Opava', '74601'),
	('Roman', 'Štěpánek', to_date('1958-07-26', 'YYYY-MM-DD'), 'Záblatská 831', 'Krnov', '79401'),
	('Lenka', 'Kovářová', to_date('1940-10-04', 'YYYY-MM-DD'), 'Vrbka 3', 'Frýdek-Místek', '73801'),
	('Miroslava', 'Vlčková', to_date('1990-08-29', 'YYYY-MM-DD'), 'Aleje 529', 'Krnov', '79401'),
	('Lenka', 'Veselá', null, 'Gregorova 545', 'Karlovy Vary', '36001'),
	('Denis', 'Staněk', null, null, null, null),
	('Lucie', 'Jelínková', to_date('2015-06-28', 'YYYY-MM-DD'), 'Karvinská 740', 'Znojmo', '66902'),
	('Vlasta', 'Lišková', to_date('1993-10-07', 'YYYY-MM-DD'), 'Pustkovecká 888', 'Příbram', '26101'),
	('Tadeáš', 'Pospíšil', to_date('1928-09-19', 'YYYY-MM-DD'), null, null, null),
	('Jarmila', 'Šťastná', to_date('1989-04-25', 'YYYY-MM-DD'), 'Janovská 161', 'Tábor', '39003'),
	('Radek', 'Novák', to_date('1969-07-03', 'YYYY-MM-DD'), 'Plechanovova 927', 'Frýdek-Místek', '73801'),
	('Marcela', 'Moravcová', to_date('1924-12-19', 'YYYY-MM-DD'), 'Stodolní 730', 'Ostrava', '70200'),
	('Michael', 'Čech', null, 'Husovo náměstí 789', 'Frýdek-Místek', '73801'),
	('Marta', 'Matoušková', null, null, null, null),
	('Vlasta', 'Sýkorová', to_date('2012-01-07', 'YYYY-MM-DD'), 'Kubínova 48', 'Ostrava', '71800'),
	('Matyáš', 'Kadlec', to_date('1997-10-06', 'YYYY-MM-DD'), 'Mojmírovců 781', 'Mladá Boleslav', '29301'),
	('Jaroslava', 'Černá', to_date('1926-04-15', 'YYYY-MM-DD'), 'Milíčova 807', 'Třebíč', '67401'),
	('Ludmila', 'Ševčíková', to_date('1958-03-29', 'YYYY-MM-DD'), 'Husovo náměstí 675', 'Český Těšín', '73701'),
	('Matěj', 'Konečný', to_date('1951-09-15', 'YYYY-MM-DD'), 'Halasova 291', 'Tábor', '39002'),
	('Vratislav', 'Havlíček', to_date('1966-11-06', 'YYYY-MM-DD'), null, null, null),
	('Kryštof', 'Šťastný', to_date('1976-06-21', 'YYYY-MM-DD'), 'Přemyslovců 307', 'Sokolov', '35601'),
	('Břetislav', 'Procházka', to_date('1981-08-28', 'YYYY-MM-DD'), 'K Myslivně 384', 'Šumperk', '78701'),
	('Michal', 'Malý', to_date('2004-02-24', 'YYYY-MM-DD'), 'Stanislavského 442', 'České Budějovice', '37008'),
	('Pavla', 'Musilová', null, 'Slavíkova 114', 'Litoměřice', '41201'),
	('Ivo', 'Liška', to_date('1962-05-07', 'YYYY-MM-DD'), 'Muzejní 839', 'Orlová', '73511'),
	('Monika', 'Čechová', null, 'Janovská 623', 'Přerov', '75003'),
	('Zuzana', 'Veselá', to_date('1966-01-01', 'YYYY-MM-DD'), 'Šenovská 588', 'Český Těšín', '73701'),
	('Zdeňka', 'Pospíšilová', to_date('1985-06-26', 'YYYY-MM-DD'), 'Rudná 357', 'Nový Jičín', '74101'),
	('Jaromír', 'Tichý', to_date('1958-07-10', 'YYYY-MM-DD'), 'Bráfova 301', 'Liberec', '46008'),
	('Miloslav', 'Horák', null, 'Soukenická 789', 'Karlovy Vary', '36001'),
	('Kryštof', 'Čech', null, null, null, null),
	('Martina', 'Fialová', to_date('1995-10-10', 'YYYY-MM-DD'), null, null, null),
	('Marcela', 'Staňková', to_date('1986-04-30', 'YYYY-MM-DD'), 'Dobrovského 414', 'Most', '43401'),
	('René', 'Bureš', to_date('1980-09-24', 'YYYY-MM-DD'), 'Na Karolíně 871', 'Olomouc', '77900'),
	('Viktor', 'Hrubý', to_date('1996-07-18', 'YYYY-MM-DD'), 'Cingrova 12', 'Ústí nad Labem', '40011'),
	('Pavla', 'Pavlíková', to_date('1984-07-29', 'YYYY-MM-DD'), 'Vrbka 365', 'Karviná', '73503'),
	('Luboš', 'Růžička', null, 'Rychvaldská 259', 'Mladá Boleslav', '29301');


-- TEAM_MEMBER TABLE
do $$	-- anonymous block
DECLARE
	-- declarations
	v_record record;
BEGIN
	-- senior team members
	for v_record in
		-- each row in following select id assigned to v_record variable
		select id from
			(select id, row_number() over(order by birthdate asc) as rnum from T_CLUB_MEMBER) as subq
		where rnum <= 20 and rnum > 5	-- offset
	loop
		--insert into team_member
		insert into T_TEAM_MEMBER (club_member_id, club_team_id) values (v_record.id, 1);
	end loop;

	-- junior team members
	for v_record in
		-- each row in following select id assigned to v_record variable
		select id from
			(select id, birthdate, row_number() over(order by birthdate desc) as rnum from T_CLUB_MEMBER) as subq
		where rnum <= 35 and birthdate is not null
	loop
		--insert into team_member
		insert into T_TEAM_MEMBER (club_member_id, club_team_id) values (v_record.id, 2);
	end loop;

	-- adult team members
	for v_record in
		-- each row in following select id assigned to v_record variable
		select id from
			(select id, birthdate, row_number() over(order by birthdate desc) as rnum from T_CLUB_MEMBER) as subq
		where (rnum <= 60 and rnum > 40 and birthdate is not null) or (rnum <= 5 and birthdate is null)
	loop
		--insert into team_member
		insert into T_TEAM_MEMBER (club_member_id, club_team_id) values (v_record.id, 3);
	end loop;
END;
$$
;

-- ARTICLES [AKTUALITY]
insert into T_ARTICLE (location, priority, caption, summary, content, creation_date, owner_type, club_team_id, category_id) values
	-- aktuality | pouze kategorie
	(
	1,
	false,
	'Ježour – maskot FC Vysočina [aktuality | pouze kategorie]',
	'Populární maskot Ježour je nedílnou součástí ligových zápasů FC Vysočina už od jara 2007. tento žluto – modro – červený sympatický ježek do Jihlavy přijel 7. března 2007 a na ligovém stadiónu debutoval 16. března 2007 při vítězném duelu proti Hlučínu. Od té doby se zařadil mezi nezdařilejší a nejviditelnější maskoty na české sportovní scéně. Zamilovali si ho děti a za svého jej přijali jak běžní diváci, tak i členové fanouškovského kotle FC Vysočina.',
	'<div>Ježour při zápasech vítá diváky, roztleskává je, zapojuje se do jejich fandění, baví svým poskakováním a po vítězném utkání společně s hráči a fanoušky sdílí a prožívá jejich euforii. Platí pro něj, že nezkazí žádnou legraci a fanoušky nabíjí dobrou náladou i ve chvílích, kdy se jejich fotbalovému týmu nedaří.</div><div><br></div><div>Potkáte ho ale nejen při domácích zápasech FC Vysočina, neboť se pravidelně účastní klubových akcí pro fanoušky všech věkových kategorií. Všude se s ním můžete vyfotit, pomazlit se nebo si společně s ním zafandit.</div><div><br></div><div>Ježour na Facebooku</div><div>Maskot Ježour si žije tak trochu svým vlastním životem. Pokud chcete vědět, co zrovna dělá, jak se mu daří a co připravuje na další setkání s vámi, můžete se s ním spřátelit na sociální síti Facebook. Pravidelně zde komentuje svůj každodenní život a občas vstupuje do témat a událostí, kde byste jej rozhodně nečekali. Chcete-li s Ježourem přátelit na síti, klikněte <a href="http://fcvysocina.cz/">ZDE</a> a přidejte se do rozrůstající rodiny jeho příznivců.</div><div><br></div><div>Ježour na vaší oslavě</div><div>Nezapomenutelná oslava dětských narozenin i s přítomností Ježoura? Proč ne! Tento zábavný maskot nepokazí žádnou legraci a vaší oslavu, party, firemní večírek nebo třeba svatební veselku pořádně okoření. Se všemi přítomnými se ochotně a netradičně vyfotí, podepíše své kartičky či cokoli jiného, přidá několik čísel ze svého vtipného rejstříku a nezapomene ani na originální dárečky. Pokud byste si Ježoura chtěli pozvat k sobě domů nebo do vaší firmy, prohlédněte si podrobnosti <a href="http://fcvysocina.cz/">ZDE</a>.</div><div><br></div><div>Ježour v akci</div><div>Zajímavé fotky s Ježourem si můžete prohlédnout níže, ovšem <a href="http://fcvysocina.cz/">TADY</a> jich naleznete nepřebernou spoustu!</div><div><br></div><div>Máte vlastní fotky s "Ježourem"? Zasílejte nám je s příslušným komentářem a popisem osoby na e-mail zeman.david@psj.cz a my je rádi zveřejníme na Ježourově facebooku!</div>',
	to_timestamp('2014-06-17 11:22:50.656', 'YYYY-MM-DD HH24:MI:SS.MS'),
	1,
	null,
	1
	),

	(
	1,
	false,
	'Zapojte se do soutěže "Vezmi FC Vysočina na dovolenou"! [aktuality | pouze kategorie | důležité]',
	'Zapojte se do letní FOTOSOUTĚŽE! Vezměte dresy, vlajky, suvenýry a další symboly FC Vysočina na dovolenou, na prázdniny či na nefotbalová místa! Na e-mail fuks@psj.cz zasílejte fotografie, na nichž jste vy nebo vaši přátelé v barvách FC Vysočina a nezapomeňte připojit své jméno. Vaší fantazii a výběru místa či lokality rozhodně meze neklademe.',
	'<p style="margin: 10px 0px; padding: 5px 0px; font-family: Arial, Helvetica, sans-serif; font-size: 14.5199995040894px; line-height: 21.7799987792969px;">Fotografie budou průběžně prezentovány ve fotogalerii na oficiálních internetových stránkách klubu. Soutěž potrvá&nbsp;<span style="font-weight: bold;">do 28. srpna 2015</span>! Autoři tří nejlepších snímků budou vybráni ze strany hráčů prvoligového „áčka“ a po zásluze budou odměněni. Ve hře bude šála, klubový dres zn. Adidas a především hlavní cena – bezplatná&nbsp;<a href="http://www.fcvysocina.cz/clanek.asp?id=5046&amp;passw=0f203d894e457eed7d66cb274ebd8727" style="color: rgb(0, 0, 0);">narozeninová oslava s Ježourem v hodnotě 2.399 Kč</a>!</p><p style="margin: 10px 0px; padding: 5px 0px; font-family: Arial, Helvetica, sans-serif; font-size: 14.5199995040894px; line-height: 21.7799987792969px;"><span style="font-weight: bold;">Ukažte i v letních měsících, pro jaké barvy bije vaše fanouškovské srdce!</span></p>',
	to_timestamp('2015-04-03 14:51:50.769', 'YYYY-MM-DD HH24:MI:SS.MS'),
	1,
	null,
	1
	),

	(
	1,
	true,
	'Fan klub [aktuality | pouze kategorie]',
	'Fanklub FC VYSOČINA JIHLAVA už je v dnešní době zaběhlou a velmi důležitou součástí našeho klubu. V současné době má podobu amatérského fanklubu, který se pomalu rozrůstá a získává příznivce nejen z řad fanoušků.',
	'<p style="margin: 10px 0px; padding: 5px 0px; font-family: Arial, Helvetica, sans-serif; font-size: 14.5199995040894px; line-height: 21.7799987792969px;">Hlavním cílem je změnit amatérský fanklub ve sdružení a především napomoci atmosféře na domácím stadionu i mimo něj. V současné době pořádáme výjezdy na zápasy „A" týmu a tým samozřejmě podporujeme i během domácích zápasů. Našim velkým společným přáním je postup do I. Gambrinus ligy.</p><p style="margin: 10px 0px; padding: 5px 0px; font-family: Arial, Helvetica, sans-serif; font-size: 14.5199995040894px; line-height: 21.7799987792969px;"><strong>Jak je možné stát se členem fanklubu?</strong><br>O registrované členství může požádat každý fanoušek FC Vysočina Jihlava. Sezónní příspěvek činí 200 Kč, v případě, že zažádáte o členství v zimní přestávce, příspěvek činí 100 Kč. Každému poté bude vydána osobní registrační karta, která přináší zajímavé výhody, jako jsou slevy na suvenýry, nižší cena výjezdů, pozvánky na zajímavé akce klubu. O kartu lze požádat na e-mailu fanklub.vysocina@seznam.cz nebo též v prodejně suvenýrů pod novou tribunou. Bližší informace o stanovách fanklubu je možné zaslat elektronickou poštou.</p><p style="margin: 10px 0px; padding: 5px 0px; font-family: Arial, Helvetica, sans-serif; font-size: 14.5199995040894px; line-height: 21.7799987792969px;"><strong>Je možné činnost fanklubu podpořit finančně i nad rámec registrovaného členství?</strong><br>Určitě ano. Pro tento účel byl založen bankovní účet s číslem 7648621207/4000. Zde uložené peníze budou použity na investice fanoušků do vlajek a bubnů. Prioritou pak samozřejmě bude financování výjezdů k zápasům na hřištích soupeřů. O výjezdech a akcích fanklubu budou fanoušci pravidelně informováni na hlavních internetových stránkách klubu, v zápasovém rozhlase při domácích zápasech a v dalších dostupných prostředcích.</p>',
	to_timestamp('2014-22-03 00:44:21.132', 'YYYY-MM-DD HH24:MI:SS.MS'),
	1,
	null,
	1
	),

	-- aktuality | pouze tým
	(
	1,
	false,
	'Účast na náboru FC Vysočina byla přes rozmary počasí vysoká [aktuality | pouze tým]',
	'Ani rozmary nevlídného počasí dnes neodradily na čtyři desítky chlapců ročníků 2009 a 2010, aby společně se svými rodiči, prarodiči či sourozenci přišli na květnový nábor FC Vysočina. Ten tradičně láká množství adeptů žlutého dresu FCV a potvrzuje velký zájem o fotbal v Jihlavě.',
	'<div><b>Ani rozmary nevlídného počasí dnes neodradily na čtyři desítky chlapců ročníků 2009 a 2010, aby společně se svými rodiči, prarodiči či sourozenci přišli na květnový nábor FC Vysočina. Ten tradičně láká množství adeptů žlutého dresu FCV a potvrzuje velký zájem o fotbal v Jihlavě.</b></div><div><br></div><div>Chlapci si na hlavní ploše stadiónu za asistence Ježoura s míčem či bez něj vyzkoušeli pohybové dovednosti a fotbalové základy. Zatím je jejich rodiče u sportovního manažera mládeže Josefa Morkuse zapsali do jednotlivých týmů přípravek FC Vysočina a získali informace o termínech tréninků. Z příjemné akce si odnesli odměnu v podobě klubového trička věnovaného obchodním centrem Reiterman. Letos se dočkali i vítaných bonusů - vstupenky na sobotní duel FC Vysočina vs. Slovan Liberec a kartičky s maskotem Ježourem.</div>',
	current_timestamp,
	2,
	2,
	null
	),

	(
	1,
	false,
	'Mládežnická koncepce [aktuality | pouze tým]',
	'Pohled do fotbalových tabulek a statistik hovoří naprosto jasně. FC Vysočina je jediným reprezentantem regionu v českých špičkových soutěžích. Vždyť druhým nejlepším dospělým týmem kraje je juniorský tým FC Vysočina, který působení ve třetí nejvyšší soutěži MSFL.',
	'<div>Pohled do fotbalových tabulek a statistik hovoří naprosto jasně. FC Vysočina je jediným reprezentantem regionu v českých špičkových soutěžích. Vždyť druhým nejlepším dospělým týmem kraje je juniorský tým FC Vysočina, který působení ve třetí nejvyšší soutěži MSFL. Obdobná situace pak panuje i v mládežnických soutěžích, kde má FC Vysočina zástupce ve všech nejvyšších ligách včetně elitní extraligy dorostu. Tento stav je pouze obrazem trpělivé a dlouhodobé spolupráce FC Vysočina s dalšími regionálními kluby, které společně sledují cíl v podobě vytvoření silného klubu reprezentujícího celý kraj. Jihlavský klub se tak stává místem koncentrace nejlepších nejen mládežnických hráčů Vysočiny. Toto přirozené centrum tak zastupuje v nejvyšších českých soutěžích takřka dvacet tisíc v kraji Vysočina registrovaných fotbalistů.</div><div><br></div><div>Je jasným faktem, že kopaná na Vysočině nejspíš nikdy nebude ekonomicky schopna vytvořit kvalitní mužstvo z hráčů skoupených ze všech koutů republiky. Z tohoto důvodu klade vedení FC Vysočina dlouhodobě velký důraz na výchovu mládežnických fotbalistů, kteří by měli do budoucna hrát klíčovou roli v dalších ambicích jihlavské kopané. Z řad mládežnických mužstev by však neměli růst pouze hráči pro první mužstvo, ale i pro další oddíly Vysočiny. V neposlední řadě by se talentovaní hráči, kteří by svými fotbalovými schopnosti přerostli rámec regionu, mohli stát i důležitým zdrojem příjmů klubu.</div><div><br></div><div>Za účelem jasného oddělení mládežnického a profesionálního fotbalu zahájilo od 1. července 2011 svoji oficiální činnost občanské sdružení FKM Vysočina Jihlava. To v úzké spolupráci s FC VYSOČINA JIHLAVA, a.s., zastřešuje činnost 15 chlapeckých mládežnických mužstev klubu ve všech věkových kategoriích. Na základní škole Evžena Rošického řadu let stabilně fungují sportovních třídy. Na ty navazuje fotbalovým svazem podporované Sportovní centrum mládeže a též v roce 2010 založená Fotbalová akademie FC Vysočina při jihlavské Střední škole obchodu a služeb. Takto klub zajišťuje i hráčům dorostenecké kategorie kvalitní tréninkové podmínky. Celkově tato fotbalová továrna obnáší takřka 350 chlapců, 30 trenérů a dalších 20 klubových zaměstnanců.</div><div><br></div><div>Velký důraz se v FC Vysočina klade na obecný rozvoj osobnosti hráče, neboť pouze třetina složitého rozvoje mladého fotbalisty se odehrává na hřišti. Přeměna talentu ve výborného hráče se z větší části odehrává mimo něj. Podstatnou roli hraje zázemí v rodině a optimální spolupráce rodičů, trenéra a učitelů ve školách. I proto studijní výsledky hráčů hlídají také trenéři jednotlivých družstev a o účasti na dopolední přípravě rozhodují třídní učitelé hráčů. Důslednost, pečlivost a trpělivost ve škole i na hřišti, spolu s přiměřenou skromností jsou vlastnosti, bez kterých nelze dosáhnout opakovaných vítězství.</div><div><br></div><div>Dlouholetá práce s mládeží nese v FC Vysočina kýžené ovoce. Vedle působení ve špičkových soutěžích to jsou nominace nejlepších hráčů klubu do dorosteneckých a juniorských reprezentací Česka. Vedení klubu je přesvědčeno, že v otázce výchovy fotbalistů kráčí po správné cestě, na jejímž konci bude stabilní působení dospělého týmu FC Vysočina prošpikovaného vlastními odchovanci v nejvyšší soutěži</div>',
	to_timestamp('2015-06-03 20:01:50.709', 'YYYY-MM-DD HH24:MI:SS.MS'),
	2,
	2,
	null
	),

	(
	1,
	true,
	'U19: Utkání s Bohemians 1905 nezvládli [aktuality | pouze tým | důležité]',
	'Starší dorost U19 do utkání proti Bohemians 1905 nevstoupil aktivně, soupeř Vysočinu přehrával a v první půli se zaslouženě ujal vedení 1:0. Ve druhé půli Bohemians svoje vedení o dva góly navýšili a o výsledku tak bylo rozhodnuto. Utkání tak skončilo 3:0 (1:0).',
	'"Domácí byli důraznější, agresivnější s větší touhou po zisku bodů, v poločase měli zasloužený jednobrankový náskok. Druhá část začala naší aktivitou, bohužel přišla hrubá chyba a soupeř navýšil vedení, poté šel sám Kolčava, neproměnil a tím byl dán ráz utkání, aby to nebylo málo, tak v závěru zápasu udělala naše defenzíva další fatální chybu uvnitř vápna a bylo rozhodnuto, Šerý M. mohl stav korigovat, ale dal těsně nad břevno. Utkání mnoho fotbalovosti nepřineslo, spíše se nakopávalo, ale to nic nemění na zaslouženém vítězství domácího celku. V sobotu nás čeká závěrečné 42. kolo soutěže, v domácím prostředí přivítáme Pardubice, tak věřím, že se rozloučíme kvalitnějším výkonem i výsledkem." řekl po utkání trenér Josef Vrzáček.',
	to_timestamp('2015-04-28 14:51:50.769', 'YYYY-MM-DD HH24:MI:SS.MS'),
	2,
	2,
	null
	),

	-- aktuality | celý web
	(
	1,
	true,
	'Kopeme za fotbal: Fotbalisté Břidličné se potili pod vedením trenérů FC Vysočina [aktuality | celý web | důležitý]',
	'Hrajete fotbal? Pokud zní vaše odpověď ano, ale zároveň nejste profesionál, možná znáte projekt Kopeme za fotbal. Ten sbližuje amatéry s profíky. Jednou z výher v projektu je trénink s prvoligovými trenéry. A právě takou tréninkovou jednotku absolvovali hráči Břidličné. Tentokrát za nimi přijeli kouči FC Vysočina Jihlava.',
	'<span style="font-family: Arial, Helvetica, sans-serif; font-size: 14.5199995040894px; font-weight: bold; line-height: 20.3279991149902px; background-color: rgb(255, 255, 255);">Hrajete fotbal? Pokud zní vaše odpověď ano, ale zároveň nejste profesionál, možná znáte projekt Kopeme za fotbal. Ten sbližuje amatéry s profíky. Jednou z výher v projektu je trénink s prvoligovými trenéry. A právě takou tréninkovou jednotku absolvovali hráči Břidličné. Tentokrát za nimi přijeli kouči FC Vysočina Jihlava.</span><div><span style="font-family: Arial, Helvetica, sans-serif; font-size: 14.5199995040894px; font-weight: bold; line-height: 20.3279991149902px; background-color: rgb(255, 255, 255);"><br></span></div><div><p style="margin: 10px 0px; padding: 5px 0px; font-family: Arial, Helvetica, sans-serif; font-size: 14.5199995040894px; line-height: 21.7799987792969px;">A blízko to zrovna neměli. Břidličná je obec ležící v okrese Bruntál, jihlavští trenéři tak museli urazit více jak 200 km. Na jejich výkonu to však znát nebylo. Hned po úvodním pozdravení bez diskuzí velí k rozcvičce.<br></p><p style="margin: 10px 0px; padding: 5px 0px; font-family: Arial, Helvetica, sans-serif; font-size: 14.5199995040894px; line-height: 21.7799987792969px;">Už během zahřívací části je vidět, že dnešní trénink se bude svým způsobem vymykat. Mezi paneláky, kde se nachází místní fotbalové hřiště, se prohání mnohem více hráčů, než je obvyklé. Oblečeni jsou navíc do sněhově bílých dresů Kopeme za fotbal. A cvičení, které po nich jejich adoptivní trenér vyžaduje, taktéž nejsou pro mnohé z nich denním chlebem. A nutno říci, že na kvalitě provedení je to občas vidět. Bago, střelba, nácvik herních situací. Ze všech hráčů dobrá nálada přímo vyzařuje.<br></p><p style="margin: 10px 0px; padding: 5px 0px; font-family: Arial, Helvetica, sans-serif; font-size: 14.5199995040894px; line-height: 21.7799987792969px;">Po skončení tréninku je na některých hráčích patrná únava. Při loučení se však všichni smějí. „Dnešní trénink měl být hlavně zábavný, aby to kluky bavilo. To je, myslím si, na této úrovni to hlavní,“ přibližuje trénink kouč Jihlavy a dnešní trenér Břidličné Luděk Klusáček. „I my jsme měli radost, když jsme viděli, jak to kluky baví,“ doplňuje.<br></p><p style="margin: 10px 0px; padding: 5px 0px; font-family: Arial, Helvetica, sans-serif; font-size: 14.5199995040894px; line-height: 21.7799987792969px;">Jeho dojmy z tréninku jsou podobné těm, jaké měl trenér Břidličné Jan Jedlička. „Dnešní trénink byl moc pěkný, zábavný. Hlavně se mi líbilo, že tím, že to bylo medializované, tak přišlo strašně moc hráčů, co normálně nechodí,“ směje se. Jeho názory sdílí i hráči. „Projekt bych určitě doporučil. Je to přínos pro mančaft a zkušenost pro mladé hráče,“ říká opora Břidličné Patrik Chmelař. „Myslím, že kdyby takové tréninky trvaly alespoň měsíc, tak bychom šli výkonnostně nahoru,“ je přesvědčen o přínosu projektu obránce Zdeněk Urbánek.<br></p><p style="margin: 10px 0px; padding: 5px 0px; font-family: Arial, Helvetica, sans-serif; font-size: 14.5199995040894px; line-height: 21.7799987792969px;">Projekt společnosti Gambrinus, Kopeme za fotbal, odstartoval v roce 2010. Kromě materiální podpory pro týmy od nejnižší soutěže po okresní přebor dává klubům možnost soutěžit o exkluzivní okamžiky s celky Synot ligy. Kromě setkání s profesionály mohou také vyhrát trénink s koučem z první ligy a mnoho dalších zajímavých cen. V současnosti je do projektu zapojeno téměř 200 týmů ve 33 regionech.</p></div>',
	current_timestamp,
	3,
	null,
	null
	),

	(
	1,
	false,
	'Poslední kolo v duchu vykutálené zábavy! [aktuality | celý web]',
	'V sobotu 23. května odehraje FC Vysočina poslední domácí duel tohoto ročníku SYNOT ligy proti Slovanu Liberec. V rámci poločasové přestávky tohoto utkání si díky projektu Crazy Bubbles budou moci vybraní šťastlivci vyzkoušet, jaké to je hrát na prvoligovém trávníku FC Vysočina. Jak tuto možnost získat?',
	'<span style="font-family: Arial, Helvetica, sans-serif; font-size: 14.5199995040894px; font-weight: bold; line-height: 20.3279991149902px; background-color: rgb(255, 255, 255);">V sobotu 23. května odehraje FC Vysočina poslední domácí duel tohoto ročníku SYNOT ligy proti Slovanu Liberec. V rámci poločasové přestávky tohoto utkání si díky projektu Crazy Bubbles budou moci vybraní šťastlivci vyzkoušet, jaké to je hrát na prvoligovém trávníku FC Vysočina. Jak tuto možnost získat?</span><div><span style="font-family: Arial, Helvetica, sans-serif; font-size: 14.5199995040894px; font-weight: bold; line-height: 20.3279991149902px; background-color: rgb(255, 255, 255);"><br></span></div><div><p style="margin: 10px 0px; padding: 5px 0px; font-family: Arial, Helvetica, sans-serif; font-size: 14.5199995040894px; line-height: 21.7799987792969px;">Zúčastni se soutěže, najdi informace, odpověz na otázku a HRAJ! Bubble Football je zábavná forma sportu kombinující prvky fotbalové techniky a fyzikálních zákonů. V sobotu 23. května od rána bude v této disciplíně probíhat kvalifikační turnaj na hřišti s umělým travnatým povrchem v ulici Evžena Rošického, kterého se může zúčastnit úplně každý. Registrovat svůj tým můžeš&nbsp;<span style="font-weight: bold;"><a href="http://www.mistrovstvibublin.cz/objednavka3/" style="color: rgb(0, 0, 0);">ZDE</a></span>. Vítěz turnaje navíc postoupí do republikového finále!<br></p><p style="margin: 10px 0px; padding: 5px 0px; font-family: Arial, Helvetica, sans-serif; font-size: 14.5199995040894px; line-height: 21.7799987792969px;">V rámci poločasového programu proběhne ukázkové utkání tohoto sportu, kterého můžeš být součástí. Staň se nejvykutálenějším hráčem kola! Více informací o turnaji a celém projektu najdeš na Facebooku&nbsp;<a href="https://www.facebook.com/crazybubblescz?fref=ts" style="color: rgb(0, 0, 0);">Crazy Bubbles CZ</a>&nbsp;a na webu&nbsp;<a href="http://www.mistrovstvibublin.cz/" style="color: rgb(0, 0, 0);">www.mistrovstvibublin.cz</a>.</p></div>',
	to_timestamp('2015-08-19 18:59:10.009', 'YYYY-MM-DD HH24:MI:SS.MS'),
	3,
	null,
	null
	),

	(
	1,
	false,
	'Plzeň chceme zaskočit a oddálit její oslavy titulu [aktuality | celý web]',
	'Od nezapomenutelného vítězství FC Vysočina nad Plzní uběhl půlrok a 13 utkání s SYNOT lize. Jihlavský tým je dávno zachráněný mezi elitou a Viktoria si stále drží pozici lídra. Před týdnem dokonce zvítězila na Letné, tudíž jí tři kola před koncem zbývá k jistotě titulu českého mistra jediné vítězství. Dokáže Vysočina v pondělním přímém přenosu ČT čelit výborné formě Plzně a vyprodanému stadiónu? Oddálí mistrovské oslavy?',
	'"<span style="font-family: Arial, Helvetica, sans-serif; font-size: 14.5199995040894px; font-weight: bold; line-height: 20.3279991149902px; background-color: rgb(255, 255, 255);">Od nezapomenutelného vítězství FC Vysočina nad Plzní uběhl půlrok a 13 utkání s SYNOT lize. Jihlavský tým je dávno zachráněný mezi elitou a Viktoria si stále drží pozici lídra. Před týdnem dokonce zvítězila na Letné, tudíž jí tři kola před koncem zbývá k jistotě titulu českého mistra jediné vítězství. Dokáže Vysočina v pondělním přímém přenosu ČT čelit výborné formě Plzně a vyprodanému stadiónu? Oddálí mistrovské oslavy?</span><div><span style="font-family: Arial, Helvetica, sans-serif; font-size: 14.5199995040894px; font-weight: bold; line-height: 20.3279991149902px; background-color: rgb(255, 255, 255);"><br></span></div><div><p style="margin: 10px 0px; padding: 5px 0px; font-family: Arial, Helvetica, sans-serif; font-size: 14.5199995040894px; line-height: 21.7799987792969px;">Domácí budou v utkání jasným favoritem, ovšem jejich bilance překvapivě není tak příznivá. V historii vzájemných duelů v I. lize, kterou představují pouze 4 sezóny, byl jihlavský tým ve Štruncových sadech vždy nepříjemným soupeřem. Odvezl si odtud jednu těsnou jednobrankovou porážku a dvě remízy. Před rokem se v Doosan aréně zrodila výsledek 1:1, když skórovali Ďuriš a Mešanović. Jak vidí šance FC Vysočina v pondělním duelu asistent trenéra&nbsp;<span style="font-weight: bold;">Roman Kučera</span>? „Přáli bychom si, abychom dokázali navázat na náš podzimní výkon proti Viktorii. Je jasné, že je před obrovsky těžký úkol, neboť se postavíme obrovskému favoritovi, který na hřišti udělá vše pro to, aby po utkání s námi v předstihu oslavil mistrovský titul. Vše bude nepochybně nachystáno na velkou oslavu. My ale v utkání nemáme co ztratit. Hodláme pozorně bránit, využívat každé příležitosti k brejkům a tím narušovat hru domácího týmu. Pokud se nám podaří jít do vedení nebo dokážeme dlouho udržet nerozhodný stav, tak mohou domácí znervóznět. Duel na vyprodaném stadiónu bude pro naše hráče představovat velký svátek a odměnu. Věříme, že budou schopní podat takový výkon, kdy Plzeň potrápí, zaskočí a oddálí jejich oslavy titulu,“ věří v sílu týmu asistent kouče Klusáčka.<br></p><p style="margin: 10px 0px; padding: 5px 0px; font-family: Arial, Helvetica, sans-serif; font-size: 14.5199995040894px; line-height: 21.7799987792969px;">Můžete zavzpomínat na strhující listopadový duel proti Plzni, v němž jste Vysočinu vedl jako hlavní trenér? „Rád si vybavuji fantastickou atmosféru na stadiónu a na jedinečné propojení hráčů a diváků. Nepochybně šlo o jeden z největších fotbalových zážitků, jakého jsme kdy byli v našem kraji svědky. Musí nás těšit, že jsme jako jedni z mála dokázali Plzeň v tomto ročníku ligy porazit,“ připomněl Kučera.<br></p><p style="margin: 10px 0px; padding: 5px 0px; font-family: Arial, Helvetica, sans-serif; font-size: 14.5199995040894px; line-height: 21.7799987792969px;">Fotbalové kvality Plzně jsou obecně dobře známé. Přesto, zkuste upozornit na největší přednosti hry Viktorie. „Za vše hovoří pohled do ligové tabulky či domácí bilance Plzně – 12 vítězství a jedna prohra proti Mladé Boleslavi. Viktoria se prezentuje ofenzivním nátlakovým fotbalem, zvládá hru v přehuštěném prostoru a zahrává velmi nebezpečné standardní situace. Její hráči jsou zkušení, technicky vyspělí a takticky na výši. Díky sebevědomí a trpělivosti jsou schopní zvrátit i nepříznivě se vyvíjející duely. Velký pozor si musíme dát na její křídelní hru, kde hrozí dvojice Pilař - Rajtoral a Limberský – Kovařík. Jejich akce jsou schopní zakončit nejen Mahmutović, Holenda, Kolář či Tecl. Malou výhodou pro nás může být absence vykartovaných Vaňka a Hořavy, ovšem ani my nebudeme kompletní,“ analyzoval plzeňskou hru Roman Kučera.<br></p><p style="margin: 10px 0px; padding: 5px 0px; font-family: Arial, Helvetica, sans-serif; font-size: 14.5199995040894px; line-height: 21.7799987792969px;">Tento týden je hodně atypický z pohledu dlouhého tréninkového cyklu. Jak čekání na pondělní duel vyplňujete? „Aktuální tréninkový mezicyklus je delší, ovšem svojí náplní se neliší od předchozích. Zařazujeme hodně herních prvků a snažíme se o tréninky zábavnou formou, aby hráči trénovali s chutí a psychicky se zvedli po dvou výsledkově nepodařených zápasech a smolných prohrách,“ řekl jihlavský asistent trenéra.<br></p><p style="margin: 10px 0px; padding: 5px 0px; font-family: Arial, Helvetica, sans-serif; font-size: 14.5199995040894px; line-height: 21.7799987792969px;">Proti Teplicím jste výrazně pozměnili sestavu, přičemž jste nasadili zálohu, v níž figurovali hned čtyři jednadvacetiletí hráči. Nakolik se jihlavská jedenáctka bude v pondělí lišit? „Nadále nemůžeme počítat se zraněnými Jungrem, Šourkem a Marcinem, navíc osmou žlutou kartu inkasoval Kučera, který nám tak bude chybět ve dvou nadcházejících zápasech. Šanci tak znovu dostanou mladí hráči. Sestava oproti utkání s Teplicemi dozná změn, ale nebudou nijak výrazné. Jasno o složení úvodní jedenáctky budeme mít asi až v sobotu,“ dodal Kučera.</p></div>"',
	to_timestamp('2015-04-04 23:11:17.519', 'YYYY-MM-DD HH24:MI:SS.MS'),
	3,
	null,
	null
	);

-- ARTICLES [AKTUALITY - PLATNOST DO]
insert into T_ARTICLE (location, priority, caption, summary, content, creation_date, expiration_date, owner_type, club_team_id, category_id) values
	-- aktuality | pouze kategorie
	(
	1,
	true,
	'Jaké změny v létě čeká kádr FC Vysočina? [aktuality | celý web | důležité | platnost do 2015-06-17]',
	'Před fotbalisty FC Vysočina je poslední duel v tomto ročníku SYNOT ligy a též dlouho očekávaná dovolená. Už v pondělí 26. června se však budou znovu hlásit v tréninkovém procesu, neboť měsíc poté startuje nový ročník SYNOT ligy 2015/16. Jakými změnami jihlavský kádr projde?',
	'<p style="margin: 10px 0px; padding: 5px 0px; font-family: Arial, Helvetica, sans-serif; font-size: 14.5199995040894px; line-height: 21.7799987792969px;">„S ohledem na fakt, že k 30. červnu končí hostování sparťanských hráčů&nbsp;<span style="font-weight: bold;">Matěje Hybše</span>&nbsp;a<span style="font-weight: bold;">Adama Jánoše</span>, tak se v letním období budeme soustředit na vyřešení postů levého obránce a defenzivního záložníka. Jistě budeme chtít na toto téma jednat i se Spartou, ovšem na pořadu dne to bude až po skončení EURA U21,“ uvedl sportovní manažer Josef Jinoch, jenž zároveň potvrdil přestup dvojice kvalitních prvoligových fotbalistů: „Již dříve jsme avizovali, že od července se změní hostování&nbsp;<span style="font-weight: bold;">Petra Nerada</span>&nbsp;z Bohemians 1905 v přestup. Profesionální kontrakt jsme podepsali též s útočníkem&nbsp;<span style="font-weight: bold;">Pavlem Dvořákem</span>&nbsp;(roč. 1989) z Hradce Králové, jenž v tomto ročníku SYNOT ligy skóroval hned devětkrát a my věříme, že se mu bude dařit i v barvách FC Vysočina. Oba přestupy nyní zbývá administrativně doladit.“</p><p style="margin: 10px 0px; padding: 5px 0px; font-family: Arial, Helvetica, sans-serif; font-size: 14.5199995040894px; line-height: 21.7799987792969px;">Kádr s velkou pravděpodobností opustí zmínění Jánoš a Hybš. Lze očekávat další odchody? „Nelze je vyloučit. Určitá jednání v současné době probíhají, navíc některým hráčům končí smlouvy. Pokud nějaké odchody nastanou, tak jsme připravení na ně adekvátně zareagovat,“ informoval Jinoch, jenž počítá s návratem hráčů z hostování a s přesunem mladíků z juniorky: „V přípravě hodláme otestovat ghanského pravého beka<span style="font-weight: bold;">Daniela Adda</span>, který stihl nastoupit do posledních dvou duelů juniorky. Do „áčka“ minimálně pro letní blok posuneme stopera&nbsp;<span style="font-weight: bold;">Dominika Pedra</span>&nbsp;(roč. 1995), levého beka<span style="font-weight: bold;">Petra Chylu&nbsp;</span>(roč. 1994), záložníka či krajního obránce&nbsp;<span style="font-weight: bold;">Davida Klusáka</span>&nbsp;(roč. 1994) a útočníka&nbsp;<span style="font-weight: bold;">Tomáše Dubu</span>&nbsp;(roč. 1996). V konfrontaci s dospělým fotbalem chceme vidět i dorosteneckého reprezentanta, útočníka&nbsp;<span style="font-weight: bold;">Jiřího Klímu</span>&nbsp;(roč. 1997). Z druholigových hostování se vrátí ze Sokolova útočník&nbsp;<span style="font-weight: bold;">Vojtěch Přeučil</span>&nbsp;(roč. 1990) a z Kolína útočník<span style="font-weight: bold;">Jakub Teplý</span>&nbsp;(roč. 1993) a gólman&nbsp;<span style="font-weight: bold;">Luděk Vejmola</span>&nbsp;(roč. 1994), jehož tréninkovou přípravu nyní omezuje tříselná kýla,“ uzavřel svůj komentář sportovní manažer FC Vysočina.</p>',
	to_timestamp('2015-05-17 11:22:50.656', 'YYYY-MM-DD HH24:MI:SS.MS'),
	to_date('2015-06-17', 'YYYY-MM-DD'),
	3,
	null,
	null
	),

	(
	1,
	false,
	'Jaromír Blažek setrvá v FC Vysočina jako trenér brankářů [aktuality | celý web | platnost do 2016-07-02]',
	'Jihlavská fotbalová etapa legendárního gólmana Jaromíra Blažka nekončí! Se sportovním vedením FC Vysočina se dohodl na pokračování svého působení v roli trenéra brankářů. Navíc v příštím ročníku hodlá svoji činnost v klubu rozšířit.',
	'<p style="margin: 10px 0px; padding: 5px 0px; font-family: Arial, Helvetica, sans-serif; font-size: 14.5199995040894px; line-height: 21.7799987792969px;">Jaromír Blažek na Vysočinu přišel v lednu 2012, aby pomohl vytouženému postupu do nejvyšší soutěže. Nezůstalo však jen u této mety. Sparťanská ikona jihlavskou branku hájila společně s Janem Hanušem během tří úspěšných prvoligových ročníků, kdy si Blažek připsal 49 startů v dresu FC Vysočina. V počtu vychytaných nul se mezi českými a československými gólmany posunul na druhou příčku za nedostižného Petra Čecha a navíc navýšil rekord Pepiho Bicana, když se stal nejstarším fotbalistou v nejvyšší soutěži. Vedle hráčské role Blažek od léta 2013 plnil roli trenéra gólmanů jihlavského „áčka“, v níž hodlá setrvat i po skončení dlouholeté profesionální kariéry.<br></p><p style="margin: 10px 0px; padding: 5px 0px; font-family: Arial, Helvetica, sans-serif; font-size: 14.5199995040894px; line-height: 21.7799987792969px;">„S vedením klubu jsme domluveni na pokračování spolupráce. Těším se, že navážu na dosavadní práci s gólmany ligového kádru FC Vysočina a budu tak moci prohlubovat svoji trenérskou praxi. Vedle toho budu pomáhat trenérovi Tomáši Jansovi, který má v klubu na starosti přípravu a rozvoj brankářů v žákovském a dorosteneckém věku,“ uvedl Jaromír Blažek, který hodlá pokračovat ve svém trenérském studiu. Poté, co v zimě dokončil studium licence B, se aktuálně přihlásil ke studiu trenérské A licence</p>',
	to_timestamp('2015-06-17 11:22:50.656', 'YYYY-MM-DD HH24:MI:SS.MS'),
	to_date('2016-07-02', 'YYYY-MM-DD'),
	3,
	null,
	null
	),



	(
	1,
	false,
	'Proběhne nábor do týmů přípravek FC Vysočina [aktuality | celý web | platnost do 2015-05-31]',
	'Hrej nejpopulárnější sport na světě a staň se jedním z nás! Přijď na tradiční nábor do týmů přípravek FC Vysočina, který se koná ve čtvrtek 21. května od 17:00 hodin na travnaté ploše stadiónu v Jiráskově ulici. Nábor je otevřen pro kluky ročníků 2009 a 2010. Každý účastník na památku obdrží tričko FC Vysočina a též vstupenku na sobotní duel proti Liberci!',
	'<p style="margin: 10px 0px; padding: 5px 0px; font-family: Arial, Helvetica, sans-serif; font-size: 14.5199995040894px; line-height: 21.7799987792969px;">Adepti dresu FC Vysočina by měli dorazit v doprovodu rodičů a měli by sebou mít sportovní oblečení, kopačky (tenisky), míč velikost č.3 nebo č.4 (ideální je č.3) a láhev s pitím.</p><p style="margin: 10px 0px; padding: 5px 0px; font-family: Arial, Helvetica, sans-serif; font-size: 14.5199995040894px; line-height: 21.7799987792969px;">„Nábor rozhodně nebude nějakou náročnou výběrovou akcí. Kluci a holky si pod vedením našich trenérů mládeže vyzkouší několik jednoduchých cvičení a užijí si pohybových aktivit. Šanci vyzkoušet si v květnu a červnu tréninky v FC Vysočina poté dostanou všichni příchozí zájemci. Tak neváhejte a přijďte," zdůraznil Miroslav Fuks, tiskový mluvčí klubu.</p>',
	to_timestamp('2015-05-01 11:22:50.656', 'YYYY-MM-DD HH24:MI:SS.MS'),
	to_date('2015-05-31', 'YYYY-MM-DD'),
	2,
	2,
	null
	),

	(
	1,
	true,
	'FAČR a KFS Vysočina podporují nábory mladých fotbalistů [aktuality | celý web | důležité | platnost do 2017-05-20]',
	'V měsíci květnu a září 2015 proběhne po celé České republice další "Měsíc náborů" mladých fotbalistů a fotbalistek ve spolupráci s projektem Můj první gól, Fotbalovou asociací České republiky, Českou televizí, kluby Synot ligy, Fotbalové Národní ligy a krajskými a okresními fotbalovými svazy. Rodiče nyní mají jedinečnou šanci přihlásit své dítě do jakéhokoli fotbalového klubu ve svém okolí a zaregistrovat ho jako člena FAČR. Dnešní tisková konference představila podobu projektu v Kraji Vysočina.',
	'<p style="margin: 10px 0px; padding: 5px 0px; font-family: Arial, Helvetica, sans-serif; font-size: 14.5199995040894px; line-height: 21.7799987792969px;">Krajský fotbalový svaz Kraje Vysočina ji uspořádal ve VIP zázemí stadiónu FC Vysočina, přičemž před přítomnými zástupci médií promluvili a projekt blíže představili&nbsp;<span style="font-weight: bold;">Miroslav Vrzáček</span>, předseda KFS Vysočina,&nbsp;<span style="font-weight: bold;">Stanislav Duben</span>, profesionální trenér mládeže KFS Vysočina a&nbsp;<span style="font-weight: bold;">Tomáš Kučera</span>, regionální patron projektu a opora prvoligového FC Vysočina. Pozvání přijali též&nbsp;<span style="font-weight: bold;">Milan Kastner</span>, vedoucí oddělení mládeže a sportu Kraje Vysočina, a&nbsp;<span style="font-weight: bold;">Tomáš Koukal</span>, vedoucí odboru školství, mládeže a tělovýchovy Statutárního města Jihlavy. V závěru vystoupil ředitel FC Vysočina&nbsp;<span style="font-weight: bold;">Zdeněk Tulis</span>, jenž média informoval o stavu příprav na vznik krajské fotbalové akademie.<br></p><h3 style="color: rgb(50, 51, 82); margin: 8px 0px 4px; font-size: 1.2em; font-family: Arial; padding: 6px 0px 7px; line-height: 1.3em; border-bottom-width: 1px; border-bottom-style: solid; border-bottom-color: rgb(229, 229, 241);">Projekty „Měsíc náborů“ a „Můj první gól“<br></h3><p style="margin: 10px 0px; padding: 5px 0px; font-family: Arial, Helvetica, sans-serif; font-size: 14.5199995040894px; line-height: 21.7799987792969px;">Projekt „Měsíc náborů“ se pod záštitou Fotbalové asociace České republiky ve fotbalovém prostředí objevuje již poněkolikáté a i díky němu získá členství ve FAČR tisíce nových členů, mladých fotbalistů a fotbalistek. V září roku 2014 proběhla tato akce v 74 klubech po celé republice. Účastnilo se jí 11.300 dětí. Jenom v Kraji Vysočina proběhlo 5 akcí, do kterých se zapojilo skoro 900 dětí. Celkový počet nových fotbalistů (od 5 do 19 let) v době kampaně (září až říjen 2014) činil 9.100.<br></p><p style="margin: 10px 0px; padding: 5px 0px; font-family: Arial, Helvetica, sans-serif; font-size: 14.5199995040894px; line-height: 21.7799987792969px;">V roce 2015 proběhnou akce v měsíci květnu a v září, na něž FAČR přispěje částkou ve výši 4 milionů korun. V každém okrese se uskuteční jeden „Měsíc náborů“ ve vybraném klubu, přičemž konkrétně v Kraji Vysočina půjde o pět akcí. Na Jihlavsku se uskuteční pod patronací FC Vysočina Jihlava (25.5.), na Pelhřimovsku jí bude pořádat FK Pelhřimov (14.5), na Žďársku místní FC Žďas Žďár nad Sázavou (19.5.), na Havlíčkobrodsku se konání chopí FC Slovan Havlíčkův Brod (28.5.) a na Třebíčsku bude garantem náboru HFK Třebíč (15.5.). V měsíci září bude vybráno dalších pět regionálních oddílů vždy po jednom z každého okresu. Celkem tedy v Kraji Vysočina proběhne 10 náborových akcí pod záštitou FAČR.<br></p><p style="margin: 10px 0px; padding: 5px 0px; font-family: Arial, Helvetica, sans-serif; font-size: 14.5199995040894px; line-height: 21.7799987792969px;">Díky projektu „Měsíc náborů“ opět získáte skvělou příležitost přihlásit své dítě do jakéhokoli fotbalového klubu ve vašem okolí a zaregistrovat ho jako člena Fotbalové asociace České republiky. Stačí si vybrat pro vás nejvhodnější klub, přivést malého fotbalistu nebo malou fotbalistku na nábor nových dětí a dát jim tím možnost začít oficiálně hrát tento nejrozšířenější, nejpopulárnější a pohybově nejvšestrannější sport na světě. Kluby vám poté vyřídí členství FAČR, díky kterému získává každý člen i mnoho zajímavých možností a výhod.<br></p><p style="margin: 10px 0px; padding: 5px 0px; font-family: Arial, Helvetica, sans-serif; font-size: 14.5199995040894px; line-height: 21.7799987792969px;">Nedílnou součástí projektu je též aktivita „<span style="font-weight: bold;">Můj první gól</span>“, díky kterému měli zaregistrovaní hráči ročníku 2007 možnost požádat o fotbalový balíček (taška na fotbal, lahev, míč, reprezentační dres, podpisové kartičky). Těchto balíčků bylo v roce 2014 distribuováno 5.675 kusů. Stejnou možnost mají malí fotbalisté ročníku 2007 i letos na jaře. Na podzim 2015 se balíček „Můj první gól“ bude týkat výhradně hráčů narozených v roce 2008.</p>',
	to_timestamp('2015-05-20 10:29:50.988', 'YYYY-MM-DD HH24:MI:SS.MS'),
	to_date('2017-05-20', 'YYYY-MM-DD'),
	2,
	2,
	null
	);

-- ARTICLES [NÁSTĚNKA]
insert into T_ARTICLE (location, priority, caption, summary, content, creation_date, owner_type, club_team_id, category_id) values
	-- nástěnka | pouze kategorie
	(
	0,
	true,
	'Zrušení tréninku [nástěnka | pouze kategorie | důležité]',
	'Dle hlášeného minimálního počtu hráčů na páteční trénink, trénink v pátek 15.5. nebude. Hráči, kteří mají čas mohou individuálně trénovat fyzičku nebo se mohou zúčastnit na tréninku v Moravanech na hřišti od 17:00 s družstvem přípravky.',
	'',
	to_timestamp('2014-05-13 11:22:50.656', 'YYYY-MM-DD HH24:MI:SS.MS'),
	2,
	2,
	null
	),

	(
	0,
	false,
	'Akce pro školáky [nástěnka | pouze kategorie]',
	'Ke dni dětí jsme pro vás připravili opékání u táboráku za hřištěm. Pokud přijdete, kontaktujte pana Malzu (ať máme dost špekáčků :-) ).',
	'',
	to_timestamp('2015-05-22 19:03:10.600', 'YYYY-MM-DD HH24:MI:SS.MS'),
	2,
	2,
	null
	),

	(
	0,
	false,
	'U hřiště byly usazeny nové lavičky [nástěnka | celý web]',
	'Připravil do startu jarní části sezony 2014/2015 malou novinku. Vedle střídačky domácích nechal zabudovat čtyři lavičky, o jejichž zhotovení i zabudování se postaral člen výboru Franta Koudelka.',
	'<div><div><font face="Tahoma, Verdana, Arial, sans-serif"><span style="font-size: 12.8000001907349px; line-height: 17.9200000762939px;">Věříme, že to bude vítaná novinka a třeba už tuto sobotu 28. 3. 2015 se lavičkám dostane první ostré zatěžkávací zkoušky. Od 10.15 se totiž MUŽI B utkají s Miroticemi. A už příští víkend bude pořádně zatěžkána i hrací plocha.</span></font></div></div>',
	to_timestamp('2015-02-11 03:29:19.002', 'YYYY-MM-DD HH24:MI:SS.MS'),
	3,
	null,
	null
	),

	(
	0,
	false,
	'Vánoční besídka [nástěnka | celý web]',
	'Hráčská schůze s novým výborem pojatá jako vánoční besídka bude v SO 15.12.12 od 18h v HK v Plzeňské restauraci (bývalá Mikulovská vinárna, naproti jsou Novákovy garáže pro orientaci). Zváni jsou i další příznivci, kteří mají ke klubu vztah ač již nejsou aktivními hráči či funkcionáři, ale bez nichž by oddíl nebyl vůbec v krajských soutěžích.',
	'',
	to_timestamp('2014-11-30 22:03:17.234', 'YYYY-MM-DD HH24:MI:SS.MS'),
	3,
	null,
	null
	),

	(
	0,
	true,
	'Akce Z [nástěnka | celý web | důležité]',
	'Prosíme jak všechny členy, tak i podporovatele klubu, aby přišly dne 28.4. pomoci se zvelebením našeho hřiště. Pracovní pomůcky (rukavice, hrábě, pytle) budou zajištěny',
	'Včasným příchodem a plným nasazením zasadíme drtivý úder americkým imperialistům.',
	to_timestamp('2014-01-12 21:43:01.099', 'YYYY-MM-DD HH24:MI:SS.MS'),
	3,
	null,
	null
	);

-- ARTICLES [NÁSTĚNKA - PLATNOST DO]
insert into T_ARTICLE (location, priority, caption, summary, content, creation_date, expiration_date, owner_type, club_team_id, category_id) values
	-- nástěnka | pouze kategorie
	(
	0,
	true,
	'Zrušený trénink [nástěnka | pouze kategorie | důležité | platnost do ]',
	'Dne 13.3. odpadá tréning mládeže z důvodu nemoci trenéra.',
	'',
	to_timestamp('2015-03-11 19:22:50.656', 'YYYY-MM-DD HH24:MI:SS.MS'),
	to_date('2015-03-17', 'YYYY-MM-DD'),
	2,
	2,
	null
	),

	(
	0,
	true,
	'Příspěvky na charitu [nástěnka | celý web | důležité | platnost do 2017-05-17]',
	'Prosíme o příspěvky na charitu. Bližší informace podá František Koudelka na telefonu 777 258 456.',
	'',
	to_timestamp('2015-05-11 19:22:50.656', 'YYYY-MM-DD HH24:MI:SS.MS'),
	to_date('2017-05-17', 'YYYY-MM-DD'),
	3,
	null,
	null
	);

-- CLUB RIVAL
insert into T_CLUB_RIVAL (name, web, gps, street, city, code, icon) values
	('Viktoria Plzeň', 'http://www.fcviktoria.cz/', '49°45''0.063"N, 13°23''7.541"E', 'Štruncovy sady', 'Plzeň', '3', null),
	('Zbrojovka Brno', null, null, null, 'Brno', null, null),
	('Sparta Praha', 'http://www.sparta.cz/', null, 'Milady Horákové', 'Praha', '1066/98', null),
	('Vysočina Jihlava', 'http://www.fcvysocina.cz/', null, 'Jiráskova', 'Jihlava', '2603/69', null),
	('Baumit Jablonec', 'http://www.fkjablonec.cz/', null, null, 'Jablonec', null, null),
	('1. FK Mladá Boleslav', null, null, null, null, null, null),
	('Dukla Praha', null, null, null, null, null, null),
	('Slavia Praha', null, null, null, null, null, null),
	('FC Hradec Králové', 'http://www.fchk.cz/', null, 'Hradec Králové', 'Úprkova', '473', null),
	('Slovan Liberec', null, null, null, 'Liberec', null, null);


-- CREATE FUNCTION FOR IMPORTING PICTURES
create or replace function bytea_import(photo_path text, photo_result out bytea) language plpgsql  as $$
declare
	l_oid oid;
	r record;
begin
	photo_result := '';
	select lo_import(photo_path) into l_oid;
	for r in (select data from pg_largeobject where loid = l_oid order by pageno ) loop
		photo_result = photo_result || r.data;
	end loop;
	perform lo_unlink(l_oid);
end;
$$;

-- INSERT LOGO TO THE CLUB SETTING
do $$
begin
	update T_CLUB_SETTING set logo = (select bytea_import('clubeek_pictures/club_logo.png')) where id = 1;
end;
$$;

-- INSERT PHOTOS (MAN, WOMAN) TO SOME OF THE CLUB MEMBERS
do $$
declare
	row record;
begin
	for row in select * from t_club_member loop
		if
			row.id = 2 or
			row.id = 6 or
			row.id = 7 or
			row.id = 12 or
			row.id = 18 or
			row.id = 24 or
			row.id = 26 or
			row.id = 32 or
			row.id = 36 or
			row.id = 40 or
			row.id = 45 or
			row.id = 46 or
			row.id = 48 or
			row.id = 52 or
			row.id = 56 or
			row.id = 60 or
			row.id = 65 or
			row.id = 69 or
			row.id = 74 or
			row.id = 88
		then
			--raise notice 'row id: %', row.id;
			update T_CLUB_MEMBER set photo = (select bytea_import('clubeek_pictures/man.png')) where id = row.id;
		elseif
			row.id = 5 or
			row.id = 11 or
			row.id = 17 or
			row.id = 20 or
			row.id = 22 or
			row.id = 23 or
			row.id = 33 or
			row.id = 35 or
			row.id = 39 or
			row.id = 44 or
			row.id = 49 or
			row.id = 51 or
			row.id = 57 or
			row.id = 78 or
			row.id = 80 or
			row.id = 87 or
			row.id = 89 or
			row.id = 90 or
			row.id = 95 or
			row.id = 96
		then
			update T_CLUB_MEMBER set photo = (select bytea_import('clubeek_pictures/woman.png')) where id = row.id;
		end if;
	end loop;
end;
$$;

--select id, name, surname, octet_length(photo) from t_club_member where photo is not null;
--show data_directory;


----------------------------------------------------------------------
-- Location
---- 1 aktuality
---- 0 nástěnka

-- Owner
---- 3 celý web
---- 2 pouze tým
---- 1 pouze kategorie
---- 0 pouze klub