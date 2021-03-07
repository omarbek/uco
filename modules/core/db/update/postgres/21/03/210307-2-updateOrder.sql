alter table UCO_ORDER rename column amount to amount__u33235 ;
alter table UCO_ORDER alter column amount__u33235 drop not null ;
alter table UCO_ORDER add column AMOUNT double precision ^
update UCO_ORDER set AMOUNT = 0 where AMOUNT is null ;
alter table UCO_ORDER alter column AMOUNT set not null ;
