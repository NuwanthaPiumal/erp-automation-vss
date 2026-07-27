package com.vss.erp.pages;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

public class CustomerPage {

    private WebDriver driver;
    private WebDriverWait wait;

    private By newCustomerBtn   = By.id("newCustomerBtn");
    private By cusName          = By.id("cusName"); //1
    private By cusNic           = By.id("cusNic");
    private By cusEmail         = By.id("CusEmail");
    private By cusTel           = By.id("CusTel");
    private By cusMobile        = By.id("cusMobile"); //2
    private By cusAddress       = By.id("cusAddress");
    private By cusDob           = By.id("cusDob");
    private By cusCode          = By.id("cusCode");
    private By cusDebLimit      = By.id("cusDebLimit");
    private By dueDays          = By.id("dueDays");
    private By vatApply         = By.id("vatApply");
    private By cusType          = By.id("cusType");
    private By cusArea          = By.id("cusArea");
    private By cusDebtorLedger  = By.id("cusDebtorLedger");
    private By cusAdvanceLedger = By.id("cusAdvanceLedger");
    private By saveAndNewBtn    = By.id("customerSavenNew");
    private By closeModalBtn    = By.cssSelector("button.text-danger[data-bs-dismiss='modal']");
    private By searchInput      = By.cssSelector("input[type='search'][aria-controls='showCustomer']");

    public CustomerPage(WebDriver driver) {
        this.driver = driver;
        this.wait   = new WebDriverWait(driver, Duration.ofSeconds(15));
    }

    private void jsClick(WebElement el) {
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", el);
    }

    private void safeClick(By locator) {
        WebElement el = wait.until(ExpectedConditions.presenceOfElementLocated(locator));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block:'center'});", el);
        try {
            wait.until(ExpectedConditions.elementToBeClickable(locator)).click();
        } catch (Exception e) {
            jsClick(el);
        }
    }

    private void type(By locator, String value) {
        WebElement el = wait.until(ExpectedConditions.elementToBeClickable(locator));
        el.click();
        el.clear();
        el.sendKeys(value);
    }

    /**
     * DevExtreme SelectBox:
     * - Open via the DevExtreme JS instance so the popup fully renders.
     * - Items exist in the DOM even when hidden, but getText() returns ""
     *   while opacity:0, so we read innerText via JS instead.
     * - JS-click the matched item directly.
     */
    private void selectCombo(By widgetLocator, String containsText) {
        WebElement widget = wait.until(ExpectedConditions.visibilityOfElementLocated(widgetLocator));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block:'center'});", widget);

        WebElement input = widget.findElement(By.cssSelector(".dx-texteditor-input"));

        ((JavascriptExecutor) driver).executeScript(
                "var inst = $(arguments[0]).dxSelectBox('instance');" +
                        "if (inst) { inst.open(); } else { arguments[0].querySelector('.dx-dropdowneditor-button').click(); }",
                widget
        );

        wait.until(d -> "true".equals(input.getAttribute("aria-expanded")));

        WebElement itemRow = wait.until(d -> {
            for (WebElement row : d.findElements(By.cssSelector(".dx-overlay-content .dx-list-item"))) {
                try {
                    String text = (String) ((JavascriptExecutor) d).executeScript(
                            "return arguments[0].innerText;", row
                    );
                    if (text != null && text.trim().contains(containsText)) {
                        return row;
                    }
                } catch (Exception ignored) {}
            }
            return null;
        });

        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block:'center'});", itemRow);
        wait.until(ExpectedConditions.elementToBeClickable(itemRow)).click();

        wait.until(d -> {
            String value = input.getAttribute("value");
            return value != null && value.contains(containsText);
        });
    }

    public void clickNewCustomer() {
        safeClick(newCustomerBtn);
        wait.until(ExpectedConditions.visibilityOfElementLocated(cusName));
    }

    public void enterCustomerName(String value) { type(cusName, value); } //1
    public void enterNic(String value)          { type(cusNic, value); }
    public void enterEmail(String value)        { type(cusEmail, value); }
    public void enterTel(String value)          { type(cusTel, value); }
    public void enterMobile(String value)       { type(cusMobile, value); } //2
    public void enterAddress(String value)      { type(cusAddress, value); }
    public void enterDob(String value)          { type(cusDob, value); }
    public void enterCode(String value)         { type(cusCode, value); }
    public void enterDebitLimit(String value)   { type(cusDebLimit, value); }
    public void enterDueDays(String value)      { type(dueDays, value); }

    public void clickVatApply() {
        WebElement el = wait.until(ExpectedConditions.elementToBeClickable(vatApply));
        if (!el.isSelected()) el.click();
    }

    public void selectType(String value)          { selectCombo(cusType, value); }
    public void selectArea(String value)          { selectCombo(cusArea, value); }
    public void selectDebtorLedger(String value)  { selectCombo(cusDebtorLedger, value); }
    public void selectAdvanceLedger(String value) { selectCombo(cusAdvanceLedger, value); }

    public void clickSaveAndNew() {
        safeClick(saveAndNewBtn);
    }

    public void closeForm() {
        safeClick(closeModalBtn);
        wait.until(ExpectedConditions.invisibilityOfElementLocated(By.cssSelector(".modal.show")));
    }

    public void searchCustomer(String name) {
        WebElement el = wait.until(ExpectedConditions.elementToBeClickable(searchInput));
        el.clear();
        el.sendKeys(name);
        el.sendKeys(Keys.ENTER);
    }

    public boolean isCustomerVisibleInResults(String name) {
        try {
            By row = By.xpath("//*[@id='showCustomer']//*[normalize-space()='" + name + "']");
            return wait.until(ExpectedConditions.visibilityOfElementLocated(row)).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }
}