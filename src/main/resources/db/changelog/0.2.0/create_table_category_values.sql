create table category_values (
    id uuid not null,
    created timestamp(6) without time zone not null,
    last_modified timestamp(6) without time zone not null,
    version integer not null,
    category_id uuid not null,
    numeric_value numeric(10, 2),
    string_value varchar(255),
    date_value timestamp(6) without time zone,
    primary key (id)
);
alter table if exists category_values
add constraint fk_category_values_and_categories foreign key (category_id) references categories;