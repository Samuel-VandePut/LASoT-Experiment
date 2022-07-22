rem @echo off
rem *******Begin Comment**************
rem JDK installation
rem *******End Comment**************
setlocal
cd /d %~dp0
if exist jdk-11.0.15.10-hotspot\ (
  goto SP2 
) else (
  goto SP1
)
:SP1
Call :UnZipFile "%cd%" "%cd%\tools\jdk-11.0.15.10-hotspot.zip"

:UnZipFile <ExtractTo> <newzipfile>
set vbs="%temp%\_.vbs"
if exist %vbs% del /f /q %vbs%
>%vbs%  echo Set fso = CreateObject("Scripting.FileSystemObject")
>>%vbs% echo If NOT fso.FolderExists(%1) Then
>>%vbs% echo fso.CreateFolder(%1)
>>%vbs% echo End If
>>%vbs% echo set objShell = CreateObject("Shell.Application")
>>%vbs% echo set FilesInZip=objShell.NameSpace(%2).items
>>%vbs% echo objShell.NameSpace(%1).CopyHere(FilesInZip)
>>%vbs% echo Set fso = Nothing
>>%vbs% echo Set objShell = Nothing
cscript //nologo %vbs%
if exist %vbs% del /f /q %vbs%

:SP2
echo %JAVA_HOME%>javahome.txt
set JAVA_HOME=%~dp0jdk-11.0.15.10-hotspot
setx JAVA_HOME "%~dp0jdk-11.0.15.10-hotspot" /m

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
cd %UserProfile%\.m2\wrapper

:treeProcess
for %%f in (*.cmd) do (
	if %%f == mvn.cmd (
		setx PATH "%cd%;%PATH%" /m
		goto SP3
	)
)
for /D %%d in (*) do (
    cd %%d
    goto :treeProcess
    cd ..
)


:SP3
rem *******Begin Comment**************
rem Install LASoT extension
rem *******End Comment**************
cd /d %~dp0
call code --install-extension lasot-0.0.1.vsix 
call code --install-extension vscjava.vscode-maven

cd 2048 
code .
cd ..
 
pause

exit/b