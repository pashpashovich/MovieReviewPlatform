create table genres
(
    id   serial primary key,
    name varchar(100) not null
        constraint uk_pe1a9woik1k97l87cieguyhh4 unique
);

alter table genres
    owner to postgres;

create table movies
(
    id           bigserial
        primary key,
    avg_rating   numeric(3, 2),
    created_at   timestamp(6) not null,
    description  text,
    duration     integer      not null,
    language     varchar(50)  not null,
    poster       text,
    release_year integer      not null,
    title        varchar(255) not null
);

alter table movies
    owner to postgres;

create table movie_genre
(
    movie_id bigint not null
        constraint fkg7f38h6umffo51no9ywq91438
            references movies,
    genre_id bigint not null
        constraint fk3pdaf1ai9eafeypc7qe401l07
            references genres
);

alter table movie_genre
    owner to postgres;

create table people
(
    id        bigserial
        primary key,
    full_name varchar(255) not null,
    role      varchar(50)  not null
);

alter table people
    owner to postgres;

create table movie_person
(
    person_id bigint not null
        constraint fkh4ksjqf4prx1bvdqptcol45ly
            references people,
    movie_id  bigint not null
        constraint fk79ugylpma3kggipf6uwablye2
            references movies
);

alter table movie_person
    owner to postgres;

create table users
(
    id         bigserial
        primary key,
    created_at timestamp(6) not null,
    email      varchar(100) not null
        constraint uk_6dotkott2kjsp8vw4d0m25fb7
            unique,
    is_blocked boolean      not null,
    password   varchar(255) not null,
    role       varchar(255) not null,
    username   varchar(50)  not null
        constraint uk_r43af9ap4edm43mmtq01oddj6
            unique
);

alter table users
    owner to postgres;

create table ratings
(
    id         bigserial
        primary key,
    created_at timestamp(6) not null,
    rating     integer      not null,
    updated_at timestamp(6),
    movie_id   bigint       not null
        constraint fk44trpo3u915t27ybt03ib4h0o
            references movies,
    user_id    bigint       not null
        constraint fkb3354ee2xxvdrbyq9f42jdayd
            references users
);

alter table ratings
    owner to postgres;

create table reviews
(
    id         bigserial
        primary key,
    content    text         not null,
    created_at timestamp(6) not null,
    rating     integer      not null,
    status     varchar(255) not null,
    updated_at timestamp(6),
    movie_id   bigint       not null
        constraint fk87tlqya0rq8ijfjscldpvvdyq
            references movies,
    user_id    bigint       not null
        constraint fkcgy7qjc1r99dp117y9en6lxye
            references users
);

alter table reviews
    owner to postgres;

create table watchlists
(
    id       bigserial
        primary key,
    added_at timestamp(6) not null,
    movie_id bigint       not null
        constraint fk2o4qyxqkybphjrjo843lg65r4
            references movies,
    user_id  bigint       not null
        constraint fksjis83bxhryaemq9m6gv5r0pb
            references users
);

alter table watchlists
    owner to postgres;

INSERT INTO people (full_name, role)
VALUES ('Леонардо ДиКаприо', 'ACTOR'),
       ('Мэрил Стрип', 'ACTOR'),
       ('Брэд Питт', 'ACTOR'),
       ('Джонни Депп', 'ACTOR'),
       ('Скарлетт Йоханссон', 'ACTOR'),
       ('Роберт Дауни мл.', 'ACTOR'),
       ('Крис Хемсворт', 'ACTOR'),
       ('Джейсон Момоа', 'ACTOR'),
       ('Том Хэнкс', 'ACTOR'),
       ('Эмма Стоун', 'ACTOR'),
       ('Квентин Тарантино', 'DIRECTOR'),
       ('Стивен Спилберг', 'DIRECTOR'),
       ('Кристофер Нолан', 'DIRECTOR'),
       ('Дэвид Финчер', 'DIRECTOR'),
       ('Джеймс Кэмерон', 'DIRECTOR'),
       ('Мартин Скорсезе', 'DIRECTOR'),
       ('Альфонсо Куарон', 'DIRECTOR'),
       ('Гильермо дель Торо', 'DIRECTOR'),
       ('Питер Джексон', 'DIRECTOR'),
       ('Ридли Скотт', 'DIRECTOR'),
       ('Кэтлин Кеннеди', 'PRODUCER'),
       ('Джон Лассетер', 'PRODUCER'),
       ('Джерри Брукхаймер', 'PRODUCER'),
       ('Кевин Файги', 'PRODUCER'),
       ('Барбара Брокколи', 'PRODUCER'),
       ('Гэйл Энн Хёрд', 'PRODUCER'),
       ('Джордж Лукас', 'PRODUCER'),
       ('Джеймс Кэмерон', 'PRODUCER'),
       ('Стивен Спилберг', 'PRODUCER'),
       ('Мартин Скорсезе', 'PRODUCER');

INSERT INTO genres (name)
VALUES ('Комедия'),
       ('Драма'),
       ('Боевик'),
       ('Фантастика'),
       ('Фэнтези'),
       ('Ужасы'),
       ('Триллер'),
       ('Детектив'),
       ('Мелодрама'),
       ('Приключения'),
       ('Анимация'),
       ('Документальный'),
       ('Криминал'),
       ('Исторический'),
       ('Биография'),
       ('Музыкальный'),
       ('Спорт'),
       ('Военный'),
       ('Семейный'),
       ('Фильм-нуар');
