
all: run

build: clean
	mkdir target
	ghc -o target/run -outputdir target Run.hs 

run: build
	./target/run

name: 
	echo "N-Fakultät"

clean:
	rm -rf ./target
