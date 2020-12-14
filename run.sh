#!/bin/bash

##process an arguments first
prog_args=""
arg_regex="-d|-t|-se"


if [ $# -ne 0 ]
then
    while [[ "$1" =~ ${arg_regex} ]]; do
	prog_args=$prog_args"$1 "
	shift
    done
fi

if [ $# -eq 1 ]
then
    userinput=$1
    sample="inputs/input_$userinput.txt"
elif [ $# -eq 2 ]
then
    userinput=$1
    sample=$2     
else
    echo -n "Enter problem number: "
    read userinput
    sample="inputs/input_$userinput.txt"
fi

#validate that file exists first     
if [[ $userinput =~ ^0[1-9]|[12][0-9]$ ]]   # checks that the input is within the desired range
then
    replace=$(./replace_index.sh $userinput)
    file="com/nbkelly/advent/Advent"$userinput".java"
    path="com.nbkelly.advent.Advent"$userinput
    if test -f "$file"; then
	#see if it compiles
	javac $file
	if [ $? -eq 0 ]
	then
	    #the file compiles, let's check the input file exists
	    if test -f "$sample"; then
		#the user input also exists, run the file (in debug mode)
		eval "cat '$sample' | $replace java $path $prog_args"
	    else
		if test -f "inputs/$sample"; then
		    eval "cat 'inputs/$sample' | $replace java $path $prog_args"
		    #cat "inputs/"$sample | $replace java $path -se
		else
		    echo "Input file $sample does not exists"
		fi
	    fi
	fi	    
    else
	echo $file "does not exist"
    fi     
else
    echo "Invalid problem number"
fi


