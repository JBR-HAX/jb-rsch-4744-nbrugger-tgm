create type direction as enum ('east', 'west', 'north', 'south');
create type event_type as enum ('move_to_location', 'move_from_location');
-- it wasn't clear from the documentation if the inputs/outputs are to be separate tables, their use cases or their relationship(s).
-- This table combines one in/output pair with each other
-- The event_type column is used to differentiate between the two types of events and if the location
-- or the move is the in/output
create table move_event (
    event_id serial primary key,
    event_type event_type not null,
    -- x and y coordinates are used to represent the start/end location depending on the event_type
    x_cord int not null,
    y_cord int not null,
    direction direction not null, -- in case of move_to_location, this is the input direction, in case of move_from_location, this is the output direction
    steps int not null -- in case of move_to_location, this is the number of steps to move, in case of move_from_location, this is the number of steps to move back
)