# Java Selenium with Docker/Docker-Compose

Java Maven Project to test Selenium Framework with Selenium Grid running in Docker

- Uses Selenium and TestNG
- use [Selenium Page Factory](https://www.selenium.dev/selenium/docs/api/java/org/openqa/selenium/support/PageFactory.html)

## How to run tests with Docker Compose

### Using ```docker-compose.yaml``` to setup grid only

Use below docker-compose yaml file:
```yaml
version: "3"
services:
  hub:
    image: selenium/hub:3.141.59
    ports:
      - "4444:4444"
  chrome:
    image: selenium/node-chrome:3.141.59
    shm_size: '1gb'
    depends_on:
      - hub
    environment:
      - HUB_HOST=hub
  firefox:
    image: selenium/node-firefox:3.141.59
    depends_on:
      - hub
    environment:
      - HUB_HOST=hub
```

This one setup selenium grid first using yaml. but tests will be run separately
1. from project root, launch grid: ```docker-compose up```
   - to increase chrome instances: ```docker-compose up --scale chrome=4 --scale firefox=4```
2. In IntelliJ, right click on ```testng.xml``` file and run to saucedemo test
3. In IntelliJ,right click on ```search_module.xml``` file and run to duck duck go  tests

### Using ```docker-compose.yaml``` to setup grid and run tests on demand

This one setup selenium grid and runs the tests using same yaml file!

1.from project root: ```docker-compose -f docker-compose.yaml up``` 
  - if only wants to see logs related to your tests: ```docker-compose -f docker-compose.yaml up | grep -e 'saucedemo-module'```

### Scaling Options
-  to speed up tests add more browser containers: ```docker-compose -f docker-compose.yaml up --scale chrome=4 --scale firefox=4```

- If you want to run same tests with mutliple browsers you can copy and create additional services:
    
    ```yaml
    ...
      #  1st test set -with firefox
      saucedemo-module_firefox:
        image: dockerselenium
        depends_on:
          - chrome
          - firefox
        environment:
          - BROWSER=firefox
          - HUB_HOST=hub
          - MODULE=saucedemo_tests.xml
        # store results in reports folder
        volumes:
          # store reports in /reports/saucedemo
          - ./reports/saucedemo:/usr/share/selenium_docker/test-output
      #  1st test set - chrome
      saucedemo-module_chrome:
        image: dockerselenium
        depends_on:
          - chrome
          - firefox
        environment:
          - BROWSER=chrome
          - HUB_HOST=hub
          - MODULE=saucedemo_tests.xml
        # store results in reports folder
        volumes:
          # store reports in /reports/saucedemo
          - ./reports/saucedemo:/usr/share/selenium_docker/test-output
    ...
    ```
## Building with Maven and running tests from target folder
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
        
## Building Docker Image with Jenkins

1. Create a Jenkins pipeline job using [GitHub project]((https://github.com/anishst/JavaSeleniumDocker)) 
2. Run the job 
    - this will build and create a docker image and push to Docker Hub
    
### Jenkinsfile example
    - Linux - see [Jenkinsfile](https://github.com/anishst/JavaSeleniumDocker/blob/master/Jenkinsfile)
    - Windows:
        ```yaml
            pipeline {
                // master executor should be set to 0
                agent any
                stages {
                    stage('Build Jar') {
                        steps {
                        bat "mvn clean package -DskipTests"
                        }
                    }
                    stage('Build Image') {
                        steps {
                        bat "docker build -t='anishst/selenium-docker' ."
                        }
                    }
                    stage('Push Image') {
                        steps {
                        withCredentials([usernamePassword(credentialsId: 'docker_hub', passwordVariable: 'pass', usernameVariable: 'user')]) {
                        bat "docker login --username=${user} --password=${pass}"
                        bat "docker push anishst/selenium-docker:latest"
                        }                           
                    }
                }
                }
            }
         ```
      
## To Do
- [x] setup to run on grid
- [ ]  make it run locally if hub is not there
- [x] test running via maven command line; currently error
    - ```mvn clean test -DsuiteXmlFile=duck_search_tests.xml``` (NOT WORKING Due to parameters)
        - Error: Parameter 'keyword' is required by @Test on method search but has not been marked @Optional or define
    - https://testng.org/doc/documentation-main.html#parameters
- [ ] setup to run on jenkins pipeline

## examples
- https://github.com/vinsguru/selenium-docker