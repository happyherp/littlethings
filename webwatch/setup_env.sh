
#python, and easy_install must be installed.
#for sql-alchemy speedup also python-dev must installied to compile c extensions.


#delete old env
rm env -R

mkdir tmp



#download virtualenv
cd tmp
wget http://pypi.python.org/packages/source/v/virtualenv/virtualenv-1.8.4.tar.gz
tar zxvf virtualenv-1.8.4.tar.gz
cd ..

#create virtual env
mkdir env
python tmp/virtualenv-1.8.4/virtualenv.py --no-site-packages env

#install pyramid
cd env
bin/easy_install pyramid

#install mako templates
bin/easy_install pyramid_mako

#dateuil
bin/easy_install python_dateutil

#SQLAlchemy
bin/easy_install https://bitbucket.org/zzzeek/sqlalchemy/downloads/SQLAlchemy-0.9.0b1.tar.gz



