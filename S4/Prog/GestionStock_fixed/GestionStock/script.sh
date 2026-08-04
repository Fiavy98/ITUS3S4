#!/bin/bash

echo "Compilation du projet..."

# créer dossier bin si pas existant
mkdir -p bin

# compiler tous les fichiers java
javac -d bin $(find . -name "*.java")

if [ $? -eq 0 ]; then
    echo "Compilation réussie ✔"
    echo "Lancement de l'application..."

    java -cp bin:postgresql-42.7.3.jar Main
else
    echo "Erreur de compilation ❌"
fi