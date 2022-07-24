cd /d %~dp0

setlocal
rem mkdir %cd%\2048\target\rapports
rem xcopy /s %cd%\2048\target\pit-reports %cd%\2048\target\rapports
rem xcopy /s %cd%\2048\target\reneri %cd%\2048\target\rapports

set TEMPDIR=%TEMP%\ZIP
set FILETOZIP=%cd%\2048\target\pit-reports
set OUTPUTZIP=rapports.zip

:: preparing VBS script
echo Set objArgs = WScript.Arguments > _zipIt.vbs
echo InputFolder = objArgs(0) >> _zipIt.vbs
echo ZipFile = objArgs(1) >> _zipIt.vbs
echo Set fso = WScript.CreateObject("Scripting.FileSystemObject") >> _zipIt.vbs
echo Set objZipFile = fso.CreateTextFile(ZipFile, True) >> _zipIt.vbs
echo objZipFile.Write "PK" ^& Chr(5) ^& Chr(6) ^& String(18, vbNullChar) >> _zipIt.vbs
echo objZipFile.Close >> _zipIt.vbs
echo Set objShell = WScript.CreateObject("Shell.Application") >> _zipIt.vbs
echo Set source = objShell.NameSpace(InputFolder).Items >> _zipIt.vbs
echo Set objZip = objShell.NameSpace(fso.GetAbsolutePathName(ZipFile)) >> _zipIt.vbs
echo if not (objZip is nothing) then  >> _zipIt.vbs
echo    objZip.CopyHere(source) >> _zipIt.vbs
echo    wScript.Sleep 12000 >> _zipIt.vbs
echo end if >> _zipIt.vbs

@ECHO Zipping, please wait...
mkdir %TEMPDIR%
xcopy /y /s %FILETOZIP% %TEMPDIR%
WScript _zipIt.vbs  %TEMPDIR%  %OUTPUTZIP%
del _zipIt.vbs
rmdir /s /q  %TEMPDIR%

@ECHO ZIP Completed.