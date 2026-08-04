@echo off
echo ========================================
echo Compilation et lancement de l'application
echo Gestion de stock (FIFO/LIFO/CUMP)
echo ========================================
echo.

REM Vérifier que Maven est installé
where mvn >nul 2>nul
if %errorlevel% neq 0 (
    echo Maven n'est pas trouve dans le PATH.
    echo Veuillez installer Maven ou ajouter mvn.cmd au PATH.
    pause
    exit /b 1
)

echo Lancement de mvn exec:java...
echo.

mvn clean compile exec:java

if %errorlevel% equ 0 (
    echo.
    echo Application terminee.
) else (
    echo.
    echo Erreur lors de l'execution.
    pause
)