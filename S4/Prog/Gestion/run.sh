#!/bin/bash

echo "========================================"
echo "Compilation et lancement de l'application"
echo "Gestion de stock (FIFO/LIFO/CUMP)"
echo "========================================"
echo

# Vérifier que Maven est installé
if ! command -v mvn >/dev/null 2>&1; then
    echo "Maven n'est pas trouvé dans le PATH."
    echo "Veuillez installer Maven ou ajouter mvn au PATH."
    read -p "Appuyez sur Entrée pour quitter..."
    exit 1
fi

echo "Lancement de mvn exec:java..."
echo

mvn clean compile exec:java

if [ $? -eq 0 ]; then
    echo
    echo "Application terminée."
else
    echo
    echo "Erreur lors de l'exécution."
    read -p "Appuyez sur Entrée pour quitter..."
fi