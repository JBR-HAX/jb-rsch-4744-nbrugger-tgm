create type direction as enum ('NORTH', 'EAST', 'SOUTH', 'WEST');
create table move
(
    id serial primary key, -- todo: maybe a timestamp would be better
    direction direction, -- the direction of the move
    steps int, -- the number of steps that were performed
    x int, -- the x coordinate of the position after the move
    y int -- the y coordinate of the position after the move
);