@echo off
"%JAVA_HOME%"\bin\java -version > nul 2>&1
if %ERRORLEVEL% == 0 goto found
echo [JAVALOG]JAVA NOT FOUND
goto end

:found
echo [JAVALOG]JAVA FOUND


java -cp ../bin com.compiler.Compiler %1


:end