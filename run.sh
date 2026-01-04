#!/bin/bash

# Forge Platform - Run Script
# This script runs the Forge server from the parent directory

# Colors for output
GREEN='\033[0;32m'
RED='\033[0;31m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

echo -e "${GREEN}========================================${NC}"
echo -e "${GREEN}   Forge CI/CD Platform${NC}"
echo -e "${GREEN}   Starting Server...${NC}"
echo -e "${GREEN}========================================${NC}"
echo ""

# Check if Maven is installed
if ! command -v mvn &> /dev/null; then
    echo -e "${RED}Error: Maven is not installed or not in PATH${NC}"
    exit 1
fi

# Check if Java is installed
if ! command -v java &> /dev/null; then
    echo -e "${RED}Error: Java is not installed or not in PATH${NC}"
    exit 1
fi

# Display versions
echo -e "${BLUE}Java Version:${NC}"
java -version
echo ""
echo -e "${BLUE}Maven Version:${NC}"
mvn -version | head -1
echo ""

# Check if project is built
if [ ! -f "work/forge-server-1.0.0-SNAPSHOT.jar" ]; then
    echo -e "${YELLOW}JAR not found in work/ directory. Building project...${NC}"
    mvn clean install -DskipTests
    if [ $? -ne 0 ]; then
        echo -e "${RED}Build failed. Please fix errors and try again.${NC}"
        exit 1
    fi
    echo ""
fi
port='2026';
echo -e "${GREEN}Starting Forge Server...${NC}"
echo -e "${YELLOW}Server will be available at: http://localhost:${port}${NC}"
echo -e "${YELLOW}Health Check: http://localhost:${port}/actuator/health${NC}"
echo -e "${YELLOW}API Health: http://localhost:${port}/api/health${NC}"
echo ""

# Run the server module
mvn spring-boot:run -pl server

