#!/bin/bash

# Créer dossier bin
mkdir -p bin

# Supprimer anciens .class
find bin -name "*.class" -delete

# Compiler tous les fichiers Java automatiquement
javac -d bin $(find . -name "*.java")

# Vérification
if [ $? -eq 0 ]; then
    echo "Compilation réussie !"

    # Exécution
    java -cp bin Main
else
    echo "Erreur de compilation"
fi