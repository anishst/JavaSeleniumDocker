package com.saucedemo.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

public class HomePage {

    private WebDriver driver;
    private WebDriverWait wait;

    //    page elements
    @FindBy(xpath="//span[@class='title']")
    private WebElement products_header;

    @FindBy(className="product_sort_container")
    private WebElement sort_dropdown;

    @FindBy(linkText = "Sauce Labs Backpack")
    private  WebElement sauce_labs_backpack_lnk;

    @FindBy(xpath = "//button[text()='Add to cart']")
    private  WebElement add_to_cart_btn;

    @FindBy(className = "inventory_details_price")
    private WebElement price_txt;

    //    constructor
    public HomePage(WebDriver driver) {
        // store provided driver
        this.driver = driver;
        this.wait = new WebDriverWait(driver,30);
        //  initialize page elements
        PageFactory.initElements(driver, this);
    }

    //    methods
    public void verify_home_page_appeared() {
        this.wait.until(ExpectedConditions.visibilityOf(this.products_header));
        System.out.println("Home page loaded with page header: " + this.products_header.getText());
    }

    public void  sort_items(String value){
        this.wait.until(ExpectedConditions.elementToBeClickable(sort_dropdown));
        Select select = new Select(sort_dropdown);
        select.selectByValue(value); // one option: Price (low to high)

    }

    public  String getPrice() {
        String price = this.price_txt.getText();
        return price;
    }

    public  void sauce_labs_backpack_to_cart() {
        this.sauce_labs_backpack_lnk.click();
        this.add_to_cart_btn.click();
        System.out.println("Added item to cart");
    }

}
