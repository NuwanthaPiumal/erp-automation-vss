package com.vss.erp.tests;

import com.vss.erp.pages.DashboardPage;
import com.vss.erp.pages.LoginPage;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;
import org.testng.annotations.*;

import java.time.Duration;

public class LoginTest {

    private WebDriver driver;
    private WebDriverWait wait;
    private final String baseUrl = "https://0001.anterp.net/";

    @BeforeMethod
    public void setUp() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        wait = new WebDriverWait(driver, Duration.ofSeconds(20));
        driver.get(baseUrl);
    }

    @AfterMethod
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Test(description = "Valid username and password should navigate to dashboard")
    public void validLogin_shouldOpenDashboard() {
        LoginPage loginPage = new LoginPage(driver);
        loginPage.loginAs("VSS", "VSS800"); // replace with secure retrieval later

        // Wait until the dashboard menu shows
        wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.cssSelector("ul.menu-list.d-flex")
        ));

        DashboardPage dashboardPage = new DashboardPage(driver);

        Assert.assertTrue(dashboardPage.isMenuVisible(), "Dashboard menu bar should be visible");
        Assert.assertEquals(dashboardPage.getDashboardLinkText(), "Dashboard", "Dashboard link text mismatch");
    }

    @Test(description = "Valid username and invalid password should show error message")
    public void invalidPassword_shouldShowError() {
        LoginPage loginPage = new LoginPage(driver);

        loginPage.loginAs("VSS", "WrongPassword123");

        // Wait explicitly for the error element to be visible
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("error")));

        Assert.assertTrue(loginPage.isErrorVisible(), "Error message should be visible for invalid password");

        String err = loginPage.getErrorText();
        Assert.assertEquals(err, "Username or Password does not match", "Error text mismatch");
    }

    @Test(description = "Invalid username should show error message")
    public void invalidUsername_shouldShowError() {
        LoginPage loginPage = new LoginPage(driver);

        loginPage.loginAs("NonexistentUser", "SomePassword");

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("error")));

        Assert.assertTrue(loginPage.isErrorVisible(), "Error message should be visible for invalid username");
        Assert.assertEquals(loginPage.getErrorText(), "Username or Password does not match", "Error text mismatch");
    }

    @DataProvider(name = "empty-field-data")
    public Object[][] emptyFieldData() {
        return new Object[][]{
                {"", "", "Username or Email Field Is Required"},
                {"VSS", "", "Password Field Is Required"},
                {"", "VSS800", "Username or Email Field Is Required"}
        };
    }

    @Test(dataProvider = "empty-field-data", description = "Empty-field login attempts should show correct error")
    public void emptyFields_shouldShowError(String username, String password, String expectedMessage) {
        LoginPage loginPage = new LoginPage(driver);
        loginPage.loginAs(username, password);

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("error")));

        Assert.assertTrue(loginPage.isErrorVisible(), "Error message should be visible");
        Assert.assertEquals(loginPage.getErrorText(), expectedMessage, "Error text mismatch");
    }
}