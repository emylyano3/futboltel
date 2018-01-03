@echo off
set INSTALL_PATH=%1
set INSTALL_SCRIPT=%2
set INSTALL_OUTPUT=%3
set DB_HOST=%4
set DB_PORT=%5
set DB_USER=%6
set DB_PASS=%7
set MYSQL_HOME=%8
set INSTALL_LOG=%INSTALL_PATH%/temp/debug.log

set LOG_PATH=%INSTALL_PATH%/bin/logs

set MYSQL_BIN=%MYSQL_HOME%

echo HOST: %DB_HOST% > %INSTALL_LOG%
echo USER: %DB_USER% >> %INSTALL_LOG%
echo PASS: %DB_PASS% >> %INSTALL_LOG% 
echo PATH: %INSTALL_PATH% >> %INSTALL_LOG%
echo SCRIPT: %INSTALL_SCRIPT% >> %INSTALL_LOG%
echo OUTPUT: %INSTALL_OUTPUT% >> %INSTALL_LOG%
echo MYSQL: %MYSQL_BIN%\mysql.exe >> %INSTALL_LOG%

call %MYSQL_BIN%\mysql.exe -h %DB_HOST% -P %DB_PORT% -u %DB_USER% -p%DB_PASS% < %INSTALL_PATH%/temp/%INSTALL_SCRIPT% > %INSTALL_PATH%/temp/%INSTALL_OUTPUT%