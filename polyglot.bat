set VERSION=21.0.0
set GRAAL_SDK_PATH=%USERPROFILE%\.m2\repository\org\graalvm\sdk\graal-sdk\%VERSION%\graal-sdk-%VERSION%.jar
set TRUFFLE_API_PATH=%USERPROFILE%\.m2\repository\org\graalvm\truffle\truffle-api\%VERSION%\truffle-api-%VERSION%.jar
set POLYGLOT_PATH=polyglot\target\polyglot-1.0-SNAPSHOT.jar
set LANGUAGE_PATH=language\target\language.jar

%JAVA_HOME%\bin\java ^
  -Dtruffle.class.path.append=%LANGUAGE_PATH% ^
  -cp "%GRAAL_SDK_PATH%;%POLYGLOT_PATH%;%LANGUAGE_PATH%;%TRUFFLE_API_PATH%" ^
  dev.bakku.aml.polyglot.PolyglotDirectlyEmbedded