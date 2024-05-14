#delete old env
rm env -R

mkdir tmp

export STACKLESS_HOME=/usr/bin/stackless-python

#download&install stackless
if [-o stackless] 
 then
  cd tmp
  wget http://www.stackless.com/binaries/stackless-272-export.tar.bz2
  tar jxvf stackless-272-export.tar.bz2

  cd stackless-272-export
  ./configure --prefix=$STACKLESS_HOME
  make all
  sudo make install
  cd ../..
fi


#download virtualenv
cd tmp
wget http://pypi.python.org/packages/source/v/virtualenv/virtualenv-1.8.4.tar.gz
tar zxvf virtualenv-1.8.4.tar.gz
cd ..

#create virtual env
mkdir env
$STACKLESS_HOME/bin/python tmp/virtualenv-1.8.4/virtualenv.py --no-site-packages env

#install pyramid
cd env
bin/easy_install pyramid

#in project dir:  
#virtualenv --no-site-packages env


