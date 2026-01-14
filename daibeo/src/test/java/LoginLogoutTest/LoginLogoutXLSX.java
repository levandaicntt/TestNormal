package LoginLogoutTest;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.*;
import java.util.List;
import DataHanding.DataReader;
import Initialization.Init;

public class LoginLogoutXLSX extends Init {

	@Parameters({"browser", "url"})
	@BeforeMethod
	public void beforeMethod(String browser, String url) {
		SetUp(browser);
	    driver.get(url);
	    driver.manage().window().maximize();

	    Reporter.log("Browser = " + browser, true);
	    Reporter.log("URL = " + url, true);
	    
	    Reporter.log("====================================", true);
	    Reporter.log("START TEST CASE: Login Test", true);
	}


	@Test(dataProvider = "dp")
	public void loginTest(String username, String password, String expected) {

	    username = username.trim();
	    password = password.trim();
	    expected = expected.trim();

	    WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

	    Reporter.log("Test data:", true);
	    Reporter.log("Username = " + username, true);
	    Reporter.log("Password = " + password, true);
	    Reporter.log("Expected = " + expected, true);

	    driver.findElement(By.name("username")).sendKeys(username);
	    driver.findElement(By.name("password")).sendKeys(password);
	    driver.findElement(By.cssSelector("button[type='submit']")).click();

	    if (expected.equalsIgnoreCase("success")) {

	        // ✅ Login đúng
	        WebElement dashboard = wait.until(
	            ExpectedConditions.visibilityOfElementLocated(
	                By.xpath("//h6[text()='Dashboard']")));

	        Assert.assertTrue(dashboard.isDisplayed(),
	                "Dashboard should be displayed");

	        Reporter.log("RESULT: PASS – Login success", true);
	        System.out.println("✅ PASS – Login success");

	    } else {

	        // ❌ Login sai
	        WebElement errorMsg = wait.until(
	            ExpectedConditions.visibilityOfElementLocated(
	                By.xpath("//p[contains(@class,'oxd-alert-content-text') and text()='Invalid credentials']")));

	        Assert.assertTrue(errorMsg.isDisplayed(),
	                "Invalid credentials message should appear");

	        Reporter.log("RESULT: PASS – Invalid credentials displayed", true);
	        System.out.println("✅ PASS – Invalid credentials shown");
	    }
	}



	@AfterMethod
	public void afterMethod() {
	    Reporter.log("END TEST CASE", true);
	    Reporter.log("====================================", true);
	    driver.quit();
	}

    @DataProvider
    public Object[][] dp() throws Exception {

        String path = getClass()
                .getClassLoader()
                .getResource("data/login.xlsx")
                .getPath();

        return DataReader.getExcelDataUsingPoi(path, "login");
    }
}
