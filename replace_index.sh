#!/bin/bash

if [ $# -eq 1 ]
then
    line=$(cat replace.txt | grep -m 1 "$1")
    if [ -z "$line" ]
    then
	#do nothing
	exit
    else
	tmp=${line#* }   # remove prefix ending in " "
	#echo $tmp
	javac "replace.java"
	echo "java replace $tmp | "
    fi    
fi
