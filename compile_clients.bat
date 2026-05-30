@echo off
setlocal enabledelayedexpansion

set "JAVA_HOME=D:\as\jbr"
set "PATH=%JAVA_HOME%\bin;%PATH%"

set "PROJECT_ROOT=%~dp0"
set "HOST_MAIN=%PROJECT_ROOT%module_host_main"
set "ASSETS_DIR=%HOST_MAIN%\src\main\assets"

echo ========================================
echo   Compiling Client One and Client Two
echo ========================================
echo.

if not exist "%ASSETS_DIR%" (
    echo Creating assets directory...
    mkdir "%ASSETS_DIR%"
)

cd /d "%PROJECT_ROOT%"

echo [1/2] Building module_client_one...
call gradlew.bat :module_client_one:assembleDebug
if %ERRORLEVEL% neq 0 (
    echo ERROR: module_client_one build failed!
    exit /b 1
)

set "APK_ONE=%PROJECT_ROOT%module_client_one\app\build\outputs\apk\debug\app-debug.apk"
if exist "%APK_ONE%" (
    copy /Y "%APK_ONE%" "%ASSETS_DIR%\module_client_one-debug.apk"
    echo [OK] module_client_one APK copied to assets
) else (
    echo WARNING: module_client_one APK not found at expected location
)

echo [2/2] Building module_client_two...
call gradlew.bat :module_client_two:assembleDebug
if %ERRORLEVEL% neq 0 (
    echo ERROR: module_client_two build failed!
    exit /b 1
)

set "APK_TWO=%PROJECT_ROOT%module_client_two\app\build\outputs\apk\debug\app-debug.apk"
if exist "%APK_TWO%" (
    copy /Y "%APK_TWO%" "%ASSETS_DIR%\module_client_two-debug.apk"
    echo [OK] module_client_two APK copied to assets
) else (
    echo WARNING: module_client_two APK not found at expected location
)

echo.
echo ========================================
echo   Build Complete!
echo ========================================
echo Output location: %ASSETS_DIR%
dir /B "%ASSETS_DIR%\*.apk"

cd /d "%PROJECT_ROOT%"
exit /b 0
