#!/usr/bin/env bash

VERSION=21.0.0
GRAAL_SDK_PATH="$HOME/.m2/repository/org/graalvm/sdk/graal-sdk/$VERSION/graal-sdk-$VERSION.jar"
TRUFFLE_API_PATH="$HOME/.m2/repository/org/graalvm/truffle/truffle-api/$VERSION/truffle-api-$VERSION.jar"
POLYGLOT_PATH="polyglot/target/polyglot-1.0-SNAPSHOT.jar"
LANGUAGE_PATH="language/target/language.jar"

$JAVA_HOME/bin/java \
  -Dtruffle.class.path.append="$LANGUAGE_PATH" \
  -cp "$GRAAL_SDK_PATH":"$POLYGLOT_PATH":"$LANGUAGE_PATH":"$TRUFFLE_API_PATH" \
  dev.bakku.aml.polyglot.Polyglot