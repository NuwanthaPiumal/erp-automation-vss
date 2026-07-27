package com.vss.erp.tests;

import com.vss.erp.pages.CustomerPage;
import com.vss.erp.pages.LoginPage;
import com.vss.erp.utils.ConfigReader;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.*;
import io.github.cdimascio.dotenv.Dotenv;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

public class CustomerTest {

    private WebDriver driver;
    private WebDriverWait wait;

    String baseUrl = ConfigReader.get("baseUrl");

    Dotenv dotenv = Dotenv.load();

    String username = dotenv.get("ERP_USERNAME");
    String password = dotenv.get("ERP_PASSWORD");


    @BeforeMethod
    public void setUp() {
        WebDriverManager.chromedriver().setup();
        ChromeOptions options = new ChromeOptions();
        Map<String, Object> prefs = new HashMap<>();
        prefs.put("credentials_enable_service", false);
        prefs.put("profile.password_manager_enabled", false);
        prefs.put("profile.password_manager_leak_detection", false);
        options.setExperimentalOption("prefs", prefs);
        driver = new ChromeDriver(options);
        driver.manage().window().maximize();
        wait = new WebDriverWait(driver, Duration.ofSeconds(20));
        driver.get(baseUrl);
    }

    @AfterMethod
    public void tearDown() {
        if (driver != null) driver.quit();
    }

    @Test
    public void createAndVerifyCustomer() {

        LoginPage loginPage = new LoginPage(driver);
        loginPage.loginAs(username, password);

        wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.cssSelector("ul.menu-list.d-flex")));

        driver.get(baseUrl + "/peoples/customers");

        CustomerPage customerPage = new CustomerPage(driver);
        String customerName = "A Traders " + System.currentTimeMillis();

        customerPage.clickNewCustomer();
        customerPage.enterCustomerName(customerName);
//        customerPage.enterNic("NIC12384567");
        customerPage.enterEmail("abcdef@test.com");
//        customerPage.enterTel("0112345678");
        customerPage.enterMobile("0771234562");

        // cusType options: "Internal" or "External"
//        customerPage.selectType("Internal");

        customerPage.enterAddress("Colombo");
//        customerPage.enterDob("2000-01-01");
//        customerPage.enterCode("CUST001");
//        customerPage.enterDebitLimit("10000");

        // cusArea options include "COL-04 — Colombo", matched by contains("Colombo")
//        customerPage.selectArea("Colombo");

//        customerPage.clickVatApply();

        // cusDebtorLedger options include "8 - Debtors", matched by contains("Debtors")
        customerPage.selectDebtorLedger("Debtors");

        // cusAdvanceLedger options include "24 - Advanced Debtor", matched by contains("Advanced Debtor")
        customerPage.selectAdvanceLedger("Advanced Debtor");

//        customerPage.enterDueDays("30");
        customerPage.clickSaveAndNew();

        // Wait for the form to reset — name field goes empty when save completes
        wait.until(driver -> {
            WebElement nameField = driver.findElement(By.id("cusName"));
            String val = nameField.getAttribute("value");
            return val == null || val.trim().isEmpty();
        });

        // Close the blank new-customer form
        customerPage.closeForm();

        // Search and verify
        customerPage.searchCustomer(customerName);
        Assert.assertTrue(
                customerPage.isCustomerVisibleInResults(customerName),
                "Customer '" + customerName + "' was not found in search results"
        );
    }
}