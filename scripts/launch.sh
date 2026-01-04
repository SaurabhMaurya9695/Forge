#!/bin/bash
#
# Forge CI/CD Platform Launcher
#

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
# Change to project root (parent of scripts directory)
PROJECT_ROOT="$(cd "$SCRIPT_DIR/.." && pwd)"
cd "$PROJECT_ROOT"

# Find Java 17
if command -v /usr/libexec/java_home &> /dev/null; then
    JAVA_HOME=$(/usr/libexec/java_home -v 17 2>/dev/null)
fi
JAVA_CMD="${JAVA_HOME:+$JAVA_HOME/bin/}java"

# Verify Java 17+
JAVA_VER=$("$JAVA_CMD" -version 2>&1 | head -1 | cut -d'"' -f2 | cut -d'.' -f1)
if [ "$JAVA_VER" -lt 17 ] 2>/dev/null; then
    echo "Error: Java 17+ required (found: $JAVA_VER)"
    exit 1
fi

# Check build artifacts
if [ ! -f "dist/lib/forge-server-1.0.0-SNAPSHOT.jar" ]; then
    echo "Error: Build artifacts not found. Run: mvn clean install"
    exit 1
fi

# Debug mode
DEBUG_PORT=5005
DEBUG_OPTS=""

if [ "$1" = "--debug" ] || [ "$DEBUG" = "true" ]; then
    DEBUG_OPTS="-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:${DEBUG_PORT}"
    echo "🐛 Debug mode enabled on port ${DEBUG_PORT}"
    echo "   Connect IntelliJ Remote JVM Debug to localhost:${DEBUG_PORT}"
    echo ""
    shift
fi

# Launch the application
exec "$JAVA_CMD" ${DEBUG_OPTS} ${JAVA_OPTS:--Xms256m -Xmx512m} -jar dist/lib/forge-server-1.0.0-SNAPSHOT.jar "$@"

