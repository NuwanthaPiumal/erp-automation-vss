package com.vss.erp.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class LoginPage {

    private WebDriver driver;

    private By usernameInput = By.id("CredentialUsr");
    private By passwordInput = By.id("password-input");
    private By signInButton  = By.id("signinBtn");


    private By loginError = By.id("error");

    public LoginPage(WebDriver driver) {
        this.driver = driver;
    }

    public void enterUsername(String username) {
        WebElement userField = driver.findElement(usernameInput);
        userField.click();
        userField.clear();
        userField.sendKeys(username);
    }

    public void enterPassword(String password) {
        WebElement passField = driver.findElement(passwordInput);
        passField.click();
        passField.clear();
        passField.sendKeys(password);
    }

    public void clickSignIn() {
        driver.findElement(signInButton).click();
    }

    public void loginAs(String username, String password) {
        enterUsername(username);
        enterPassword(password);
        clickSignIn();
    }

    public boolean isErrorVisible() {
        try {
            WebElement e = driver.findElement(loginError);
            return e.isDisplayed() && !e.getText().trim().isEmpty();
        } catch (Exception ex) {
            return false;
        }
    }

    public String getErrorText() {
        try {
            WebElement e = driver.findElement(loginError);
            return e.getText().trim();
        } catch (Exception ex) {
            return "";
        }
    }
}