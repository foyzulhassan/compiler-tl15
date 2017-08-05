#!/bin/bash

if type -p java; then

	echo found java executable in PATH

	java -cp ../bin com.compiler.Compiler $1

else
	echo "Unable to find Java"
fi
