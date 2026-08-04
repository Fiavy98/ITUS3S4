@echo off
rem Script de compilation et d'exécution pour Windows (cmd.exe)
rem Placez le driver JDBC Oracle (ojdbc8.jar ou ojdbc11.jar) dans le dossier lib\

setlocal
echo === Vérification du jar JDBC dans lib ===
if not exist "lib\ojdbc8.jar" if not exist "lib\ojdbc11.jar" (
  echo Aucun driver Oracle trouvé dans lib\. Copiez ojdbc8.jar ou ojdbc11.jar dans lib\ et relancez.
  pause
  exit /b 1
) else (
  echo Driver JDBC trouvé.
)

echo === Création du dossier de classes ===
if not exist out\classes mkdir out\classes

echo === Compilation des sources ===
set JAVA_CP=lib\*;.

rem Générer la liste des sources dans un fichier temporaire
if exist sources.txt del /f /q sources.txt
for /R "src" %%f in (*.java) do (
  echo %%f>>sources.txt
)

rem Compiler tous les fichiers d'un seul coup pour respecter les dépendances entre classes
javac -d out\classes -cp "%JAVA_CP%" @sources.txt
if errorlevel 1 (
  echo Erreur de compilation.
  del /f /q sources.txt
  pause
  exit /b 1
)
del /f /q sources.txt
echo Compilation réussie.

echo === Lancement de l'application ===
java -cp "out\classes;lib\*;." Main

endlocal
pause
