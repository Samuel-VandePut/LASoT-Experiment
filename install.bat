@echo off
rem *******Begin Comment**************
rem JDK installation
rem *******End Comment**************
setlocal
cd /d %~dp0
@REM ==== START VALIDATION ====
if not "%JAVA_HOME%" == "" goto OkJHome

echo.
echo Error: JAVA_HOME not found in your environment. >&2
echo Please set the JAVA_HOME variable in your environment to match the >&2
echo location of your Java installation. >&2
echo.
goto error

:OkJHome

if exist "%JAVA_HOME%\bin\java.exe" goto SP2

echo.
echo Error: JAVA_HOME is set to an invalid directory. >&2
echo JAVA_HOME = "%JAVA_HOME%" >&2
echo Please set the JAVA_HOME variable in your environment to match the >&2
echo location of your Java installation. >&2
echo.
goto error

@REM ==== END VALIDATION ====

:SP2
rem *******Begin Comment**************
rem Reneri Installation
rem *******End Comment**************

cd tarshes-descartes-reneri
call mvnw.cmd clean install

rem *******Begin Comment**************
rem 2048 Installation
rem *******End Comment**************
cd../2048
call mvnw.cmd clean install


rem *******Begin Comment**************
rem Set Path environment for maven wrapper bin folder
rem *******End Comment**************
cd /d %~dp0
echo %PATH% > oldpath.txt
setx PATH "%cd%\apache-maven-3.6.0\bin;%PATH%" /m

:SP3
rem *******Begin Comment**************
rem Install LASoT extension
rem *******End Comment**************
cd /d %~dp0
call code --install-extension lasot-0.0.1.vsix
call code --install-extension vscjava.vscode-maven.vsix

cd 2048 
code .
cd ..

:error

pause

exit/b