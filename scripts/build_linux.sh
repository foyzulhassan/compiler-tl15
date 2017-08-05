#!/bin/bash

if type -p java; then

	echo found java executable in PATH

	javac -d ../bin -sourcepath ../src ../src/com/compiler/Compiler.java

else
	echo "Unable to find Java"
fi
