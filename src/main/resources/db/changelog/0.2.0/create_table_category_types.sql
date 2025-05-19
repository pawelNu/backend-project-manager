create table category_types (
    id uuid not null,
    created timestamp(6) without time zone not null,
    last_modified timestamp(6) without time zone not null,
    version integer not null,
    name varchar(255) not null,
    primary key (id)
);