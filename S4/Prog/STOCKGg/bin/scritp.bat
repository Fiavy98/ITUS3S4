@echo off
title Creation structure projet gestion stock

REM ================================
REM RACINE DU PROJET
REM ================================

set ROOT=com\gestion\stock

REM ================================
REM CREATION DOSSIERS
REM ================================

mkdir %ROOT%

mkdir %ROOT%\core
mkdir %ROOT%\core\entity
mkdir %ROOT%\core\repository
mkdir %ROOT%\core\service
mkdir %ROOT%\core\ui

mkdir %ROOT%\metier
mkdir %ROOT%\metier\entity
mkdir %ROOT%\metier\repository
mkdir %ROOT%\metier\service
mkdir %ROOT%\metier\ui

mkdir %ROOT%\config
mkdir %ROOT%\exception
mkdir %ROOT%\util

REM ================================
REM CREATION FICHIERS CORE ENTITY
REM ================================

type nul > %ROOT%\core\entity\BaseEntity.java
type nul > %ROOT%\core\entity\ColumnDef.java
type nul > %ROOT%\core\entity\EntityMetadata.java

REM ================================
REM CREATION FICHIERS CORE REPOSITORY
REM ================================

type nul > %ROOT%\core\repository\GenericRepository.java
type nul > %ROOT%\core\repository\CritereRecherche.java

REM ================================
REM CREATION FICHIERS CORE SERVICE
REM ================================

type nul > %ROOT%\core\service\GenericService.java

REM ================================
REM CREATION FICHIERS CORE UI
REM ================================

type nul > %ROOT%\core\ui\GenericTableModel.java
type nul > %ROOT%\core\ui\GenericFormPanel.java
type nul > %ROOT%\core\ui\GenericListPanel.java
type nul > %ROOT%\core\ui\GenericDetailPanel.java

REM ================================
REM CREATION FICHIERS METIER ENTITY
REM ================================

type nul > %ROOT%\metier\entity\Article.java
type nul > %ROOT%\metier\entity\Categorie.java
type nul > %ROOT%\metier\entity\Lot.java
type nul > %ROOT%\metier\entity\Mouvement.java
type nul > %ROOT%\metier\entity\MouvementLotSource.java

REM ================================
REM CREATION FICHIERS METIER REPOSITORY
REM ================================

type nul > %ROOT%\metier\repository\ArticleRepository.java
type nul > %ROOT%\metier\repository\CategorieRepository.java
type nul > %ROOT%\metier\repository\LotRepository.java
type nul > %ROOT%\metier\repository\MouvementRepository.java
type nul > %ROOT%\metier\repository\MouvementLotSourceRepository.java

REM ================================
REM CREATION FICHIERS METIER SERVICE
REM ================================

type nul > %ROOT%\metier\service\StockService.java
type nul > %ROOT%\metier\service\ValorizationStrategy.java
type nul > %ROOT%\metier\service\FifoStrategy.java
type nul > %ROOT%\metier\service\LifoStrategy.java
type nul > %ROOT%\metier\service\CumpStrategy.java
type nul > %ROOT%\metier\service\ResultatSortie.java

REM ================================
REM CREATION FICHIERS METIER UI
REM ================================

type nul > %ROOT%\metier\ui\ArticleFormPanel.java
type nul > %ROOT%\metier\ui\MouvementSaisiePanel.java
type nul > %ROOT%\metier\ui\EtatStockGeneralPanel.java
type nul > %ROOT%\metier\ui\DetailArticlePanel.java

REM ================================
REM CREATION FICHIERS CONFIG
REM ================================

type nul > %ROOT%\config\DatabaseConfig.java

REM ================================
REM CREATION FICHIERS EXCEPTION
REM ================================

type nul > %ROOT%\exception\StockInsuffisantException.java
type nul > %ROOT%\exception\BusinessException.java

REM ================================
REM CREATION FICHIERS UTIL
REM ================================

type nul > %ROOT%\util\SwingUtils.java

REM ================================
REM MAIN APPLICATION
REM ================================

type nul > %ROOT%\MainApplication.java

echo.
echo =========================================
echo STRUCTURE PROJET CREE AVEC SUCCES
echo =========================================
pause