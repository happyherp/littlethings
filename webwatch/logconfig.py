import logging
import sys

def setup():
  ch = logging.StreamHandler(sys.stdout)
  ch.setLevel(logging.DEBUG)
  logging.getLogger().addHandler(ch);
  # create formatter
  formatter = logging.Formatter('%(asctime)s - %(name)s - %(levelname)s - %(message)s')
  
  # add formatter to ch
  ch.setFormatter(formatter)
  
  # add ch to logger
  
  logging.getLogger().setLevel(logging.WARN)
  
  
  logging.getLogger("webwatch").setLevel(logging.DEBUG)
  
  