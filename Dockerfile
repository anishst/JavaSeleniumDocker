FROM openjdk:8u191-jre-alpine

# Workspace
WORKDIR /usr/share/selenium_docker

# ADD jar files and any other dependencies from HOST
ADD target/selenium-docker.jar selenium-docker.jar
ADD target/selenium-docker-tests.jar selenium-docker-tests.jar
ADD target/libs libs

# add TestNG suite files
ADD duck_search_tests.xml duck_search_tests.xml
ADD saucedemo_tests.xml saucedemo_tests.xml

# run tests using provided browser/hub address/test suite module
ENTRYPOINT java -cp selenium-docker.jar:selenium-docker-tests.jar:libs/* -DBROWSER=$BROWSER -DHUB_HOST=$HUB_HOST org.testng.TestNG $MODULE