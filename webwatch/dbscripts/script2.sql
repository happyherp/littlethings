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
             
                       