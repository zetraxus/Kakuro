#!/bin/bash

mkdir -p out/autotest

javac -cp src/ -d out/autotest  src/sample/Main.java

i=0
passed=0
for example in examples/*.in
do
	i=$((i+1))
	echo -n "Run test $i ($example): "
	java -cp out/autotest/ sample.Main "$example" out/autotest/tmp.out > /dev/null
	filename="${example##*/}"
	if cmp -s out/autotest/tmp.out examples/outputs/${filename%.*}.out; then
		echo "Ok"
		passed=$((passed+1))
	else
		echo "FAILED"
	fi
	
done

echo "=============================="
echo "Tests end. Passed $passed of $i tests."

rm -r out/autotest
