# Selenium with Docker

Java Maven Project to test Selenium Framework with 

- Uses Selenium and TestNG
- use [Selenium Page Factory](https://www.selenium.dev/selenium/docs/api/java/org/openqa/selenium/support/PageFactory.html)
## How to run tests

1. right click on ```testng.xml``` file and run to saucedemo test
1. right click on ```search_module.xml``` file and run to duck duck go  tests
3. from command  line: ```mvn clean test -DsuiteXmlFile=duck_search_tests.xml``` (NOT WORKING Due to parameters)

## Troubleshooting

Error: Parameter 'keyword' is required by @Test on method search but has not been marked @Optional or defined
https://testng.org/doc/documentation-main.html#parameters

## examples
- https://github.com/vinsguru/selenium-docker