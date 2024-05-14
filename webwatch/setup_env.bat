echo Install easyinstall and put python/scripts in PATH to run this.
echo https://pypi.python.org/pypi/setuptools/1.1.6#installation-instructions


echo setting up a virutal enviroment

mkdir env
virutalenv env --no-site-packages

#install pyramid
cd env
scripts\easy_install pyramid

#install mako templates
scripts\easy_install pyramid_mako

#dateuil
scripts\easy_install python_dateutil


