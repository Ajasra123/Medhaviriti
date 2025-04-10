@echo off
echo Compiling Java files...
javac -cp "lib/mysql-connector-j-8.0.33.jar" src/views/*.java src/controllers/*.java src/models/*.java src/database/*.java src/Main/*.java

echo Running application...
java -cp "lib/mysql-connector-j-8.0.33.jar;src" main.Main

pause
