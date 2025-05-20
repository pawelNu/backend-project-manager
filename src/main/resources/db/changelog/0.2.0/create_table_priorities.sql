create table priorities (
    id uuid not null,
    created timestamp(6) without time zone not null,
    last_modified timestamp(6) without time zone not null,
    version integer not null,
    name varchar(255) not null,
    category_value_id uuid not null,
    primary key (id)
);
alter table if exists priorities
add constraint fk_priorities_and_category_values foreign key (category_value_id) references category_values;