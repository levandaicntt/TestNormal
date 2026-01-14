package PIM;

import java.time.Duration;
import java.nio.file.Paths;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.*;

import Initialization.Init;

public class PIMTest extends Init {

    WebDriverWait wait;

    @BeforeMethod
    @Parameters({ "browser", "url" })
    public void beforeMethod(String browser, String url) {

        SetUp(browser);
        driver.get(url);
        driver.manage().window().maximize();

        wait = new WebDriverWait(driver, Duration.ofSeconds(15));

        Reporter.log("====================================", true);
        Reporter.log("START TEST CASE: PIM Add Employee", true);
    }

    @Test
    public void addEmployeeAndAttachmentSuccess() {

        /* =======================
         * STEP 1: LOGIN
         * ======================= */
        Reporter.log("STEP 1: Login", true);

        driver.findElement(By.name("username")).sendKeys("Admin");
        driver.findElement(By.name("password")).sendKeys("admin123");
        driver.findElement(By.cssSelector("button[type='submit']")).click();

        wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//h6[text()='Dashboard']")));

        Reporter.log("✅ Login success", true);

        /* =======================
         * STEP 2: OPEN PIM
         * ======================= */
        Reporter.log("STEP 2: Open PIM module", true);

        driver.findElement(By.xpath(
                "//a[contains(@href,'viewPimModule')]"))
                .click();

        /* =======================
         * STEP 3: CLICK ADD
         * ======================= */
        Reporter.log("STEP 3: Click Add Employee", true);
        wait.until(ExpectedConditions.urlContains("viewEmployeeList"));

        WebElement addEmployeeBtn = wait.until(
        	    ExpectedConditions.elementToBeClickable(
        	        By.xpath("//div[@class='orangehrm-header-container']//button[.//text()='Add']")
        	    )
        	);

        	addEmployeeBtn.click();


        /* =======================
         * STEP 4: INPUT EMPLOYEE INFO
         * ======================= */
        Reporter.log("STEP 4: Input employee information", true);

        driver.findElement(By.name("firstName")).sendKeys("a");
        driver.findElement(By.name("middleName")).sendKeys("b");
        driver.findElement(By.name("lastName")).sendKeys("c");

        WebElement empId = driver.findElement(
                By.xpath("//label[text()='Employee Id']/following::input[1]"));
        empId.clear();
        empId.sendKeys("006700");

        /* =======================
         * STEP 5: SAVE EMPLOYEE
         * ======================= */
        Reporter.log("STEP 5: Save employee", true);

        driver.findElement(By.xpath("//button[@type='submit']")).click();

        WebElement toast1 = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//p[contains(text(),'Successfully')]")));

        Assert.assertTrue(toast1.isDisplayed());
        Reporter.log("✅ PASS – Employee saved successfully", true);

        /* =======================
         * STEP 6: SAVE PERSONAL DETAILS
         * ======================= */
        Reporter.log("STEP 6: Save Personal Details", true);

        driver.findElement(By.xpath("(//button[@type='submit'][normalize-space()='Save'])[1]"))
                .click();

        WebElement toast2 = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//p[contains(text(),'Successfully')]")));

        Assert.assertTrue(toast2.isDisplayed());
        Reporter.log("✅ PASS – Personal details saved", true);

        /* =======================
         * STEP 7: SAVE JOB DETAILS
         * ======================= */
        Reporter.log("STEP 7: Save Job Details", true);

        driver.findElement(By.xpath("(//button[@type='submit'][normalize-space()='Save'])[2]"))
                .click();

        WebElement toast3 = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//p[contains(text(),'Successfully')]")));

        Assert.assertTrue(toast3.isDisplayed());
        Reporter.log("✅ PASS – Job details saved", true);

        /* =======================
         * STEP 8: ADD ATTACHMENT
         * ======================= */
        Reporter.log("STEP 8: Add Attachment", true);

        driver.findElement(By.xpath(
                "//button[contains(@class,'oxd-button--text') and .//text()='Add']"))
                .click();

        /* =======================
         * STEP 9: UPLOAD FILE
         * ======================= */
        Reporter.log("STEP 9: Upload attachment file", true);

        String filePath = Paths.get("src/test/resources/data/testfile.txt")
                .toAbsolutePath().toString();

        WebElement uploadInput = driver.findElement(
                By.xpath("//input[@type='file']"));
        uploadInput.sendKeys(filePath);

        WebElement fileName = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//div[contains(@class,'oxd-file-input-div') and not(text()='No file selected')]")));

        Assert.assertTrue(fileName.isDisplayed());
        Reporter.log("✅ File selected successfully", true);

        /* =======================
         * STEP 10: SAVE ATTACHMENT
         * ======================= */
        Reporter.log("STEP 10: Save attachment", true);

        driver.findElement(By.xpath("(//button[@type='submit'][normalize-space()='Save'])[3]"))
                .click();

        WebElement toast4 = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//p[contains(text(),'Successfully')]")));

        Assert.assertTrue(toast4.isDisplayed());

        WebElement recordFound = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//span[contains(text(),'Record Found')]")));

        Assert.assertTrue(recordFound.isDisplayed());

        Reporter.log("✅ PASS – Attachment added, Record Found displayed", true);
    }

    @AfterMethod
    public void afterMethod() {

        Reporter.log("END TEST CASE", true);
        Reporter.log("====================================", true);
        driver.quit();
    }
}
