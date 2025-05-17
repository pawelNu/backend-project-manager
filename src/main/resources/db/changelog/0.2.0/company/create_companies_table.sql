create table companies (
version integer not null,
created timestamp(6) without time zone not null,
last_modified timestamp(6) without time zone not null,
regon varchar(9) not null,
nip varchar(10) not null,
id uuid not null,
name varchar(255) not null,
status varchar(255) not null check (status in ('ACTIVE','TERMINATED')),
website varchar(255) not null,
is_deleted boolean not null,
primary key (id)
)
;