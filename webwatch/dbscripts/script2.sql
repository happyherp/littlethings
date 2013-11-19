DROP TABLE IF EXISTS action;

CREATE TABLE action (fkrecordid integer not null,
                       position integer not null,
                       time datetime not null,
                       type string not null,
                       target string not null,
                       at integer,
                       removed integer,
                       attributeName string,
                       attributeValue string,
                       inserted string,
                       nodeValue string,
                       
             PRIMARY KEY (fkrecordid, position),
             foreign key (fkrecordid) references userrecording (id) ON DELETE CASCADE);
             
             
DROP TABLE IF EXISTS mouseaction;
             
CREATE TABLE mouseaction (fkrecordid integer not null,
                          position integer not null,
                          time datetime not null,
                          type string not null,
                          x integer not null,
                          y integer not null,
                          
             PRIMARY KEY (fkrecordid, position),
             foreign key (fkrecordid) references userrecording (id) ON DELETE CASCADE);                                      
                       