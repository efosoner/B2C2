@echo off
setlocal enabledelayedexpansion

echo.
echo  ============================================
echo   B2C2 Website Test Automation
echo  ============================================
echo.

REM --- Check Java ---
set JAVA_FOUND=false
where java >nul 2>&1 && set JAVA_FOUND=true

if "!JAVA_FOUND!"=="false" if defined JAVA_HOME if exist "%JAVA_HOME%\bin\java.exe" (
    set "PATH=%JAVA_HOME%\bin;%PATH%"
    set JAVA_FOUND=true
)

if "!JAVA_FOUND!"=="false" for /d %%D in ("C:\Program Files\Microsoft\jdk*") do (
    if exist "%%D\bin\java.exe" set "JAVA_HOME=%%D" & set "PATH=%%D\bin;!PATH!" & set JAVA_FOUND=true
)
if "!JAVA_FOUND!"=="false" for /d %%D in ("C:\Program Files\Eclipse Adoptium\jdk*") do (
    if exist "%%D\bin\java.exe" set "JAVA_HOME=%%D" & set "PATH=%%D\bin;!PATH!" & set JAVA_FOUND=true
)
if "!JAVA_FOUND!"=="false" for /d %%D in ("C:\Program Files\Java\jdk*") do (
    if exist "%%D\bin\java.exe" set "JAVA_HOME=%%D" & set "PATH=%%D\bin;!PATH!" & set JAVA_FOUND=true
)
if "!JAVA_FOUND!"=="false" for /d %%D in ("C:\Program Files\Amazon Corretto\jdk*") do (
    if exist "%%D\bin\java.exe" set "JAVA_HOME=%%D" & set "PATH=%%D\bin;!PATH!" & set JAVA_FOUND=true
)

if "!JAVA_FOUND!"=="false" (
    echo  [ERROR] Java not found. Please install Java 17 or higher.
    echo          Download: https://adoptium.net/
    echo.
    pause
    exit /b 1
)
for /f "tokens=3" %%v in ('java -version 2^>^&1 ^| findstr /i "version"') do (
    set JAVA_VER=%%~v
)
echo  [OK] Java found: %JAVA_VER%

REM --- Auto-detect browser (for display only; code handles Chrome->Edge fallback) ---
set BROWSER=chrome
set BROWSER_NAME=Google Chrome (default, auto-downloads via Selenium Manager)

REM Check if user has a local Chrome
if exist "C:\Program Files\Google\Chrome\Application\chrome.exe" set "BROWSER_NAME=Google Chrome"
if exist "C:\Program Files (x86)\Google\Chrome\Application\chrome.exe" set "BROWSER_NAME=Google Chrome"
if exist "%LOCALAPPDATA%\Google\Chrome\Application\chrome.exe" set "BROWSER_NAME=Google Chrome"

REM Check Edge (always available on Windows 10/11)
if exist "C:\Program Files (x86)\Microsoft\Edge\Application\msedge.exe" (
    if "!BROWSER_NAME!"=="Google Chrome (default, auto-downloads via Selenium Manager)" (
        set "BROWSER_NAME=Google Chrome / Microsoft Edge (fallback)"
    )
)

REM Check Firefox
if exist "C:\Program Files\Mozilla Firefox\firefox.exe" set "BROWSER_NAME=!BROWSER_NAME! / Firefox available"

echo  [OK] Browser found: %BROWSER_NAME%
echo.

REM --- Parse arguments ---
set MODE=headless
set SLOW=0
if /i "%~1"=="headed" (
    set MODE=headed
    set SLOW=1500
)
if /i "%~1"=="demo" (
    set MODE=demo
    set SLOW=3000
)

REM --- Build command ---
set HEADLESS_FLAG=true
if "%MODE%"=="headed" set HEADLESS_FLAG=false
if "%MODE%"=="demo" set HEADLESS_FLAG=false

echo  Running tests...
echo  Browser: %BROWSER_NAME%
if "%MODE%"=="headless" (
    echo  Mode:    Headless ^(fast^)
) else if "%MODE%"=="headed" (
    echo  Mode:    Headed with slow-motion ^(1.5s per step^)
) else (
    echo  Mode:    Demo ^(3s per step^)
)
echo.
echo  ============================================
echo.

call mvnw.cmd clean test -Dbrowser=%BROWSER% -Dheadless=%HEADLESS_FLAG% -Dslow=%SLOW%

echo.
if %errorlevel% equ 0 (
    echo  ============================================
    echo   ALL TESTS PASSED
    echo  ============================================
) else (
    echo  ============================================
    echo   SOME TESTS FAILED - see output above
    echo  ============================================
)
echo.
pause
