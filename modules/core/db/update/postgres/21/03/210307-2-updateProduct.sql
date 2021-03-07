alter table UCO_PRODUCT rename column price to price__u61625 ;
alter table UCO_PRODUCT alter column price__u61625 drop not null ;
alter table UCO_PRODUCT add column PRICE double precision ^
update UCO_PRODUCT set PRICE = 0 where PRICE is null ;
alter table UCO_PRODUCT alter column PRICE set not null ;
