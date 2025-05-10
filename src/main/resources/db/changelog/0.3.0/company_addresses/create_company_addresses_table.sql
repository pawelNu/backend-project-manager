create table company_addresses (
id uuid not null,
created timestamp(6) without time zone,
last_modified timestamp(6) without time zone,
version integer,
address_type varchar(255) not null,
city varchar(255) not null,
country varchar(255) not null,
email_address varchar(255) not null,
phone_number varchar(255) not null,
street varchar(255) not null,
street_number varchar(255) not null,
zip_code varchar(255) not null,
company_id uuid not null,
primary key (id)
)
;

alter table if exists company_addresses
add constraint fk_company_addresses_and_companies
foreign key (company_id)
references companies
;
