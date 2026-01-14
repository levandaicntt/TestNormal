package Test;

import Initialization.Init;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.*;

import java.time.Duration;
import java.util.List;

public class Practice5 {

    private static WebDriver driver;
    private static WebDriverWait wait;
    private static Actions actions;

    private static final String BASE_URL = "https://automationexercise.com/";

    public static void main(String[] args) {
        try {
            // ===== SETUP giống Bài 4 =====
            Init.SetUp("chrome");
            driver = Init.driver;
            wait = new WebDriverWait(driver, Duration.ofSeconds(15));
            actions = new Actions(driver);

            // ===== RUN ALL TEST CASE =====
            tc01_addProductToCart();
            tc02_verifyCartQuantity();
            tc03_removeProductFromCart();
            tc04_viewCategoryProducts();

            System.out.println("\n[PASS] BÀI THỰC HÀNH 5 HOÀN TẤT");
        } catch (Throwable t) {
            t.printStackTrace();
        } finally {
            try { Init.Teardown(); } catch (Exception ignored) {}
        }
    }

    // =====================================================
    // TC01 – Thêm sản phẩm vào giỏ hàng
    // =====================================================
    private static void tc01_addProductToCart() {
        System.out.println("\nTC01 – Add product to cart");

        driver.get(BASE_URL);

        driver.findElement(By.cssSelector("a[href='/products']")).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//h2[contains(text(),'All Products')]")));

        addProduct(0);
        closeModal();

        addProduct(1);
        viewCart();

        int rows = driver.findElements(By.cssSelector("#cart_info_table tbody tr")).size();
        expectTrue(rows >= 2, "Giỏ hàng phải có ít nhất 2 sản phẩm");
    }

    // =====================================================
    // TC02 – Xác minh số lượng sản phẩm trong giỏ hàng
    // =====================================================
    private static void tc02_verifyCartQuantity() {
        System.out.println("\nTC02 – Verify cart quantity");

        goToCart();

        List<WebElement> rows = driver.findElements(By.cssSelector("#cart_info_table tbody tr"));
        expectTrue(!rows.isEmpty(), "Giỏ hàng đang trống");

        for (WebElement row : rows) {
            int qty = Integer.parseInt(
                    row.findElement(By.cssSelector("td.cart_quantity button")).getText());
            expectTrue(qty >= 1, "Số lượng phải >= 1");
        }
    }

    // =====================================================
    // TC03 – Xóa sản phẩm khỏi giỏ hàng
    // =====================================================
    private static void tc03_removeProductFromCart() {
        System.out.println("\nTC03 – Remove product from cart");

        goToCart();

        List<WebElement> rows = driver.findElements(By.cssSelector("#cart_info_table tbody tr"));
        int before = rows.size();

        rows.get(0).findElement(By.cssSelector(".cart_quantity_delete")).click();

        wait.until(d ->
                d.findElements(By.cssSelector("#cart_info_table tbody tr")).size() < before
        );
    }

    // =====================================================
    // TC04 – Xem danh mục sản phẩm
    // =====================================================
    private static void tc04_viewCategoryProducts() {
        System.out.println("\nTC04 – View category products");

        driver.get(BASE_URL);

        WebElement sidebar = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.cssSelector(".left-sidebar")));
        expectTrue(sidebar.getText().contains("Category"), "Không thấy Category");

        driver.findElement(By.cssSelector("#accordian .panel-title a")).click();
        driver.findElement(By.cssSelector(
                "#accordian .panel-collapse.in a")).click();

        wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.cssSelector(".title.text-center")));

        int products = driver.findElements(
                By.cssSelector(".features_items .product-image-wrapper")).size();
        expectTrue(products > 0, "Category phải có sản phẩm");
    }

    // ================= HELPER =================
    private static void addProduct(int index) {
        List<WebElement> products = driver.findElements(
                By.cssSelector(".product-image-wrapper"));
        actions.moveToElement(products.get(index)).perform();
        products.get(index).findElement(By.cssSelector(".add-to-cart")).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("cartModal")));
    }

    private static void closeModal() {
        driver.findElement(By.cssSelector(".close-modal")).click();
    }

    private static void viewCart() {
        driver.findElement(By.xpath("//a[@href='/view_cart']")).click();
    }

    private static void goToCart() {
        driver.findElement(By.cssSelector("a[href='/view_cart']")).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("cart_info_table")));
    }

    private static void expectTrue(boolean condition, String msg) {
        if (!condition) throw new AssertionError(msg);
    }
}
