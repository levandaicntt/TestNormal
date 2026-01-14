package Test;

import Initialization.Init;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.*;

import java.time.Duration;
import java.util.List;
import java.util.function.BooleanSupplier;

public class Practice5 {

    private static WebDriver driver;
    private static WebDriverWait wait;
    private static Actions actions;

    private static final String BASE_URL = "https://automationexercise.com/";

    // ===== Locators (đỡ viết lại nhiều) =====
    private static final By HOME_LOGO = By.xpath("//img[@alt='Website for automation practice']");
    private static final By PRODUCTS_MENU = By.cssSelector("a[href='/products']");
    private static final By CART_MENU = By.cssSelector("a[href='/view_cart']");
    private static final By ALL_PRODUCTS_TITLE = By.xpath("//h2[contains(@class,'title') and contains(.,'All Products')]");
    private static final By CART_MODAL = By.id("cartModal");
    private static final By CART_TABLE = By.id("cart_info_table");
    private static final By CART_ROWS = By.cssSelector("#cart_info_table tbody tr");
    private static final By CART_DELETE = By.cssSelector("a.cart_quantity_delete");

    public static void main(String[] args) {
        try {
            Init.SetUp("chrome");
            driver = Init.driver;

            wait = new WebDriverWait(driver, Duration.ofSeconds(15));
            actions = new Actions(driver);

            run("TC12 - Add Products in Cart", Practice5::tc12_addProductsInCart, true);
            run("TC13 - Verify Product quantity in Cart", Practice5::tc13_verifyProductQuantityInCart, true);
            run("TC17 - Remove Products From Cart", Practice5::tc17_removeProductsFromCart, true);
            run("TC18 - View Category Products", Practice5::tc18_viewCategoryProducts, false);

            System.out.println("\n[PASS] DONE: TC12 + TC13 + TC17 + TC18");
        } catch (Throwable t) {
            System.err.println("\n[FAIL] " + t.getMessage());
            t.printStackTrace();
        } finally {
            try { Init.Teardown(); } catch (Throwable ignored) {}
        }
    }

    // =========================================================
    // Runner
    // =========================================================
    private static void run(String name, Runnable tc, boolean cleanupCart) {
        System.out.println("\n========== " + name + " ==========");
        try {
            tc.run();
            System.out.println("✅ " + name + " PASSED");
        } finally {
            if (cleanupCart) clearCartIfAny();
        }
    }

    // =========================================================
    // Test Case 12: Add Products in Cart
    // =========================================================
    private static void tc12_addProductsInCart() {
        openHome();

        click(PRODUCTS_MENU);
        wait.until(ExpectedConditions.visibilityOfElementLocated(ALL_PRODUCTS_TITLE));

        addProductByIndex(0);
        clickContinueShoppingOnModal();

        addProductByIndex(1);
        clickViewCartOnModal();

        wait.until(ExpectedConditions.visibilityOfElementLocated(CART_TABLE));
        List<WebElement> rows = driver.findElements(CART_ROWS);
        System.out.println("Cart rows = " + rows.size());
        expectTrue(rows.size() >= 2, "Cart phải có ít nhất 2 sản phẩm.");

        for (int i = 0; i < 2; i++) {
            WebElement row = rows.get(i);
            String priceTxt = row.findElement(By.cssSelector("td.cart_price p")).getText().trim();
            String qtyTxt   = row.findElement(By.cssSelector("td.cart_quantity button")).getText().trim();
            String totalTxt = row.findElement(By.cssSelector("td.cart_total p")).getText().trim();

            System.out.println("Row " + (i + 1) + ": price=" + priceTxt + ", qty=" + qtyTxt + ", total=" + totalTxt);

            expectTrue(!priceTxt.isEmpty(), "Price trống row " + i);
            expectTrue(!qtyTxt.isEmpty(), "Qty trống row " + i);
            expectTrue(!totalTxt.isEmpty(), "Total trống row " + i);

            int qty = Integer.parseInt(qtyTxt);
            expectTrue(qty >= 1, "Qty phải >= 1 row " + i);
        }
    }

    // =========================================================
    // Test Case 13: Verify Product quantity in Cart
    // =========================================================
    private static void tc13_verifyProductQuantityInCart() {
        openHome();

        WebElement viewProduct = wait.until(ExpectedConditions.presenceOfElementLocated(
                By.cssSelector("a[href^='/product_details/']")));
        scrollIntoView(viewProduct);
        jsClick(viewProduct);

        WebElement info = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".product-information")));
        String productName = info.findElement(By.cssSelector("h2")).getText().trim();
        System.out.println("Product = " + productName);

        WebElement qtyInput = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("quantity")));
        setQuantityHard(qtyInput, "4");
        expectTrue("4".equals(qtyInput.getAttribute("value")), "Không set được quantity=4");

        WebElement addBtn = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("button.cart")));
        jsClick(addBtn);

        wait.until(ExpectedConditions.visibilityOfElementLocated(CART_MODAL));
        clickViewCartOnModal();

        wait.until(ExpectedConditions.visibilityOfElementLocated(CART_TABLE));
        int qtyInCart = getQtyInCartByProductName(productName);
        System.out.println("Qty in cart = " + qtyInCart);

        expectTrue(qtyInCart == 4, "Quantity trong cart phải = 4, actual=" + qtyInCart);
    }

    // =========================================================
    // Test Case 17: Remove Products From Cart
    // =========================================================
    private static void tc17_removeProductsFromCart() {
        openHome();

        click(PRODUCTS_MENU);
        wait.until(ExpectedConditions.visibilityOfElementLocated(ALL_PRODUCTS_TITLE));

        addProductByIndex(0);
        clickViewCartOnModal();

        click(CART_MENU);
        wait.until(ExpectedConditions.visibilityOfElementLocated(CART_TABLE));

        int before = driver.findElements(CART_ROWS).size();
        expectTrue(before > 0, "Cart empty before remove.");

        WebElement del = wait.until(ExpectedConditions.presenceOfElementLocated(CART_DELETE));
        scrollIntoView(del);
        jsClick(del);

        waitUntil(() -> cartIsEmptyText() || driver.findElements(CART_ROWS).size() < before, 20);
        int after = driver.findElements(CART_ROWS).size();
        System.out.println("rows before=" + before + ", after=" + after + ", emptyText=" + cartIsEmptyText());

        expectTrue(after < before || cartIsEmptyText(), "Product chưa bị xóa khỏi cart.");
    }

    // =========================================================
    // Test Case 18: View Category Products
    // =========================================================
    private static void tc18_viewCategoryProducts() {
        openHome();

        WebElement sidebar = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".left-sidebar")));
        expectTrue(sidebar.getText().toLowerCase().contains("category"), "Categories not visible on left sidebar");

        WebElement women = driver.findElement(By.xpath("//div[@id='accordian']//a[contains(.,'Women')]"));
        scrollIntoView(women);
        jsClick(women);

        WebElement dress = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//div[@id='accordian']//div[contains(@class,'panel-collapse') and contains(@class,'in')]//a[contains(.,'Dress')]")));
        jsClick(dress);

        WebElement title = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".title.text-center")));
        expectTrue(title.getText().toUpperCase().contains("WOMEN"), "Expected WOMEN title, actual=" + title.getText());

        WebElement men = driver.findElement(By.xpath("//div[@id='accordian']//a[contains(.,'Men')]"));
        scrollIntoView(men);
        jsClick(men);

        WebElement menSub = wait.until(ExpectedConditions.elementToBeClickable(
                By.cssSelector("#accordian .panel-collapse.in .panel-body ul li a")));
        jsClick(menSub);

        WebElement title2 = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".title.text-center")));
        expectTrue(title2.getText().toUpperCase().contains("MEN"), "Expected MEN title, actual=" + title2.getText());
    }

    // =========================================================
    // Common steps
    // =========================================================
    private static void openHome() {
        driver.get(BASE_URL);
        dismissAdsIfAny();
        wait.until(ExpectedConditions.visibilityOfElementLocated(HOME_LOGO));
    }

    // =========================================================
    // Cart actions
    // =========================================================
    private static void addProductByIndex(int index) {
        dismissAdsIfAny();

        List<WebElement> cards = wait.until(d -> d.findElements(By.cssSelector(".features_items .product-image-wrapper")));
        expectTrue(cards.size() > index, "Not enough products. index=" + index + ", total=" + cards.size());

        WebElement card = cards.get(index);
        scrollIntoView(card);
        actions.moveToElement(card).perform();

        WebElement addBtn = card.findElement(By.cssSelector("a.add-to-cart"));
        jsClick(addBtn);

        wait.until(ExpectedConditions.visibilityOfElementLocated(CART_MODAL));
    }

    private static void clickContinueShoppingOnModal() {
        WebElement btn = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("#cartModal .close-modal")));
        jsClick(btn);
        wait.until(ExpectedConditions.invisibilityOfElementLocated(CART_MODAL));
    }

    private static void clickViewCartOnModal() {
        WebElement modal = wait.until(ExpectedConditions.visibilityOfElementLocated(CART_MODAL));
        WebElement viewCart = modal.findElement(By.xpath(".//a[@href='/view_cart']"));
        jsClick(viewCart);
    }

    private static void clearCartIfAny() {
        try {
            click(CART_MENU);
            wait.until(ExpectedConditions.visibilityOfElementLocated(CART_TABLE));

            List<WebElement> deletes = driver.findElements(CART_DELETE);
            while (!deletes.isEmpty()) {
                int before = driver.findElements(CART_ROWS).size();

                jsClick(deletes.get(0));
                waitUntil(() -> cartIsEmptyText() || driver.findElements(CART_ROWS).size() < before, 20);

                deletes = driver.findElements(CART_DELETE);
            }
        } catch (Throwable ignored) {}
    }

    private static boolean cartIsEmptyText() {
        try {
            return driver.getPageSource().toLowerCase().contains("cart is empty");
        } catch (Throwable t) {
            return false;
        }
    }

    // =========================================================
    // Helpers
    // =========================================================
    private static void click(By by) {
        WebElement el = wait.until(ExpectedConditions.elementToBeClickable(by));
        scrollIntoView(el);
        dismissAdsIfAny();
        jsClick(el);
    }

    private static void jsClick(WebElement el) {
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", el);
    }

    private static void scrollIntoView(WebElement el) {
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block:'center'});", el);
    }

    private static void setQuantityHard(WebElement qtyInput, String value) {
        try {
            qtyInput.click();
            qtyInput.sendKeys(Keys.chord(Keys.CONTROL, "a"));
            qtyInput.sendKeys(Keys.BACK_SPACE);
            qtyInput.sendKeys(value);
        } catch (Throwable ignored) {}

        String v = "";
        try { v = qtyInput.getAttribute("value"); } catch (Throwable ignored) {}

        if (!value.equals(v)) {
            ((JavascriptExecutor) driver).executeScript(
                    "arguments[0].value = arguments[1];" +
                            "arguments[0].dispatchEvent(new Event('input', {bubbles:true}));" +
                            "arguments[0].dispatchEvent(new Event('change', {bubbles:true}));",
                    qtyInput, value
            );
        }
    }

    private static int getQtyInCartByProductName(String productName) {
        List<WebElement> rows = driver.findElements(CART_ROWS);
        for (WebElement row : rows) {
            String name = "";
            try { name = row.findElement(By.cssSelector("td.cart_description a")).getText().trim(); }
            catch (Throwable ignored) {}

            if (name.equalsIgnoreCase(productName)) {
                String qtyTxt = row.findElement(By.cssSelector("td.cart_quantity button")).getText().trim();
                return Integer.parseInt(qtyTxt);
            }
        }
        String qtyTxt = driver.findElement(By.cssSelector("td.cart_quantity button")).getText().trim();
        return Integer.parseInt(qtyTxt);
    }

    private static void waitUntil(BooleanSupplier condition, int seconds) {
        new WebDriverWait(driver, Duration.ofSeconds(seconds)).until(d -> condition.getAsBoolean());
    }

    private static void expectTrue(boolean condition, String messageIfFail) {
        if (!condition) throw new AssertionError(messageIfFail);
    }

    // ===== FIX: remove/hide google ads iframes that intercept click =====
    private static void dismissAdsIfAny() {
        try {
            ((JavascriptExecutor) driver).executeScript(
                    "document.querySelectorAll(" +
                            "'iframe[id^=\"aswift_\"],iframe[name^=\"aswift_\"],iframe[src*=\"doubleclick\"],iframe[src*=\"googlesyndication\"]," +
                            "iframe[id*=\"google_ads\"],iframe[name*=\"google_ads\"]'" +
                            ").forEach(i=>i.remove());" +
                            "var e=document.getElementById('ad_position_box'); if(e) e.remove();" +
                            "document.querySelectorAll('.adsbygoogle,.ad,.ads').forEach(x=>x.remove());"
            );
        } catch (Throwable ignored) {}
    }
}
