#!/bin/bash

# A simple script to compile and run the Library Management System

export JAVA_HOME=/Library/Java/JavaVirtualMachines/jdk-25.jdk/Contents/Home

echo "Starting Library Management System..."
# Use absolute path for mvn if it's not in standard PATH when running from IDE/Terminal
MVN_CMD="mvn"
if [ -f "/opt/homebrew/bin/mvn" ]; then
    MVN_CMD="/opt/homebrew/bin/mvn"
fi

$MVN_CMD clean compile javafx:run
