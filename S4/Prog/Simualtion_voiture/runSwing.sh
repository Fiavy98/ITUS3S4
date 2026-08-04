#!/usr/bin/env bash
set -euo pipefail

ROOT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"

cd "$ROOT_DIR"

javac \
  "$ROOT_DIR/MainSwing.java" \
  "$ROOT_DIR/Swing"/*.java \
  "$ROOT_DIR/Back/modele"/*.java \
  "$ROOT_DIR/Back/logique"/*.java \
  "$ROOT_DIR/Back/logique/DAO"/*.java

java MainSwing
