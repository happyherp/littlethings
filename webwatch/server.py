from wsgiref.simple_server import make_server
from pyramid.config import Configurator

import views

import os

from db import Session as DBSession



def manageSession(request):
  session = DBSession()  
  def closeSession(request):
    session.close()
  request.add_finished_callback(closeSession)
  return session

if __name__ == '__main__':

  here = os.path.dirname(os.path.abspath(__file__))

  settings = {}
  settings['reload_all'] = True
  settings['debug_all'] = True
  settings['mako.directories'] = os.path.join(here, 'templates')
  

  config = Configurator(settings=settings)
  
  config.add_request_method(manageSession, "session", reify=True)

  config.include('pyramid_mako')

  config.add_static_view("static", "static", cache_max_age = 0)

  #also show sample page. this is for testing and not part of the project itself. 
  config.add_static_view("samplepage", "samplepage", cache_max_age = 0)


  views.addToConfig(config)

  app = config.make_wsgi_app()
  server = make_server('0.0.0.0', 8080, app)

  server.serve_forever()
