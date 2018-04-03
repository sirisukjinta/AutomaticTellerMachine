create user ATM identified by ATM default tablespace USERS temporary tablespace TEMP profile DEFAULT;
grant dba to ATM;
grant unlimited tablespace to ATM;
create user ATM_RW identified by ATM_RW default tablespace USERS temporary tablespace TEMP profile DEFAULT;
grant dba to ATM_RW;
grant unlimited tablespace to ATM_RW;

create table ATM.T_NOTE_BALANCE
(
  NOTE_TYPE                     NUMBER(4) not null,
  NOTE_BALANCE      			NUMBER(10) not null,
  UPDATED_DATE                 	DATE default SYSDATE
);
alter table ATM.T_NOTE_BALANCE
  add constraint T_NOTE_BALANCE_PK primary key (NOTE_TYPE);
-- Grant/Revoke object privileges 
grant select, insert, update, delete, references on ATM.T_NOTE_BALANCE to ATM_RW;


INSERT INTO ATM.T_NOTE_BALANCE (NOTE_TYPE, NOTE_BALANCE, UPDATED_DATE)
VALUES (20, 100, SYSDATE);
INSERT INTO ATM.T_NOTE_BALANCE (NOTE_TYPE, NOTE_BALANCE, UPDATED_DATE)
VALUES (50, 100, SYSDATE);
INSERT INTO ATM.T_NOTE_BALANCE (NOTE_TYPE, NOTE_BALANCE, UPDATED_DATE)
VALUES (100, 100, SYSDATE);
INSERT INTO ATM.T_NOTE_BALANCE (NOTE_TYPE, NOTE_BALANCE, UPDATED_DATE)
VALUES (500, 100, SYSDATE);
INSERT INTO ATM.T_NOTE_BALANCE (NOTE_TYPE, NOTE_BALANCE, UPDATED_DATE)
VALUES (1000, 100, SYSDATE);

COMMIT;
