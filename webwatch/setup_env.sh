#delete old env
rm env -R
rm tmp -R

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

#in project dir:  
#virtualenv --no-site-packages env


