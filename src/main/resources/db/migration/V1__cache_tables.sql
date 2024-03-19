create type direction as enum ('NORTH', 'EAST', 'SOUTH', 'WEST');
create table move
(
    id serial primary key,
    direction direction,
    steps int,
    x int,
    y int
);