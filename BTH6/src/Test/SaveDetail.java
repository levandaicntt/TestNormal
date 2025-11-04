package Test;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import Initialization.Init;

import java.time.Duration;

public class SaveDetail extends Init {
    public static void main(String[] args) {
        // Cấu hình đường dẫn đến EdgeDriver
        SetUp("edge");

        try {
        	JavascriptExecutor js = (JavascriptExecutor) driver;
            driver.get("https://opensource-demo.orangehrmlive.com");
            driver.manage().window().maximize();

            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

            // Đăng nhập
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("username"))).sendKeys("Admin");
            driver.findElement(By.name("password")).sendKeys("admin123");
            driver.findElement(By.cssSelector("button[type='submit']")).click();

            // Truy cập My Info
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//span[text()='My Info']"))).click();

            // Chờ trang Personal Details hiển thị
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//h6[text()='Personal Details']")));

            // Nhập thông tin cá nhân
            WebElement firstName = wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("firstName")));
            WebElement middleName = driver.findElement(By.name("middleName"));
            WebElement lastName = driver.findElement(By.name("lastName"));


      // Xóa và nhập First Name
      firstName.click();
      firstName.sendKeys(Keys.chord(Keys.CONTROL, "a"));
      firstName.sendKeys(Keys.BACK_SPACE);
      firstName.sendKeys("Lê");

      // Xóa và nhập Middle Name
      middleName.click();
      middleName.sendKeys(Keys.chord(Keys.CONTROL, "a"));
      middleName.sendKeys(Keys.BACK_SPACE);
      middleName.sendKeys("Văn");

      // Xóa và nhập Last Name
      lastName.click();
      lastName.sendKeys(Keys.chord(Keys.CONTROL, "a"));
      lastName.sendKeys(Keys.BACK_SPACE);
      lastName.sendKeys("Hai");




            // Chọn giới tính: Male (value='1')
            js.executeScript("document.querySelector(\"input[type='radio'][value='1']\").click();");



            // Chọn Marital Status: Single
            WebElement maritalStatusDropdown = driver.findElement(By.xpath("//label[text()='Marital Status']/following::div[@class='oxd-select-wrapper'][1]"));
            maritalStatusDropdown.click();
            WebElement maritalSingle = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@role='option']/span[text()='Single']")));
            maritalSingle.click();

            // Chọn Nationality: Vietnamese
            WebElement nationalityDropdown = driver.findElement(By.xpath("//label[text()='Nationality']/following::div[@class='oxd-select-wrapper'][1]"));
            nationalityDropdown.click();
            WebElement nationalityVietnamese = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@role='option']/span[text()='Vietnamese']")));
            nationalityVietnamese.click();

            // Nhấn nút Save
            WebElement saveButton = driver.findElement(By.xpath("//button[@type='submit']"));
            saveButton.click();

            // Chờ xác nhận lưu (có thể kiểm tra thông báo hoặc reload lại dữ liệu)
            Thread.sleep(2000); // hoặc dùng WebDriverWait nếu có thông báo

            System.out.println("✅ Đã lưu thông tin cá nhân thành công.");

        } catch (Exception e) {
            System.out.println("❌ Lỗi khi lưu thông tin: " + e.getMessage());
        } finally {
            driver.quit();
        }
    }
}
