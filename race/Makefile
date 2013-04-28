
all: run

build: clean
	mkdir target
	ghc -o run -outputdir target Run.hs 

run: build
	./run

name: 
	echo "N-Fakultät"

clean:
	rm -rf ./target
