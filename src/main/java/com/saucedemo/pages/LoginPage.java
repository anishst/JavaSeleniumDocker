package com.saucedemo.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class LoginPage {

    private WebDriver driver;
    private WebDriverWait wait;

    //    page elements
    @FindBy(name="user-name")
    private WebElement username_txt;

    @FindBy(name="password")
    private WebElement password_txt;

    @FindBy(id="login-button")
    private  WebElement login_btn;


    //    constructor
    public LoginPage(WebDriver driver) {
        // store provided driver
        this.driver = driver;
        this.wait = new WebDriverWait(driver,30);
        //  initialize page elements
        PageFactory.initElements(driver, this);
    }

    //    method
    public void goTo() {
        this.driver.get("https://www.saucedemo.com/");
        this.wait.until(ExpectedConditions.visibilityOf(this.username_txt));
    }

    public void login() {
        goTo();
        System.out.println("Logging in to Sauce Demo site...");
        this.username_txt.sendKeys("standard_user");
        this.password_txt.sendKeys("secret_sauce");
        this.login_btn.click();
        System.out.println("Logged In!");

    }


}
