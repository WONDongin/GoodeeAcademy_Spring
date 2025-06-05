create table useraccount (
   userid varchar(10) primary key,
   password varchar(15),
   username varchar(20),
   phoneno varchar(20),
   postcode varchar(7),
   address varchar(30),
   email varchar(50),
   birthday datetime
);

SELECT * FROM useraccount