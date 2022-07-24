cd /d %~dp0

set /p javahome=<javahome.txt
if javahome == "" goto SP2
setx JAVA_HOME "%javahome%" /m 

:SP2
set /p oldpath=<oldpath.txt
if oldpath == "" goto SP3
setx PATH "%oldpath%" /m

:SP3
code --uninstall-extension undefined_publisher.lasot
pause