create table employees (
id uuid not null,
created timestamp(6) without time zone,
last_modified timestamp(6) without time zone,
version integer,
email varchar(255) not null,
first_name varchar(255) not null,
last_name varchar(255) not null,
password varchar(255) not null,
phone_number varchar(255) not null,
username varchar(255) not null,
company_id uuid not null,
primary key (id)
)
;

alter table if exists employees
add constraint fk_employees_and_companies
foreign key (company_id)
references employees
;
