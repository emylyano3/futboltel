@echo off

REM
REM 1. Compila el proyecto futboltel y arma un jar ejecutable
REM 2. Con el jar armado crea un nuevo jar de instalacion 
REM

if "%1"=="" (
	set APP_JAR=futboltel-backoffice.jar
) else (
	set APP_JAR=%1
)

if "%2"=="" (
	set INSTALL_FILE=futboltel-instalation.xml
) else (
	set INSTALL_FILE=%2
)

set SETUP_EXE_CONFIG=futboltel-launch4j.xml

echo Creando el JAR de la aplicacion...
pushd ..
call mvn assembly:assembly -Dmaven.test.skip=true
echo Copiando el JAR de la aplicacion al directorio de instalacion...
copy /y target\%APP_JAR% installation\bin\%APP_JAR%
popd

echo Armando el jar de instalacion...
REM Compile izPack
call compile %INSTALL_FILE% -b . -o Setup.jar -k standard

echo Creando el instalador ejecutable...
call launch4jc %SETUP_EXE_CONFIG%

echo Proceso finalizado.

