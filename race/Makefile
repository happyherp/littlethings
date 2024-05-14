
all: run

build-profile: clean 
	mkdir target
	ghc -prof -fprof-auto -rtsopts -O -o target/run -outputdir target Run.hs  

build: clean
	mkdir target
	ghc -O -o target/run -outputdir target Run.hs 

run: build
	./target/run

name: 
	echo "N-Fakultät"

clean:
	rm -rf ./target
