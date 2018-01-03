@echo off

set WORK_TEMP=%1
set DB_CLIENT_HOST=%2
set DB_CLIENT_PORT=%3
set DB_CLIENT_USER=%4
set DB_CLIENT_PASS=%5
set DB_LOCAL_HOST=%6
set DB_LOCAL_PORT=%7
set DB_LOCAL_USER=%8
set DB_LOCAL_PASS=%9

set WAR_NAME=deportel
set INSTALL_LOG=%WORK_TEMP%\install.log
set WAR_FILE=%WORK_TEMP%\%WAR_NAME%
set DESCOMPRESSOR=%WORK_TEMP%\7z.exe

set COMP_CONN_PROP_FILE=componentes-connection.properties
set TORNEO_CONN_PROP_FILE=torneo-connection.properties

echo WAR_NAME: %WAR_NAME% >> %INSTALL_LOG%
echo WAR_FILE: %WAR_FILE% >> %INSTALL_LOG%
echo CLIENT HOST: %DB_CLIENT_HOST% > %INSTALL_LOG%
echo CLIENT PORT: %DB_CLIENT_PORT% >> %INSTALL_LOG%
echo CLIENT USER : %DB_CLIENT_USER% >> %INSTALL_LOG%
echo CLIENT PASS: %DB_CLIENT_PASS% >> %INSTALL_LOG%
echo LOCAL HOST: %DB_LOCAL_HOST% >> %INSTALL_LOG%
echo LOCAL PORT: %DB_LOCAL_PORT% >> %INSTALL_LOG%
echo LOCAL USER : %DB_LOCAL_USER% >> %INSTALL_LOG%
echo LOCAL PASS: %DB_LOCAL_PASS% >> %INSTALL_LOG%

set INSTALL_PATH=%WORK_TEMP%\..\deportel
set CONF_PATH=%INSTALL_PATH%\conf

if not exist %INSTALL_PATH% mkdir %INSTALL_PATH%
if not exist %CONF_PATH% mkdir %CONF_PATH%

echo hibernate.connection.url=jdbc:mysql://%DB_LOCAL_HOST%:%DB_LOCAL_PORT%/componentes>%CONF_PATH%/%COMP_CONN_PROP_FILE%
echo hibernate.connection.username=%DB_LOCAL_USER%>>%CONF_PATH%/%COMP_CONN_PROP_FILE%
echo hibernate.connection.password=%DB_LOCAL_PASS%>>%CONF_PATH%/%COMP_CONN_PROP_FILE%

echo hibernate.connection.url=jdbc:mysql://%DB_CLIENT_HOST%:%DB_CLIENT_PORT%/torneo>%CONF_PATH%/%TORNEO_CONN_PROP_FILE%
echo hibernate.connection.username=%DB_CLIENT_USER%>>%CONF_PATH%/%TORNEO_CONN_PROP_FILE%
echo hibernate.connection.password=%DB_CLIENT_PASS%>>%CONF_PATH%/%TORNEO_CONN_PROP_FILE%

call %DESCOMPRESSOR% x %WAR_FILE% -o%INSTALL_PATH%
