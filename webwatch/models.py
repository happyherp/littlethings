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
  starthtml = Column(String, nullable = False)
  url = Column(String, nullable = False)
  windowWidth = Column(Integer, nullable = False)
  windowHeight = Column(Integer, nullable = False)
  
  session = relationship("Session", 
                         backref=backref("recordings", cascade="all,delete",  order_by=time))
                         
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
                           backref=backref("dom_actions", cascade="all,delete",  order_by=position))  
  
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
                           backref=backref("mouse_actions", cascade="all,delete", order_by=position))  
  
class FocusAction(Base):
  __tablename__="Focusaction"
  record_id = Column(Integer, ForeignKey('Pagerecording.id'), primary_key = True, nullable = False)
  position = Column(Integer, primary_key = True, nullable = False)
  time = Column(DateTime, nullable = False)   
  
  recording = relationship("Pagerecording", 
                         backref=backref("focus_actions", cascade="all,delete", order_by=position))
  
  
class ScrollAction(Base):
  __tablename__="Scrollaction"
  record_id = Column(Integer, ForeignKey('Pagerecording.id'), primary_key = True, nullable = False)
  position = Column(Integer, primary_key = True, nullable = False)
  time = Column(DateTime, nullable = False) 
  target = Column(String, nullable = False)
  left = Column(Integer, nullable = False)
  top = Column(Integer, nullable = False)  
  
  recording = relationship("Pagerecording", 
                         backref=backref("scroll_actions", cascade="all,delete", order_by=position))  
  
  
  
class ResizeAction(Base):
  __tablename__="Resizeaction"
  record_id = Column(Integer, ForeignKey('Pagerecording.id'), primary_key = True, nullable = False)
  position = Column(Integer, primary_key = True, nullable = False)
  time = Column(DateTime, nullable = False) 
  windowWidth = Column(Integer, nullable = False)
  windowHeight = Column(Integer, nullable = False)  
  
  recording = relationship("Pagerecording", 
                         backref=backref("resize_actions", cascade="all,delete", order_by=position))  
    
      