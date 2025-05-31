create table Artiste
(
    id              int         not null
        primary key,
    nom             varchar(30) not null,
    prenom          varchar(30) not null,
    annee_naissance int         null,
    constraint nom
        unique (nom, prenom)
)
    collate = utf8mb3_bin;

create table Artiste_SEQ
(
    next_val bigint null
);

create table Film_SEQ
(
    next_val bigint null
);

create table Genre
(
    code varchar(20) not null
        primary key
)
    collate = utf8mb3_bin;

create table Internaute
(
    email           varchar(40) not null
        primary key,
    nom             varchar(30) not null,
    prenom          varchar(30) not null,
    mot_de_passe    varchar(32) not null,
    annee_naissance int         null,
    region          varchar(30) null
)
    collate = utf8mb3_bin;

create table Message
(
    id             int           not null
        primary key,
    id_pere        int default 0 null,
    id_film        int           not null,
    sujet          varchar(30)   not null,
    texte          text          not null,
    date_creation  int           null,
    email_createur varchar(40)   not null,
    constraint Message_ibfk_1
        foreign key (email_createur) references Internaute (email)
)
    collate = utf8mb3_bin;

create index email_createur
    on Message (email_createur);

create table Pays
(
    code   varchar(4)                    not null
        primary key,
    nom    varchar(30) default 'Inconnu' not null,
    langue varchar(30)                   not null
)
    collate = utf8mb3_bin;

create table Film
(
    id             int         not null
        primary key,
    titre          varchar(50) not null,
    annee          int         not null,
    id_realisateur int         null,
    genre          varchar(30) not null,
    resume         text        null,
    code_pays      varchar(4)  null,
    constraint Film_ibfk_1
        foreign key (id_realisateur) references Artiste (id),
    constraint Film_ibfk_2
        foreign key (code_pays) references Pays (code)
)
    collate = utf8mb3_bin;

create index code_pays
    on Film (code_pays);

create index id_realisateur
    on Film (id_realisateur);

create table Notation
(
    id_film int         not null,
    email   varchar(40) not null,
    note    int         not null,
    primary key (id_film, email),
    constraint Notation_ibfk_1
        foreign key (id_film) references Film (id),
    constraint Notation_ibfk_2
        foreign key (email) references Internaute (email)
)
    collate = utf8mb3_bin;

create index email
    on Notation (email);

create table Region
(
    nom varchar(30) not null
        primary key
)
    collate = utf8mb3_bin;

create table Role
(
    id_film   int         not null,
    id_acteur int         not null,
    nom_role  varchar(60) null,
    primary key (id_film, id_acteur),
    constraint Role_ibfk_1
        foreign key (id_film) references Film (id),
    constraint Role_ibfk_2
        foreign key (id_acteur) references Artiste (id)
)
    collate = utf8mb3_bin;

create index id_acteur
    on Role (id_acteur);

create table SessionWeb
(
    id_session   varchar(40) not null
        primary key,
    email        varchar(60) not null,
    nom          varchar(30) not null,
    prenom       varchar(30) not null,
    temps_limite int         not null,
    constraint SessionWeb_ibfk_1
        foreign key (email) references Internaute (email)
)
    collate = utf8mb3_bin;

create index email
    on SessionWeb (email);