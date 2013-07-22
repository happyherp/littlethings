from wsgiref.simple_server import make_server
from pyramid.config import Configurator
from pyramid.response import Response


def hello_world(request):
    return Response('Hello %(name)s!' % request.matchdict)

def showRequest(request):
    s = str(request) + ' ' + str(dir(request))
    return Response(s)

if __name__ == '__main__':
    config = Configurator()

    config.add_route('hello', '/hello/{name}')
    config.add_view(hello_world, route_name='hello')

    config.add_route('showReq','/showReq')
    config.add_view(showRequest, route_name='showReq')

    app = config.make_wsgi_app()
    server = make_server('0.0.0.0', 8080, app)
    server.serve_forever()
