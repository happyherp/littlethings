VERSION = '0.0.1'

from setuptools import setup, find_packages

setup(
      name = 'my_first_app',
      version = VERSION,
      author = '',
      author_email = '',
      description = '',
      license = '',
      keywords = '',
      url = '',
      packages = find_packages(),
      include_package_data = True,
      package_data = {'' : ['*.cfg']},
      zip_safe = False,
      install_requires = ('nagare',),
      message_extractors = { 'my_first_app' : [('**.py', 'python', None)] },
      entry_points = """
      [nagare.applications]
      my_first_app = my_first_app.app:app
      """
     )

