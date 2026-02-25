@echo off
setlocal EnableExtensions EnableDelayedExpansion

set "PORT=8080"

if "%~1"=="" goto :help
if /I "%~1"=="start" goto :start
if /I "%~1"=="stop" goto :stop
if /I "%~1"=="status" goto :status
goto :help

:start
echo [server] Starting Spring Boot on port %PORT%...
call mvnw.cmd -DskipTests spring-boot:run
goto :end

:stop
set "FOUND="
for /f "tokens=5" %%a in ('netstat -ano ^| findstr /R /C:":%PORT% .*LISTENING"') do (
  set "FOUND=1"
  echo [server] Stopping PID %%a on port %PORT%...
  taskkill /PID %%a /F >nul 2>&1
)
if not defined FOUND (
  echo [server] No LISTENING process found on port %PORT%.
) else (
  echo [server] Stop completed.
)
goto :end

:status
set "FOUND="
echo [server] Port %PORT% status:
for /f "tokens=1,2,3,4,5" %%a in ('netstat -ano ^| findstr /R /C:":%PORT% .*LISTENING"') do (
  set "FOUND=1"
  echo   %%a %%b %%c %%d PID=%%e
)
if not defined FOUND (
  echo   STOPPED ^(no LISTENING process^)
)
goto :end

:help
echo Usage: %~nx0 ^<start^|stop^|status^>
echo   start  - run Spring Boot in foreground
echo   stop   - kill process listening on port %PORT%
echo   status - show if something is listening on port %PORT%

:end
endlocal
