#!/bin/bash
set -e
readonly PROG_DIR="$(realpath "$(dirname $0)")"

readonly OUT="${OUT:-"out"}"
readonly DIST="${DIST_DIR:-"out/dist"}"
mkdir -p $OUT
mkdir -p $DIST
readonly OUT_DIR="$(realpath $OUT)"
readonly DIST_DIR="$(realpath $DIST)"
function set_java_home() {
    case `uname -s` in
        Darwin)
            export JAVA_HOME=../../../prebuilts/jdk/jdk8/darwin-x86
            export JDK_9=../../../prebuilts/jdk/jdk9/darwin-x86
            ;;
        *)
            export JAVA_HOME=../../../prebuilts/jdk/jdk8/linux-x86
            export JDK_9=../../../prebuilts/jdk/jdk9/linux-x86
            ;;
    esac
}
readonly R4A_BUILD_NUMBER=1.3.70
function copy_jar_into_maven_repo() {
    local SOURCE_JAR="$1"
    local MODULE_NAME="$2"
    if [ ! -f $SOURCE_JAR ]; then
        echo -e "\033[1;31mERROR: Could not publish module $MODULE_NAME \033[0m"
        echo "  File $SOURCE_JAR does not exist"
        exit 1
    fi
    local MODULE_DIRECTORY=$OUT_DIR/m2/org/jetbrains/kotlin/$MODULE_NAME/$R4A_BUILD_NUMBER
    mkdir -p $MODULE_DIRECTORY
    cp $SOURCE_JAR $MODULE_DIRECTORY/$MODULE_NAME-$R4A_BUILD_NUMBER.jar
}
set_java_home
export JDK_18=$JAVA_HOME
export JDK_17=$JAVA_HOME
export JDK_16=$JAVA_HOME

cd $PROG_DIR

./bunch-cli/bin/bunch switch as40

# Build a custom version of Kotlin
./gradlew writeVersions --no-daemon -Pbuild.number=$R4A_BUILD_NUMBER -PdeployVersion=$R4A_BUILD_NUMBER
./gradlew install ideaPlugin  :compiler:tests-common:testJar --no-daemon -Pbuild.number=$R4A_BUILD_NUMBER -PdeployVersion=$R4A_BUILD_NUMBER -Dmaven.repo.local=$OUT_DIR/m2  -Pteamcity=true

# Run tests
#rm -rf $DIST_DIR/host-test-reports
#mkdir $DIST_DIR/host-test-reports

#./gradlew --info --full-stacktrace --continue :compiler:test --tests *ParsingTestGenerated* -Pteamcity=true
#cd compiler/build/test-results/test
#zip -r $DIST_DIR/host-test-reports/compilerTests.zip *
#cd $PROG_DIR

#./gradlew  --info --full-stacktrace --continue :idea:test --tests *FormatterTestGenerated* -Pteamcity=true
#cd idea/build/test-results/test
#zip -r $DIST_DIR/host-test-reports/ideaTests.zip *
#cd $PROG_DIR

# Copy jar files that are not published in the build but are required by androidx.compose
echo "Copying additional repositories"
#readonly INTELLIJ_SDK_VERSION=$(grep intellijSdk gradle/versions.properties | sed 's/^[^=]*=//')
#readonly ANDROID_STUDIO_BUILD=$(grep androidStudioBuild gradle/versions.properties | sed 's/^[^=]*=//')
#readonly INTELLIJ_DEPENDENCIES=dependencies/repo/kotlin.build
#if [ ! -f $INTELLIJ_DEPENDENCIES/intellij-core/$INTELLIJ_SDK_VERSION/artifacts/intellij-core.jar ]; then
#    echo -e "\033[1;31mError: Could not determine intellij version, tried $INTELLIJ_DEPENDENCIES/intellij-core/$INTELLIJ_SDK_VERSION/artifacts/intellij-core.jar\033[0m"
#    exit 1
#fi
#copy_jar_into_maven_repo $INTELLIJ_DEPENDENCIES/intellij-core/$INTELLIJ_SDK_VERSION/artifacts/intellij-core.jar kotlin-intellij-core
#copy_jar_into_maven_repo $INTELLIJ_DEPENDENCIES/android-studio-ide/$ANDROID_STUDIO_BUILD/artifacts/lib/idea.jar kotlin-idea
#copy_jar_into_maven_repo $INTELLIJ_DEPENDENCIES/android-studio-ide/$ANDROID_STUDIO_BUILD/artifacts/lib/idea_rt.jar kotlin-idea-rt
#copy_jar_into_maven_repo $INTELLIJ_DEPENDENCIES/android-studio-ide/$ANDROID_STUDIO_BUILD/artifacts/plugins/java/lib/java-impl.jar kotlin-java-impl
#copy_jar_into_maven_repo $INTELLIJ_DEPENDENCIES/android-studio-ide/$ANDROID_STUDIO_BUILD/artifacts/lib/bootstrap.jar kotlin-bootstrap
#copy_jar_into_maven_repo $INTELLIJ_DEPENDENCIES/android-studio-ide/$ANDROID_STUDIO_BUILD/artifacts/lib/openapi.jar kotlin-openapi
#copy_jar_into_maven_repo $INTELLIJ_DEPENDENCIES/android-studio-ide/$ANDROID_STUDIO_BUILD/artifacts/lib/platform-api.jar kotlin-platform-api
#copy_jar_into_maven_repo $INTELLIJ_DEPENDENCIES/android-studio-ide/$ANDROID_STUDIO_BUILD/artifacts/lib/platform-impl.jar kotlin-platform-impl
copy_jar_into_maven_repo dist/artifacts/ideaPlugin/Kotlin/lib/kotlin-plugin.jar kotlin-plugin
copy_jar_into_maven_repo dist/artifacts/ideaPlugin/Kotlin/lib/jps/kotlin-jps-plugin.jar kotlin-jps-plugin
copy_jar_into_maven_repo idea/idea-jps-common/build/libs/idea-jps-common-$R4A_BUILD_NUMBER.jar kotlin-jps-common-ide
copy_jar_into_maven_repo j2k/build/libs/j2k-$R4A_BUILD_NUMBER.jar kotlin-j2k
copy_jar_into_maven_repo compiler/tests-common/build/libs/tests-common-$R4A_BUILD_NUMBER-tests.jar kotlin-tests-common
# remove a bunch of build artifacts that often accidentally get committed and then break future builds
rm -rf libraries/tools/kotlin-source-map-loader/lib/
rm -rf libraries/tools/kotlin-source-map-loader/node_modules/
rm -rf libraries/tools/kotlin-test-js-runner/.rpt2_cache/
rm -rf libraries/tools/kotlin-test-js-runner/lib/
rm -rf libraries/tools/kotlin-test-js-runner/node_modules/
# tar up the distrbution
echo "tar'ing result m2.tar"
tar cf $OUT_DIR/m2.tar -C $OUT_DIR m2
mv $OUT_DIR/m2.tar $DIST_DIR
