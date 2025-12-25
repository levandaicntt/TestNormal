package Test;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;
import java.time.Duration;

public class Register {
    public static void main(String[] args) throws InterruptedException {
        WebDriver driver = new EdgeDriver();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        driver.manage().window().maximize();

        // Step 1–2: Mở trang chủ
        driver.get("https://automationexercise.com");

        // Step 3: Xác minh trang chủ hiển thị
        WebElement homeLogo = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//img[@alt='Website for automation practice']")));
        System.out.println("✅ Home page loaded");

        // Step 4: Nhấn nút Signup/Login
        driver.findElement(By.xpath("//a[contains(text(),'Signup / Login')]")).click();

        // Step 5: Xác minh 'New User Signup!' hiển thị
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//h2[text()='New User Signup!']")));

        // Step 6–7: Nhập tên và email, nhấn Signup
        String email = "test" + System.currentTimeMillis() + "@example.com";
        driver.findElement(By.name("name")).sendKeys("Nguyen Van A");
        driver.findElement(By.xpath("//input[@data-qa='signup-email']")).sendKeys(email);
        driver.findElement(By.xpath("//button[text()='Signup']")).click();

        // Step 8: Xác minh 'ENTER ACCOUNT INFORMATION' hiển thị
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//b[text()='Enter Account Information']")));

        // Step 9: Điền thông tin tài khoản
        driver.findElement(By.id("id_gender1")).click();
        driver.findElement(By.id("password")).sendKeys("Abc@123456");
        new Select(driver.findElement(By.id("days"))).selectByValue("19");
        new Select(driver.findElement(By.id("months"))).selectByValue("5");
        new Select(driver.findElement(By.id("years"))).selectByValue("1990");

        // Step 10–11: Tích chọn checkbox
        driver.findElement(By.id("newsletter")).click();
        driver.findElement(By.id("optin")).click();

        // Step 12: Điền thông tin địa chỉ
        driver.findElement(By.id("first_name")).sendKeys("Nguyen");
        driver.findElement(By.id("last_name")).sendKeys("Van A");
        driver.findElement(By.id("company")).sendKeys("ABC Corp");
        driver.findElement(By.id("address1")).sendKeys("123 Đường Lê Lợi");
        driver.findElement(By.id("address2")).sendKeys("Tòa nhà XYZ");
        new Select(driver.findElement(By.id("country"))).selectByVisibleText("India");
        driver.findElement(By.id("state")).sendKeys("Karnataka");
        driver.findElement(By.id("city")).sendKeys("Bangalore");
        driver.findElement(By.id("zipcode")).sendKeys("560001");
        driver.findElement(By.id("mobile_number")).sendKeys("+84901234567");

        // Step 13: Nhấn nút Create Account
        driver.findElement(By.xpath("//button[text()='Create Account']")).click();

        // Step 14: Xác minh 'ACCOUNT CREATED!' hiển thị
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//h2[@data-qa='account-created']")));
        System.out.println("✅ Account created successfully");

        // Step 15: Nhấn nút Continue
        driver.findElement(By.xpath("//a[@data-qa='continue-button']")).click();

        // Step 16: Xác minh 'Logged in as username'
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//a[contains(text(),'Logged in as')]")));
        System.out.println("✅ Logged in as user");

//        // Step 17: Nhấn nút Delete Account
//        driver.findElement(By.xpath("//a[contains(text(),'Delete Account')]")).click();
//
//        // Step 18: Xác minh 'ACCOUNT DELETED!' hiển thị
//        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//h2[@data-qa='account-deleted']")));
//        System.out.println("✅ Account deleted successfully");
//
//        // Đóng trình duyệt
//        driver.quit();
    }
}
