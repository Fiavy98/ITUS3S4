#!/bin/bash

mkdir -p bin

# Compiler
javac -d bin -cp "lib/*" src/app/Main.java src/ui/MainFrame.java src/dao/VilleDAO.java src/db/DBConnection.java src/model/Ville.java

# Lancer
java -cp "bin:lib/*" app.Main
