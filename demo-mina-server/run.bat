IF "%JAVA_HOME%"==""  SET /P JAVA_HOME="Please Set JAVA_HOME:" 
IF "%CLASSPATH%"=="" SET /P CLASSPATH="Please Set CLASSPATH:" 

@REM LOOP JAR UNDER LIB, AND SET CLASSPATH
FOR %%F IN (%~dp0lib\*.jar) DO CALL :addcp %%F
GOTO extlibe
:addcp
SET CLASSPATH=%CLASSPATH%;%1
GOTO :EOF
:extlibe
SET java_call=java -classpath "%CLASSPATH%" cn.pubinfo.main.server.Main

%java_call%

PAUSE