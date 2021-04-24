#!/usr/bin/env bash

POLYGLOT_PATH="polyglot/target/polyglot-1.0-SNAPSHOT.jar"
LANGUAGE_PATH="language/target/language.jar"

$JAVA_HOME/bin/java \
  -Dtruffle.class.path.append="$LANGUAGE_PATH" \
  -cp "$POLYGLOT_PATH" \
  dev.bakku.aml.polyglot.PolyglotDirectlyEmbedded