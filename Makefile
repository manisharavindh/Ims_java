# Makefile for Library Management System

# Set the Java 25 home directory and Maven path
JAVA_HOME_PATH ?= /Library/Java/JavaVirtualMachines/jdk-25.jdk/Contents/Home
MVN ?= mvn
# Fallback if global mvn isn't working perfectly: MVN ?= /opt/homebrew/bin/mvn

.PHONY: all run clean compile test db-reset

all: run

# Run the JavaFX application
run:
	@echo "Starting Library Management System..."
	JAVA_HOME=$(JAVA_HOME_PATH) $(MVN) clean compile javafx:run

# Clean the target directory
clean:
	@echo "Cleaning project..."
	JAVA_HOME=$(JAVA_HOME_PATH) $(MVN) clean

# Compile the project
compile:
	@echo "Compiling project..."
	JAVA_HOME=$(JAVA_HOME_PATH) $(MVN) compile

# Run tests
test:
	@echo "Running tests..."
	JAVA_HOME=$(JAVA_HOME_PATH) $(MVN) test

# Reset the database (optional helper, assuming you have MySQL running locally)
db-reset:
	@echo "Resetting database using schema.sql..."
	mysql -u root -p < database/schema.sql
