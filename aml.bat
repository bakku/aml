set RUNNER_PATH=runner\target\runner.jar
set LANGUAGE_PATH=language\target\language.jar

%JAVA_HOME%\bin\java ^
  -Dtruffle.class.path.append=%LANGUAGE_PATH% ^
  -cp "%RUNNER_PATH%" ^
  dev.bakku.aml.runner.Runner %1