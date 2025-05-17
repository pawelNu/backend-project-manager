create table project_attachments (
id uuid not null,
created timestamp(6) without time zone not null,
last_modified timestamp(6) without time zone not null,
version integer not null,
name varchar(255) not null,
path_to_file text not null,
project_id uuid,
primary key (id)
)
;

alter table if exists project_attachments
add constraint fk_project_attachments_and_projects
foreign key (project_id)
references projects
;

