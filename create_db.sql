create table users (
    email varchar(100) primary key,
    first_name varchar(50) not null,
    last_name varchar(50) not null,
    age int not null,
    password varchar(50) not null,
    created_at date not null
);