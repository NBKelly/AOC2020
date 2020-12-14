#!/bin/bash

if [ $# -eq 1 ]
then
    userinput=$1
else
    echo -n "Enter problem number: "
    read userinput
fi

if [[ $userinput =~ ^0[1-9]|[12][0-9]([a-zA-Z]?)$ ]]   # checks that the input is within the desired range
then
    file="com/nbkelly/advent/Advent"$userinput".java"
    if test -f "$file"; then
	emacs -nw $file
    else
	echo -n "Create file Advent$userinput?: "
	read userinput2
	if [ -z "$userinput2" ]
	then	    
	    echo $file	    
	     built-tool/easy.sh -y -c Advent$userinput -l com/nbkelly/advent -a com/nbkelly/helper
	     emacs -nw $file
	else
	    echo "Cancelled"
	fi
    fi     
else
    echo "Invalid problem number"
fi


