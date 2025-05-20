create table project_steps (
    id uuid not null,
    created timestamp(6) without time zone not null,
    last_modified timestamp(6) without time zone not null,
    version integer not null,
    name varchar(255) not null,
    priority_id uuid,
    deadline timestamp(6) without time zone,
    primary key (id)
);
alter table if exists project_steps
add constraint fk_project_steps_and_priorities foreign key (priority_id) references priorities;