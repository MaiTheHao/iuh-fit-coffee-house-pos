#!/bin/bash
set -euo pipefail

LIB_DIR="lib"
SRC_DIR="src"
OUT_DIR="bin"
RESOURCES_DIR="resources"
MAIN_CLASS="com.coffeehouse.App"
TEST_JAR="lib/junit-platform-console-standalone-6.0.3.jar"

show_help() {
    cat <<'EOF'
Usage: ./run.sh [--compile-only|--run-only|--test|--test-only|--help]

    --compile-only  Compile sources only
    --run-only      Run without compiling
    --test          Compile then run tests
    --test-only     Run tests without compiling
    --help          Show this help
EOF
}

mode="all"
while [[ $# -gt 0 ]]; do
    case "$1" in
        --compile-only)
            mode="compile"
            ;;
        --run-only)
            mode="run"
            ;;
        --test)
            mode="test"
            ;;
        --test-only)
            mode="test-only"
            ;;
        -h|--help)
            show_help
            exit 0
            ;;
        *)
            echo "Unknown option: $1"
            show_help
            exit 1
            ;;
    esac
    shift
done

echo "Preparing environment"

if [[ ! -d "$LIB_DIR" ]]; then
    echo "Missing lib directory: $LIB_DIR"
    exit 1
fi

shopt -s nullglob
jars=("$LIB_DIR"/*.jar)
shopt -u nullglob

if [[ ${#jars[@]} -eq 0 ]]; then
    echo "No jar files found in $LIB_DIR"
    exit 1
fi

LOMBOK_JAR=""
for jar in "${jars[@]}"; do
    if [[ "$(basename "$jar")" == lombok-*.jar ]]; then
        LOMBOK_JAR="$jar"
        break
    fi
done

if [[ -z "$LOMBOK_JAR" ]]; then
    echo "Missing Lombok jar in $LIB_DIR"
    exit 1
fi

CLASSPATH=$(IFS=:; echo "${jars[*]}")

if [[ "$mode" != "run" && "$mode" != "test-only" ]]; then
    echo "Cleaning output"
    rm -rf "$OUT_DIR"
    mkdir -p "$OUT_DIR"

    if [[ ! -d "$SRC_DIR" ]]; then
        echo "Missing src directory: $SRC_DIR"
        exit 1
    fi

    echo "Compiling sources"
    javac -cp "$CLASSPATH" \
                -processorpath "$LOMBOK_JAR" \
                -d "$OUT_DIR" \
                --add-opens jdk.compiler/com.sun.tools.javac.code=ALL-UNNAMED \
                --add-opens jdk.compiler/com.sun.tools.javac.comp=ALL-UNNAMED \
                --add-opens jdk.compiler/com.sun.tools.javac.file=ALL-UNNAMED \
                --add-opens jdk.compiler/com.sun.tools.javac.main=ALL-UNNAMED \
                --add-opens jdk.compiler/com.sun.tools.javac.model=ALL-UNNAMED \
                --add-opens jdk.compiler/com.sun.tools.javac.parser=ALL-UNNAMED \
                --add-opens jdk.compiler/com.sun.tools.javac.processing=ALL-UNNAMED \
                --add-opens jdk.compiler/com.sun.tools.javac.tree=ALL-UNNAMED \
                --add-opens jdk.compiler/com.sun.tools.javac.util=ALL-UNNAMED \
                $(find "$SRC_DIR" -name "*.java")

    echo "Compile done"
fi

if [[ "$mode" == "test" || "$mode" == "test-only" ]]; then
    if [[ ! -d "$OUT_DIR" ]]; then
        echo "Missing output directory: $OUT_DIR"
        exit 1
    fi

    if [[ ! -f "$TEST_JAR" ]]; then
        echo "Missing test jar: $TEST_JAR"
        exit 1
    fi

    echo "Running tests"
    java -jar "$TEST_JAR" execute --class-path "$OUT_DIR:$CLASSPATH" --scan-class-path
fi

if [[ "$mode" != "compile" && "$mode" != "test" && "$mode" != "test-only" ]]; then
    if [[ ! -d "$OUT_DIR" ]]; then
        echo "Missing output directory: $OUT_DIR"
        exit 1
    fi

    echo "Running app"
    java -cp "$OUT_DIR:$CLASSPATH:$RESOURCES_DIR" "$MAIN_CLASS"
fi