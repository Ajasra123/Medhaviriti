@echo off
echo Compiling Java files...
javac -d target -cp "lib/mysql-connector-j-8.0.33.jar" src/utils/*.java src/views/*.java src/controllers/*.java src/models/*.java src/database/*.java src/Main/*.java

echo Running application...
java -cp "target;lib/mysql-connector-j-8.0.33.jar" main.Main

pause
