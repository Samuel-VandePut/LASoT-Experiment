cd /d %~dp0
rem *******Begin Comment**************
rem Set Path environment for maven wrapper bin folder
rem *******End Comment**************

set /p javahome=<javahome.txt
if javahome == "" goto SP2
setx JAVA_HOME "%javahome%" /m 

:SP2
set /p oldpath=<oldpath.txt
setx PATH "%oldpath%" /m

code --uninstall-extension undefined_publisher.lasot
pause