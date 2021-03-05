alter table UCO_PRODUCT add constraint FK_UCO_PRODUCT_ON_ORDER foreign key (ORDER_ID) references UCO_ORDER(ID);
create index IDX_UCO_PRODUCT_ON_ORDER on UCO_PRODUCT (ORDER_ID);
