

DROP TABLE IF EXISTS session;

CREATE TABLE session
    (id string primary key);


DROP TABLE IF EXISTS userrecording;

CREATE TABLE userrecording 
    (id integer primary key autoincrement,
     fksessionId string not null, 
     time datetime not null, 
     htmlcontent text not null,
     url string not null,
     
     foreign key (fksessionId) references session (id) ON DELETE CASCADE);  

     