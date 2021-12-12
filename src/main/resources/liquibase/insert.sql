insert into subscription_type (id, name, price, company_count)
values (1,'BASIC', 0.0, 0),
       (2,'LOW',10.0, 2),
       (3,'MEDIUM',20.0, 3),
       (4,'HIGH',30.0, 4);

insert into roles (id, name)
values (5, 'ROLE_USER_INACTIVE'),
       (6, 'ROLE_ADMIN'),
       (7, 'ROLE_USER');

insert into subscription (id, status, start_time, finish_time, subscription_type_id)
values (8, 'ACTIVE', '2021-10-21 08:36:49', '2022-10-23 08:36:49',4),
       (9, 'ACTIVE', '2021-10-21 08:36:49', '2022-10-23 08:36:49',2);

insert into users (id, login, email, first_name, last_name, password, status, subscription_id, created, updated)
values (10, 'admin', 'admin@gmail.com', 'Andrey', 'Ivanov',
        '$2a$12$8VMde87/w4i/OJVqPUxbbOfIXNo.NmRw45oEq.Qv0CXFhfQ4AFj62', 'ACTIVE', 8, '2021-10-21 08:36:49',
        '2021-10-23 08:36:49'),
       (11, 'user', 'user@gmail.com', 'Sergey', 'Ivanov', '$2a$12$3zcfWpX4JuRoKRJw1LuFW..kcrg5de7m3kmAwOMPiVkbjviPYqOVW',
        'ACTIVE', 9, '2021-10-21 08:36:49', '2021-10-23 08:36:49');

insert into users_roles (users_id, roles_id)
values (10, 6),
       (11, 7);