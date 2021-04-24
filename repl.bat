set REPL_PATH=repl\target\repl.jar
set LANGUAGE_PATH=language\target\language.jar

%JAVA_HOME%\bin\java ^
  -Dtruffle.class.path.append=%LANGUAGE_PATH% ^
  -cp "%REPL_PATH%" ^
  dev.bakku.aml.repl.REPL