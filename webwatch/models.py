from sqlalchemy.ext.declarative import declarative_base
from sqlalchemy.types import String, Integer, DateTime
from sqlalchemy.schema import Column, ForeignKey
from sqlalchemy.orm import relationship, backref

Base = declarative_base()


class Session(Base):
  __tablename__ = 'Session'
  id = Column(String, primary_key = True, nullable = False)
  
  def __repr__(self):
    return "Session<id=%s>" %(self.id)
  
class Pagerecording(Base):
  __tablename__ = 'Pagerecording'
  id = Column(Integer, primary_key = True, nullable = False)
  session_id = Column(Integer, ForeignKey('Session.id'), nullable = False)
  time = Column(DateTime, nullable = False)
  html = Column(String, nullable = False)
  url = Column(String, nullable = False)
  
  session = relationship("Session", 
                         backref=backref("recordings",  order_by=time))
                         
  def __repr__(self):
    return "<Pagerecording at=%s>"%(self.time.__repr__())

class DOMAction(Base):
  __tablename__ = "DOMAction"
  record_id = Column(Integer, ForeignKey('Pagerecording.id'), primary_key = True, nullable = False)
  position = Column(Integer, primary_key = True, nullable = False)
  time = Column(DateTime, nullable = False)
  type = Column(String, nullable = False)
  target = Column(String, nullable = False)
  at = Column(Integer)
  removed = Column(Integer)
  attributeName = Column(String)  
  attributeValue = Column(String)
  inserted = Column(String)
  nodeValue = Column(String)
  
  recording = relationship("Pagerecording", 
                           backref=backref("dom_actions",  order_by=position))  
  
  def __repr__(self):
    return "<DOMAction type=%s>" %(self.type)
  
class MouseAction(Base):
  __tablename__ = "Mouseaction"
  record_id = Column(Integer, ForeignKey('Pagerecording.id'), primary_key = True, nullable = False)
  position = Column(Integer, primary_key = True, nullable = False)
  time = Column(DateTime, nullable = False)
  type = Column(String, nullable = False)
  x = Column(Integer, nullable = False)
  y = Column(Integer, nullable = False)
  
  recording = relationship("Pagerecording", 
                           backref=backref("mouse_actions", order_by=position))  
  
class FocusAction(Base):
  __tablename__="Focus"
  record_id = Column(Integer, ForeignKey('Pagerecording.id'), primary_key = True, nullable = False)
  position = Column(Integer, primary_key = True, nullable = False)
  time = Column(DateTime, nullable = False)   
  
  recording = relationship("Pagerecording", 
                         backref=backref("focus_actions", order_by=position))  