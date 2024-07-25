create table pilatihomes_homes (
    id bigint auto_increment,
    player_uuid varchar(36) not null,
    name varchar(255) not null,
    location_world varchar(255) not null,
    location_x double not null,
    location_y double not null,
    location_z double not null,
    location_yaw double not null,
    location_pitch double not null,
    primary key (id)
);