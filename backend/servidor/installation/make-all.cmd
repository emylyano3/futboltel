@echo off

set APP_WAR=servidor-1.4.0.war
set INSTALL_FILE=server-instalation.xml

pushd ..\..\..\persistencia
echo Compilando y empaquetando el modulo de persistencia de la aplicacion...
call mvn install -Dmaven.test.skip=true
popd

pushd ..
echo Moviendo el WAR target\%APP_WAR%  al directorio de instalacion %CD%\installation\bin
copy /y target\%APP_WAR% installation\bin\deportel
popd

call package.cmd

echo Proceso finalizado.
