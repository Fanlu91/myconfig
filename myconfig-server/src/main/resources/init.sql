create schema if not exists `myconfig`;
use `myconfig`;
create table if not exists `configs`
(
    `app`  varchar(64)  not null,
    `env`  varchar(64)  not null,
    `ns`   varchar(64)  not null,
    `pkey` varchar(64)  not null,
    `pval` varchar(128) null
);

create table if not exists `locks`
(
    `id`  int primary key not null,
    `app` varchar(64)     not null
);

-- insert into locks (id, app) values (1, 'kkconfig-server');