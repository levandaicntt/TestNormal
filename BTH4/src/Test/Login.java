package Test;

import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.*;

import java.time.Duration;
import java.util.Locale;

public class Login {

    private static WebDriver driver;
    private static WebDriverWait wait;

    // Test data
    private static final String BASE_URL = "https://automationexercise.com/";
    private static final String NAME = "Tien Dung";
    private static final String EMAIL = "t" + System.currentTimeMillis() + "@example.com";
    private static final String PASSWORD = "P@ssw0rd123";
    private static final String FIRST_NAME = "Tien";
    private static final String LAST_NAME = "Dung";
    private static final String ADDRESS1 = "123 Test Street";
    private static final String COUNTRY = "Canada";
    private static final String STATE = "ON";
    private static final String CITY = "Toronto";
    private static final String ZIPCODE = "A1A1A1";
    private static final String MOBILE = "0900000000";

    public static void main(String[] args) {
        try {
            setUp();
            precondition_registerNewUser();
            tc02_loginUserWithCorrectEmailAndPassword();
            cleanup_deleteAccount(); // có thể comment nếu không muốn xoá
            System.out.println("[PASS] Flow TC02 (no TestNG) hoàn tất.");
        } catch (Throwable t) {
            System.err.println("[FAIL] " + t.getMessage());
            t.printStackTrace();
        } finally {
            if (driver != null) driver.quit();
        }
    }

    private static void setUp() {
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(2));
        wait = new WebDriverWait(driver, Duration.ofSeconds(15));
    }

    /** Precondition: Đăng ký tài khoản mới */
    private static void precondition_registerNewUser() {
        driver.get(BASE_URL);

        // Verify home page
        expectTrue(driver.getTitle().toLowerCase(Locale.ROOT).contains("automation exercise"),
                "Home page title must contain 'Automation Exercise'");

        // Go to Signup/Login
        driver.findElement(By.linkText("Signup / Login")).click();

        // Verify 'New User Signup!' visible
        wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//*[contains(.,'New User Signup!')]")));

        // Điền name & email (khu Signup)
        driver.findElement(By.cssSelector("input[data-qa='signup-name']")).sendKeys(NAME);
        driver.findElement(By.cssSelector("input[data-qa='signup-email']")).sendKeys(EMAIL);

        // Click Signup
        driver.findElement(By.cssSelector("button[data-qa='signup-button']")).click();

        // Form 'Enter Account Information'
        wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//*[contains(translate(.,'abcdefghijklmnopqrstuvwxyz','ABCDEFGHIJKLMNOPQRSTUVWXYZ'),'ENTER ACCOUNT INFORMATION')]")));

        // Title
        driver.findElement(By.id("id_gender1")).click();
        // Password
        driver.findElement(By.id("password")).sendKeys(PASSWORD);
        // Date of Birth
        new Select(driver.findElement(By.id("days"))).selectByVisibleText("12");
        new Select(driver.findElement(By.id("months"))).selectByVisibleText("December");
        new Select(driver.findElement(By.id("years"))).selectByVisibleText("2000");

        // Address
        driver.findElement(By.id("first_name")).sendKeys(FIRST_NAME);
        driver.findElement(By.id("last_name")).sendKeys(LAST_NAME);
        driver.findElement(By.id("address1")).sendKeys(ADDRESS1);
        new Select(driver.findElement(By.id("country"))).selectByVisibleText(COUNTRY);
        driver.findElement(By.id("state")).sendKeys(STATE);
        driver.findElement(By.id("city")).sendKeys(CITY);
        driver.findElement(By.id("zipcode")).sendKeys(ZIPCODE);
        driver.findElement(By.id("mobile_number")).sendKeys(MOBILE);

        // Create Account
        driver.findElement(By.cssSelector("button[data-qa='create-account']")).click();

        // Verify 'ACCOUNT CREATED!'
        wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//*[contains(translate(.,'abcdefghijklmnopqrstuvwxyz','ABCDEFGHIJKLMNOPQRSTUVWXYZ'),'ACCOUNT CREATED')]")));

        // Continue về home (đã đăng nhập)
        driver.findElement(By.cssSelector("a[data-qa='continue-button']")).click();

        // Đợi xuất hiện nút Logout (đồng nghĩa đã login)
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("a[href='/logout']")));

        expectTrue(pageHasText("logged in as"),
                "'Logged in as' must appear after registration.");
    }

    /** TC02: Login với email/password đúng (sau khi logout) */
    private static void tc02_loginUserWithCorrectEmailAndPassword() {
        // Logout để quay về form login
        driver.findElement(By.linkText("Logout")).click();

        // Verify 'Login to your account'
        wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//*[contains(.,'Login to your account')]")));

        // Nhập đúng email/password đã đăng ký
        driver.findElement(By.cssSelector("input[data-qa='login-email']")).sendKeys(EMAIL);
        driver.findElement(By.cssSelector("input[data-qa='login-password']")).sendKeys(PASSWORD);

        // Click Login
        driver.findElement(By.cssSelector("button[data-qa='login-button']")).click();

        // Verify 'Logged in as NAME'
        wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//*[contains(.,'Logged in as')]")));
        expectTrue(driver.getPageSource().contains("Logged in as " + NAME),
                "Page must show: Logged in as " + NAME);
    }

    /** Tuỳ chọn: xoá tài khoản để sạch dữ liệu */
    private static void cleanup_deleteAccount() {
        try {
            driver.findElement(By.linkText("Delete Account")).click();
            wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.xpath("//*[contains(translate(.,'abcdefghijklmnopqrstuvwxyz','ABCDEFGHIJKLMNOPQRSTUVWXYZ'),'ACCOUNT DELETED')]")));
            driver.findElement(By.cssSelector("a[data-qa='continue-button']")).click();
        } catch (Exception e) {
            System.out.println("[WARN] Không xoá được tài khoản (nút/flow thay đổi). Bỏ qua cleanup.");
        }
    }

    // ===== Helpers =====
    private static void expectTrue(boolean condition, String messageIfFail) {
        if (!condition) throw new AssertionError(messageIfFail);
    }

    private static boolean pageHasText(String needleLowerCase) {
        return driver.getPageSource().toLowerCase(Locale.ROOT).contains(needleLowerCase.toLowerCase(Locale.ROOT));
    }
}
