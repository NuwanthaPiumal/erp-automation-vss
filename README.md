# VSS ERP Login Automation

This project automates the VSS ERP login page using Java, Selenium WebDriver, TestNG, and the Page Object Model. It covers valid login, invalid login, and empty-field validation scenarios.

## Project Structure

```text
erp-automation-vss
├── pom.xml
├── testng.xml
├── src
│   ├── main
│   │   └── java
│   │       └── com.vss.erp.pages
│   │           ├── LoginPage.java
│   │           └── DashboardPage.java
│   └── test
│       └── java
│           └── com.vss.erp.tests
│               └── LoginTest.java
```

## Tech Stack

- Java.
- Selenium WebDriver.
- TestNG.
- Maven.
- IntelliJ IDEA.

## Login Page Elements

The login page uses these locators:
- Username: `id="CredentialUsr"`.
- Password: `id="password-input"`.
- Sign In button: `id="signinBtn"`.
- Error message: `id="error"`.

## Test Scenarios

- Valid login should open the dashboard.
- Invalid password should show an error.
- Invalid username should show an error.
- Empty username should show a required-field message.
- Empty password should show a required-field message.
- Both fields empty should show validation errors.

## How It Works

The test opens the ERP login page, enters credentials, clicks Sign In, and checks either the dashboard menu or the error message. This follows the normal Selenium + TestNG pattern of separating page actions from test assertions.

## How To Run

1. Open the project in IntelliJ IDEA.
2. Make sure Maven dependencies are downloaded.
3. Set environment variables if you use secure credentials.
4. Run `LoginTest.java` as a TestNG test, or run:
```bash
mvn test
```

## Notes

- Keep real passwords out of source code.
- Update locators in `LoginPage.java` if the UI changes.
- Update expected error text if the ERP validation message changes.
