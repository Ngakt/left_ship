

create table plants
(  
  id int primary key auto_increment,
  date date,
  name varchar(20),
  users varchar(20),
  bs_info varchar(800),
  leaves varchar(800),
  trunk varchar(800),
  insect varchar(800),
  soil varchar(800)
);  
  create table tips
  (
   id int primary key auto_increment,
   date date,
   users varchar(20),
   tips varchar(400)
  );
 create table notes
 (
  id int primary key auto_increment,
   date date,
   user varchar(20),
   notes varchar(400)
 );
 
 select * from plants；
 select * from tips；
 select * from notes;
 
 
  insert into tips values(1,20180324,'user1','浇水'); 
  insert into tips values(4,20180325,'user1','浇花'); 
  insert into tips values(3,20180324,'user1','浇花');
  insert into tips values(9,20180324,'user1','浇'); 
  
  insert into tips(date,user,tips) values(20180328,'user1','杜鹃浇水');
   
  insert into notes(date,user,notes) values(20180328,'user1','杜鹃病害枝叶摘除的工作量有点大，还没完成');
  
  
   
  select tips from tips where user = 'user1' and date = 20180324 ;
  
  
 select tips from tips where user = 'user1' and date = 20180324 and id = (select max(id) from tips);
 select notes from tips where user = 'user1' and date = 20180324 and id = (select max(id) from notes);
 
 getYesId
 select id from tips where user = '' and date =  ;
 
 select id from notes where user = '' and date =  ;
 
 getYesList
 select tips from tips where user = '' and date =  ;
 
 getYesPre
 select notes from notes where user = '' and date =  ;