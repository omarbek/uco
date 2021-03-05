alter table UCO_ORDER add constraint FK_UCO_ORDER_ON_ACCOUNT foreign key (ACCOUNT_ID) references UCO_ACCOUNT(ID);
create index IDX_UCO_ORDER_ON_ACCOUNT on UCO_ORDER (ACCOUNT_ID);
