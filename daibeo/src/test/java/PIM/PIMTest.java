package PIM;

import java.time.Duration;
import java.nio.file.Paths;
import java.util.Random;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.*;

import DataHanding.DataReader;
import Initialization.Init;

public class PIMTest extends Init {

    private WebDriverWait wait;

    @BeforeMethod
    @Parameters({ "browser", "url" })
    public void beforeMethod(String browser, String url) {
        SetUp(browser);
        driver.get(url);
        driver.manage().window().maximize();
        wait = new WebDriverWait(driver, Duration.ofSeconds(20));

        Reporter.log("====================================", true);
        Reporter.log("START TEST CASE: PIM Add Employee with DataProvider", true);
    }

    private void waitLoader() {
        try {
            new WebDriverWait(driver, Duration.ofMillis(1000))
                .until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".oxd-loading-spinner, .oxd-form-loader")));
        } catch (Exception ignored) {}
        wait.until(ExpectedConditions.invisibilityOfElementLocated(By.cssSelector(".oxd-loading-spinner, .oxd-form-loader")));
    }

    private void smartClick(By locator) {
        WebElement el = wait.until(ExpectedConditions.presenceOfElementLocated(locator));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block:'center'});", el);
        wait.until(ExpectedConditions.elementToBeClickable(el));
        try {
            el.click();
        } catch (Exception e) {
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", el);
        }
    }

    private String generateRandomId() {
        return String.valueOf(new Random().nextInt(900000) + 100000);
    }

    /**
     * Tích hợp DataProvider để chạy test với nhiều bộ dữ liệu từ file Excel
     */
    @Test(dataProvider = "pimData")
    public void addEmployeeAndAttachmentSuccess(String fName, String mName, String lName, String employeeId) {
        
        Reporter.log("DATA: " + fName + " " + mName + " " + lName + " (ID: " + employeeId + ")", true);

        Reporter.log("STEP 1: Login", true);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("username"))).sendKeys("Admin");
        driver.findElement(By.name("password")).sendKeys("admin123");
        driver.findElement(By.cssSelector("button[type='submit']")).click();

        Reporter.log("STEP 2: Open PIM", true);
        smartClick(By.xpath("//a[contains(@href,'viewPimModule')]"));
        waitLoader();

        Reporter.log("STEP 3: Add Employee", true);
        smartClick(By.xpath("//button[text()=' Add ' or .//i[contains(@class,'bi-plus')]]"));
        waitLoader();

        Reporter.log("STEP 4: Input employee info from Excel", true);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("firstName"))).sendKeys(fName);
        driver.findElement(By.name("middleName")).sendKeys(mName);
        driver.findElement(By.name("lastName")).sendKeys(lName);

        WebElement empIdInput = driver.findElement(By.xpath("//label[text()='Employee Id']/following::input[1]"));
        empIdInput.sendKeys(Keys.CONTROL + "a", Keys.BACK_SPACE);
        empIdInput.sendKeys(employeeId);
        empIdInput.sendKeys(Keys.TAB);

        // Xử lý nếu ID từ Excel đã tồn tại trong hệ thống
        if (!driver.findElements(By.xpath("//span[contains(.,'already exists')]")).isEmpty()) {
            Reporter.log("⚠️ ID " + employeeId + " đã tồn tại, đang tạo ID ngẫu nhiên...", true);
            empIdInput.sendKeys(Keys.CONTROL + "a", Keys.BACK_SPACE);
            empIdInput.sendKeys(generateRandomId());
            empIdInput.sendKeys(Keys.TAB);
        }

        Reporter.log("STEP 5: Save Add Employee FORM", true);
        smartClick(By.xpath("//button[@type='submit' and contains(.,'Save')]"));
        wait.until(ExpectedConditions.urlContains("viewPersonalDetails"));
        waitLoader();

        Reporter.log("STEP 6: Save Personal Details", true);
        smartClick(By.xpath("//h6[text()='Personal Details']/following::button[@type='submit'][1]"));
        waitLoader();

        Reporter.log("STEP 7: Save Custom Fields", true);
        smartClick(By.xpath("//h6[text()='Custom Fields']/following::button[@type='submit'][1]"));
        waitLoader();

        Reporter.log("STEP 8: Add Attachment", true);
        smartClick(By.xpath("//h6[text()='Attachments']/following::button[text()=' Add ']"));

        Reporter.log("STEP 9: Upload file & Add Comment", true);
        String fileName = "login.csv"; // File này phải có sẵn trong folder data
        String filePath = Paths.get("src/test/resources/data/" + fileName).toAbsolutePath().toString();
        
        driver.findElement(By.xpath("//input[@type='file']")).sendKeys(filePath);
        driver.findElement(By.xpath("//textarea[@placeholder='Type comment here']")).sendKeys("Automation Upload for " + fName);

        Reporter.log("STEP 10: Save Attachment", true);
        smartClick(By.xpath("//div[@class='orangehrm-attachment']//button[@type='submit']"));
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@id='oxd-toaster_1']")));
        waitLoader();

        Reporter.log("STEP 11: Verify File exists", true);
        By fileInTable = By.xpath("//div[@class='orangehrm-attachment']//div[text()='" + fileName + "']");
        WebElement foundFile = wait.until(ExpectedConditions.visibilityOfElementLocated(fileInTable));
        
        Assert.assertTrue(foundFile.isDisplayed(), "Lỗi: File không hiển thị sau khi lưu!");
        Reporter.log("✅ PASS – Hoàn tất cho nhân viên: " + fName, true);
    }

    @AfterMethod
    public void afterMethod() {
        if (driver != null) {
            driver.quit();
        }
    }

    /**
     * DataProvider đọc từ file Excel
     * Lưu ý: File Excel cần có 4 cột: FirstName, MiddleName, LastName, EmployeeId
     */
    @DataProvider(name = "pimData")
    public Object[][] dp() throws Exception {
        String path = getClass()
                .getClassLoader()
                .getResource("data/login.xlsx") // Tận dụng file login.xlsx của bạn
                .getPath();

        // Giả sử bạn tạo một sheet mới tên là "PIM" trong file login.xlsx
        return DataReader.getExcelDataUsingPoi(path, "PIM");
    }
}