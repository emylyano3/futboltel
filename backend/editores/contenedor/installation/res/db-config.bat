@echo off

set INSTALL_PATH=%1
set DB_HOST=%2
set DB_PORT=%3
set DB_USER=%4
set DB_PASS=%5
set CLIENT_DB_HOST=%6
set CLIENT_DB_PORT=%7
set CLIENT_DB_USER=%8
set CLIENT_DB_PASS=%9

set COMP_CONN_PROP_FILE=componentes-connection.properties
set ADMIN_CONN_PROP_FILE=administracion-connection.properties
set CLIENT_CONN_PROP_FILE=torneo-connection.properties

set CONF_PATH=%INSTALL_PATH%/conf

if not exist %CONF_PATH% mkdir %CONF_PATH%

echo hibernate.connection.url=jdbc:mysql://%DB_HOST%:%DB_PORT%/componentes>%CONF_PATH%/%COMP_CONN_PROP_FILE%
echo hibernate.connection.username=%DB_USER%>>%CONF_PATH%/%COMP_CONN_PROP_FILE%
echo hibernate.connection.password=%DB_PASS%>>%CONF_PATH%/%COMP_CONN_PROP_FILE%

echo hibernate.connection.url=jdbc:mysql://%DB_HOST%:%DB_PORT%/administracion>%CONF_PATH%/%ADMIN_CONN_PROP_FILE%
echo hibernate.connection.username=%DB_USER%>>%CONF_PATH%/%ADMIN_CONN_PROP_FILE%
echo hibernate.connection.password=%DB_PASS%>>%CONF_PATH%/%ADMIN_CONN_PROP_FILE%

echo hibernate.connection.url=jdbc:mysql://%CLIENT_DB_HOST%:%CLIENT_DB_PORT%/torneo>%CONF_PATH%/%CLIENT_CONN_PROP_FILE%
echo hibernate.connection.username=%CLIENT_DB_USER%>>%CONF_PATH%/%CLIENT_CONN_PROP_FILE%
echo hibernate.connection.password=%CLIENT_DB_PASS%>>%CONF_PATH%/%CLIENT_CONN_PROP_FILE%
