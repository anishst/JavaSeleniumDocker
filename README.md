# Java Selenium with Docker/Docker-Compose

Java Maven Project to test Selenium Framework with Selenium Grid running in Docker

- Uses Selenium and TestNG
- use [Selenium Page Factory](https://www.selenium.dev/selenium/docs/api/java/org/openqa/selenium/support/PageFactory.html)

## How to run tests with docker compose

1. from project root, launch grid: ```docker-compose up```
   - to increase chrome instances: ```docker-compose up --scale chrome=4 --scale firefox=4```
2. In IntelliJ, right click on ```testng.xml``` file and run to saucedemo test
3. In IntelliJ,right click on ```search_module.xml``` file and run to duck duck go  tests

## Running with Maven
1. Generate jar files: ```mvn clean package -DskipTests```
    - this will generate 
        - ```selenium-docker.jar``` (page object)
        - ```selenium-docker-tests.jar``` (test classes)
        - libs folder (JavaSeleniumDocker\target\libs) will have all dependencies
2. Make sure grid is running 
   - Run duck search tests: 
        - linux/mac: ```java -cp selenium-docker.jar:selenium-docker-tests.jar:libs/* org.testng.TestNG ../duck_search_tests.xml```
        - windows: ```java -cp selenium-docker.jar;selenium-docker-tests.jar;libs/* org.testng.TestNG ../duck_search_tests.xml```
   - Run sauce demo tests: 
     - linux/mac: ```java -cp selenium-docker.jar:selenium-docker-tests.jar:libs/* org.testng.TestNG ../saucedemo_tests.xml```
     - windows: ```java -cp selenium-docker.jar;selenium-docker-tests.jar;libs/* org.testng.TestNG ../saucedemo_tests.xml```

java -cp selenium-docker.jar:selenium-docker-tests.jar:libs/* org.testng.TestNG ../search-module.xml
## To Do
- [x] setup to run on grid
- [ ]  make it run locally if hub is not there
- [ ] test running via maven command line; currently error
    - ```mvn clean test -DsuiteXmlFile=duck_search_tests.xml``` (NOT WORKING Due to parameters)
        - Error: Parameter 'keyword' is required by @Test on method search but has not been marked @Optional or define
    - https://testng.org/doc/documentation-main.html#parameters
- [ ] setup to run on jenkins pipeline

## examples
- https://github.com/vinsguru/selenium-docker