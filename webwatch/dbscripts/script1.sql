
DROP TABLE IF EXISTS userrecording;

CREATE TABLE userrecording 
    (id integer primary key autoincrement, 
     time datetime not null, 
     htmlcontent text not null,
     url string not null);
     