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