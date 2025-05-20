create table companies (
    id uuid not null,
    created timestamp(6) without time zone not null,
    last_modified timestamp(6) without time zone not null,
    version integer not null,
    regon varchar(9) not null,
    nip varchar(10) not null,
    name varchar(255) not null,
    category_value_id uuid,
    website varchar(255) not null,
    is_deleted boolean not null,
    primary key (id)
);
alter table if exists companies
add constraint fk_companies_and_category_values foreign key (category_value_id) references category_values;