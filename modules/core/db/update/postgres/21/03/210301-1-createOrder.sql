create table UCO_ORDER (
    ID uuid,
    VERSION integer not null,
    CREATE_TS timestamp,
    CREATED_BY varchar(50),
    UPDATE_TS timestamp,
    UPDATED_BY varchar(50),
    DELETE_TS timestamp,
    DELETED_BY varchar(50),
    --
    ORDER_DATE timestamp not null,
    AMOUNT integer not null,
    ACCOUNT_ID uuid not null,
    --
    primary key (ID)
);