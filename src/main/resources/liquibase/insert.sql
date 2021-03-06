insert into roles (id, name)
values (3, 'ROLE_USER_INACTIVE'),
       (4, 'ROLE_ADMIN'),
       (5, 'ROLE_USER');

insert into subscription (id, name, start_time, finish_time)
values (6, 'LOW', '2021-10-21 08:36:49', '2022-10-23 08:36:49'),
       (7, 'HIGH', '2021-10-21 08:36:49', '2022-10-23 08:36:49');

insert into users (id, login, email, first_name, last_name, password, status, subscription_id, created, updated)
values (1, 'admin', 'admin@gmail.com', 'Andrey', 'Ivanov',
        '$2a$12$8VMde87/w4i/OJVqPUxbbOfIXNo.NmRw45oEq.Qv0CXFhfQ4AFj62', 'ACTIVE', 7, '2021-10-21 08:36:49',
        '2021-10-23 08:36:49'),
       (2, 'user', 'user@gmail.com', 'Sergey', 'Ivanov', '$2a$12$3zcfWpX4JuRoKRJw1LuFW..kcrg5de7m3kmAwOMPiVkbjviPYqOVW',
        'ACTIVE', 6, '2021-10-21 08:36:49', '2021-10-23 08:36:49');

insert into users_roles (users_id, roles_id)
values (1, 4),
       (2, 5);