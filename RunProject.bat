@echo off
setlocal

echo 'Starting Gradle build...'
call .\gradlew clean build
echo 'Gradle build complete.'

echo 'Current Directory: %CD%'

cd build\libs

rem Print the list of files in the current directory for debugging
dir

rem Check if the JAR file exists before running it
if exist ejercicio-tecnico-camilo.jar (
    echo 'Running JAR...'
    java -jar ejercicio-tecnico-camilo.jar
) else (
    echo 'Error: JAR file not found.'
)

rem Navigate back to the original directory
cd ..\..

endlocal