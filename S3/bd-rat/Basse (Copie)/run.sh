#!/bin/bash

rm -rf bin
mkdir -p bin

find src -name "*.java" > sources.txt

javac -d bin @sources.txt
if [ $? -ne 0 ]; then
    echo "❌ Erreur de compilation"
    exit 1
fi

java -cp bin mydb.Main
