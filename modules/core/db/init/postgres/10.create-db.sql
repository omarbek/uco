-- begin UCO_CONTACTS
create table UCO_CONTACTS (
    ID uuid,
    VERSION integer not null,
    CREATE_TS timestamp,
    CREATED_BY varchar(50),
    UPDATE_TS timestamp,
    UPDATED_BY varchar(50),
    DELETE_TS timestamp,
    DELETED_BY varchar(50),
    --
    ACCOUNT_ID uuid not null,
    CONTACT_TYPE varchar(50) not null,
    VALUE_ varchar(255) not null,
    --
    primary key (ID)
)^
-- end UCO_CONTACTS
-- begin UCO_ACCOUNT
create table UCO_ACCOUNT (
    ID uuid,
    VERSION integer not null,
    CREATE_TS timestamp,
    CREATED_BY varchar(50),
    UPDATE_TS timestamp,
    UPDATED_BY varchar(50),
    DELETE_TS timestamp,
    DELETED_BY varchar(50),
    --
    NAME varchar(255) not null,
    LAST_NAME varchar(255) not null,
    MIDDLE_NAME varchar(255),
    PHOTO uuid,
    --
    primary key (ID)
)^
-- end UCO_ACCOUNT
-- begin UCO_ORDER
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
)^
-- end UCO_ORDER
-- begin UCO_PRODUCT
create table UCO_PRODUCT (
    ID uuid,
    VERSION integer not null,
    CREATE_TS timestamp,
    CREATED_BY varchar(50),
    UPDATE_TS timestamp,
    UPDATED_BY varchar(50),
    DELETE_TS timestamp,
    DELETED_BY varchar(50),
    --
    NAME varchar(255) not null,
    PRICE integer not null,
    QUANTITY integer not null,
    ORDER_ID uuid not null,
    --
    primary key (ID)
)^
-- end UCO_PRODUCT
