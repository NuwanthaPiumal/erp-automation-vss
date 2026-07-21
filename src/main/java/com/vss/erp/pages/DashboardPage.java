package com.vss.erp.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class DashboardPage {

    private WebDriver driver;

    private By dashboardLink = By.xpath("//ul[contains(@class,'menu-list')]/li/a[normalize-space()='Dashboard']");
    private By menuList = By.cssSelector("ul.menu-list.d-flex");

    public DashboardPage(WebDriver driver) {
        this.driver = driver;
    }

    public boolean isMenuVisible() {
        WebElement menu = driver.findElement(menuList);
        return menu.isDisplayed();
    }

    public boolean isDashboardLinkVisible() {
        WebElement link = driver.findElement(dashboardLink);
        return link.isDisplayed();
    }

    public String getDashboardLinkText() {
        return driver.findElement(dashboardLink).getText().trim();
    }
}