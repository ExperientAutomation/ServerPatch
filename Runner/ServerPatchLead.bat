set ProjectPath=N:\QA\LiveWorkSpace\ServerPatch
echo %ProjectPath%
set classpath=%ProjectPath%\bin;%ProjectPath%\lib\*
echo %classpath%
java org.testng.TestNG %ProjectPath%\ServerPatchLead.xml
pause
