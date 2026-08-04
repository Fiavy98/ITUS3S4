#!/bin/bash

# Nom du projet
PROJECT_DIR=$(pwd)
SRC_DIR="$PROJECT_DIR/src"
BIN_DIR="$PROJECT_DIR/bin"
LIB_DIR="$PROJECT_DIR/lib"

# Créer le dossier bin s'il n'existe pas
mkdir -p "$BIN_DIR"

echo "Compilation en cours..."

# Compiler tous les .java avec le classpath incluant les .jar
javac -d "$BIN_DIR" -cp "$LIB_DIR/*" $(find "$SRC_DIR" -name "*.java")

# Vérifier si la compilation a échoué
if [ $? -ne 0 ]; then
    echo "Erreur de compilation !"
    exit 1
fi

echo "Compilation réussie !"

# Exécuter Main (qui est directement dans src, sans package)
echo "Exécution de Main..."
java -cp "$BIN_DIR:$LIB_DIR/*" Main
