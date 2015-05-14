------------------------------
-- Dropping tables if exist --
------------------------------

drop table if exists club_settings;
drop table if exists contact;
drop table if exists applicant_for_action;
drop table if exists applicant_for_team_training;
drop table if exists applicant_for_team_match;
drop table if exists user_data;
drop table if exists participant_of_training;
drop table if exists participant_of_match;
drop table if exists action;
drop table if exists team_training;

drop view if exists club_member_by_team;
drop table if exists team_member;
drop table if exists club_member;
drop table if exists team_match;
drop table if exists club_rival;
drop table if exists article;
drop table if exists club_team;
drop table if exists category;


---------------------
-- Creating tables --
---------------------

CREATE TABLE club_settings
(
    id 		serial,
    title	varchar(200),
    comment	varchar(200),
    logo	bytea,

    primary key (id)
);

CREATE TABLE category
(
    id			serial,
    description		varchar(100)	not null,
    active		boolean	default 'true',
    sorting		integer		default 0,

    primary key (id)
);

CREATE TABLE club_member
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

CREATE TABLE club_rival
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

CREATE TABLE club_team
(
    id			serial,
    name		varchar(100)		not null,
    active		boolean,
    sorting		integer 		not null  default 0,
    category_id		integer	        	default null,

    primary key (id),
    foreign key (category_id) references category (id)
        ON DELETE SET NULL
);

CREATE TABLE contact
(
    id 			serial,
    contact		varchar(100)		not null,
    description		varchar(100),
    type		smallint		not null,
    notification	smallint		not null,
    club_member_id	integer			not null,

    primary key (id),
    foreign key (club_member_id) references club_member(id)
        ON DELETE CASCADE,
    unique (contact, club_member_id)

);

CREATE TABLE team_member
(
    id			serial,
    functions		integer		not null  default 0,
    club_member_id	integer		not null,
    club_team_id	integer		not null,

    primary key (id),
    foreign key (club_member_id) references club_member (id)
        ON DELETE CASCADE,
    foreign key ( club_team_id ) references club_team ( id )
        ON DELETE CASCADE,
    unique(club_member_id, club_team_id)


);

CREATE TABLE team_training
(
    id			serial,
    start		timestamp	not null,
    finish		timestamp	not null,
    place		varchar(500),
    comment		TEXT,
    club_team_id	integer		not null,

    primary key (id),
    foreign key (club_team_id) references club_team (id)
        ON DELETE CASCADE
);

CREATE TABLE team_match
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
    foreign key (club_rival_id) references club_rival (id)
        ON DELETE SET NULL,
    foreign key (club_team_id) references club_team (id)
        ON DELETE CASCADE

);

CREATE TABLE action
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
    foreign key ( club_team_id ) references club_team ( id )
        ON DELETE CASCADE,
    foreign key ( category_id ) references category ( id )
        ON DELETE CASCADE,

    check ((category_id is not null AND club_team_id is null) OR (club_team_id is not null AND category_id is null) OR (club_team_id is null AND category_id is null))

);

CREATE TABLE applicant_for_action
(
    attend		boolean	not null default 'false',
    club_member_id	integer		not null,
    action_id		integer		not null,

    primary key ( club_member_id, action_id ),
    foreign key ( club_member_id ) references club_member( id )
	ON DELETE CASCADE,
    foreign key ( action_id ) references action ( id )
	ON DELETE CASCADE
);

CREATE TABLE applicant_for_team_training
(
    attend		boolean	not null default 'false',
    club_member_id	integer		not null,
    team_training_id	integer		not null,

    primary key ( club_member_id, team_training_id ),
    foreign key ( club_member_id ) references club_member( id )
	ON DELETE CASCADE,
    foreign key ( team_training_id ) references team_training ( id )
	ON DELETE CASCADE
);

CREATE TABLE applicant_for_team_match
(
    attend		boolean	not null default 'false',
    club_member_id	integer		not null,
    team_match_id	integer		not null,

    primary key (club_member_id, team_match_id),
    foreign key (club_member_id) references club_member(id)
	ON DELETE CASCADE,
    foreign key (team_match_id) references team_match (id)
	ON DELETE CASCADE
);

CREATE TABLE article
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
    foreign key (club_team_id) references club_team (id)
        ON DELETE CASCADE,
    foreign key (category_id) references category (id)
        ON DELETE CASCADE,
    check (((owner_type = 1) AND category_id is not null) OR ((owner_type = 2) AND club_team_id is not null) OR (owner_type <> 1 AND owner_type <> 2))
);

CREATE TABLE user_data
(
    id			serial,
    name		varchar(100)	not null,
    password		varchar(100)	not null,
    permissions		integer		not null default 0,
    club_member_id	integer,

    primary key (id),
    foreign key ( club_member_id ) references club_member ( id )
        ON DELETE CASCADE,
    unique (name),
    unique (id, club_member_id)

);

CREATE TABLE participant_of_match
(
    points		smallint	not null default 0,
    yellow_cards	smallint	not null default 0,
    red_cards		smallint	not null default 0,
    club_member_id	integer		not null,
    team_match_id	integer		not null,

    primary key (club_member_id, team_match_id),
    foreign key ( club_member_id ) references club_member ( id )
        ON DELETE CASCADE,
    foreign key ( team_match_id ) references team_match ( id )
        ON DELETE CASCADE
);

CREATE TABLE participant_of_training
(
    club_member_id	integer		not null,
    team_training_id	integer		not null,

    primary key (club_member_id, team_training_id),
    foreign key ( club_member_id ) references club_member ( id )
        ON DELETE CASCADE,

    foreign key ( team_training_id ) references team_training ( id )
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
FROM    team_member tm
	JOIN club_team t ON tm.club_team_id = t.id
	JOIN club_member cm ON tm.club_member_id = cm.id
;