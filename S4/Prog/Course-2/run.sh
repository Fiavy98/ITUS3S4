#!/bin/bash

set -e

ROOT="$(cd "$(dirname "$0")" && pwd)"
SRC="$ROOT/src"
OUT="$ROOT/out"

# Vérification javac
if ! command -v javac >/dev/null 2>&1; then
    echo "Erreur: javac introuvable. Installe un JDK et vérifie le PATH."
    exit 1
fi

# Vérification java
if ! command -v java >/dev/null 2>&1; then
    echo "Erreur: java introuvable. Installe un JDK et vérifie le PATH."
    exit 1
fi

# Vérification dossier source
if [ ! -d "$SRC" ]; then
    echo "Erreur: dossier source introuvable: $SRC"
    exit 1
fi

# Création dossier output
mkdir -p "$OUT"

TMP_FILE="$(mktemp)"

# Recherche des fichiers Java
find "$SRC" -name "*.java" > "$TMP_FILE"

if [ ! -s "$TMP_FILE" ]; then
    echo "Erreur: aucun fichier Java trouvé dans $SRC"
    rm -f "$TMP_FILE"
    exit 1
fi

echo "Compilation en cours..."

# Compilation
javac -d "$OUT" @"$TMP_FILE"

if [ $? -ne 0 ]; then
    echo "Échec de la compilation."
    rm -f "$TMP_FILE"
    exit 1
fi

rm -f "$TMP_FILE"

echo "Lancement de l'application..."
java -cp "$OUT" racesim.Main "$@"