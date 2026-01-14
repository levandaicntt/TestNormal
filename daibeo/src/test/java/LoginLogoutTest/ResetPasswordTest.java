package LoginLogoutTest;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.*;

import Initialization.Init;

public class ResetPasswordTest extends Init {

    @BeforeMethod
    @Parameters({ "browser", "url" })
    public void beforeMethod(String browser, String url) {

        SetUp(browser);
        driver.get(url);
        driver.manage().window().maximize();

        Reporter.log("====================================", true);
        Reporter.log("START TEST CASE: Reset Password Test", true);
    }

    @Test
    public void resetPasswordAndLoginSuccess() {

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        Reporter.log("STEP 1: Click 'Forgot your password?'", true);
        driver.findElement(By.xpath(
                "//p[@class='oxd-text oxd-text--p orangehrm-login-forgot-header']"))
                .click();

        Reporter.log("STEP 2: Input username = Admin", true);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("username")))
                .sendKeys("Admin");

        Reporter.log("STEP 3: Click Reset Password button", true);
        driver.findElement(By.xpath("//button[@type='submit']")).click();

        Reporter.log("STEP 4: Verify reset password success message", true);
        WebElement successMsg = wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        By.xpath("//h6[text()='Reset Password link sent successfully']")));

        Assert.assertTrue(successMsg.isDisplayed(),
                "❌ Reset password success message not displayed");

        Reporter.log("✅ PASS – Reset password success message displayed", true);

        // ===== STEP 5: LOGIN AGAIN =====
        Reporter.log("STEP 5: Back to Login page", true);
        driver.get("https://opensource-demo.orangehrmlive.com");

        Reporter.log("STEP 6: Login with valid credentials", true);
        driver.findElement(By.name("username")).sendKeys("Admin");
        driver.findElement(By.name("password")).sendKeys("admin123");
        driver.findElement(By.cssSelector("button[type='submit']")).click();

        WebElement dashboard = wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        By.xpath("//h6[text()='Dashboard']")));

        Assert.assertTrue(dashboard.isDisplayed(),
                "❌ Login failed after reset password");

        Reporter.log("✅ PASS – Login successful after reset password", true);
    }

    @AfterMethod
    public void afterMethod() {

        Reporter.log("END TEST CASE", true);
        Reporter.log("====================================", true);
        driver.quit();
    }
}
