@echo off

set INSTALL_FILE=server-instalation.xml
set SETUP_JAR=Server-Setup.jar
set SETUP_EXE_CONFIG=server-launch4j.xml

echo Armando el jar de instalacion con iZpack...
call compile %INSTALL_FILE% -b . -o %SETUP_JAR% -k standard

echo Creando el exe de instalacion con Launch4J...
call launch4jc %SETUP_EXE_CONFIG%

echo Empaquetado de instaladores finalizado.
