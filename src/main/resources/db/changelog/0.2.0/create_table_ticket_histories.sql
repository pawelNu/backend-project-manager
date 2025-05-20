create table ticket_histories (
    id uuid not null,
    created timestamp(6) without time zone not null,
    last_modified timestamp(6) without time zone not null,
    version integer not null,
    name varchar(255) not null,
    ticket_id uuid,
    from_status_id uuid,
    to_status_id uuid,
    from_employee_id uuid,
    to_employee_id uuid,
    comment text,
    primary key (id)
);
alter table if exists ticket_histories
add constraint fk_ticket_histories_and_tickets foreign key (ticket_id) references tickets;
alter table if exists ticket_histories
add constraint fk_ticket_histories_and_category_values_from_status foreign key (from_status_id) references category_values;
alter table if exists ticket_histories
add constraint fk_ticket_histories_and_category_values_to_status foreign key (to_status_id) references category_values;
alter table if exists ticket_histories
add constraint fk_ticket_histories_and_employees_from_employee foreign key (from_employee_id) references employees;
alter table if exists ticket_histories
add constraint fk_ticket_histories_and_employees_to_employee foreign key (to_employee_id) references employees;