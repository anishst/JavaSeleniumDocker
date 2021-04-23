package com.saucedemo.tests;

import com.saucedemo.pages.HomePage;
import com.saucedemo.pages.LoginPage;
import com.tests.BaseTest;
import org.testng.Assert;
import org.testng.annotations.*;

public class LoginTests extends BaseTest {

//    for testng suite parameter
    private String expectedPrice;

//    assumes chrome web driver is setup and is in your path variable
    @BeforeTest
    @Parameters("expectedPrice")
    public  void setupParameter(String expectedPrice) {
        //  get the saucedemo_tests.xml parameter passed in
        this.expectedPrice = expectedPrice;
    }

    @Test
    public void LoginTest() {
        LoginPage loginPage = new LoginPage(driver);
        loginPage.login();
    }

    @Test
    public void HomePageTest() {
        LoginPage loginPage = new LoginPage(driver);
        loginPage.login();
        HomePage homePage = new HomePage(driver);
        homePage.verify_home_page_appeared();
        homePage.sauce_labs_backpack_to_cart();
        System.out.println("Price of item is: " + homePage.getPrice());
//        assert price match expectedPrice
        Assert.assertEquals(homePage.getPrice(), expectedPrice);
    }
}
