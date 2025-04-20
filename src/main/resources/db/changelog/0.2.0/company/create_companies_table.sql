  create table dev.companies (
      version integer,
      created timestamp(6) without time zone,
      last_modified timestamp(6) without time zone,
      regon varchar(9) not null,
      nip varchar(10) not null,
      id uuid not null,
      name varchar(255) not null,
      status varchar(255) check (status in ('ACTIVE','TERMINATED')),
      website varchar(255) not null,
      primary key (id)
  )