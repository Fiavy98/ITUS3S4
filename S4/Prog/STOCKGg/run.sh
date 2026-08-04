#!/bin/bash

echo "========================================"
echo "Compilation et lancement de l'application"
echo "Gestion de stock (FIFO/LIFO/CUMP)"
echo "========================================"
echo

# Verifier que Maven est disponible
if ! command -v mvn &> /dev/null; then
    echo "Maven n'a pas ete trouve. Veuillez installer Maven."
    exit 1
fi

echo "Lancement de mvn exec:java..."
echo

mvn clean compile exec:java

if [ $? -eq 0 ]; then
    echo
    echo "Application terminee."
else
    echo
    echo "Erreur lors de l'execution."
    read -p "Appuyez sur Entrée pour continuer..."
fi