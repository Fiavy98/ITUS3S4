#!/bin/bash

echo "=============================="
echo " Compilation du projet"
echo "=============================="

mkdir -p bin

# Compilation
javac \
-d bin \
-cp "lib/postgresql.jar:." \
$(find com -name "*.java")

if [ $? -ne 0 ]; then
    echo "❌ Erreur de compilation"
    exit 1
fi

echo ""
echo "=============================="
echo " Lancement application"
echo "=============================="

# Exécution
java \
-cp "bin:lib/postgresql-42.7.3.jar" \
com.gestion.stock.MainApplication