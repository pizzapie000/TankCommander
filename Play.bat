@echo off
setlocal
cd /d "%~dp0"

rem Use an existing JDK so starting the example needs as little setup as possible.
if defined JAVA_HOME if exist "%JAVA_HOME%\bin\javac.exe" (
    set "JAVA_BIN=%JAVA_HOME%\bin\"
    goto compile
)
where javac >nul 2>&1
if not errorlevel 1 (
    set "JAVA_BIN="
    goto compile
)

rem A bundled JDK can make the game usable even when Java is not on PATH.
for /d %%D in ("%ProgramFiles%\Java\*" "%ProgramFiles%\Eclipse Adoptium\*" "%ProgramFiles%\Microsoft\jdk*") do (
    if exist "%%~fD\bin\javac.exe" (
        set "JAVA_BIN=%%~fD\bin\"
        goto compile
    )
)
for /d %%D in ("%ProgramFiles%\JetBrains\*") do (
    if exist "%%~fD\jbr\bin\javac.exe" (
        set "JAVA_BIN=%%~fD\jbr\bin\"
        goto compile
    )
)
echo A Java Development Kit ^(JDK 8 or newer^) is required.
echo Set JAVA_HOME to your JDK folder or add its bin folder to PATH.
pause
exit /b 1

:compile
rem Rebuild on launch so each practice change is included in the game you play.
if not exist build mkdir build
"%JAVA_BIN%javac.exe" -encoding UTF-8 -d build Tank.java PlayerTank.java EnemyTank.java Game.java
if errorlevel 1 (
    echo Compilation failed. Please check the messages above.
    pause
    exit /b 1
)
"%JAVA_BIN%java.exe" -cp build Game
set "GAME_EXIT=%ERRORLEVEL%"
echo.
pause
exit /b %GAME_EXIT%
