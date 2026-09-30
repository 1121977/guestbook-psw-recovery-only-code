create table users(
                      username varchar_ignorecase(500) not null primary key,
                      password varchar_ignorecase(500) not null,
                      firstname varchar_ignorecase(500) not null,
                      lastname varchar_ignorecase(500) not null,
                      enabled boolean not null,
                      emailaddress varchar_ignorecase(500) not null UNIQUE
);

create table authorities (
                             username varchar_ignorecase(500) not null,
                             authority varchar_ignorecase(50) not null,
                             constraint fk_authorities_users foreign key(username) references users(username)
);

create unique index ix_auth_username on authorities (username,authority);

create table note (
                    id bigint not null,
                    message varchar(10000),
                    noteDateTime timestamp(6),
                    recipient varchar(255),
                    userName varchar(255),
                    primary key (id)
);

create table PasswordResetToken (
    userEmail varchar(255) not null,
    expiryDate timestamp(6),
    token varchar(255),
    primary key (userEmail)
);

CREATE SEQUENCE Note_SEQ START WITH 1 INCREMENT BY 50;
