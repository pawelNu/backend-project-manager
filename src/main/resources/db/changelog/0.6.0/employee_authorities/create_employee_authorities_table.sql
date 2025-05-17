create table employee_authorities (
id uuid not null,
created timestamp(6) without time zone,
last_modified timestamp(6) without time zone,
version integer,
authority_id uuid,
employee_id uuid,
is_deleted boolean not null,
primary key (id)
)
;

alter table if exists employee_authorities
add constraint fk_employee_authorities_and_authorities
foreign key (authority_id)
references authorities
;

alter table if exists employee_authorities
add constraint fk_employee_authorities_and_employees
foreign key (employee_id)
references employees
;
