-- begin UCO_CONTACTS
alter table UCO_CONTACTS add constraint FK_UCO_CONTACTS_ON_ACCOUNT foreign key (ACCOUNT_ID) references UCO_ACCOUNT(ID)^
create index IDX_UCO_CONTACTS_ON_ACCOUNT on UCO_CONTACTS (ACCOUNT_ID)^
-- end UCO_CONTACTS
-- begin UCO_ACCOUNT
alter table UCO_ACCOUNT add constraint FK_UCO_ACCOUNT_ON_PHOTO foreign key (PHOTO) references SYS_FILE(ID)^
create index IDX_UCO_ACCOUNT_ON_PHOTO on UCO_ACCOUNT (PHOTO)^
-- end UCO_ACCOUNT
-- begin UCO_ORDER
alter table UCO_ORDER add constraint FK_UCO_ORDER_ON_ACCOUNT foreign key (ACCOUNT_ID) references UCO_ACCOUNT(ID)^
create index IDX_UCO_ORDER_ON_ACCOUNT on UCO_ORDER (ACCOUNT_ID)^
-- end UCO_ORDER
-- begin UCO_PRODUCT
alter table UCO_PRODUCT add constraint FK_UCO_PRODUCT_ON_ORDER foreign key (ORDER_ID) references UCO_ORDER(ID)^
create index IDX_UCO_PRODUCT_ON_ORDER on UCO_PRODUCT (ORDER_ID)^
-- end UCO_PRODUCT
