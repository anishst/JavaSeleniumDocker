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
    - this will generate below files under  *JavaSeleniumDocker\target* folder
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

    - run using firefox by passing BROWSER env variable: ```java -cp selenium-docker.jar:selenium-docker-tests.jar:libs/* -DBROWSER=firefox org.testng.TestNG ../saucedemo_tests.xml```    

## Creating Docker Image and Running Selenium Tests

- Image Info
    - [OpenJDK](https://hub.docker.com/_/openjdk)
        - [alpine jdk 8 ](https://hub.docker.com/layers/openjdk/library/openjdk/8u191-jre-alpine/images/sha256-c0d7a59e2af6f469ab596dfebc41336b1eb4472821a86bf1b73560022c508800?context=explore)
- create Dockerfile
  ```yaml
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
  ```
- build image: ``` docker build -t=dockerselenium .```
- run test by launching image built: ```docker run -e HUB_HOST=192.168.1.25 -e MODULE=saucedemo_tests.xml dockerselenium```
- to save results to local folder:  ```docker run -e HUB_HOST=192.168.1.25 -e MODULE=saucedemo_tests.xml -v /home/ats/Documents/github/JavaSeleniumDocker/reports:/usr/share/selenium_docker/test-output dockerselenium``` 
- explore image:
    - get into command prompt inside container: ```docker run -it --entrypoint=/bin/sh  dockerselenium```
    - check to see if yuor files are there : ```ls -al```
    - run tests from command line: ```java -cp selenium-docker.jar:selenium-docker-tests.jar:libs/* org.testng.
      TestNG saucedemo_tests.xml```
        - this will create test-output folder inside the container
        - to see report output make sure to volume map current folder/reoprts as this: ```docker run -it --entrypoint=/bin/sh  -v /home/ats/Documents/github/JavaSeleniumDocker/reports:/usr/share/selenium_docker/test-output d
          ockerselenium```
      - To change hub ip during run, use DHUB_HOST variable: ```java -cp selenium-docker.jar:selenium-docker-tests.jar:libs/* -DHUB_HOST=
        192.168.1.25 org.testng.TestNG saucedemo_tests.xml```
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