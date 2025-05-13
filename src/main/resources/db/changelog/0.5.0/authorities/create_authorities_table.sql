create table authorities (
id uuid not null,
created timestamp(6) without time zone,
last_modified timestamp(6) without time zone,
version integer,
name varchar(255) not null,
is_deleted boolean not null,
primary key (id)
)
;
