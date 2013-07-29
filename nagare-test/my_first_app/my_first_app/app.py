from __future__ import with_statement

import os
from nagare import presentation

class My_first_app(object):
    pass

@presentation.render_for(My_first_app)
def render(self, h, *args):
    this_file = __file__
    if this_file.endswith('.pyc'):
        this_file = __file__[:-1]

    models_file = os.path.join(os.path.dirname(__file__), 'models.py')

    h.head << h.head.title('Up and Running !')

    h.head.css_url('/static/nagare/application.css')
    h.head.css('defaultapp', '#main { margin-left: 20px; padding-bottom: 100px; background: url(/static/nagare/img/sakura.jpg) no-repeat 123px 100% }')

    with h.div(id='body'):
        h << h.a(h.img(src='/static/nagare/img/logo.png'), id='logo', href='http://www.nagare.org/', title='Nagare home')

        with h.div(id='content'):
            h << h.div('Congratulations!', id='title')

            with h.div(id='main'):
                h << h.h1('Your application is running')

                h << 'You can now:'
                with h.ul:
                    h << h.li('If your application uses a database, add your database entities into ', h.em(models_file))
                    h << h.li('Add your application components into ', h.em(this_file), ' or create new files')

                h << h.em("Have fun!")

    with h.div(id='footer'):
        with h.table:
            with h.tr:
                h << h.th('About Nagare')
                h << h.th('Community')
                h << h.th('Learn', class_='last')

            with h.tr:
                with h.td:
                    with h.ul:
                        h << h.li(h.a('Description', href='http://www.nagare.org/trac/wiki/NagareDescription'))
                        h << h.li(h.a('Features', href='http://www.nagare.org/trac/wiki/NagareFeatures'))
                        h << h.li(h.a('Who uses Nagare?', href='http://www.nagare.org/trac/wiki/WhoUsesNagare'))
                        h << h.li(h.a('Licence', href='http://www.nagare.org/trac/wiki/NagareLicence'))

                with h.td:
                    with h.ul:
                        h << h.li(h.a('Blogs', href='http://www.nagare.org/trac/blog'))
                        h << h.li(h.a('Mailing list', href='http://www.nagare.org/trac/wiki/MailingLists'))
                        h << h.li(h.a('IRC', href='http://www.nagare.org/trac/wiki/IrcChannel'))
                        h << h.li(h.a('Bug report', href='http://www.nagare.org/trac/wiki/BugReport'))

                with h.td(class_='last'):
                    with h.ul:
                        h << h.li(h.a('Documentation', href='http://www.nagare.org/trac/wiki'))
                        h << h.li(h.a('Demonstrations portal', href='http://www.nagare.org/portal'))
                        h << h.li(h.a('Demonstrations', href='http://www.nagare.org/demo'))
                        h << h.li(h.a('Wiki Tutorial', href='http://www.nagare.org/wiki'))

    return h.root

# ---------------------------------------------------------------

app = My_first_app
