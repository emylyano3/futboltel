@echo off

set INSTALL_FILE=futboltel-instalation.xml
set SETUP_JAR=Setup.jar
set SETUP_EXE_CONFIG=futboltel-launch4j.xml

if "%1"=="" (
	set APP_JAR=futboltel-backoffice.jar
) else (
	set APP_JAR=%1
)

if exist "..\target\%APP_JAR%" (
	echo Copiando el JAR de la aplicacion al directorio de instalacion...
	copy /y ..\target\%APP_JAR% bin\%APP_JAR%
)

echo Armando el jar y exe de instalacion...
call compile %INSTALL_FILE% -b . -o %SETUP_JAR% -k standard

call launch4jc %SETUP_EXE_CONFIG%

echo Empaquetado de instaladores finalizado.
