#!/usr/bin/env bash

JAVA_VERSION=11
GRAALVM_VERSION=21.0.0
COMPONENT_DIR="component_temp_dir"
LANGUAGE_PATH="$COMPONENT_DIR/languages/aml"

rm -rf COMPONENT_DIR

mkdir -p "$LANGUAGE_PATH"
cp language/target/language.jar "$LANGUAGE_PATH"

mkdir -p "$LANGUAGE_PATH/runner"
cp runner/target/runner.jar "$LANGUAGE_PATH/runner/"

mkdir -p "$LANGUAGE_PATH/bin"
cp aml $LANGUAGE_PATH/bin/

touch "$LANGUAGE_PATH/native-image.properties"

mkdir -p "$COMPONENT_DIR/META-INF"
{
    echo "Bundle-Name: AML";
    echo "Bundle-Symbolic-Name: dev.bakku.aml";
    echo "Bundle-Version: $GRAALVM_VERSION";
    echo "Bundle-RequireCapability: org.graalvm; filter:=\"(&(graalvm_version=$GRAALVM_VERSION)(os_arch=amd64))\"";
    echo "x-GraalVM-Polyglot-Part: True"
} > "$COMPONENT_DIR/META-INF/MANIFEST.MF"

(
cd $COMPONENT_DIR || exit 1
jar cfm ../aml-component.jar META-INF/MANIFEST.MF .

echo "bin/aml = ../languages/aml/bin/aml" > META-INF/symlinks
jar uf ../aml-component.jar META-INF/symlinks

{
    echo 'languages/aml/bin/aml = rwxrwxr-x'
} > META-INF/permissions
jar uf ../aml-component.jar META-INF/permissions
)
rm -rf $COMPONENT_DIR
