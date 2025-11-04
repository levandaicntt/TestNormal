package Test;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import Initialization.Init;

import java.time.Duration;

public class Login extends Init{
    public static void main(String[] args) {

        // Khởi tạo trình duyệt Edge
        SetUp("edge");

        try {
            // Mở trang OrangeHRM
            driver.get("https://opensource-demo.orangehrmlive.com");

            // Tối đa hóa cửa sổ trình duyệt
            driver.manage().window().maximize();

            // Khởi tạo WebDriverWait để chờ phần tử xuất hiện
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

            // Chờ và nhập tên đăng nhập
            WebElement usernameField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("input[name='username']")));
            usernameField.sendKeys("Admin");

            // Chờ và nhập mật khẩu
            WebElement passwordField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("input[name='password']")));
            passwordField.sendKeys("admin123");

            // Chờ và nhấn nút Login
            WebElement loginButton = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("button[type='submit']")));
            loginButton.click();

            // Chờ trang Dashboard hiển thị
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//h6[text()='Dashboard']")));

            // Kiểm tra đăng nhập thành công
            System.out.println("✅ Đăng nhập thành công. Đã vào trang Dashboard.");

        } catch (Exception e) {
            System.out.println("❌ Đăng nhập thất bại: " + e.getMessage());
        } finally {
            // Đóng trình duyệt
            driver.quit();
        }
    }
}
