package Test;

import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.*;

import java.time.Duration;

public class Login {

    private static WebDriver driver;
    private static WebDriverWait wait;

    // Test data
    private static final String BASE_URL   = "https://automationexercise.com/";
    private static final String NAME       = "Tien Dung";
    private static final String EMAIL      = "t" + System.currentTimeMillis() + "@example.com";
    private static final String PASSWORD   = "P@ssw0rd123";
    private static final String FIRST_NAME = "Tien";
    private static final String LAST_NAME  = "Dung";
    private static final String ADDRESS1   = "123 Test Street";
    private static final String COUNTRY    = "Canada";
    private static final String STATE      = "ON";
    private static final String CITY       = "Toronto";
    private static final String ZIPCODE    = "A1A1A1";
    private static final String MOBILE     = "0900000000";

    public static void main(String[] args) {
        try {
            setUp();
            precondition_registerNewUser();              // Precondition: Register & auto login
            tc02_loginUserWithCorrectEmailAndPassword(); // 2) Login đúng sau khi logout
            tc03_searchProduct();                        // 3) Chức năng tìm kiếm sản phẩm
            // cleanup_deleteAccount();                  // Bật nếu muốn xoá account sau test
            System.out.println("[PASS] Flow TC02 + TC03 (no TestNG) hoàn tất.");
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

    /** Precondition: Đăng ký tài khoản mới với EMAIL/PASSWORD ở trên */
    private static void precondition_registerNewUser() {
        // Step 1–2: Mở trình duyệt và truy cập trang chủ
        driver.get(BASE_URL);
        System.out.println("Step 1-2: Mở trình duyệt và truy cập " + BASE_URL);

        // Step 3: Xác minh trang chủ hiển thị
        WebElement homeLogo = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//img[@alt='Website for automation practice']")));
        System.out.println("Step 3: Home page loaded, logo alt = " + homeLogo.getAttribute("alt"));

        // Step 4: Nhấn nút 'Signup / Login'
        WebElement signupLoginBtn = driver.findElement(By.xpath("//a[contains(text(),'Signup / Login')]"));
        signupLoginBtn.click();
        System.out.println("Step 4: Click 'Signup / Login'");

        // Step 5: Xác minh 'New User Signup!' hiển thị
        WebElement newUserHeader = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//h2[text()='New User Signup!']")));
        System.out.println("Step 5: Header hiển thị = '" + newUserHeader.getText() + "'");

        // Step 6–7: Nhập tên và email, nhấn 'Signup'
        driver.findElement(By.name("name")).sendKeys(NAME);
        driver.findElement(By.xpath("//input[@data-qa='signup-email']")).sendKeys(EMAIL);
        driver.findElement(By.xpath("//button[text()='Signup']")).click();
        System.out.println("Step 6-7: Nhập NAME = '" + NAME + "', EMAIL = '" + EMAIL + "' và click 'Signup'");

        // Step 8: Xác minh 'ENTER ACCOUNT INFORMATION' hiển thị
        WebElement enterInfoHeader = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//b[text()='Enter Account Information']")));
        System.out.println("Step 8: Header hiển thị = '" + enterInfoHeader.getText() + "'");

        // Step 9: Điền thông tin tài khoản (Title, Password, DOB)
        driver.findElement(By.id("id_gender1")).click();
        driver.findElement(By.id("password")).sendKeys(PASSWORD);
        new Select(driver.findElement(By.id("days"))).selectByValue("19");
        new Select(driver.findElement(By.id("months"))).selectByValue("5");
        new Select(driver.findElement(By.id("years"))).selectByValue("1990");
        System.out.println("Step 9: Điền Title, Password, DOB");

        // Step 10–11: Tích chọn checkbox 'Sign up for our newsletter!' và 'Receive special offers...'
        driver.findElement(By.id("newsletter")).click();
        driver.findElement(By.id("optin")).click();
        System.out.println("Step 10-11: Tick newsletter + optin");

        // Step 12: Điền thông tin địa chỉ
        driver.findElement(By.id("first_name")).sendKeys(FIRST_NAME);
        driver.findElement(By.id("last_name")).sendKeys(LAST_NAME);
        driver.findElement(By.id("company")).sendKeys("ABC Corp");
        driver.findElement(By.id("address1")).sendKeys(ADDRESS1);
        driver.findElement(By.id("address2")).sendKeys("Address line 2");
        new Select(driver.findElement(By.id("country"))).selectByVisibleText(COUNTRY);
        driver.findElement(By.id("state")).sendKeys(STATE);
        driver.findElement(By.id("city")).sendKeys(CITY);
        driver.findElement(By.id("zipcode")).sendKeys(ZIPCODE);
        driver.findElement(By.id("mobile_number")).sendKeys(MOBILE);
        System.out.println("Step 12: Điền địa chỉ: "
                + FIRST_NAME + " " + LAST_NAME + ", " + ADDRESS1 + ", " + CITY + ", " + COUNTRY);

        // Step 13: Nhấn nút 'Create Account'
        driver.findElement(By.xpath("//button[text()='Create Account']")).click();
        System.out.println("Step 13: Click 'Create Account'");

        // Step 14: Xác minh 'ACCOUNT CREATED!' hiển thị
        WebElement accountCreatedHeader = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//h2[@data-qa='account-created']")));
        System.out.println("Step 14: ACCOUNT CREATED! text = '" + accountCreatedHeader.getText() + "'");

        // Step 15: Nhấn nút 'Continue'
        driver.findElement(By.xpath("//a[@data-qa='continue-button']")).click();
        System.out.println("Step 15: Click 'Continue'");

        // Step 16: Xác minh 'Logged in as NAME' hiển thị
        WebElement loggedInAs = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//a[contains(text(),'Logged in as')]")));
        String loggedInText = loggedInAs.getText();
        System.out.println("Step 16: Logged in label hiển thị = '" + loggedInText + "'");
        expectTrue(loggedInText.contains(NAME),
                "Page must show: Logged in as " + NAME);
        System.out.println("✅ Precondition: Đăng ký & auto login thành công cho user: " + NAME);
    }

    /** 2) TC02: Login với email/password đúng (sau khi logout) */
    private static void tc02_loginUserWithCorrectEmailAndPassword() {
        System.out.println("========== BẮT ĐẦU TC02: Login User with correct email & password ==========");

        // Step 1: Click 'Logout' để thoát khỏi account (về trang login)
        WebElement logoutLink = driver.findElement(By.linkText("Logout"));
        logoutLink.click();
        System.out.println("Step 1: Click 'Logout'");

        // Step 2: Xác minh 'Login to your account' hiển thị trên form login
        WebElement loginHeader = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//*[contains(text(),'Login to your account')]")));
        System.out.println("Step 2: Header form login hiển thị = '" + loginHeader.getText() + "'");

        // Step 3: Nhập đúng email và password đã đăng ký ở precondition
        WebElement loginEmailInput = driver.findElement(By.cssSelector("input[data-qa='login-email']"));
        WebElement loginPasswordInput = driver.findElement(By.cssSelector("input[data-qa='login-password']"));
        loginEmailInput.sendKeys(EMAIL);
        loginPasswordInput.sendKeys(PASSWORD);
        System.out.println("Step 3: Nhập EMAIL = '" + EMAIL + "', PASSWORD = '" + PASSWORD + "'");

        // Step 4: Click nút 'Login'
        WebElement loginButton = driver.findElement(By.cssSelector("button[data-qa='login-button']"));
        loginButton.click();
        System.out.println("Step 4: Click 'Login'");

        // Step 5: Xác minh 'Logged in as NAME' hiển thị lại sau khi login
        WebElement loggedInAs = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//a[contains(text(),'Logged in as')]")));
        String loggedInText = loggedInAs.getText();
        System.out.println("Step 5: Sau login, label hiển thị = '" + loggedInText + "'");
        expectTrue(loggedInText.contains(NAME),
                "Page must show: Logged in as " + NAME);

        // Step 6: In ra kết quả TC02 (xuất hiển thị kết luận TC)
        System.out.println("Step 6: ✅ TC02 PASSED - Login với email/password đúng sau khi logout thành công.");
    }

    /** 3) TC03: Chức năng tìm kiếm sản phẩm */
    private static void tc03_searchProduct() {
        System.out.println("========== BẮT ĐẦU TC03: Search Product ==========");

        // Step 1: Từ trang bất kỳ, nhấn menu 'Products'
        WebElement productsLink = driver.findElement(By.xpath("//a[@href='/products']"));
        productsLink.click();
        System.out.println("Step 1: Click menu 'Products'");

        // Step 2: Xác minh user đang ở trang ALL PRODUCTS
        WebElement allProductsHeader = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//h2[@class='title text-center' and contains(.,'All Products')]")));
        System.out.println("Step 2: Header trang products = '" + allProductsHeader.getText() + "'");

        // Step 3: Nhập tên sản phẩm cần tìm vào ô 'Search Product'
        String keyword = "dress"; // có thể đổi tuỳ TC
        WebElement searchInput = driver.findElement(By.id("search_product"));
        searchInput.sendKeys(keyword);
        System.out.println("Step 3: Nhập từ khoá tìm kiếm = '" + keyword + "'");

        // Step 4: Click nút 'Submit' để tìm kiếm
        WebElement searchButton = driver.findElement(By.id("submit_search"));
        searchButton.click();
        System.out.println("Step 4: Click nút 'Search'");

        // Step 5: Xác minh tiêu đề 'SEARCHED PRODUCTS' hiển thị
        WebElement searchedProductsHeader = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//h2[@class='title text-center' and contains(.,'Searched Products')]")));
        System.out.println("Step 5: Header sau tìm kiếm = '" + searchedProductsHeader.getText() + "'");

        // Step 6: Xác minh có ít nhất 1 sản phẩm trong khu vực kết quả
        java.util.List<WebElement> resultItems = driver.findElements(
                By.cssSelector(".features_items .product-image-wrapper"));
        System.out.println("Step 6: Số lượng sản phẩm tìm được = " + resultItems.size());
        expectTrue(!resultItems.isEmpty(), "Kết quả tìm kiếm phải có ít nhất 1 sản phẩm.");

        // Step 7: In ra kết luận TC03
        System.out.println("Step 7: ✅ TC03 PASSED - Tìm kiếm sản phẩm với từ khoá '" + keyword + "' thành công.");
    }

    // ===== Helpers =====
    private static void expectTrue(boolean condition, String messageIfFail) {
        if (!condition) throw new AssertionError(messageIfFail);
    }
}
