CREATE TABLE category
( 
    id			INT UNSIGNED		NOT NULL  PRIMARY KEY  AUTO_INCREMENT, 
    description		VARCHAR(100)		NOT NULL , 
    active		TINYINT(1) UNSIGNED	NOT NULL  DEFAULT 1, 
    sorting		INT UNSIGNED 		NOT NULL  DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE club_settings
( 
    id			INT UNSIGNED		NOT NULL  PRIMARY KEY  AUTO_INCREMENT, 
    title		VARCHAR(200), 
    comment		VARCHAR(200), 
    logo		BLOB 
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE club_member
( 
    id			INT UNSIGNED		NOT NULL  PRIMARY KEY  AUTO_INCREMENT, 
    id_personal		VARCHAR(20), 
    id_registration	VARCHAR(20), 
    name		VARCHAR(100)		NOT NULL, 
    surname		VARCHAR(100)		NOT NULL, 
    birthdate		DATE, 
    street		VARCHAR(200), 
    city		VARCHAR(100), 
    code		VARCHAR(20), 
    photo		BLOB 
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE club_rival
( 
    id			INT UNSIGNED		NOT NULL  PRIMARY KEY  AUTO_INCREMENT,
    name		VARCHAR(100)		NOT NULL,
    web			VARCHAR(100),
    gps			VARCHAR(100),
    street		VARCHAR(200),
    city		VARCHAR(100),
    code		VARCHAR(20),
    icon		BLOB
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE club_team
( 
    id			INT UNSIGNED		NOT NULL  PRIMARY KEY  AUTO_INCREMENT,
    name		VARCHAR(100)		NOT NULL, 
    active		TINYINT(1) UNSIGNED,
    sorting		INT UNSIGNED 		NOT NULL  DEFAULT 0 ,
    category_id		INT UNSIGNED	        DEFAULT NULL ,

    FOREIGN KEY ( category_id ) REFERENCES category ( id ) 
        ON DELETE SET NULL

) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE contact
( 
    id 			INT UNSIGNED		NOT NULL  PRIMARY KEY  AUTO_INCREMENT,
    contact		VARCHAR(100)		NOT NULL, 
    description		VARCHAR(100), 
    type		INT(2) UNSIGNED		NOT NULL,
    notification	INT(2) UNSIGNED		NOT NULL,
    club_member_id	INT UNSIGNED		NOT NULL,

    FOREIGN KEY ( club_member_id ) REFERENCES club_member(id) 
        ON DELETE CASCADE,

    CONSTRAINT contact_pk_uk UNIQUE(contact, club_member_id)

) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE team_member
( 
    id			INT UNSIGNED		NOT NULL  PRIMARY KEY  AUTO_INCREMENT,
    functions		INT UNSIGNED		NOT NULL  DEFAULT 0,
    club_member_id	INT UNSIGNED		NOT NULL,
    club_team_id	INT UNSIGNED		NOT NULL,

    FOREIGN KEY ( club_member_id ) REFERENCES club_member ( id ) 
        ON DELETE CASCADE,

    FOREIGN KEY ( club_team_id ) REFERENCES club_team ( id ) 
        ON DELETE CASCADE,

    CONSTRAINT team_member_uk1 UNIQUE(club_member_id, club_team_id)


) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE team_training
( 
    id			INT UNSIGNED		NOT NULL  PRIMARY KEY  AUTO_INCREMENT,
    start		DATETIME		NOT NULL,
    end			DATETIME		NOT NULL,
    place		VARCHAR(500),
    comment		TEXT,
    club_team_id	INT UNSIGNED		NOT NULL,

    FOREIGN KEY ( club_team_id ) REFERENCES club_team ( id ) 
        ON DELETE CASCADE,

) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE team_match
( 
    id			INT UNSIGNED		NOT NULL  PRIMARY KEY  AUTO_INCREMENT,
    start		DATETIME		NOT NULL,
    score_pos		INT(5),
    score_neg		INT(5),
    home_court		TINYINT(1) UNSIGNED 	NOT NULL,
    comment		TEXT,
    publish		TINYINT(1) UNSIGNED 	NOT NULL DEFAULT 0,
    club_team_id	INT UNSIGNED		NOT NULL,
    club_rival_id	INT UNSIGNED,
    club_rival_comment	VARCHAR(200),

    FOREIGN KEY ( club_rival_id ) REFERENCES club_rival ( id ) 
        ON DELETE SET NULL,

    FOREIGN KEY ( club_team_id ) REFERENCES club_team ( id ) 
        ON DELETE CASCADE,

) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE action
( 
    id			INT UNSIGNED		NOT NULL  PRIMARY KEY  AUTO_INCREMENT,
    start		DATETIME		NOT NULL,
    end			DATETIME,
    caption		VARCHAR(500)		NOT NULL,
    place		VARCHAR(500),
    description		TEXT,
    sign_participation	TINYINT(1) UNSIGNED,
    club_team_id	INT UNSIGNED,
    category_id		INT UNSIGNED,

    FOREIGN KEY ( club_team_id ) REFERENCES club_team ( id ) 
        ON DELETE CASCADE,

    FOREIGN KEY ( category_id ) REFERENCES category ( id ) 
        ON DELETE CASCADE,

    CHECK ((category_id is not null AND club_team_id is null) OR (club_team_id is not null AND category_id is null) OR (club_team_id is null AND category_id is null))

) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE applicant_for_action
( 
    attend		TINYINT(1) UNSIGNED	NOT NULL DEFAULT 0,
    club_member_id	INT UNSIGNED		NOT NULL,
    action_id		INT UNSIGNED		NOT NULL,

    FOREIGN KEY ( club_member_id_fk ) REFERENCES club_member( id ) 
	ON DELETE CASCADE,

    FOREIGN KEY ( action_id ) REFERENCES action ( id ) 
	ON DELETE CASCADE,

    CONSTRAINT ( applicant_for_action_pk ) PRIMARY KEY ( club_member_id, action_id ) 

) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE applicant_for_team_training
( 
    attend		TINYINT(1) UNSIGNED	NOT NULL DEFAULT 0,
    club_member_id	INT UNSIGNED		NOT NULL,
    team_training_id	INT UNSIGNED		NOT NULL,

    FOREIGN KEY ( club_member_id_fk ) REFERENCES club_member( id ) 
	ON DELETE CASCADE,

    FOREIGN KEY ( team_training_id ) REFERENCES team_training ( id ) 
	ON DELETE CASCADE,

    CONSTRAINT ( applicant_for_action_pk ) PRIMARY KEY ( club_member_id, team_training_id ) 

) ENGINE=InnoDB DEFAULT CHARSET=utf8;


CREATE TABLE applicant_for_team_match
( 
    attend		TINYINT(1) UNSIGNED	NOT NULL DEFAULT 0,
    club_member_id	INT UNSIGNED		NOT NULL,
    team_match_id	INT UNSIGNED		NOT NULL,

    FOREIGN KEY ( club_member_id_fk ) REFERENCES club_member( id ) 
	ON DELETE CASCADE,

    FOREIGN KEY ( team_match_id ) REFERENCES team_match ( id ) 
	ON DELETE CASCADE,

    CONSTRAINT ( applicant_for_action_pk ) PRIMARY KEY ( club_member_id, team_match_id ) 

) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE article
( 
    id			INT UNSIGNED		NOT NULL  PRIMARY KEY  AUTO_INCREMENT,
    location		INT(2)			NOT NULL DEFAULT 1,
    priority		TINYINT(1) UNSIGNED	NOT NULL DEFAULT 0,
    caption		VARCHAR(2000)		NOT NULL,
    summary		TEXT,
    content		TEXT,
    creation_date	DATETIME		NOT NULL,
    expiration_date	DATE,
    owner_type		INT(3)			NOT NULL DEFAULT 0,
    club_team_id	INT UNSIGNED,
    category_id		INT UNSIGNED,

    FOREIGN KEY ( club_team_id ) REFERENCES club_team ( id ) 
        ON DELETE CASCADE,

    FOREIGN KEY ( category_id ) REFERENCES category ( id ) 
        ON DELETE CASCADE,

    CHECK (((owner_type = 1) AND category_id is not null) OR ((owner_type = 2) AND club_team_id is not null) OR (owner_type <> 1 AND owner_type <> 2))


) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE user
( 
    id			INT UNSIGNED		NOT NULL  PRIMARY KEY  AUTO_INCREMENT,
    name		VARCHAR(100)		NOT NULL,
    password		VARCHAR(100)		NOT NULL,
    permissions		INT UNSIGNED		NOT NULL DEFAULT 0,
    club_member_id	INT UNSIGNED,

    FOREIGN KEY ( club_member_id ) REFERENCES club_member ( id ) 
        ON DELETE CASCADE,

    CONSTRAINT user__name__uk UNIQUE (name),
    CONSTRAINT user__id__club_member_id__uk UNIQUE (id, club_member_id)

) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE participant_of_training
( 
    club_member_id	INT UNSIGNED		NOT NULL,
    team_training_id	INT UNSIGNED		NOT NULL,

    FOREIGN KEY ( club_member_id ) REFERENCES club_member ( id ) 
        ON DELETE CASCADE,

    FOREIGN KEY ( team_training_id ) REFERENCES team_training ( id ) 
        ON DELETE CASCADE,

    CONSTRAINT participant_of_training_pk PRIMARY KEY (club_member_id, team_training_id)

) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE participant_of_match
( 
    points		INT(3)			NOT NULL DEFAULT 0,
    yellow_cards	INT(2)			NOT NULL DEFAULT 0,
    red_cards		INT(2)			NOT NULL DEFAULT 0,
    club_member_id	INT UNSIGNED		NOT NULL,
    team_match_id	INT UNSIGNED		NOT NULL,

    FOREIGN KEY ( club_member_id ) REFERENCES club_member ( id ) 
        ON DELETE CASCADE,

    FOREIGN KEY ( team_match_id ) REFERENCES team_match ( id ) 
        ON DELETE CASCADE

    CONSTRAINT participant_of_match_pk PRIMARY KEY (club_member_id, team_match_id)

) ENGINE=InnoDB DEFAULT CHARSET=utf8;


CREATE OR REPLACE VIEW club_member_by_team AS
SELECT  cm.id club_member_id,
        t.id club_team_id,
        cm.id_personal id_personal,
        cm.id_registration id_registration,
        cm.name name,
        cm.surname surname,
        cm.birthdate birthdate,
        cm.street street,
        cm.city city,
        cm.code code,
        cm.photo photo
FROM    team_member tm 
	JOIN club_team t ON tm.club_team_id = t.id
	JOIN club_member cm ON tm.club_member_id = cm.id
