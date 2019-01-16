#!/bin/bash

mkdir -p out/run

javac -cp src/ -d out/run  src/sample/Main.java

i=0
passed=0
SECONDS=0
for example in examples/*.in
do
	i=$((i+1))
	echo "========= Run $i ($example) =========="
	java -cp out/run/ sample.Main "$example"
done

echo "=============================="
echo "Run $i inputs end. Total time: $SECONDS seconds"

rm -r out/run
