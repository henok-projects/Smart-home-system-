@echo off

REM Getting the script directory
for %%i in ("%~dp0") do set SCRIPT_DIR=%%~dpi

REM rabbitmq-env.bat is used to define environment variables. Check https://www.rabbitmq.com/configure.html#rabbitmq-env-file-windows
set NODENAME=galsie-node1@localhost

REM Define environment variable for advanced config. Variable name is as needed by RabbitMQ
set ADVANCED_CONFIG_FILE="%SCRIPT_DIR%config\advanced.config"

REM Define environment variable for advanced config. Variable name is as needed by RabbitMQ
set CONFIG_FILES="%SCRIPT_DIR%config\config.d"

REM Server logs are put here in the main dir under RabbitMQ. It generates /out/logs folder and puts logs in it
set LOG_BASE="%SCRIPT_DIR%logs"

REM Print the environment variable values for verification
echo .......
echo RabbitMQ configuration:
echo - NODENAME=%NODENAME%
echo - ADVANCED_CONFIG_FILE=%ADVANCED_CONFIG_FILE%
echo - CONFIG_FILES=%CONFIG_FILES%
echo - LOG_BASE=%LOG_BASE%
echo .......