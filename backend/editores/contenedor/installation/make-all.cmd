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

pushd ..\..\..\..\
echo Compilando y empaquetando todos los modulos de la aplicacion...
call mvn clean install -Dmaven.test.skip=true
popd

echo Creando el JAR ejecutable de la aplicacion...
pushd ..
call mvn assembly:assembly -Dmaven.test.skip=true
echo Moviendo el JAR de la aplicacion al directorio de instalacion...
move target\%APP_JAR% installation\bin\%APP_JAR%
popd

call package.cmd

echo Proceso finalizado.
pause
