﻿------------------------------
-- Dropping tables if exist --
------------------------------

drop table if exists T_CONTACT;
drop table if exists T_EVENT_APPLICANT;
drop table if exists T_APPLICANT_FOR_TEAM_TRAINING;
drop table if exists T_APPLICANT_FOR_TEAM_MATCH;
drop table if exists T_USER;
drop table if exists T_PARTICIPANT_OF_TRAINING;
drop table if exists T_PARTICIPANT_OF_MATCH;
drop table if exists T_ARTICLE_DISCUSSION_POST;
drop table if exists T_EVENT_DISCUSSION_POST;
drop table if exists T_TEAM_TRAINING_DISCUSSION_POST;
drop table if exists T_TEAM_MATCH_DISCUSSION_POST;
drop table if exists T_CLUB_URL;

drop table if exists T_STAT_SCORE_BASE;
drop table if exists T_STAT_SCORE_FULL;
drop table if exists T_STAT_SUBSTITUTION;
drop table if exists T_STAT_PENALTY;

drop view if exists club_member_by_team;

drop table if exists T_TEAM_TRAINING;
drop table if exists T_EVENT;
drop table if exists T_TEAM_MEMBER;
drop table if exists T_CLUB_MEMBER;
drop table if exists T_TEAM_MATCH;
drop table if exists T_CLUB_RIVAL;
drop table if exists T_SEASON;
drop table if exists T_ARTICLE;
drop table if exists T_CLUB_TEAM;
drop table if exists T_CATEGORY;
drop table if exists T_CLUB;


---------------------
-- Creating tables --
---------------------

CREATE TABLE T_CLUB
(
    id 			serial,
    licence_type	smallint not null,	
    title		varchar(200),
    comment		varchar(200),
    logo		bytea,

    primary key (id)
);

CREATE TABLE T_CLUB_URL
(
    id			serial,
    url 		character varying(100) not null,
    club_id		integer not null,

    primary key (id),
    foreign key (club_id) references T_CLUB (id) ON DELETE CASCADE
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
    club_id		integer			not null,

    primary key (id),
    foreign key (club_id) references T_CLUB (id) ON DELETE CASCADE
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
    sport_type		smallint		not null,
    active		boolean,
    sorting		integer 		not null  default 0,
    category_id		integer	        	default null,
    club_id		integer			not null,

    primary key (id),
    foreign key (category_id) references T_CATEGORY (id)
        ON DELETE SET NULL,
    foreign key (club_id) references T_CLUB (id)
);

CREATE TABLE T_CONTACT
(
    id 			serial,
    contact		varchar(100)		not null,
    description		varchar(100),
    contact_type	smallint		not null,
    notification_type	smallint		not null,
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

CREATE TABLE T_TEAM_TRAINING_DISCUSSION_POST
(
    id            	SERIAL,
    creation_time 	TIMESTAMP WITH TIME ZONE NOT NULL ,
    comment 		TEXT NOT NULL ,
    team_training_id 	INTEGER NOT NULL,

    primary key (id),
    foreign key (team_training_id) references t_team_training(id)
	MATCH SIMPLE ON UPDATE NO ACTION ON DELETE CASCADE
);

CREATE TABLE T_SEASON
(
    id			serial,
    description		character varying(100),

    primary key (id)
);

CREATE TABLE T_TEAM_MATCH
(
    id				serial,

    is_home_match		boolean		not null,
    match_type			smallint	not null,
    start			timestamptz	not null,
    comment			text,
    club_rival_comment		character varying (200),
    publish			boolean 	not null default 'false',
    score_A			smallint,
    score_B			smallint,
    score_detail		character varying (200),

    season_id			integer,
    club_team_id		integer		not null,
    club_rival_id		integer,

    primary key (id),
    foreign key (season_id) references T_SEASON (id) ON DELETE CASCADE,
    foreign key (club_team_id) references T_CLUB_TEAM (id) ON DELETE CASCADE,
    foreign key (club_rival_id) references T_CLUB_RIVAL (id) ON DELETE SET NULL
);

CREATE TABLE T_STAT_PENALTY (
	id		serial,
	time		timestamptz	not null,
	penalty_type	smallint	not null,
	penalty_value	smallint,
	club_member_id	integer		not null,
	team_match_id	integer		not null,

	primary key (id),
	foreign key (club_member_id) references t_club_member (id),
	foreign key (team_match_id) references t_team_match (id)
);

CREATE TABLE T_STAT_SUBSTITUTION (
    id			serial,
    time		timestamptz	not null,
    team_match_id	integer		not null,
    player_out		integer		not null,
    player_in		integer		not null,

    primary key (id),
    foreign key (team_match_id) references t_team_match (id),
    foreign key (player_out) references t_club_member (id),
    foreign key (player_in) references t_club_member (id),
    unique (team_match_id, player_out, player_in)
);

CREATE TABLE T_STAT_SCORE_BASE (
    id			serial,
    count		smallint	not null,
    club_member_id	integer		not null,
    team_match_id	integer		not null,

    primary key (id),
    foreign key (club_member_id) references T_CLUB_MEMBER (id),
    foreign key (team_match_id) references T_TEAM_MATCH (id),
    unique (club_member_id, team_match_id)
);

CREATE TABLE T_STAT_SCORE_FULL (
    id			serial,
    time		timestamptz	not null,
    count		smallint	not null,
    club_member_id	integer		not null,
    team_match_id	integer		not null,
    assistance_1_id	integer,
    assistance_2_id	integer,

    primary key (id),
    foreign key (assistance_1_id) references T_CLUB_MEMBER (id),
    foreign key (assistance_2_id) references T_CLUB_MEMBER (id),
    foreign key (club_member_id) references T_CLUB_MEMBER (id),
    foreign key (team_match_id) references T_TEAM_MATCH (id),
    unique (club_member_id, team_match_id)
);

CREATE TABLE T_TEAM_MATCH_DISCUSSION_POST
(
    id            	SERIAL,
    creation_time 	TIMESTAMP WITH TIME ZONE NOT NULL ,
    comment 		TEXT NOT NULL ,
    team_match_id 	INTEGER NOT NULL,

    primary key (id),
    foreign key (team_match_id) references t_team_match(id)
	MATCH SIMPLE ON UPDATE NO ACTION ON DELETE CASCADE
);

CREATE TABLE T_EVENT
(
    id			serial,
    start		timestamp		not null,
    finish		timestamp,
    caption		varchar(500)		not null,
    place		varchar(500),
    description		TEXT,
    sign_participation	boolean,
    owner_type 		smallint 		NOT NULL DEFAULT 0,
    club_team_id	integer,
    category_id		integer,

    primary key (id),
    foreign key ( club_team_id ) references T_CLUB_TEAM ( id )
        ON DELETE CASCADE,
    foreign key ( category_id ) references T_CATEGORY ( id )
        ON DELETE CASCADE,

    check ((category_id is not null AND club_team_id is null) OR (club_team_id is not null AND category_id is null) OR (club_team_id is null AND category_id is null))

);

CREATE TABLE T_EVENT_DISCUSSION_POST
(
    id            	SERIAL,
    creation_time 	TIMESTAMP WITH TIME ZONE NOT NULL ,
    comment 		TEXT NOT NULL ,
    event_id 		INTEGER NOT NULL,

    primary key (id),
    foreign key (event_id) references t_event(id)
	MATCH SIMPLE ON UPDATE NO ACTION ON DELETE CASCADE
);

CREATE TABLE T_EVENT_APPLICANT
(
    attend		boolean	not null default 'false',
    club_member_id	integer		not null,
    action_id		integer		not null,

    primary key ( club_member_id, action_id ),
    foreign key ( club_member_id ) references T_CLUB_MEMBER( id )
	ON DELETE CASCADE,
    foreign key ( action_id ) references T_EVENT ( id )
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
    location_type		smallint	not null default 1,
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

CREATE TABLE T_ARTICLE_DISCUSSION_POST
(
    id            	SERIAL,
    creation_time 	TIMESTAMP WITH TIME ZONE NOT NULL ,
    comment 		TEXT NOT NULL ,
    article_id 		INTEGER NOT NULL,

    primary key (id),
    foreign key (article_id) references t_article(id)
	MATCH SIMPLE ON UPDATE NO ACTION ON DELETE CASCADE
);

CREATE TABLE T_USER
(
    id			serial,
    username		varchar(100)	not null,
    password		varchar(100)	not null,
    user_role_type	integer		not null default 0,
    club_member_id	integer,

    primary key (id),
    foreign key ( club_member_id ) references T_CLUB_MEMBER ( id )
        ON DELETE CASCADE,
    unique (username),
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
INSERT INTO T_CLUB (licence_type, title, comment) VALUES
	(0, 'FC Chvojkovice Brod', 'Zřejmě dobrý oddíl ...'),
	(1, 'Testovací superklub', 'Komentář testovacího klubu, který vyhraje superpohár.');

INSERT INTO T_CLUB_URL (url, club_id) VALUES 
	('fc-chojkovice-brod', 1),
	('fcchvojkovice', 1),
	('chvojkovicebrod', 1),
	('testklub', 2);

-- user name, password (the same as user name)
-- administrator is the same as admin (admin is shorter to write)
insert into T_USER (username, password, user_role_type) values
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

insert into T_CLUB_TEAM (name, sport_type, active, category_id, club_id) values
	('Senioři [týmy]', 1, true, 3, 1),
	('Mládež [týmy]', 1, true, 3, 1),
	('Dospělí [týmy]', 1, true, 3, 1),
	('Pokus [týmy]', 1, false, null, 1);

insert into t_team_training (start, finish, place, comment, club_team_id) values 
	(to_timestamp('2015-9-7 15:30', 'YYYY-MM-DD HH24:MI'), to_timestamp('2015-9-7 17:00', 'YYYY-MM-DD HH24:MI'), 'Horní hřiště', 'Trénink povede Standa.', 2),
	(to_timestamp('2015-9-14 15:30', 'YYYY-MM-DD HH24:MI'), to_timestamp('2015-9-14 17:00', 'YYYY-MM-DD HH24:MI'), 'Horní hřiště', 'Trénink povede Standa.', 2),
	(to_timestamp('2015-9-21 15:30', 'YYYY-MM-DD HH24:MI'), to_timestamp('2015-9-21 17:00', 'YYYY-MM-DD HH24:MI'), 'Horní hřiště', 'Trénink povede Standa.', 2),
	(to_timestamp('2015-9-28 15:30', 'YYYY-MM-DD HH24:MI'), to_timestamp('2015-9-28 17:00', 'YYYY-MM-DD HH24:MI'), 'Horní hřiště', 'Trénink povede Standa.', 2),
	(to_timestamp('2015-9-9 16:00', 'YYYY-MM-DD HH24:MI'), to_timestamp('2015-9-9 18:00', 'YYYY-MM-DD HH24:MI'), 'Dolní hřiště', 'Trénink povede Petr. V případě špatného počasí se jde do tělocvičny.', 2),
	(to_timestamp('2015-9-16 16:00', 'YYYY-MM-DD HH24:MI'), to_timestamp('2015-9-16 18:00', 'YYYY-MM-DD HH24:MI'), 'Dolní hřiště', 'Trénink povede Petr. V případě špatného počasí se jde do tělocvičny.', 2),
	(to_timestamp('2015-9-23 16:00', 'YYYY-MM-DD HH24:MI'), to_timestamp('2015-9-23 18:00', 'YYYY-MM-DD HH24:MI'), 'Dolní hřiště', 'Trénink povede Petr. V případě špatného počasí se jde do tělocvičny.', 2),
	(to_timestamp('2015-9-30 16:00', 'YYYY-MM-DD HH24:MI'), to_timestamp('2015-9-30 18:00', 'YYYY-MM-DD HH24:MI'), 'Dolní hřiště', 'Trénink povede Petr. V případě špatného počasí se jde do tělocvičny.', 2),
	(to_timestamp('2016-9-7 15:30', 'YYYY-MM-DD HH24:MI'), to_timestamp('2016-9-7 17:00', 'YYYY-MM-DD HH24:MI'), 'Horní hřiště', 'Trénink povede Standa.', 2),
	(to_timestamp('2016-9-14 15:30', 'YYYY-MM-DD HH24:MI'), to_timestamp('2016-9-14 17:00', 'YYYY-MM-DD HH24:MI'), 'Horní hřiště', 'Trénink povede Standa.', 2),
	(to_timestamp('2016-9-21 15:30', 'YYYY-MM-DD HH24:MI'), to_timestamp('2016-9-21 17:00', 'YYYY-MM-DD HH24:MI'), 'Horní hřiště', 'Trénink povede Standa.', 2),
	(to_timestamp('2016-9-28 15:30', 'YYYY-MM-DD HH24:MI'), to_timestamp('2016-9-28 17:00', 'YYYY-MM-DD HH24:MI'), 'Horní hřiště', 'Trénink povede Standa.', 2),
	(to_timestamp('2016-9-9 16:00', 'YYYY-MM-DD HH24:MI'), to_timestamp('2016-9-9 18:00', 'YYYY-MM-DD HH24:MI'), 'Dolní hřiště', 'Trénink povede Petr. V případě špatného počasí se jde do tělocvičny.', 2),
	(to_timestamp('2016-9-16 16:00', 'YYYY-MM-DD HH24:MI'), to_timestamp('2016-9-16 18:00', 'YYYY-MM-DD HH24:MI'), 'Dolní hřiště', 'Trénink povede Petr. V případě špatného počasí se jde do tělocvičny.', 2),
	(to_timestamp('2016-9-23 16:00', 'YYYY-MM-DD HH24:MI'), to_timestamp('2016-9-23 18:00', 'YYYY-MM-DD HH24:MI'), 'Dolní hřiště', 'Trénink povede Petr. V případě špatného počasí se jde do tělocvičny.', 2),
	(to_timestamp('2016-9-30 16:00', 'YYYY-MM-DD HH24:MI'), to_timestamp('2016-9-30 18:00', 'YYYY-MM-DD HH24:MI'), 'Dolní hřiště', 'Trénink povede Petr. V případě špatného počasí se jde do tělocvičny.', 2),
	(to_timestamp('2016-5-29 15:30', 'YYYY-MM-DD HH24:MI'), to_timestamp('2016-5-29 17:00', 'YYYY-MM-DD HH24:MI'), 'Horní hřiště', 'V případě špatného počasí se půjde do tělocvičny.', 3),
	(to_timestamp('2016-1-30 15:30', 'YYYY-MM-DD HH24:MI'), to_timestamp('2016-1-30 17:00', 'YYYY-MM-DD HH24:MI'), 'Tělocvična', 'V případě nepříznivého počasí bude trénink zrušen.', 1),
	(to_timestamp('2016-6-12 17:00', 'YYYY-MM-DD HH24:MI'), to_timestamp('2016-6-12 19:00', 'YYYY-MM-DD HH24:MI'), 'Dolní hřiště', 'Příprava na zápas.', 1),
	(to_timestamp('2015-4-15 17:00', 'YYYY-MM-DD HH24:MI'), to_timestamp('2015-4-15 19:00', 'YYYY-MM-DD HH24:MI'), 'Tělocvična', 'V případě nepříznivého počasí bude trénink zrušen.', 3),
	(to_timestamp('2016-1-23 17:00', 'YYYY-MM-DD HH24:MI'), to_timestamp('2016-1-23 19:00', 'YYYY-MM-DD HH24:MI'), 'Dolní hřiště', 'V případě nepříznivého počasí bude trénink zrušen.', 1),
	(to_timestamp('2015-1-29 19:00', 'YYYY-MM-DD HH24:MI'), to_timestamp('2015-1-29 20:30', 'YYYY-MM-DD HH24:MI'), 'Dolní hřiště', '', 3),
	(to_timestamp('2016-11-8 15:30', 'YYYY-MM-DD HH24:MI'), to_timestamp('2016-11-8 17:00', 'YYYY-MM-DD HH24:MI'), 'Dolní hřiště', 'V případě nepříznivého počasí bude trénink zrušen.', 2),
	(to_timestamp('2016-2-9 15:30', 'YYYY-MM-DD HH24:MI'), to_timestamp('2016-2-9 17:00', 'YYYY-MM-DD HH24:MI'), 'Horní hřiště', 'V případě špatného počasí se půjde do tělocvičny.', 2),
	(to_timestamp('2016-8-27 19:00', 'YYYY-MM-DD HH24:MI'), to_timestamp('2016-8-27 20:30', 'YYYY-MM-DD HH24:MI'), 'Dolní hřiště', 'Příprava na zápas.', 2),
	(to_timestamp('2015-10-8 17:00', 'YYYY-MM-DD HH24:MI'), to_timestamp('2015-10-8 19:00', 'YYYY-MM-DD HH24:MI'), 'Dolní hřiště', 'V případě špatného počasí se půjde do tělocvičny.', 1),
	(to_timestamp('2015-9-8 15:30', 'YYYY-MM-DD HH24:MI'), to_timestamp('2015-9-8 17:00', 'YYYY-MM-DD HH24:MI'), 'Dolní hřiště', 'Příprava na zápas.', 2),
	(to_timestamp('2016-7-6 17:00', 'YYYY-MM-DD HH24:MI'), to_timestamp('2016-7-6 19:00', 'YYYY-MM-DD HH24:MI'), 'Tělocvična', 'V případě nepříznivého počasí bude trénink zrušen.', 3),
	(to_timestamp('2016-5-19 17:00', 'YYYY-MM-DD HH24:MI'), to_timestamp('2016-5-19 19:00', 'YYYY-MM-DD HH24:MI'), 'Horní hřiště', '', 2),
	(to_timestamp('2016-10-23 17:00', 'YYYY-MM-DD HH24:MI'), to_timestamp('2016-10-23 19:00', 'YYYY-MM-DD HH24:MI'), 'Horní hřiště', 'V případě nepříznivého počasí bude trénink zrušen.', 2),
	(to_timestamp('2016-4-13 19:00', 'YYYY-MM-DD HH24:MI'), to_timestamp('2016-4-13 20:30', 'YYYY-MM-DD HH24:MI'), 'Tělocvična', 'V případě špatného počasí se půjde do tělocvičny.', 2),
	(to_timestamp('2016-2-23 19:00', 'YYYY-MM-DD HH24:MI'), to_timestamp('2016-2-23 20:30', 'YYYY-MM-DD HH24:MI'), 'Dolní hřiště', 'V případě špatného počasí se půjde do tělocvičny.', 1),
	(to_timestamp('2015-6-20 17:00', 'YYYY-MM-DD HH24:MI'), to_timestamp('2015-6-20 19:00', 'YYYY-MM-DD HH24:MI'), 'Horní hřiště', 'V případě nepříznivého počasí bude trénink zrušen.', 1),
	(to_timestamp('2016-2-25 15:30', 'YYYY-MM-DD HH24:MI'), to_timestamp('2016-2-25 17:00', 'YYYY-MM-DD HH24:MI'), 'Horní hřiště', 'V případě nepříznivého počasí bude trénink zrušen.', 2),
	(to_timestamp('2015-8-16 19:00', 'YYYY-MM-DD HH24:MI'), to_timestamp('2015-8-16 20:30', 'YYYY-MM-DD HH24:MI'), 'Dolní hřiště', 'V případě špatného počasí se půjde do tělocvičny.', 1),
	(to_timestamp('2016-2-12 17:00', 'YYYY-MM-DD HH24:MI'), to_timestamp('2016-2-12 19:00', 'YYYY-MM-DD HH24:MI'), 'Horní hřiště', '', 2),
	(to_timestamp('2015-8-14 17:00', 'YYYY-MM-DD HH24:MI'), to_timestamp('2015-8-14 19:00', 'YYYY-MM-DD HH24:MI'), 'Tělocvična', 'V případě nepříznivého počasí bude trénink zrušen.', 2),
	(to_timestamp('2015-6-23 15:30', 'YYYY-MM-DD HH24:MI'), to_timestamp('2015-6-23 17:00', 'YYYY-MM-DD HH24:MI'), 'Dolní hřiště', 'Příprava na zápas.', 2),
	(to_timestamp('2016-6-30 17:00', 'YYYY-MM-DD HH24:MI'), to_timestamp('2016-6-30 19:00', 'YYYY-MM-DD HH24:MI'), 'Tělocvična', 'V případě špatného počasí se půjde do tělocvičny.', 2),
	(to_timestamp('2016-9-3 17:00', 'YYYY-MM-DD HH24:MI'), to_timestamp('2016-9-3 19:00', 'YYYY-MM-DD HH24:MI'), 'Tělocvična', '', 1),
	(to_timestamp('2015-12-13 15:30', 'YYYY-MM-DD HH24:MI'), to_timestamp('2015-12-13 17:00', 'YYYY-MM-DD HH24:MI'), 'Dolní hřiště', 'Příprava na zápas.', 1),
	(to_timestamp('2016-8-2 17:00', 'YYYY-MM-DD HH24:MI'), to_timestamp('2016-8-2 19:00', 'YYYY-MM-DD HH24:MI'), 'Dolní hřiště', '', 1),
	(to_timestamp('2015-5-12 17:00', 'YYYY-MM-DD HH24:MI'), to_timestamp('2015-5-12 19:00', 'YYYY-MM-DD HH24:MI'), 'Tělocvična', 'V případě nepříznivého počasí bude trénink zrušen.', 1),
	(to_timestamp('2016-12-1 19:00', 'YYYY-MM-DD HH24:MI'), to_timestamp('2016-12-1 20:30', 'YYYY-MM-DD HH24:MI'), 'Horní hřiště', 'V případě nepříznivého počasí bude trénink zrušen.', 1),
	(to_timestamp('2016-4-1 17:00', 'YYYY-MM-DD HH24:MI'), to_timestamp('2016-4-1 19:00', 'YYYY-MM-DD HH24:MI'), 'Tělocvična', '', 1),
	(to_timestamp('2015-5-22 19:00', 'YYYY-MM-DD HH24:MI'), to_timestamp('2015-5-22 20:30', 'YYYY-MM-DD HH24:MI'), 'Horní hřiště', '', 1),
	(to_timestamp('2015-6-30 15:30', 'YYYY-MM-DD HH24:MI'), to_timestamp('2015-6-30 17:00', 'YYYY-MM-DD HH24:MI'), 'Dolní hřiště', 'V případě nepříznivého počasí bude trénink zrušen.', 2),
	(to_timestamp('2016-8-5 15:30', 'YYYY-MM-DD HH24:MI'), to_timestamp('2016-8-5 17:00', 'YYYY-MM-DD HH24:MI'), 'Dolní hřiště', 'Příprava na zápas.', 2),
	(to_timestamp('2016-8-28 15:30', 'YYYY-MM-DD HH24:MI'), to_timestamp('2016-8-28 17:00', 'YYYY-MM-DD HH24:MI'), 'Dolní hřiště', 'Příprava na zápas.', 1),
	(to_timestamp('2016-1-9 17:00', 'YYYY-MM-DD HH24:MI'), to_timestamp('2016-1-9 19:00', 'YYYY-MM-DD HH24:MI'), 'Horní hřiště', '', 3),
	(to_timestamp('2016-5-22 17:00', 'YYYY-MM-DD HH24:MI'), to_timestamp('2016-5-22 19:00', 'YYYY-MM-DD HH24:MI'), 'Tělocvična', 'Příprava na zápas.', 3),
	(to_timestamp('2015-4-11 17:00', 'YYYY-MM-DD HH24:MI'), to_timestamp('2015-4-11 19:00', 'YYYY-MM-DD HH24:MI'), 'Tělocvična', 'V případě špatného počasí se půjde do tělocvičny.', 1),
	(to_timestamp('2016-5-2 15:30', 'YYYY-MM-DD HH24:MI'), to_timestamp('2016-5-2 17:00', 'YYYY-MM-DD HH24:MI'), 'Dolní hřiště', '', 2),
	(to_timestamp('2016-11-21 19:00', 'YYYY-MM-DD HH24:MI'), to_timestamp('2016-11-21 20:30', 'YYYY-MM-DD HH24:MI'), 'Tělocvična', 'V případě nepříznivého počasí bude trénink zrušen.', 2),
	(to_timestamp('2016-10-16 15:30', 'YYYY-MM-DD HH24:MI'), to_timestamp('2016-10-16 17:00', 'YYYY-MM-DD HH24:MI'), 'Horní hřiště', '', 1),
	(to_timestamp('2015-3-27 15:30', 'YYYY-MM-DD HH24:MI'), to_timestamp('2015-3-27 17:00', 'YYYY-MM-DD HH24:MI'), 'Horní hřiště', '', 3),
	(to_timestamp('2016-7-7 15:30', 'YYYY-MM-DD HH24:MI'), to_timestamp('2016-7-7 17:00', 'YYYY-MM-DD HH24:MI'), 'Tělocvična', 'Příprava na zápas.', 2),
	(to_timestamp('2015-5-6 17:00', 'YYYY-MM-DD HH24:MI'), to_timestamp('2015-5-6 19:00', 'YYYY-MM-DD HH24:MI'), 'Horní hřiště', '', 3),
	(to_timestamp('2016-9-12 15:30', 'YYYY-MM-DD HH24:MI'), to_timestamp('2016-9-12 17:00', 'YYYY-MM-DD HH24:MI'), 'Horní hřiště', 'V případě špatného počasí se půjde do tělocvičny.', 3),
	(to_timestamp('2015-6-23 17:00', 'YYYY-MM-DD HH24:MI'), to_timestamp('2015-6-23 19:00', 'YYYY-MM-DD HH24:MI'), 'Horní hřiště', '', 3),
	(to_timestamp('2015-7-11 15:30', 'YYYY-MM-DD HH24:MI'), to_timestamp('2015-7-11 17:00', 'YYYY-MM-DD HH24:MI'), 'Horní hřiště', 'V případě špatného počasí se půjde do tělocvičny.', 3),
	(to_timestamp('2016-6-25 19:00', 'YYYY-MM-DD HH24:MI'), to_timestamp('2016-6-25 20:30', 'YYYY-MM-DD HH24:MI'), 'Tělocvična', '', 3),
	(to_timestamp('2015-6-6 19:00', 'YYYY-MM-DD HH24:MI'), to_timestamp('2015-6-6 20:30', 'YYYY-MM-DD HH24:MI'), 'Dolní hřiště', '', 2),
	(to_timestamp('2016-1-30 17:00', 'YYYY-MM-DD HH24:MI'), to_timestamp('2016-1-30 19:00', 'YYYY-MM-DD HH24:MI'), 'Tělocvična', 'V případě špatného počasí se půjde do tělocvičny.', 1),
	(to_timestamp('2015-1-30 15:30', 'YYYY-MM-DD HH24:MI'), to_timestamp('2015-1-30 17:00', 'YYYY-MM-DD HH24:MI'), 'Dolní hřiště', 'V případě špatného počasí se půjde do tělocvičny.', 3),
	(to_timestamp('2015-6-18 15:30', 'YYYY-MM-DD HH24:MI'), to_timestamp('2015-6-18 17:00', 'YYYY-MM-DD HH24:MI'), 'Tělocvična', 'V případě špatného počasí se půjde do tělocvičny.', 3);

insert into T_CLUB_MEMBER (club_id, name, surname, birthdate, street, city, code) values
	(1, 'Kryštof', 'Staněk', null, null, null, null),
	(1, 'Marian', 'Pavlík', to_date('1968-04-04', 'YYYY-MM-DD'), 'Dr. Slabihoudka 988', 'Mladá Boleslav', '29301'),
	(1, 'Kristýna', 'Sedláčková', to_date('1953-10-04', 'YYYY-MM-DD'), 'Wattova 172', 'Cheb', '35002'),
	(1, 'Dana', 'Kolářová', to_date('2002-11-08', 'YYYY-MM-DD'), 'Nálepkovo náměstí 573', 'Jablonec nad Nisou', '46601'),
	(1, 'Božena', 'Jandová', to_date('1922-10-26', 'YYYY-MM-DD'), 'Žofínská 0', 'Znojmo', '67181'),
	(1, 'Václav', 'Vávra', to_date('2004-11-07', 'YYYY-MM-DD'), 'Hlubinská 184', 'Olomouc', '77200'),
	(1, 'Patrik', 'Hájek', to_date('1975-06-02', 'YYYY-MM-DD'), 'Porážková 331', 'Karlovy Vary', '36001'),
	(1, 'Jitka', 'Pokorná', to_date('1941-06-20', 'YYYY-MM-DD'), 'Horní 572', 'Valašské Meziříčí', '75701'),
	(1, 'Ludmila', 'Králová', to_date('1938-05-18', 'YYYY-MM-DD'), 'Františka a Anny Ryšových 18', 'Český Těšín', '73701'),
	(1, 'Martina', 'Kadlecová', to_date('1975-03-16', 'YYYY-MM-DD'), 'nám. Boženy Němcové 534', 'České Budějovice', '37001'),
	(1, 'Marcela', 'Poláková', to_date('1940-04-21', 'YYYY-MM-DD'), 'Žerotínova 998', 'Liberec', '46008'),
	(1, 'Ivan', 'Sedláček', null, 'Lechowiczova 761', 'Pardubice', '53012'),
	(1, 'Eliška', 'Šimková', to_date('1920-07-20', 'YYYY-MM-DD'), '1. května 676', 'Ostrava', '70900'),
	(1, 'Otakar', 'Doležal', to_date('1981-06-10', 'YYYY-MM-DD'), null, null, null),
	(1, 'Jarmila', 'Kučerová', null, 'Pod Tratí 750', 'Kladno', '27201'),
	(1, 'Radovan', 'Staněk', to_date('1972-12-30', 'YYYY-MM-DD'), 'Dolní 111', 'Jihlava', '58601'),
	(1, 'Vlasta', 'Mašková', to_date('1954-09-28', 'YYYY-MM-DD'), 'Edisonova 767', 'Valašské Meziříčí', '75701'),
	(1, 'Richard', 'Pavlík', null, 'Čapkova 264', 'Pardubice', '53012'),
	(1, 'Alexandr', 'Doležal', to_date('1930-10-25', 'YYYY-MM-DD'), 'Soukenická 448', 'Havířov', '73601'),
	(1, 'Jaroslava', 'Musilová', to_date('1944-07-04', 'YYYY-MM-DD'), 'Hornopolní 477', 'Hradec Králové', '50008'),
	(1, 'Zuzana', 'Malá', to_date('1978-03-06', 'YYYY-MM-DD'), 'Výškovická 827', 'Znojmo', '67181'),
	(1, 'Ludmila', 'Dostálová', to_date('1959-03-28', 'YYYY-MM-DD'), 'Hollarova 509', 'Písek', '39701'),
	(1, 'Petra', 'Fialová', to_date('2013-08-06', 'YYYY-MM-DD'), 'Palackého 511', 'Karlovy Vary', '36001'),
	(1, 'Eduard', 'Dostál', null, 'Opavská 956', 'Příbram', '26101'),
	(1, 'Eliška', 'Benešová', to_date('1918-06-10', 'YYYY-MM-DD'), '17. listopadu 488', 'Český Těšín', '73701'),
	(1, 'Vratislav', 'Bláha', null, 'Harantova 629', 'Karviná', '73506'),
	(1, 'Dana', 'Ševčíková', to_date('1970-04-06', 'YYYY-MM-DD'), 'Husovo náměstí 73', 'Mladá Boleslav', '29301'),
	(1, 'Jaroslava', 'Machová', to_date('2000-12-19', 'YYYY-MM-DD'), 'Dr. Slabihoudka 119', 'Nový Jičín', '74101'),
	(1, 'Martina', 'Procházková', to_date('1957-08-22', 'YYYY-MM-DD'), 'nám. Dr. E. Beneše 257', 'Třinec', '73961'),
	(1, 'Marta', 'Ševčíková', to_date('1944-03-15', 'YYYY-MM-DD'), 'Jurečkova 747', 'Praha', '11800'),
	(1, 'Andrea', 'Vávrová', to_date('1977-03-18', 'YYYY-MM-DD'), 'Zkrácená 525', 'Teplice', '41501'),
	(1, 'Stanislav', 'Kučera', to_date('1921-04-17', 'YYYY-MM-DD'), 'Záblatská 210', 'Kolín', '28123'),
	(1, 'Lenka', 'Svobodová', null, null, null, null),
	(1, 'Božena', 'Sýkorová', to_date('2012-04-25', 'YYYY-MM-DD'), null, null, null),
	(1, 'Alena', 'Jelínková', to_date('1971-10-25', 'YYYY-MM-DD'), 'Koněvova 972', 'Opava', '74601'),
	(1, 'Dalibor', 'Mareš', to_date('1988-11-06', 'YYYY-MM-DD'), 'Bieblova 200', 'Jihlava', '58601'),
	(1, 'Barbora', 'Růžičková', to_date('1942-07-15', 'YYYY-MM-DD'), null, null, null),
	(1, 'Aleš', 'Marek', to_date('1976-02-20', 'YYYY-MM-DD'), 'Radvanická 394', 'Tábor', '39003'),
	(1, 'Markéta', 'Hrubá', to_date('1942-01-29', 'YYYY-MM-DD'), 'Španihelova 632', 'Frýdek-Místek', '73801'),
	(1, 'Roman', 'Kříž', to_date('1969-02-21', 'YYYY-MM-DD'), 'K Myslivně 964', 'Vsetín', '75501'),
	(1, 'Libuše', 'Kopecká', null, 'Pod Landekem 657', 'Ústí nad Labem', '40011'),
	(1, 'Petra', 'Ševčíková', to_date('1957-08-10', 'YYYY-MM-DD'), null, null, null),
	(1, 'Hana', 'Staňková', to_date('1972-05-01', 'YYYY-MM-DD'), 'Balcarova 999', 'Litvínov', '43601'),
	(1, 'Lucie', 'Kopecká', to_date('1937-01-01', 'YYYY-MM-DD'), 'Muzejní 186', 'Litvínov', '43601'),
	(1, 'Radovan', 'Bureš', to_date('1955-01-06', 'YYYY-MM-DD'), 'Heřmanická 236', 'Přerov', '75003'),
	(1, 'Jindřich', 'Polák', null, 'Provaznická 640', 'Most', '43401'),
	(1, 'Tereza', 'Nguyen', to_date('1920-06-07', 'YYYY-MM-DD'), 'Gregorova 167', 'Uherské Hradiště', '68606'),
	(1, 'Jozef', 'Novotný', null, 'Umělecká 586', 'Nový Jičín', '74101'),
	(1, 'Veronika', 'Tichá', to_date('1922-06-10', 'YYYY-MM-DD'), 'Bartovická 312', 'Plzeň', '31700'),
	(1, 'Veronika', 'Marková', to_date('1996-02-20', 'YYYY-MM-DD'), 'U Koupaliště 658', 'Valašské Meziříčí', '75701'),
	(1, 'Marta', 'Kučerová', null, 'Fišerova 315', 'Frýdek-Místek', '73801'),
	(1, 'Luboš', 'Sýkora', to_date('2005-06-25', 'YYYY-MM-DD'), 'Lechowiczova 307', 'Prostějov', '79604'),
	(1, 'Radovan', 'Kříž', to_date('1991-03-24', 'YYYY-MM-DD'), null, null, null),
	(1, 'Radek', 'Bartoš', to_date('1973-08-22', 'YYYY-MM-DD'), 'Horova 285', 'Vsetín', '75501'),
	(1, 'Martin', 'Nguyen', to_date('1932-09-25', 'YYYY-MM-DD'), null, null, null),
	(1, 'Vlastimil', 'Kovář', to_date('1971-02-26', 'YYYY-MM-DD'), null, null, null),
	(1, 'Božena', 'Svobodová', to_date('1988-07-24', 'YYYY-MM-DD'), 'Fryštátská 803', 'Mladá Boleslav', '29301'),
	(1, 'Ján', 'Horák', null, 'Karla Svobody 731', 'Krnov', '79401'),
	(1, 'Eduard', 'Čermák', to_date('1924-07-12', 'YYYY-MM-DD'), 'Mongolská 179', 'Brno', '63800'),
	(1, 'Jindřich', 'Hrubý', to_date('2011-05-29', 'YYYY-MM-DD'), 'Slavíčkova 869', 'Hradec Králové', '50002'),
	(1, 'Andrea', 'Pavlíková', null, 'Dr. Slabihoudka 263', 'Frýdek-Místek', '73801'),
	(1, 'Miloš', 'Sedláček', to_date('1988-03-21', 'YYYY-MM-DD'), 'Zelená 3', 'Brno', '60200'),
	(1, 'Anna', 'Bartošová', to_date('2010-11-12', 'YYYY-MM-DD'), 'Dostojevského 383', 'Liberec', '46008'),
	(1, 'Eliška', 'Nguyenová', to_date('1922-12-27', 'YYYY-MM-DD'), 'Michalské náměstí 709', 'Opava', '74601'),
	(1, 'Roman', 'Štěpánek', to_date('1958-07-26', 'YYYY-MM-DD'), 'Záblatská 831', 'Krnov', '79401'),
	(1, 'Lenka', 'Kovářová', to_date('1940-10-04', 'YYYY-MM-DD'), 'Vrbka 3', 'Frýdek-Místek', '73801'),
	(1, 'Miroslava', 'Vlčková', to_date('1990-08-29', 'YYYY-MM-DD'), 'Aleje 529', 'Krnov', '79401'),
	(1, 'Lenka', 'Veselá', null, 'Gregorova 545', 'Karlovy Vary', '36001'),
	(1, 'Denis', 'Staněk', null, null, null, null),
	(1, 'Lucie', 'Jelínková', to_date('2015-06-28', 'YYYY-MM-DD'), 'Karvinská 740', 'Znojmo', '66902'),
	(1, 'Vlasta', 'Lišková', to_date('1993-10-07', 'YYYY-MM-DD'), 'Pustkovecká 888', 'Příbram', '26101'),
	(1, 'Tadeáš', 'Pospíšil', to_date('1928-09-19', 'YYYY-MM-DD'), null, null, null),
	(1, 'Jarmila', 'Šťastná', to_date('1989-04-25', 'YYYY-MM-DD'), 'Janovská 161', 'Tábor', '39003'),
	(1, 'Radek', 'Novák', to_date('1969-07-03', 'YYYY-MM-DD'), 'Plechanovova 927', 'Frýdek-Místek', '73801'),
	(1, 'Marcela', 'Moravcová', to_date('1924-12-19', 'YYYY-MM-DD'), 'Stodolní 730', 'Ostrava', '70200'),
	(1, 'Michael', 'Čech', null, 'Husovo náměstí 789', 'Frýdek-Místek', '73801'),
	(1, 'Marta', 'Matoušková', null, null, null, null),
	(1, 'Vlasta', 'Sýkorová', to_date('2012-01-07', 'YYYY-MM-DD'), 'Kubínova 48', 'Ostrava', '71800'),
	(1, 'Matyáš', 'Kadlec', to_date('1997-10-06', 'YYYY-MM-DD'), 'Mojmírovců 781', 'Mladá Boleslav', '29301'),
	(1, 'Jaroslava', 'Černá', to_date('1926-04-15', 'YYYY-MM-DD'), 'Milíčova 807', 'Třebíč', '67401'),
	(1, 'Ludmila', 'Ševčíková', to_date('1958-03-29', 'YYYY-MM-DD'), 'Husovo náměstí 675', 'Český Těšín', '73701'),
	(1, 'Matěj', 'Konečný', to_date('1951-09-15', 'YYYY-MM-DD'), 'Halasova 291', 'Tábor', '39002'),
	(1, 'Vratislav', 'Havlíček', to_date('1966-11-06', 'YYYY-MM-DD'), null, null, null),
	(1, 'Kryštof', 'Šťastný', to_date('1976-06-21', 'YYYY-MM-DD'), 'Přemyslovců 307', 'Sokolov', '35601'),
	(1, 'Břetislav', 'Procházka', to_date('1981-08-28', 'YYYY-MM-DD'), 'K Myslivně 384', 'Šumperk', '78701'),
	(1, 'Michal', 'Malý', to_date('2004-02-24', 'YYYY-MM-DD'), 'Stanislavského 442', 'České Budějovice', '37008'),
	(1, 'Pavla', 'Musilová', null, 'Slavíkova 114', 'Litoměřice', '41201'),
	(1, 'Ivo', 'Liška', to_date('1962-05-07', 'YYYY-MM-DD'), 'Muzejní 839', 'Orlová', '73511'),
	(1, 'Monika', 'Čechová', null, 'Janovská 623', 'Přerov', '75003'),
	(1, 'Zuzana', 'Veselá', to_date('1966-01-01', 'YYYY-MM-DD'), 'Šenovská 588', 'Český Těšín', '73701'),
	(1, 'Zdeňka', 'Pospíšilová', to_date('1985-06-26', 'YYYY-MM-DD'), 'Rudná 357', 'Nový Jičín', '74101'),
	(1, 'Jaromír', 'Tichý', to_date('1958-07-10', 'YYYY-MM-DD'), 'Bráfova 301', 'Liberec', '46008'),
	(1, 'Miloslav', 'Horák', null, 'Soukenická 789', 'Karlovy Vary', '36001'),
	(1, 'Kryštof', 'Čech', null, null, null, null),
	(1, 'Martina', 'Fialová', to_date('1995-10-10', 'YYYY-MM-DD'), null, null, null),
	(1, 'Marcela', 'Staňková', to_date('1986-04-30', 'YYYY-MM-DD'), 'Dobrovského 414', 'Most', '43401'),
	(1, 'René', 'Bureš', to_date('1980-09-24', 'YYYY-MM-DD'), 'Na Karolíně 871', 'Olomouc', '77900'),
	(1, 'Viktor', 'Hrubý', to_date('1996-07-18', 'YYYY-MM-DD'), 'Cingrova 12', 'Ústí nad Labem', '40011'),
	(1, 'Pavla', 'Pavlíková', to_date('1984-07-29', 'YYYY-MM-DD'), 'Vrbka 365', 'Karviná', '73503'),
	(1, 'Luboš', 'Růžička', null, 'Rychvaldská 259', 'Mladá Boleslav', '29301');


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
		insert into T_TEAM_MEMBER (club_member_id, club_team_id, functions) values (v_record.id, 1, 1);
	end loop;

	-- junior team members
	for v_record in
		-- each row in following select id assigned to v_record variable
		select id from
			(select id, birthdate, row_number() over(order by birthdate desc) as rnum from T_CLUB_MEMBER) as subq
		where rnum <= 35 and birthdate is not null
	loop
		--insert into team_member
		insert into T_TEAM_MEMBER (club_member_id, club_team_id, functions) values (v_record.id, 2, 1);
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

insert into t_participant_of_training (club_member_id, team_training_id) values
	(19, 1), (52, 1), (60, 1), (15, 1), (68, 2), (32, 2), (2, 2), (33, 2), (3, 2), (38, 2), (98, 2), (4, 2), (96, 2), (37, 2), (67, 2), (76, 2), (8, 2), (42, 2), (72, 2), (85, 2), (16, 2), (87, 2), (20, 2), (52, 2), (95, 2), (88, 2), (28, 2), (91, 2), (60, 2), (48, 3), (33, 3), (55, 3), (65, 3), (82, 3), (61, 3), (69, 4), (32, 4), (67, 4), (97, 4), (43, 4), (72, 4), (14, 4), (51, 4), (86, 4), (55, 4), (20, 4), (54, 4), (52, 4), (22, 4), (29, 4), (50, 6), (36, 6), (69, 8), (4, 8), (98, 8), (99, 8), (66, 8), (96, 8), (43, 8), (10, 8), (41, 8), (79, 8), (72, 8), (46, 8), (85, 8), (16, 8), (19, 8), (58, 8), (24, 8), (30, 8), (17, 9), (64, 9), (6, 9), (53, 9), (67, 9), (77, 9), (29, 9), (2, 10), (99, 10), (6, 10), (7, 10), (76, 10), (77, 10), (10, 10), (12, 10), (17, 10), (53, 10), (82, 10), (92, 10), (95, 10), (63, 10), (62, 10), (91, 10), (59, 12), (99, 14), (28, 14), (49, 16), (100, 16), (34, 17), (32, 17), (39, 17), (9, 17), (41, 17), (14, 17), (74, 17), (44, 17), (15, 17), (51, 17), (16, 17), (87, 17), (49, 17), (18, 17), (21, 17), (82, 17), (24, 17), (92, 17), (94, 17), (60, 17), (30, 17), (100, 18), (3, 18), (64, 18), (66, 18), (37, 18), (9, 18), (43, 18), (45, 18), (85, 18), (86, 18), (81, 18), (83, 18), (93, 18), (59, 18), (94, 18), (56, 18), (89, 18), (88, 18), (61, 18), (91, 18), (60, 18), (30, 18), (16, 19), (70, 19), (18, 19), (66, 19), (7, 19), (93, 19), (29, 19), (73, 19), (38, 20), (53, 20), (95, 20), (72, 20), (44, 20), (51, 21), (65, 21), (59, 21), (49, 22), (33, 22), (4, 22), (98, 22), (99, 22), (43, 22), (92, 22), (89, 22), (46, 22), (30, 22), (70, 24), (39, 24), (97, 24), (24, 24), (46, 24), (44, 24), (91, 24), (60, 24), (21, 25), (77, 25), (10, 25), (99, 26), (27, 26), (40, 26), (78, 26), (94, 26), (47, 27), (15, 28), (69, 29), (35, 29), (100, 29), (20, 29), (96, 29), (23, 29), (22, 29), (95, 29), (56, 29), (72, 29), (65, 30), (96, 30), (24, 30), (68, 31), (3, 31), (38, 31), (99, 31), (36, 31), (66, 31), (9, 31), (10, 31), (40, 31), (72, 31), (46, 31), (44, 31), (75, 31), (53, 31), (25, 31), (58, 31), (56, 31), (89, 31), (63, 31), (17, 32), (8, 32), (42, 32), (56, 32), (74, 32), (70, 33), (4, 33), (20, 33), (59, 33), (76, 33), (92, 33), (85, 34), (17, 34), (86, 34), (33, 34), (48, 34), (85, 36), (2, 36), (100, 36), (95, 36), (94, 36), (13, 36), (28, 36), (75, 36), (1, 37), (69, 37), (100, 37), (33, 37), (7, 37), (76, 37), (47, 37), (75, 37), (85, 37), (87, 37), (19, 37), (28, 37), (91, 37), (90, 37), (38, 38), (39, 38), (53, 38), (13, 38), (91, 38), (74, 40), (17, 42), (3, 42), (38, 42), (39, 42), (23, 42), (46, 42), (89, 42), (13, 42), (39, 44), (6, 44), (47, 44), (49, 45), (100, 45), (71, 45), (54, 45), (66, 45), (24, 45), (41, 45), (11, 45), (29, 45), (14, 45), (60, 45), (75, 45), (51, 47), (69, 47), (71, 47), (38, 47), (4, 47), (39, 47), (78, 47), (62, 47), (3, 50), (96, 50), (59, 50), (40, 50), (57, 50), (88, 50), (13, 50), (74, 50);


insert into t_contact (contact, description, contact_type, notification_type, club_member_id) values
	('b3957e8d-4dcd-435f-9c30-0e20f6a4814b@yahoo.com',null, 0, 0, 88),
	('723223977',null, 1, 2, 84),
	('727339550',null, 1, 2, 37),
	('736647158',null, 1, 2, 22),
	('734928165','popis telefonního kontaktu', 1, 0, 24),
	('eb764c78-3fdf-4c8b-94c8-458e7ab4c01a@tiscali.cz','popis emailového kontaktu', 0, 2, 13),
	('f8764bdf-8f2d-4572-8dfd-f7d6a126a2e6@post.cz',null, 0, 2, 8),
	('776128026',null, 1, 0, 21),
	('735597181',null, 1, 0, 52),
	('c2f22586-7ef8-4a7b-8035-6f8134a95b4d@tiscali.cz',null, 0, 1, 29),
	('90a9fa47-0ddc-42c5-9c39-f27556ea90c6@tiscali.cz',null, 0, 2, 67),
	('0d822847-5200-4756-8614-5a62f80e3295@centrum.cz',null, 0, 2, 1),
	('922cc181-231c-4595-87e8-b0011f97aeb4@centrum.cz',null, 0, 1, 65),
	('721271385',null, 1, 0, 11),
	('737898966',null, 1, 1, 85),
	('77f3939b-ed39-41d4-b9ba-90005f24324f@seznam.cz','popis emailového kontaktu', 0, 0, 22),
	('702259773',null, 1, 1, 78),
	('6ff9703c-1421-44ed-bd36-64c50351b438@hotmail.com','popis emailového kontaktu', 0, 0, 48),
	('736061546',null, 1, 1, 70),
	('e4e75dd5-edc6-4f7e-bdfd-446f2933d47d@post.cz',null, 0, 2, 94),
	('bad89acc-6ac8-4119-8241-d79abcbb542a@volny.cz',null, 0, 1, 85),
	('35acff40-945e-4e5c-8dde-7353d17b7623@centrum.cz',null, 0, 2, 58),
	('bd839e55-ced7-4faf-8597-4fbea87c2873@hotmail.cz','popis emailového kontaktu', 0, 2, 20),
	('4ff83f61-d38d-42d6-b1c9-1d00ab3a70ba@seznam.cz',null, 0, 0, 85),
	('c96931b6-b869-4847-b62c-c61bbb842ac9@yahoo.com',null, 0, 1, 67),
	('720938209',null, 1, 2, 35),
	('734265623',null, 1, 2, 22),
	('af5e9c66-59f0-4733-b949-746ded585d8e@seznam.cz','popis emailového kontaktu', 0, 1, 43),
	('9cde3027-9d6b-4598-a26d-f00f6e98644f@gmail.com','popis emailového kontaktu', 0, 2, 76),
	('702605874',null, 1, 1, 20),
	('2d7c6424-3791-47ce-ba6f-0b54a932fa85@tiscali.cz',null, 0, 0, 12),
	('201b8a48-f2f5-423a-be44-db4cff783922@hotmail.com',null, 0, 0, 51),
	('1040e46d-1ebf-4507-80dc-65c20c3cdc8a@email.cz',null, 0, 2, 73),
	('b263d8d7-c678-444b-a47f-a6ee35722c56@post.cz','popis emailového kontaktu', 0, 0, 23),
	('733569147',null, 1, 0, 74),
	('ee1289f4-f086-4d2c-b71e-1edef9538283@yahoo.com',null, 0, 1, 6),
	('272d1f6b-2bee-4ae2-a01e-4d4f49059d69@volny.cz',null, 0, 0, 15),
	('776500883','popis telefonního kontaktu', 1, 1, 70),
	('722043131',null, 1, 0, 95),
	('607847834',null, 1, 1, 89),
	('790990182',null, 1, 2, 65),
	('85ccd7bf-990d-4c5a-8664-1d05aa5b9c5a@gmail.com','popis emailového kontaktu', 0, 0, 61),
	('8a921b1f-39e3-4758-8fd8-f47b4bcb3f2b@hotmail.cz',null, 0, 0, 85),
	('774054818',null, 1, 2, 48),
	('5bc442c0-fc7f-418a-886c-56b79e9e848e@post.cz',null, 0, 2, 18),
	('ac24b470-6371-4fbb-8a30-3369feaac0fa@centrum.cz',null, 0, 2, 66),
	('7acb9dab-98fb-4228-9661-ca63f856696b@hotmail.cz',null, 0, 0, 80),
	('3072a3fd-14d9-46de-95aa-969def55be94@hotmail.com',null, 0, 0, 67),
	('9a5ef6fa-15ba-4b2f-85d9-317522305e3b@yahoo.com',null, 0, 1, 36),
	('671d7d54-6a2f-45d7-b7b6-36a7018d8aa2@email.cz','popis emailového kontaktu', 0, 2, 42),
	('720555238','popis telefonního kontaktu', 1, 1, 53),
	('6c4bb1a8-a2f0-442e-9361-5aa5d1edfd9f@hotmail.com',null, 0, 1, 15),
	('5479d6d9-7ae9-4384-a392-bc7cc0fa8b28@seznam.cz',null, 0, 1, 93),
	('11312a3d-0cb0-49ba-b29d-1d6b01e745f8@yahoo.com',null, 0, 0, 56),
	('720341166',null, 1, 2, 47),
	('f7c6f1f7-8fa0-4da3-b31a-b922a5e1ae7c@tiscali.cz',null, 0, 0, 26),
	('5ca725dd-6d1d-4303-a179-41c4a2435530@gmail.com',null, 0, 0, 32),
	('8f2b9951-c832-4f0a-a848-974fe30fd61b@hotmail.com',null, 0, 0, 19),
	('cff6e2e9-6f53-468b-b9d8-06ef309efcbd@email.cz',null, 0, 2, 16),
	('bd019a45-7bcc-42d6-84a4-54cf57a22a94@seznam.cz',null, 0, 1, 57),
	('e337bd0f-90e3-4c7d-b9c0-b5565e78b377@post.cz','popis emailového kontaktu', 0, 1, 44),
	('08d19452-848b-487f-a966-dd500c36c131@post.cz','popis emailového kontaktu', 0, 0, 20),
	('71d1c3a4-0887-403f-b432-31907720ebd1@seznam.cz','popis emailového kontaktu', 0, 0, 89),
	('295da30e-e4c9-42ff-a092-9ec15bfb318d@yahoo.com',null, 0, 2, 74),
	('726809626',null, 1, 2, 6),
	('c0264ca8-59da-4159-ac94-21fe3cb6e777@centrum.cz',null, 0, 2, 77),
	('065353b3-1d6b-4ffe-b198-0d350bd533ef@hotmail.com',null, 0, 2, 28),
	('737376519',null, 1, 0, 78),
	('8db75c8e-9050-4daa-bac8-6a44964285da@hotmail.com',null, 0, 2, 40),
	('afa43d23-fded-4c20-8314-a6f4fd2a2926@seznam.cz',null, 0, 1, 10),
	('8b404812-df08-4f01-bbb4-84eaae6a050d@hotmail.com',null, 0, 1, 14),
	('601167774',null, 1, 0, 17),
	('946c33f9-ed8e-4c8a-a4ab-36bb87d5cc31@seznam.cz',null, 0, 0, 83),
	('607163803','popis telefonního kontaktu', 1, 2, 22),
	('0e8844db-6178-4801-94b6-a5c464c44dc4@seznam.cz','popis emailového kontaktu', 0, 1, 62),
	('726608161',null, 1, 2, 69),
	('603601200',null, 1, 2, 53),
	('fc1531fc-deed-4544-aa46-40852ea87d10@yahoo.com','popis emailového kontaktu', 0, 2, 34),
	('736651882',null, 1, 0, 100),
	('721612298',null, 1, 2, 78),
	('6c7d7007-0d96-48cb-a68b-4d4df26d70d7@hotmail.com',null, 0, 1, 88),
	('6ec744fe-0981-4b38-8e9b-b7d6b0ced55d@email.cz',null, 0, 1, 41),
	('7c01d690-8f68-4da2-8b8d-9e40cd529023@volny.cz',null, 0, 1, 24),
	('725296251',null, 1, 1, 75),
	('f4e059d8-9a6e-4737-b1db-be4bd6a0b709@hotmail.com','popis emailového kontaktu', 0, 1, 48),
	('1b238d07-b3dc-45b4-9c4d-ae666b9ff1a2@gmail.com','popis emailového kontaktu', 0, 0, 100),
	('738628795',null, 1, 2, 91),
	('8297acce-8995-4149-8f5d-357ba237c4be@volny.cz',null, 0, 1, 59),
	('2a1073f8-f4c2-41a0-a3ff-970e65533fa0@centrum.cz',null, 0, 1, 72),
	('2c25ffc2-d71d-4441-9c4d-ada6cf40e8e6@email.cz','popis emailového kontaktu', 0, 2, 9),
	('6cb3053d-8fb2-4789-8dc9-4627243c3578@post.cz',null, 0, 0, 28),
	('380f1b25-e449-4fd0-92fc-868825a8b7c3@hotmail.cz',null, 0, 2, 94),
	('738757759',null, 1, 2, 93),
	('61017070-4afc-45e0-a770-c45a24b46d27@yahoo.com',null, 0, 1, 60),
	('d390fe45-b360-406a-b42b-896da049dc65@hotmail.cz','popis emailového kontaktu', 0, 0, 21),
	('47c88123-80e5-4995-ae36-d0b4f602dae3@hotmail.com',null, 0, 0, 94),
	('67967321-0c76-4ef0-97d5-9b8285e3c7a4@tiscali.cz','popis emailového kontaktu', 0, 0, 34),
	('394696fd-35fd-4c16-b05a-df57d0701faa@email.cz',null, 0, 0, 78),
	('3df04153-6e3a-4783-8227-1aa923742206@volny.cz',null, 0, 1, 27),
	('606763195','popis telefonního kontaktu', 1, 1, 67),
	('d926964c-efd9-4175-8491-221b65cf6dbd@tiscali.cz',null, 0, 1, 67),
	('733924554',null, 1, 1, 94),
	('e24daee0-2426-4762-8064-9bf852c288e4@gmail.com',null, 0, 0, 53),
	('ee435b17-41e4-4cf6-83d3-6933504ba8a1@email.cz','popis emailového kontaktu', 0, 2, 63),
	('a0202cc2-8477-4057-906c-36e12069a5a7@hotmail.com',null, 0, 2, 89),
	('e7d05e7a-44b7-4deb-9a88-8af0c0a98c80@seznam.cz','popis emailového kontaktu', 0, 0, 87),
	('19c0a118-c0b7-44fa-b23b-0e23045e8509@hotmail.cz',null, 0, 1, 9),
	('1d334d0c-1952-4677-aba1-4aa0cf47b388@yahoo.com',null, 0, 1, 64),
	('6afccde9-0f04-4434-a0d1-b4c1deda7af3@centrum.cz',null, 0, 1, 20),
	('60e15f09-d436-4220-b808-985807244c89@centrum.cz',null, 0, 1, 67);


-- ARTICLES [AKTUALITY]
insert into T_ARTICLE (location_type, priority, caption, summary, content, creation_date, owner_type, club_team_id, category_id) values
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
insert into T_ARTICLE (location_type, priority, caption, summary, content, creation_date, expiration_date, owner_type, club_team_id, category_id) values
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
insert into T_ARTICLE (location_type, priority, caption, summary, content, creation_date, owner_type, club_team_id, category_id) values
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
insert into T_ARTICLE (location_type, priority, caption, summary, content, creation_date, expiration_date, owner_type, club_team_id, category_id) values
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


-- SEASON
insert into T_SEASON (description) values
	('Sezóna 2013'),
	('Sezóna 2014'),
	('Sezóna 2015'),
	('Sezóna 2016');
	

-- TEAM MATCH
insert into T_TEAM_MATCH (is_home_match, match_type, start, comment, club_rival_comment, publish, score_A, score_B, score_detail, season_id, club_team_id, club_rival_id) values
	(false, 3, to_timestamp('2017-7-2 14:00', 'YYYY-MM-DD HH24:MI'), 'Bomba zápas', 'To bude pohodička', false, 2, 1, '0:0, 1:0', 2, 3, 4),
	(true, 0, to_timestamp('2016-3-28 12:00', 'YYYY-MM-DD HH24:MI'), 'Super', 'To bude pohodička', true, 3, 1, '2:1, 1:3', 4, 3, 3),
	(false, 0, to_timestamp('2017-4-18 13:00', 'YYYY-MM-DD HH24:MI'), '', '', false, 4, 0, '0:0, 1:0', 4, 1, null),
	(false, 2, to_timestamp('2015-5-28 17:00', 'YYYY-MM-DD HH24:MI'), 'Bomba zápas', 'To bude pohodička', true, 0, 1, '', 3, 1, 1),
	(true, 3, to_timestamp('2017-3-30 17:00', 'YYYY-MM-DD HH24:MI'), 'Vemte si podvlíkačky', '', false, 4, 1, '2:1, 1:3', 2, 3, 1),
	(false, 0, to_timestamp('2017-12-24 11:00', 'YYYY-MM-DD HH24:MI'), 'Super', '', false, 4, 4, '', 4, 1, null),
	(true, 3, to_timestamp('2015-10-18 17:00', 'YYYY-MM-DD HH24:MI'), 'Super', 'Přijedou z daleka, je třeba zajisti ubytování, chléb a sůl', true, 4, 3, '0:0, 1:0', 2, 3, 4),
	(false, 0, to_timestamp('2016-8-19 11:00', 'YYYY-MM-DD HH24:MI'), 'Dnešní zápas se od začátku vyvíjel velmi slibně, až do poslední čtvrtiny jsme vedli nad holkami 4:2, ale bohužel jak už je to u nás zvykem opět vedení neudrželi a v posledních minutách zápasu si nechali nasázet 3góly . Škoda jinak to byl zápas pěkný. Díky Jirka', '', false, 3, 2, '0:0, 1:0', 2, 3, null),
	(true, 3, to_timestamp('2016-5-10 12:00', 'YYYY-MM-DD HH24:MI'), 'Poslední domácí zápas, třetí v řadě s týmem silné čtyřky a další vítězství. Zdá se tedy vše ideální, nicméně předvedená hra měla k dokonalosti přeci jen nějaký ten krůček.. Přivítali jsme omlazený tým Kačerova a i jsme tomu přizpůsobili předzápasovou taktiku. Předpokládali jsme, že soupeř bude běhavější a nebylo naším cílem se s ním honit po hřišti. Do sestavy se po dvou zápasech vrátil Ondra Janda a do branky se opět postavil Petr Bureš. V počátku utkání nás soupeř lehce tlačil, ale vyložené šance si vypracovat nedokázal. Naopak v 10 minutě si na centr naběhl Johnny a hlavou k tyči nás poslal do vedení. Soupeře to očividně zaskočilo a tlak počal polevovat. Naopak jsme to byli my, kdo se díky brejkům dostával do šancí, ale ani Hoskovec, ani Zahradník, Leo a Johnny v dalších gólovkách neuspěli. Naopak s přibývajícím časem se soupeř opět dostával do tlaku a Fury v brance musel několikrát čarovat.. Poločas tedy skončil naším jednogólovým vedením. Do druhého poločasu naskočil místo zraněného Honzy Málka Tomáš Pivokonský, což přineslo do naší hry zklidnění a dokázali jsme podržet déle míč. Soupeř hrál stále stejně naivně a když už se dostal do šance, tak nevyzrál na připraveného Bureše. Bohužel v 59 minutě se v ofsaidové pasti zapomněl Michal Zouhar a nabídl tak hostujícímu útočníkovi nájezd na našeho gólmana – 1:1 Sypu si popel na hlavu…. Naštěstí se ukázal opět zlobivec Janda a po centru od Píva se prosadil jako při prvním našem gólu hlavou. Nutno dodat, že hostující obránci byly přibližně o hlavu větší… V závěru se soupeř snažil o jakýsi tlak a v jedné šanci se projevila naplno jeho herní nezkušenost, když naprosto jasnou gólovou šanci dokázal překombinovat a zahodit. Navíc se zbytečně (pro nás pochopitelně dobře) rozptyloval častým simulováním a tak jsme zápas dotáhli do vítězného konce. Za zmínku ještě stojí tyč Tomáše Pivokonského, který si po nahrávce Zouhara posadil na zadek soupeřovic beka a placírkou málem skóroval. V příštím týdnu nás čeká těžký zápas v Nebušicích, který věříme že zvládneme a přezimujeme tak na prvním místě tabulky. Sestava: Bureš Petr, Staněk Robert, Srp Roman, Tomáš Vodrážka (Hrabálek Jarda), Babica Jaroušek , Zouhar Michal, Málek  Honzík (Pivokonský Tom), Martin Hoskovců, Marku Leonard, Ondra Janda (Mates Holubů)', '', true, 0, 4, '1:2, 1:0, 0:0, 0:1 ', 2, 3, 9),
	(true, 0, to_timestamp('2016-5-12 17:00', 'YYYY-MM-DD HH24:MI'), 'Opět jsme nastoupili v málo hráčích, a to je náš jarní kolorit. Tři hráči v práci a Pejičič v trestu na dvě utkání za kritiku vedení malé kopané, která pod vedením Nehery, který všem na komisi lhal jak když tiskne a všichni jak ovce odsouhlasili jeho lživou verzi a neschopnost řešit některé situace logicky,pragmaticky a to co si dnešní doba žádá ve sportě.', 'To bude pohodička', false, 3, 0, '', 3, 1, 3),
	(false, 3, to_timestamp('2015-6-7 16:00', 'YYYY-MM-DD HH24:MI'), 'Opět jsme nastoupili v málo hráčích, a to je náš jarní kolorit. Tři hráči v práci a Pejičič v trestu na dvě utkání za kritiku vedení malé kopané, která pod vedením Nehery, který všem na komisi lhal jak když tiskne a všichni jak ovce odsouhlasili jeho lživou verzi a neschopnost řešit některé situace logicky,pragmaticky a to co si dnešní doba žádá ve sportě.', 'A ja jaj', true, 3, 0, '0:0, 1:0', 2, 2, 1),
	(true, 0, to_timestamp('2015-12-30 14:00', 'YYYY-MM-DD HH24:MI'), null, 'A ja jaj', true, 0, 3, '0:0, 1:0', 3, 2, 7),
	(true, 3, to_timestamp('2015-11-25 16:00', 'YYYY-MM-DD HH24:MI'), 'Super', 'To bude pohodička', false, 1, 0, '0:0, 1:0', 2, 1, 1),
	(true, 0, to_timestamp('2017-4-12 13:00', 'YYYY-MM-DD HH24:MI'), 'Poslední domácí zápas, třetí v řadě s týmem silné čtyřky a další vítězství. Zdá se tedy vše ideální, nicméně předvedená hra měla k dokonalosti přeci jen nějaký ten krůček.. Přivítali jsme omlazený tým Kačerova a i jsme tomu přizpůsobili předzápasovou taktiku. Předpokládali jsme, že soupeř bude běhavější a nebylo naším cílem se s ním honit po hřišti. Do sestavy se po dvou zápasech vrátil Ondra Janda a do branky se opět postavil Petr Bureš. V počátku utkání nás soupeř lehce tlačil, ale vyložené šance si vypracovat nedokázal. Naopak v 10 minutě si na centr naběhl Johnny a hlavou k tyči nás poslal do vedení. Soupeře to očividně zaskočilo a tlak počal polevovat. Naopak jsme to byli my, kdo se díky brejkům dostával do šancí, ale ani Hoskovec, ani Zahradník, Leo a Johnny v dalších gólovkách neuspěli. Naopak s přibývajícím časem se soupeř opět dostával do tlaku a Fury v brance musel několikrát čarovat.. Poločas tedy skončil naším jednogólovým vedením. Do druhého poločasu naskočil místo zraněného Honzy Málka Tomáš Pivokonský, což přineslo do naší hry zklidnění a dokázali jsme podržet déle míč. Soupeř hrál stále stejně naivně a když už se dostal do šance, tak nevyzrál na připraveného Bureše. Bohužel v 59 minutě se v ofsaidové pasti zapomněl Michal Zouhar a nabídl tak hostujícímu útočníkovi nájezd na našeho gólmana – 1:1 Sypu si popel na hlavu…. Naštěstí se ukázal opět zlobivec Janda a po centru od Píva se prosadil jako při prvním našem gólu hlavou. Nutno dodat, že hostující obránci byly přibližně o hlavu větší… V závěru se soupeř snažil o jakýsi tlak a v jedné šanci se projevila naplno jeho herní nezkušenost, když naprosto jasnou gólovou šanci dokázal překombinovat a zahodit. Navíc se zbytečně (pro nás pochopitelně dobře) rozptyloval častým simulováním a tak jsme zápas dotáhli do vítězného konce. Za zmínku ještě stojí tyč Tomáše Pivokonského, který si po nahrávce Zouhara posadil na zadek soupeřovic beka a placírkou málem skóroval. V příštím týdnu nás čeká těžký zápas v Nebušicích, který věříme že zvládneme a přezimujeme tak na prvním místě tabulky. Sestava: Bureš Petr, Staněk Robert, Srp Roman, Tomáš Vodrážka (Hrabálek Jarda), Babica Jaroušek , Zouhar Michal, Málek  Honzík (Pivokonský Tom), Martin Hoskovců, Marku Leonard, Ondra Janda (Mates Holubů)', 'To bude pohodička', true, 1, 2, '2:1, 1:3', 4, 1, 3),
	(true, 0, to_timestamp('2017-12-19 12:00', 'YYYY-MM-DD HH24:MI'), 'Super', '', false, 0, 4, '', 1, 3, null),
	(false, 0, to_timestamp('2016-2-17 15:00', 'YYYY-MM-DD HH24:MI'), 'Opět jsme nastoupili v málo hráčích, a to je náš jarní kolorit. Tři hráči v práci a Pejičič v trestu na dvě utkání za kritiku vedení malé kopané, která pod vedením Nehery, který všem na komisi lhal jak když tiskne a všichni jak ovce odsouhlasili jeho lživou verzi a neschopnost řešit některé situace logicky,pragmaticky a to co si dnešní doba žádá ve sportě.', '', true, 0, 3, '', 1, 2, 1),
	(false, 0, to_timestamp('2014-3-22 14:00', 'YYYY-MM-DD HH24:MI'), 'Poslední domácí zápas, třetí v řadě s týmem silné čtyřky a další vítězství. Zdá se tedy vše ideální, nicméně předvedená hra měla k dokonalosti přeci jen nějaký ten krůček.. Přivítali jsme omlazený tým Kačerova a i jsme tomu přizpůsobili předzápasovou taktiku. Předpokládali jsme, že soupeř bude běhavější a nebylo naším cílem se s ním honit po hřišti. Do sestavy se po dvou zápasech vrátil Ondra Janda a do branky se opět postavil Petr Bureš. V počátku utkání nás soupeř lehce tlačil, ale vyložené šance si vypracovat nedokázal. Naopak v 10 minutě si na centr naběhl Johnny a hlavou k tyči nás poslal do vedení. Soupeře to očividně zaskočilo a tlak počal polevovat. Naopak jsme to byli my, kdo se díky brejkům dostával do šancí, ale ani Hoskovec, ani Zahradník, Leo a Johnny v dalších gólovkách neuspěli. Naopak s přibývajícím časem se soupeř opět dostával do tlaku a Fury v brance musel několikrát čarovat.. Poločas tedy skončil naším jednogólovým vedením. Do druhého poločasu naskočil místo zraněného Honzy Málka Tomáš Pivokonský, což přineslo do naší hry zklidnění a dokázali jsme podržet déle míč. Soupeř hrál stále stejně naivně a když už se dostal do šance, tak nevyzrál na připraveného Bureše. Bohužel v 59 minutě se v ofsaidové pasti zapomněl Michal Zouhar a nabídl tak hostujícímu útočníkovi nájezd na našeho gólmana – 1:1 Sypu si popel na hlavu…. Naštěstí se ukázal opět zlobivec Janda a po centru od Píva se prosadil jako při prvním našem gólu hlavou. Nutno dodat, že hostující obránci byly přibližně o hlavu větší… V závěru se soupeř snažil o jakýsi tlak a v jedné šanci se projevila naplno jeho herní nezkušenost, když naprosto jasnou gólovou šanci dokázal překombinovat a zahodit. Navíc se zbytečně (pro nás pochopitelně dobře) rozptyloval častým simulováním a tak jsme zápas dotáhli do vítězného konce. Za zmínku ještě stojí tyč Tomáše Pivokonského, který si po nahrávce Zouhara posadil na zadek soupeřovic beka a placírkou málem skóroval. V příštím týdnu nás čeká těžký zápas v Nebušicích, který věříme že zvládneme a přezimujeme tak na prvním místě tabulky. Sestava: Bureš Petr, Staněk Robert, Srp Roman, Tomáš Vodrážka (Hrabálek Jarda), Babica Jaroušek , Zouhar Michal, Málek  Honzík (Pivokonský Tom), Martin Hoskovců, Marku Leonard, Ondra Janda (Mates Holubů)', 'To bude pohodička', false, 0, 4, '', 2, 3, 3),
	(false, 0, to_timestamp('2014-8-23 17:00', 'YYYY-MM-DD HH24:MI'), 'Dnešní zápas se od začátku vyvíjel velmi slibně, až do poslední čtvrtiny jsme vedli nad holkami 4:2, ale bohužel jak už je to u nás zvykem opět vedení neudrželi a v posledních minutách zápasu si nechali nasázet 3góly . Škoda jinak to byl zápas pěkný. Díky Jirka', 'A ja jaj', true, 3, 3, '0:0, 1:0', 2, 3, 8),
	(true, 1, to_timestamp('2014-6-19 14:00', 'YYYY-MM-DD HH24:MI'), 'Poslední domácí zápas, třetí v řadě s týmem silné čtyřky a další vítězství. Zdá se tedy vše ideální, nicméně předvedená hra měla k dokonalosti přeci jen nějaký ten krůček.. Přivítali jsme omlazený tým Kačerova a i jsme tomu přizpůsobili předzápasovou taktiku. Předpokládali jsme, že soupeř bude běhavější a nebylo naším cílem se s ním honit po hřišti. Do sestavy se po dvou zápasech vrátil Ondra Janda a do branky se opět postavil Petr Bureš. V počátku utkání nás soupeř lehce tlačil, ale vyložené šance si vypracovat nedokázal. Naopak v 10 minutě si na centr naběhl Johnny a hlavou k tyči nás poslal do vedení. Soupeře to očividně zaskočilo a tlak počal polevovat. Naopak jsme to byli my, kdo se díky brejkům dostával do šancí, ale ani Hoskovec, ani Zahradník, Leo a Johnny v dalších gólovkách neuspěli. Naopak s přibývajícím časem se soupeř opět dostával do tlaku a Fury v brance musel několikrát čarovat.. Poločas tedy skončil naším jednogólovým vedením. Do druhého poločasu naskočil místo zraněného Honzy Málka Tomáš Pivokonský, což přineslo do naší hry zklidnění a dokázali jsme podržet déle míč. Soupeř hrál stále stejně naivně a když už se dostal do šance, tak nevyzrál na připraveného Bureše. Bohužel v 59 minutě se v ofsaidové pasti zapomněl Michal Zouhar a nabídl tak hostujícímu útočníkovi nájezd na našeho gólmana – 1:1 Sypu si popel na hlavu…. Naštěstí se ukázal opět zlobivec Janda a po centru od Píva se prosadil jako při prvním našem gólu hlavou. Nutno dodat, že hostující obránci byly přibližně o hlavu větší… V závěru se soupeř snažil o jakýsi tlak a v jedné šanci se projevila naplno jeho herní nezkušenost, když naprosto jasnou gólovou šanci dokázal překombinovat a zahodit. Navíc se zbytečně (pro nás pochopitelně dobře) rozptyloval častým simulováním a tak jsme zápas dotáhli do vítězného konce. Za zmínku ještě stojí tyč Tomáše Pivokonského, který si po nahrávce Zouhara posadil na zadek soupeřovic beka a placírkou málem skóroval. V příštím týdnu nás čeká těžký zápas v Nebušicích, který věříme že zvládneme a přezimujeme tak na prvním místě tabulky. Sestava: Bureš Petr, Staněk Robert, Srp Roman, Tomáš Vodrážka (Hrabálek Jarda), Babica Jaroušek , Zouhar Michal, Málek  Honzík (Pivokonský Tom), Martin Hoskovců, Marku Leonard, Ondra Janda (Mates Holubů)', '', true, 4, 4, '', 3, 1, null),
	(true, 0, to_timestamp('2016-8-25 10:00', 'YYYY-MM-DD HH24:MI'), 'Opět jsme nastoupili v málo hráčích, a to je náš jarní kolorit. Tři hráči v práci a Pejičič v trestu na dvě utkání za kritiku vedení malé kopané, která pod vedením Nehery, který všem na komisi lhal jak když tiskne a všichni jak ovce odsouhlasili jeho lživou verzi a neschopnost řešit některé situace logicky,pragmaticky a to co si dnešní doba žádá ve sportě.', '', true, 2, 1, '1:2, 1:0, 0:0, 0:1 ', 2, 2, null),
	(false, 0, to_timestamp('2015-8-20 16:00', 'YYYY-MM-DD HH24:MI'), 'Dnešní zápas se od začátku vyvíjel velmi slibně, až do poslední čtvrtiny jsme vedli nad holkami 4:2, ale bohužel jak už je to u nás zvykem opět vedení neudrželi a v posledních minutách zápasu si nechali nasázet 3góly . Škoda jinak to byl zápas pěkný. Díky Jirka', '', true, 3, 1, '0:0, 1:0', 4, 2, 1),
	(true, 2, to_timestamp('2016-1-4 12:00', 'YYYY-MM-DD HH24:MI'), 'Vemte si podvlíkačky', '', false, 3, 3, '0:0, 1:0', 1, 3, 1),
	(false, 0, to_timestamp('2017-5-16 10:00', 'YYYY-MM-DD HH24:MI'), 'Super', 'Přijedou z daleka, je třeba zajisti ubytování, chléb a sůl', true, 1, 0, '0:0, 1:0', 3, 2, 5),
	(false, 0, to_timestamp('2015-4-7 15:00', 'YYYY-MM-DD HH24:MI'), 'Super', 'Přijedou z daleka, je třeba zajisti ubytování, chléb a sůl', false, 2, 2, '', 4, 1, 7),
	(true, 0, to_timestamp('2016-6-15 17:00', 'YYYY-MM-DD HH24:MI'), 'Bomba zápas', 'A ja jaj', true, 3, 4, '1:2, 1:0, 0:0, 0:1 ', 1, 1, 4),
	(false, 2, to_timestamp('2016-8-7 16:00', 'YYYY-MM-DD HH24:MI'), 'Super', 'A ja jaj', false, 0, 1, '2:1, 1:3', 3, 3, 7),
	(true, 0, to_timestamp('2017-6-17 17:00', 'YYYY-MM-DD HH24:MI'), 'Vemte si podvlíkačky', '', true, 4, 0, '2:1, 1:3', 2, 1, 5),
	(true, 3, to_timestamp('2015-5-24 15:00', 'YYYY-MM-DD HH24:MI'), 'Super', null, true, 4, 1, '1:2, 1:0, 0:0, 0:1 ', 1, 3, null),
	(false, 0, to_timestamp('2014-1-28 11:00', 'YYYY-MM-DD HH24:MI'), 'Opět jsme nastoupili v málo hráčích, a to je náš jarní kolorit. Tři hráči v práci a Pejičič v trestu na dvě utkání za kritiku vedení malé kopané, která pod vedením Nehery, který všem na komisi lhal jak když tiskne a všichni jak ovce odsouhlasili jeho lživou verzi a neschopnost řešit některé situace logicky,pragmaticky a to co si dnešní doba žádá ve sportě.', 'To bude pohodička', false, 1, 0, '1:2, 1:0, 0:0, 0:1 ', 2, 3, 6),
	(true, 1, to_timestamp('2016-11-25 10:00', 'YYYY-MM-DD HH24:MI'), '', '', true, 1, 2, '', 3, 1, 6);








-- PARTICIPANT OF MATCH
insert into t_participant_of_match values 
	(0, 0, 0, 34, 1), (0, 1, 1, 1, 1), (0, 0, 0, 32, 1), (1, 2, 1, 71, 1), (1, 0, 0, 98, 1), (0, 0, 1, 96, 1), (2, 2, 0, 78, 1), (1, 0, 1, 73, 1), (1, 0, 0, 85, 1), (2, 1, 1, 51, 1), (1, 2, 1, 87, 1), (1, 2, 0, 49, 1), (1, 1, 1, 21, 1), (1, 1, 1, 52, 1), (1, 0, 0, 94, 1), (1, 2, 1, 62, 1), (0, 2, 1, 100, 2), (0, 0, 1, 3, 2), (0, 2, 1, 38, 2), (1, 0, 1, 6, 2), (2, 0, 1, 43, 2), (0, 0, 1, 9, 2), (2, 0, 1, 78, 2), (2, 1, 1, 11, 2), (1, 0, 1, 45, 2), (2, 1, 0, 85, 2), (2, 1, 1, 87, 2), (2, 2, 0, 86, 2), (0, 0, 1, 54, 2), (0, 0, 0, 53, 2), (2, 1, 1, 58, 2), (0, 1, 0, 27, 2), (2, 1, 0, 57, 2), (1, 1, 1, 30, 2), (1, 2, 0, 2, 3), (0, 1, 0, 32, 3), (2, 2, 0, 100, 3), (2, 2, 1, 6, 3), (1, 2, 0, 67, 3), (0, 2, 1, 37, 3), (0, 2, 0, 77, 3), (0, 1, 0, 41, 3), (2, 1, 0, 13, 3), (2, 0, 0, 44, 3), (0, 1, 0, 14, 3), (1, 2, 1, 51, 3), (2, 2, 1, 87, 3), (2, 1, 0, 21, 3), (2, 0, 0, 83, 3), (2, 2, 1, 22, 3), (1, 1, 1, 25, 3), (1, 0, 0, 27, 3), (2, 0, 1, 29, 3), (1, 2, 1, 28, 3), (2, 0, 0, 35, 4), (2, 2, 1, 96, 4), (0, 0, 1, 37, 4), (0, 0, 0, 7, 4), (2, 0, 1, 67, 4), (0, 1, 1, 76, 4), (2, 0, 0, 79, 4), (2, 1, 1, 14, 4), (2, 2, 1, 45, 4), (2, 1, 0, 50, 4), (0, 0, 0, 80, 4), (1, 1, 0, 83, 4), (2, 0, 1, 59, 4), (2, 2, 1, 57, 4), (0, 1, 1, 95, 4), (2, 0, 1, 28, 4), (0, 0, 0, 31, 4), (2, 0, 0, 91, 4), (1, 0, 0, 30, 4), (2, 2, 0, 34, 5), (1, 1, 0, 69, 5), (2, 1, 0, 33, 5), (0, 1, 0, 3, 5), (0, 2, 0, 67, 5), (2, 0, 0, 76, 5), (0, 2, 1, 43, 5), (0, 1, 1, 12, 5), (0, 1, 0, 47, 5), (0, 0, 0, 13, 5), (1, 0, 0, 44, 5), (1, 1, 1, 51, 5), (0, 0, 1, 55, 5), (2, 0, 1, 81, 5), (2, 1, 1, 54, 5), (1, 2, 0, 53, 5), (2, 2, 1, 59, 5), (2, 2, 0, 88, 5), (0, 0, 0, 34, 6), (1, 1, 1, 99, 6), (1, 1, 1, 37, 6), (0, 1, 0, 97, 6), (0, 2, 1, 7, 6), (1, 2, 0, 10, 6), (0, 1, 1, 11, 6), (2, 2, 1, 47, 6), (2, 1, 1, 75, 6), (0, 2, 0, 85, 6), (0, 0, 0, 87, 6), (2, 2, 0, 54, 6), (0, 1, 0, 83, 6), (0, 2, 1, 22, 6), (0, 0, 0, 24, 6), (1, 1, 1, 26, 6), (1, 1, 0, 90, 6), (1, 1, 1, 2, 7), (0, 0, 1, 98, 7), (0, 0, 1, 65, 7), (2, 1, 1, 99, 7), (1, 1, 0, 6, 7), (0, 1, 1, 36, 7), (1, 2, 1, 96, 7), (2, 0, 0, 97, 7), (2, 1, 0, 42, 7), (1, 1, 1, 77, 7), (0, 2, 0, 43, 7), (2, 0, 1, 74, 7), (0, 2, 1, 14, 7), (2, 0, 0, 19, 7), (2, 1, 0, 18, 7), (2, 2, 1, 86, 7), (0, 1, 1, 53, 7), (1, 0, 0, 93, 7), (0, 0, 0, 59, 7), (0, 1, 0, 1, 8), (2, 0, 0, 36, 8), (1, 2, 0, 42, 8), (1, 1, 1, 12, 8), (1, 1, 1, 73, 8), (2, 2, 1, 14, 8), (2, 2, 1, 44, 8), (2, 1, 1, 15, 8), (2, 1, 0, 87, 8), (2, 0, 1, 48, 8), (1, 2, 1, 82, 8), (0, 1, 0, 93, 8), (1, 1, 0, 95, 8), (0, 1, 1, 56, 8), (1, 1, 1, 63, 8), (2, 2, 1, 88, 8), (1, 1, 0, 31, 8), (1, 1, 1, 90, 8), (1, 1, 1, 30, 8), (0, 1, 1, 34, 9), (2, 0, 0, 71, 9), (1, 2, 0, 38, 9), (0, 1, 1, 6, 9), (1, 2, 1, 7, 9), (0, 0, 0, 76, 9), (2, 2, 0, 43, 9), (1, 1, 1, 9, 9), (1, 2, 0, 78, 9), (1, 0, 0, 74, 9), (1, 2, 1, 44, 9), (2, 1, 0, 16, 9), (0, 0, 0, 19, 9), (0, 0, 1, 55, 9), (1, 1, 1, 20, 9), (2, 2, 1, 54, 9), (2, 2, 0, 82, 9), (2, 1, 0, 59, 9), (2, 2, 1, 24, 9), (1, 0, 1, 90, 9), (2, 2, 0, 32, 10), (2, 1, 0, 4, 10), (1, 0, 0, 64, 10), (2, 0, 1, 66, 10), (0, 2, 1, 67, 10), (0, 1, 1, 11, 10), (0, 0, 0, 47, 10), (1, 1, 1, 44, 10), (2, 2, 0, 19, 10), (0, 1, 0, 23, 10), (2, 0, 0, 59, 10), (1, 0, 0, 25, 10), (0, 0, 0, 57, 10), (2, 0, 1, 95, 10), (0, 2, 1, 56, 10), (0, 2, 0, 28, 10), (0, 0, 1, 31, 10), (1, 0, 1, 60, 10), (2, 0, 0, 3, 11), (1, 1, 1, 71, 11), (1, 0, 1, 38, 11), (0, 1, 1, 42, 11), (0, 0, 0, 11, 11), (0, 0, 0, 73, 11), (1, 0, 1, 13, 11), (1, 0, 1, 16, 11), (0, 1, 0, 49, 11), (1, 2, 1, 87, 11), (0, 0, 0, 21, 11), (2, 2, 0, 23, 11), (0, 1, 0, 24, 11), (2, 0, 1, 27, 11), (2, 1, 0, 95, 11), (0, 1, 1, 26, 11), (0, 2, 1, 29, 11), (1, 1, 1, 28, 11), (0, 1, 1, 61, 11), (2, 0, 0, 90, 11), (0, 2, 0, 68, 12), (1, 0, 0, 33, 12), (2, 1, 0, 4, 12), (0, 1, 0, 98, 12), (1, 1, 0, 42, 12), (2, 0, 0, 78, 12), (1, 1, 1, 11, 12), (2, 2, 0, 74, 12), (1, 2, 1, 51, 12), (0, 2, 1, 84, 12), (0, 0, 1, 16, 12), (0, 1, 0, 83, 12), (1, 0, 1, 92, 12), (0, 2, 0, 57, 12), (0, 1, 0, 56, 12), (2, 2, 1, 26, 12), (0, 0, 1, 62, 12), (2, 2, 1, 88, 12), (0, 2, 0, 30, 12), (0, 0, 0, 69, 14), (1, 2, 0, 71, 14), (1, 2, 0, 6, 14), (2, 0, 0, 7, 14), (2, 1, 0, 97, 14), (0, 0, 0, 78, 14), (2, 0, 1, 74, 14), (2, 2, 0, 15, 14), (1, 2, 0, 16, 14), (1, 2, 0, 19, 14), (0, 1, 0, 86, 14), (2, 0, 0, 81, 14), (0, 1, 1, 20, 14), (0, 2, 0, 92, 14), (2, 1, 1, 56, 14), (2, 2, 1, 30, 14), (2, 2, 0, 90, 14), (0, 0, 1, 68, 15), (2, 1, 1, 34, 15), (0, 2, 0, 35, 15), (0, 2, 1, 71, 15), (2, 2, 1, 99, 15), (2, 1, 0, 76, 15), (0, 2, 1, 42, 15), (0, 1, 1, 15, 15), (2, 1, 0, 51, 15), (2, 0, 0, 19, 15), (1, 2, 0, 49, 15), (1, 0, 0, 54, 15), (1, 0, 1, 82, 15), (0, 2, 0, 95, 15), (1, 1, 1, 63, 15), (2, 1, 1, 29, 15), (1, 1, 0, 89, 15), (2, 0, 0, 30, 15), (1, 0, 1, 90, 15), (2, 2, 1, 69, 18), (2, 2, 0, 2, 18), (1, 1, 0, 32, 18), (2, 1, 1, 98, 18), (1, 1, 1, 4, 18), (1, 0, 1, 99, 18), (0, 2, 0, 5, 18), (2, 0, 0, 36, 18), (1, 2, 0, 7, 18), (2, 0, 1, 8, 18), (0, 0, 0, 77, 18), (2, 1, 1, 85, 18), (0, 1, 0, 17, 18), (0, 0, 1, 50, 18), (0, 0, 1, 16, 18), (0, 0, 1, 87, 18), (2, 1, 1, 48, 18), (2, 1, 1, 26, 18), (1, 2, 1, 89, 18), (2, 0, 1, 68, 19), (0, 2, 0, 32, 19), (1, 1, 1, 3, 19), (0, 2, 0, 38, 19), (2, 1, 0, 64, 19), (2, 1, 1, 97, 19), (0, 1, 1, 13, 19), (2, 0, 0, 74, 19), (0, 2, 1, 14, 19), (2, 0, 1, 15, 19), (0, 0, 0, 75, 19), (0, 1, 1, 85, 19), (2, 1, 0, 51, 19), (1, 2, 1, 87, 19), (1, 0, 0, 83, 19), (1, 0, 0, 92, 19), (1, 1, 1, 58, 19), (0, 0, 1, 26, 19), (1, 0, 0, 31, 19), (2, 1, 1, 35, 20), (1, 1, 0, 100, 20), (2, 2, 1, 98, 20), (0, 2, 1, 40, 20), (0, 2, 1, 41, 20), (0, 0, 0, 74, 20), (1, 2, 1, 15, 20), (0, 1, 0, 75, 20), (1, 0, 1, 16, 20), (2, 1, 0, 81, 20), (2, 0, 0, 80, 20), (2, 0, 1, 20, 20), (2, 2, 0, 83, 20), (0, 2, 1, 82, 20), (1, 0, 0, 93, 20), (0, 2, 1, 92, 20), (1, 1, 1, 31, 20), (1, 2, 1, 60, 20), (2, 1, 0, 68, 21), (1, 1, 0, 1, 21), (0, 0, 0, 65, 21), (1, 1, 0, 96, 21), (0, 0, 0, 7, 21), (2, 0, 1, 76, 21), (1, 0, 1, 12, 21), (1, 2, 1, 85, 21), (2, 1, 0, 17, 21), (1, 1, 1, 84, 21), (2, 0, 0, 21, 21), (1, 1, 0, 80, 21), (2, 2, 0, 20, 21), (0, 0, 1, 54, 21), (1, 2, 1, 22, 21), (2, 0, 0, 93, 21), (2, 2, 1, 92, 21), (0, 0, 1, 61, 21), (2, 2, 0, 100, 22), (1, 1, 1, 33, 22), (0, 2, 0, 37, 22), (0, 2, 1, 43, 22), (1, 1, 0, 77, 22), (1, 1, 1, 40, 22), (0, 0, 0, 74, 22), (0, 2, 0, 15, 22), (0, 0, 1, 75, 22), (2, 2, 1, 17, 22), (2, 2, 0, 19, 22), (2, 0, 1, 49, 22), (1, 2, 0, 18, 22), (2, 1, 1, 48, 22), (2, 1, 0, 81, 22), (1, 2, 0, 80, 22), (0, 0, 0, 93, 22), (2, 0, 0, 58, 22), (0, 1, 0, 57, 22), (2, 2, 0, 88, 22), (1, 2, 0, 71, 23), (1, 1, 1, 3, 23), (2, 0, 0, 38, 23), (1, 2, 1, 4, 23), (1, 1, 0, 66, 23), (0, 1, 0, 41, 23), (2, 0, 1, 51, 23), (0, 2, 1, 50, 23), (0, 0, 1, 87, 23), (2, 2, 1, 80, 23), (2, 1, 0, 53, 23), (1, 0, 1, 83, 23), (2, 1, 1, 23, 23), (1, 1, 1, 25, 23), (2, 1, 1, 94, 23), (2, 0, 0, 63, 23), (1, 0, 1, 62, 23), (1, 0, 0, 30, 23), (2, 1, 1, 70, 24), (2, 0, 0, 3, 24), (0, 1, 1, 64, 24), (1, 2, 1, 7, 24), (0, 2, 0, 43, 24), (0, 2, 1, 10, 24), (1, 2, 0, 40, 24), (0, 2, 0, 72, 24), (0, 1, 1, 13, 24), (0, 2, 1, 73, 24), (2, 2, 0, 44, 24), (0, 2, 1, 85, 24), (0, 2, 1, 80, 24), (0, 0, 1, 53, 24), (2, 2, 0, 24, 24), (2, 1, 1, 27, 24), (2, 0, 0, 57, 24), (2, 2, 1, 62, 24), (2, 2, 0, 31, 24), (2, 0, 1, 32, 25), (0, 1, 1, 64, 25), (2, 2, 1, 38, 25), (0, 2, 1, 36, 25), (0, 0, 0, 96, 25), (0, 2, 0, 43, 25), (2, 2, 1, 79, 25), (2, 1, 1, 73, 25), (1, 2, 0, 74, 25), (0, 1, 1, 15, 25), (0, 1, 0, 75, 25), (2, 0, 0, 24, 25), (1, 0, 0, 29, 25), (2, 1, 0, 89, 25), (2, 0, 0, 91, 25), (1, 2, 0, 61, 25), (2, 2, 1, 60, 25), (0, 2, 1, 30, 25), (0, 1, 0, 1, 26), (1, 2, 1, 2, 26), (2, 1, 0, 33, 26), (0, 2, 0, 7, 26), (2, 0, 1, 77, 26), (1, 2, 0, 10, 26), (1, 2, 1, 78, 26), (2, 2, 1, 12, 26), (2, 1, 1, 14, 26), (1, 1, 1, 45, 26), (2, 2, 0, 51, 26), (0, 2, 1, 48, 26), (0, 2, 0, 55, 26), (1, 0, 1, 54, 26), (1, 2, 0, 82, 26), (2, 1, 1, 56, 26), (2, 1, 1, 89, 26), (0, 1, 0, 62, 26), (0, 0, 1, 68, 27), (2, 1, 1, 1, 27), (2, 0, 0, 98, 27), (1, 1, 1, 97, 27), (2, 0, 1, 7, 27), (2, 0, 1, 78, 27), (2, 0, 0, 14, 27), (1, 2, 1, 45, 27), (2, 2, 0, 17, 27), (1, 2, 1, 50, 27), (2, 0, 0, 25, 27), (0, 2, 0, 92, 27), (0, 1, 1, 56, 27), (0, 0, 1, 63, 27), (2, 2, 0, 89, 27), (2, 2, 0, 62, 27), (0, 0, 0, 28, 27), (1, 0, 1, 61, 27), (2, 0, 0, 60, 27), (2, 2, 0, 35, 28), (2, 2, 1, 1, 28), (1, 1, 0, 32, 28), (0, 1, 1, 4, 28), (1, 2, 0, 5, 28), (1, 0, 1, 7, 28), (2, 2, 1, 42, 28), (2, 0, 0, 43, 28), (2, 0, 0, 40, 28), (1, 1, 0, 79, 28), (1, 0, 1, 74, 28), (2, 1, 1, 49, 28), (1, 2, 1, 55, 28), (1, 2, 0, 21, 28), (2, 2, 0, 82, 28), (0, 2, 0, 62, 28), (2, 0, 0, 30, 28), (2, 0, 1, 2, 30), (0, 1, 1, 36, 30), (1, 2, 0, 96, 30), (2, 0, 0, 7, 30), (2, 2, 0, 67, 30), (0, 1, 1, 77, 30), (0, 2, 1, 9, 30), (1, 1, 1, 41, 30), (1, 0, 0, 15, 30), (0, 0, 0, 85, 30), (0, 2, 0, 80, 30), (2, 0, 1, 82, 30), (1, 1, 1, 24, 30), (1, 0, 0, 92, 30), (2, 0, 1, 94, 30), (2, 1, 1, 63, 30), (2, 2, 0, 88, 30), (1, 1, 0, 91, 30);

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
	update T_CLUB set logo = (select bytea_import('clubeek_pictures/club_logo.png')) where id = 1;
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
-- Location type
---- 1 aktuality
---- 0 nástěnka

-- Owner type
---- 3 celý web
---- 2 pouze tým
---- 1 pouze kategorie
---- 0 pouze klub