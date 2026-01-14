package Test;

import Initialization.Init;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.*;

import java.nio.file.*;
import java.time.Duration;
import java.util.List;
import java.util.Locale;
import java.util.function.BooleanSupplier;

public class AutomationExercise_TC01_26_All {

    // ===================== Driver =====================
    private static WebDriver driver;
    private static WebDriverWait wait;
    private static Actions actions;

    // ===================== Base =====================
    private static final String BASE_URL = "https://automationexercise.com/";

    // ===================== Locators (common) =====================
    private static final By HOME_LOGO = By.xpath("//img[@alt='Website for automation practice']");
    private static final By MENU_SIGNUP_LOGIN = By.xpath("//a[contains(.,'Signup / Login')]");
    private static final By MENU_LOGOUT = By.xpath("//a[contains(.,'Logout')]");
    private static final By MENU_DELETE_ACCOUNT = By.xpath("//a[contains(.,'Delete Account')]");
    private static final By MENU_CONTACT_US = By.xpath("//a[contains(.,'Contact us') or contains(.,'Contact Us')]");
    private static final By MENU_TEST_CASES = By.xpath("//a[contains(.,'Test Cases')]");
    private static final By MENU_PRODUCTS = By.cssSelector("a[href='/products']");
    private static final By MENU_CART = By.cssSelector("a[href='/view_cart']");
    private static final By FOOTER_SUBSCRIPTION = By.xpath("//*[normalize-space()='Subscription' or normalize-space()='SUBSCRIPTION']");

    // Cart
    private static final By CART_MODAL = By.id("cartModal");
    private static final By CART_TABLE = By.id("cart_info_table");
    private static final By CART_ROWS = By.cssSelector("#cart_info_table tbody tr");
    private static final By CART_DELETE = By.cssSelector("a.cart_quantity_delete");

    // Products page
    private static final By ALL_PRODUCTS_TITLE = By.xpath("//h2[contains(@class,'title') and contains(.,'All Products')]");
    private static final By FEATURES_ITEMS = By.cssSelector(".features_items");

    // ===================== Shared test account (for TC2/4/16/20 etc.) =====================
    private static String OK_NAME;
    private static String OK_EMAIL;
    private static String OK_PASS;

    // ===================== Main =====================
    public static void main(String[] args) {
        try {
            Init.SetUp("chrome");
            driver = Init.driver;
            wait = new WebDriverWait(driver, Duration.ofSeconds(15));
            actions = new Actions(driver);

            // create a reusable account once (do NOT delete here)
            ensureReusableAccount();

            run("TC01 Register User (then Delete)", AutomationExercise_TC01_26_All::tc01_registerUser_delete, false, true);
            run("TC02 Login User with correct email and password (then Delete)", AutomationExercise_TC01_26_All::tc02_loginCorrect_delete, false, true);
            run("TC03 Login User with incorrect email and password", AutomationExercise_TC01_26_All::tc03_loginIncorrect, false, false);
            run("TC04 Logout User", AutomationExercise_TC01_26_All::tc04_logoutUser, false, false);
            run("TC05 Register User with existing email", AutomationExercise_TC01_26_All::tc05_registerExistingEmail, false, false);
            run("TC06 Contact Us Form", AutomationExercise_TC01_26_All::tc06_contactUsForm, false, false);
            run("TC07 Verify Test Cases Page", AutomationExercise_TC01_26_All::tc07_testCasesPage, false, false);
            run("TC08 Verify All Products and product detail page", AutomationExercise_TC01_26_All::tc08_allProducts_detail, false, false);
            run("TC09 Search Product", AutomationExercise_TC01_26_All::tc09_searchProduct, false, false);
            run("TC10 Verify Subscription in home page", AutomationExercise_TC01_26_All::tc10_subscriptionHome, false, false);
            run("TC11 Verify Subscription in Cart page", AutomationExercise_TC01_26_All::tc11_subscriptionCart, true, false);
            run("TC12 Add Products in Cart", AutomationExercise_TC01_26_All::tc12_addProductsInCart, true, false);
            run("TC13 Verify Product quantity in Cart", AutomationExercise_TC01_26_All::tc13_verifyQuantityInCart, true, false);
            run("TC14 Place Order: Register while Checkout", AutomationExercise_TC01_26_All::tc14_placeOrder_registerWhileCheckout, true, true);
            run("TC15 Place Order: Register before Checkout", AutomationExercise_TC01_26_All::tc15_placeOrder_registerBeforeCheckout, true, true);
            run("TC16 Place Order: Login before Checkout", AutomationExercise_TC01_26_All::tc16_placeOrder_loginBeforeCheckout, true, false);
            run("TC17 Remove Products From Cart", AutomationExercise_TC01_26_All::tc17_removeProductsFromCart, true, false);
            run("TC18 View Category Products", AutomationExercise_TC01_26_All::tc18_viewCategoryProducts, false, false);
            run("TC19 View & Cart Brand Products", AutomationExercise_TC01_26_All::tc19_brandProducts, true, false);
            run("TC20 Search Products and Verify Cart After Login", AutomationExercise_TC01_26_All::tc20_searchProducts_verifyCartAfterLogin, true, false);
            run("TC21 Add review on product", AutomationExercise_TC01_26_All::tc21_addReviewOnProduct, false, false);
            run("TC22 Add to cart from Recommended items", AutomationExercise_TC01_26_All::tc22_addToCartFromRecommendedItems, true, false);
            run("TC23 Verify address details in checkout page", AutomationExercise_TC01_26_All::tc23_verifyAddressDetailsCheckout, true, true);
            run("TC24 Download Invoice after purchase order", AutomationExercise_TC01_26_All::tc24_downloadInvoiceAfterPurchase, true, true);
            run("TC25 Verify Scroll Up using 'Arrow' button", AutomationExercise_TC01_26_All::tc25_scrollUpWithArrow, false, false);
            run("TC26 Verify Scroll Up without 'Arrow' button", AutomationExercise_TC01_26_All::tc26_scrollUpWithoutArrow, false, false);

            System.out.println("\n[PASS] DONE: TC01..TC26");
        } catch (Throwable t) {
            System.err.println("\n[FAIL] " + t.getMessage());
            t.printStackTrace();
        } finally {
            try { Init.Teardown(); } catch (Throwable ignored) {}
        }
    }

    // ===================== Runner =====================
    private static void run(String name, Runnable tc, boolean cartTC, boolean endsWithDeleteAccount) {
        System.out.println("\n========== " + name + " ==========");
        try {
            if (cartTC) resetCartBeforeCartTC();
            tc.run();
            System.out.println("✅ " + name + " PASSED");
        } finally {
            if (cartTC) clearCartIfAny();
            // if test deleted account, make sure we are logged out and back home
            if (endsWithDeleteAccount) safeLogoutIfLoggedIn();
        }
    }

    // =========================================================
    // TC01: Register User + Delete Account
    // =========================================================
    private static void tc01_registerUser_delete() {
        openHomeAndVerify();

        click(MENU_SIGNUP_LOGIN);
        mustVisible(By.xpath("//h2[text()='New User Signup!']"));

        String name = "Tien Dung";
        String email = "t" + System.currentTimeMillis() + "@example.com";
        String pass = "P@ssw0rd123";

        type(By.name("name"), name);
        type(By.cssSelector("input[data-qa='signup-email']"), email);
        click(By.cssSelector("button[data-qa='signup-button']"));

        mustVisible(By.xpath("//b[normalize-space()='Enter Account Information']"));

        click(By.id("id_gender1"));
        type(By.id("password"), pass);
        selectByValue(By.id("days"), "19");
        selectByValue(By.id("months"), "5");
        selectByValue(By.id("years"), "1990");

        safeClick(By.id("newsletter"));
        safeClick(By.id("optin"));

        type(By.id("first_name"), "Tien");
        type(By.id("last_name"), "Dung");
        type(By.id("company"), "ABC Corp");
        type(By.id("address1"), "123 Test Street");
        type(By.id("address2"), "Line 2");
        selectByText(By.id("country"), "Canada");
        type(By.id("state"), "ON");
        type(By.id("city"), "Toronto");
        type(By.id("zipcode"), "A1A1A1");
        type(By.id("mobile_number"), "0900000000");

        click(By.cssSelector("button[data-qa='create-account']"));

        mustVisible(By.cssSelector("h2[data-qa='account-created']"));
        click(By.cssSelector("a[data-qa='continue-button']"));

        mustLoggedInAs(name);

        click(MENU_DELETE_ACCOUNT);
        mustVisible(By.cssSelector("h2[data-qa='account-deleted']"));
        click(By.cssSelector("a[data-qa='continue-button']"));
    }

    // =========================================================
    // TC02: Login correct + Delete
    // =========================================================
    private static void tc02_loginCorrect_delete() {
        // create a fresh account, then logout, then login, then delete
        Account a = createAccountViaUI();
        safeLogoutIfLoggedIn();

        openHomeAndVerify();
        click(MENU_SIGNUP_LOGIN);
        mustVisible(By.xpath("//h2[contains(.,'Login to your account')]"));

        type(By.cssSelector("input[data-qa='login-email']"), a.email);
        type(By.cssSelector("input[data-qa='login-password']"), a.pass);
        click(By.cssSelector("button[data-qa='login-button']"));

        mustLoggedInAs(a.name);

        click(MENU_DELETE_ACCOUNT);
        mustVisible(By.cssSelector("h2[data-qa='account-deleted']"));
        click(By.cssSelector("a[data-qa='continue-button']"));
    }

    // =========================================================
    // TC03: Login incorrect
    // =========================================================
    private static void tc03_loginIncorrect() {
        openHomeAndVerify();
        click(MENU_SIGNUP_LOGIN);
        mustVisible(By.xpath("//h2[contains(.,'Login to your account')]"));

        type(By.cssSelector("input[data-qa='login-email']"), "wrong" + System.currentTimeMillis() + "@example.com");
        type(By.cssSelector("input[data-qa='login-password']"), "wrongpass123");
        click(By.cssSelector("button[data-qa='login-button']"));

        mustVisible(By.xpath("//*[contains(.,'Your email or password is incorrect!')]"));
    }

    // =========================================================
    // TC04: Logout user
    // =========================================================
    private static void tc04_logoutUser() {
        // login with reusable account
        loginWithReusableAccount();

        // Logout
        click(MENU_LOGOUT);

        // verify navigated to login page
        mustVisible(By.xpath("//h2[contains(.,'Login to your account')]"));
        expectTrue(driver.getCurrentUrl().contains("/login"), "Expected /login, actual=" + driver.getCurrentUrl());
    }

    // =========================================================
    // TC05: Register existing email
    // =========================================================
    private static void tc05_registerExistingEmail() {
        // make sure an account exists
        Account a = createAccountViaUI();
        safeLogoutIfLoggedIn();

        openHomeAndVerify();
        click(MENU_SIGNUP_LOGIN);
        mustVisible(By.xpath("//h2[text()='New User Signup!']"));

        type(By.name("name"), "Someone");
        type(By.cssSelector("input[data-qa='signup-email']"), a.email);
        click(By.cssSelector("button[data-qa='signup-button']"));

        mustVisible(By.xpath("//*[contains(.,'Email Address already exist!')]"));

        // cleanup: login and delete that created account
        login(a.email, a.pass, a.name);
        click(MENU_DELETE_ACCOUNT);
        mustVisible(By.cssSelector("h2[data-qa='account-deleted']"));
        click(By.cssSelector("a[data-qa='continue-button']"));
    }

    // =========================================================
    // TC06: Contact Us
    // =========================================================
    private static void tc06_contactUsForm() {
        openHomeAndVerify();
        click(MENU_CONTACT_US);

        mustVisible(By.xpath("//h2[contains(.,'Get In Touch') or contains(.,'GET IN TOUCH')]"));

        type(By.cssSelector("input[data-qa='name']"), "Tien Dung");
        type(By.cssSelector("input[data-qa='email']"), "td" + System.currentTimeMillis() + "@example.com");
        type(By.cssSelector("input[data-qa='subject']"), "Automation Exercise - Contact");
        type(By.cssSelector("textarea[data-qa='message']"), "Hello, this is a test message.");

        // upload file
        Path tmp = createTempUploadFile();
        WebElement upload = mustVisible(By.cssSelector("input[name='upload_file']"));
        upload.sendKeys(tmp.toAbsolutePath().toString());

        click(By.cssSelector("input[data-qa='submit-button']"));

        // accept alert
        try { driver.switchTo().alert().accept(); } catch (Throwable ignored) {}

        mustVisible(By.xpath("//*[contains(.,'Success! Your details have been submitted successfully.')]"));

        click(By.xpath("//a[contains(.,'Home')]"));
        mustVisible(HOME_LOGO);
    }

    // =========================================================
    // TC07: Test Cases page
    // =========================================================
    private static void tc07_testCasesPage() {
        openHomeAndVerify();
        click(MENU_TEST_CASES);
        // just confirm page has Test Cases header
        mustVisible(By.xpath("//*[contains(.,'Test Cases') or contains(.,'TEST CASES')]"));
    }

    // =========================================================
    // TC08: All Products + Detail
    // =========================================================
    private static void tc08_allProducts_detail() {
        openHomeAndVerify();

        click(MENU_PRODUCTS);
        mustVisible(ALL_PRODUCTS_TITLE);
        mustVisible(FEATURES_ITEMS);

        // open first product detail via View Product (use JS click because ads)
        WebElement view = mustVisible(By.cssSelector("a[href^='/product_details/']"));
        scrollIntoView(view);
        dismissAdsIfAny();
        jsClick(view);

        WebElement info = mustVisible(By.cssSelector(".product-information"));
        expectTrue(!info.findElement(By.cssSelector("h2")).getText().trim().isEmpty(), "Missing product name");
        mustVisible(By.xpath("//div[contains(@class,'product-information')]//p[contains(.,'Category')]"));
        mustVisible(By.xpath("//div[contains(@class,'product-information')]//span//span"));
        mustVisible(By.xpath("//div[contains(@class,'product-information')]//p[contains(.,'Availability')]"));
        mustVisible(By.xpath("//div[contains(@class,'product-information')]//p[contains(.,'Condition')]"));
        mustVisible(By.xpath("//div[contains(@class,'product-information')]//p[contains(.,'Brand')]"));
    }

    // =========================================================
    // TC09: Search Product
    // =========================================================
    private static void tc09_searchProduct() {
        openHomeAndVerify();

        click(MENU_PRODUCTS);
        mustVisible(ALL_PRODUCTS_TITLE);

        type(By.id("search_product"), "dress");
        click(By.id("submit_search"));

        mustVisible(By.xpath("//h2[contains(@class,'title') and contains(.,'Searched Products')]"));
        List<WebElement> results = driver.findElements(By.cssSelector(".features_items .product-image-wrapper"));
        expectTrue(!results.isEmpty(), "Search results empty");
    }

    // =========================================================
    // TC10: Subscription home
    // =========================================================
    private static void tc10_subscriptionHome() {
        openHomeAndVerify();

        scrollToBottom();
        mustVisible(FOOTER_SUBSCRIPTION);

        type(By.id("susbscribe_email"), "sub" + System.currentTimeMillis() + "@example.com");
        click(By.id("subscribe"));

        mustVisible(By.xpath("//*[contains(.,'You have been successfully subscribed!')]"));
    }

    // =========================================================
    // TC11: Subscription cart
    // =========================================================
    private static void tc11_subscriptionCart() {
        openHomeAndVerify();
        click(MENU_CART);
        mustVisible(CART_TABLE);

        scrollToBottom();
        mustVisible(FOOTER_SUBSCRIPTION);

        type(By.id("susbscribe_email"), "sub" + System.currentTimeMillis() + "@example.com");
        click(By.id("subscribe"));

        mustVisible(By.xpath("//*[contains(.,'You have been successfully subscribed!')]"));
    }

    // =========================================================
    // TC12: Add Products in Cart
    // =========================================================
    private static void tc12_addProductsInCart() {
        openHomeAndVerify();

        click(MENU_PRODUCTS);
        mustVisible(ALL_PRODUCTS_TITLE);

        addProductByIndex(0);
        clickContinueShoppingOnModal();
        addProductByIndex(1);
        clickViewCartOnModal();

        mustVisible(CART_TABLE);
        List<WebElement> rows = driver.findElements(CART_ROWS);
        expectTrue(rows.size() >= 2, "Cart must have >=2 rows, actual=" + rows.size());

        for (int i = 0; i < 2; i++) {
            WebElement row = rows.get(i);
            String price = row.findElement(By.cssSelector("td.cart_price p")).getText().trim();
            String qty = row.findElement(By.cssSelector("td.cart_quantity button")).getText().trim();
            String total = row.findElement(By.cssSelector("td.cart_total p")).getText().trim();
            expectTrue(!price.isEmpty() && !qty.isEmpty() && !total.isEmpty(), "Missing price/qty/total at row " + i);
        }
    }

    // =========================================================
    // TC13: Verify Product quantity in Cart (exact 4)
    // =========================================================
    private static void tc13_verifyQuantityInCart() {
        openHomeAndVerify();

        WebElement viewProduct = mustVisible(By.cssSelector("a[href^='/product_details/']"));
        scrollIntoView(viewProduct);
        dismissAdsIfAny();
        jsClick(viewProduct);

        WebElement info = mustVisible(By.cssSelector(".product-information"));
        String productName = info.findElement(By.cssSelector("h2")).getText().trim();

        WebElement qtyInput = mustVisible(By.id("quantity"));
        setQuantityHard(qtyInput, "4");
        expectTrue("4".equals(qtyInput.getAttribute("value")), "Cannot set qty=4, actual=" + qtyInput.getAttribute("value"));

        WebElement addBtn = mustVisible(By.cssSelector("button.cart"));
        dismissAdsIfAny();
        jsClick(addBtn);

        mustVisible(CART_MODAL);
        clickViewCartOnModal();

        mustVisible(CART_TABLE);
        int qtyInCart = getQtyInCartByProductName(productName);
        expectTrue(qtyInCart == 4, "Cart quantity must be 4, actual=" + qtyInCart);
    }

    // =========================================================
    // TC14: Place Order - Register while Checkout
    // =========================================================
    private static void tc14_placeOrder_registerWhileCheckout() {
        openHomeAndVerify();

        // Add product
        click(MENU_PRODUCTS);
        mustVisible(ALL_PRODUCTS_TITLE);
        addProductByIndex(0);
        clickViewCartOnModal();

        // Checkout -> Register/Login
        click(By.xpath("//a[contains(.,'Proceed To Checkout')]"));
        click(By.xpath("//u[contains(.,'Register / Login') or contains(.,'Register')]/.. | //a[contains(.,'Register / Login')]"));

        // Register account
        Account a = createAccountFromSignupScreen();

        // Go cart -> checkout
        click(MENU_CART);
        mustVisible(CART_TABLE);
        click(By.xpath("//a[contains(.,'Proceed To Checkout')]"));

        mustVisible(By.xpath("//*[contains(.,'Address Details')]"));
        mustVisible(By.xpath("//*[contains(.,'Review Your Order')]"));

        type(By.name("message"), "Please deliver fast.");
        click(By.xpath("//a[contains(.,'Place Order')]"));

        fillPaymentAndConfirm();

        mustVisible(By.xpath("//*[contains(.,'Your order has been placed successfully!')]"));

        // Delete account (as required)
        click(MENU_DELETE_ACCOUNT);
        mustVisible(By.cssSelector("h2[data-qa='account-deleted']"));
        click(By.cssSelector("a[data-qa='continue-button']"));
    }

    // =========================================================
    // TC15: Place Order - Register before Checkout
    // =========================================================
    private static void tc15_placeOrder_registerBeforeCheckout() {
        openHomeAndVerify();
        click(MENU_SIGNUP_LOGIN);

        Account a = createAccountFromSignupScreen();

        // Add products
        click(MENU_PRODUCTS);
        mustVisible(ALL_PRODUCTS_TITLE);
        addProductByIndex(0);
        clickContinueShoppingOnModal();
        addProductByIndex(1);
        clickViewCartOnModal();

        mustVisible(CART_TABLE);
        click(By.xpath("//a[contains(.,'Proceed To Checkout')]"));

        mustVisible(By.xpath("//*[contains(.,'Address Details')]"));
        mustVisible(By.xpath("//*[contains(.,'Review Your Order')]"));

        type(By.name("message"), "Order comment.");
        click(By.xpath("//a[contains(.,'Place Order')]"));

        fillPaymentAndConfirm();

        mustVisible(By.xpath("//*[contains(.,'Your order has been placed successfully!')]"));

        click(MENU_DELETE_ACCOUNT);
        mustVisible(By.cssSelector("h2[data-qa='account-deleted']"));
        click(By.cssSelector("a[data-qa='continue-button']"));
    }

    // =========================================================
    // TC16: Place Order - Login before Checkout
    // =========================================================
    private static void tc16_placeOrder_loginBeforeCheckout() {
        // login reusable account
        loginWithReusableAccount();

        // Add product and checkout
        click(MENU_PRODUCTS);
        mustVisible(ALL_PRODUCTS_TITLE);
        addProductByIndex(0);
        clickViewCartOnModal();

        mustVisible(CART_TABLE);
        click(By.xpath("//a[contains(.,'Proceed To Checkout')]"));

        mustVisible(By.xpath("//*[contains(.,'Address Details')]"));
        mustVisible(By.xpath("//*[contains(.,'Review Your Order')]"));

        type(By.name("message"), "Login-before-checkout comment");
        click(By.xpath("//a[contains(.,'Place Order')]"));

        fillPaymentAndConfirm();
        mustVisible(By.xpath("//*[contains(.,'Your order has been placed successfully!')]"));

        // steps say delete account
        click(MENU_DELETE_ACCOUNT);
        mustVisible(By.cssSelector("h2[data-qa='account-deleted']"));
        click(By.cssSelector("a[data-qa='continue-button']"));

        // recreate reusable account for following tests
        ensureReusableAccount();
    }

    // =========================================================
    // TC17: Remove Products From Cart
    // =========================================================
    private static void tc17_removeProductsFromCart() {
        openHomeAndVerify();

        click(MENU_PRODUCTS);
        mustVisible(ALL_PRODUCTS_TITLE);
        addProductByIndex(0);
        clickViewCartOnModal();

        mustVisible(CART_TABLE);

        int before = driver.findElements(CART_ROWS).size();
        expectTrue(before > 0, "Cart empty before remove");

        WebElement del = mustVisible(CART_DELETE);
        scrollIntoView(del);
        dismissAdsIfAny();
        jsClick(del);

        waitUntil(() -> pageHasText("cart is empty") || driver.findElements(CART_ROWS).size() < before, 25);

        boolean empty = pageHasText("cart is empty");
        int after = driver.findElements(CART_ROWS).size();
        expectTrue(after < before || empty, "Product not removed. before=" + before + ", after=" + after + ", empty=" + empty);
    }

    // =========================================================
    // TC18: View Category Products
    // =========================================================
    private static void tc18_viewCategoryProducts() {
        openHomeAndVerify();

        WebElement sidebar = mustVisible(By.cssSelector(".left-sidebar"));
        expectTrue(sidebar.getText().toLowerCase(Locale.ROOT).contains("category"), "Categories not visible");

        WebElement women = driver.findElement(By.xpath("//div[@id='accordian']//a[contains(.,'Women')]"));
        scrollIntoView(women);
        dismissAdsIfAny();
        jsClick(women);

        WebElement dress = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//div[@id='accordian']//div[contains(@class,'panel-collapse') and contains(@class,'in')]//a[contains(.,'Dress')]")));
        jsClick(dress);

        WebElement title = mustVisible(By.cssSelector(".title.text-center"));
        expectTrue(title.getText().toUpperCase(Locale.ROOT).contains("WOMEN"), "Expected WOMEN title, actual=" + title.getText());

        WebElement men = driver.findElement(By.xpath("//div[@id='accordian']//a[contains(.,'Men')]"));
        scrollIntoView(men);
        jsClick(men);

        WebElement menSub = wait.until(ExpectedConditions.elementToBeClickable(
                By.cssSelector("#accordian .panel-collapse.in .panel-body ul li a")));
        jsClick(menSub);

        WebElement title2 = mustVisible(By.cssSelector(".title.text-center"));
        expectTrue(title2.getText().toUpperCase(Locale.ROOT).contains("MEN"), "Expected MEN title, actual=" + title2.getText());
    }

    // =========================================================
    // TC19: Brand Products
    // =========================================================
    private static void tc19_brandProducts() {
        openHomeAndVerify();
        click(MENU_PRODUCTS);
        mustVisible(ALL_PRODUCTS_TITLE);

        // Brands visible
        WebElement brands = mustVisible(By.xpath("//h2[contains(.,'Brands')]"));
        expectTrue(brands.isDisplayed(), "Brands header not visible");

        // click any brand
        WebElement brand1 = mustVisible(By.cssSelector(".brands-name ul li a"));
        String b1 = brand1.getText().trim();
        jsClick(brand1);

        mustVisible(By.cssSelector(".title.text-center"));
        expectTrue(driver.getPageSource().toLowerCase(Locale.ROOT).contains("brand"), "Not on brand page (best effort)");

        // click another brand
        List<WebElement> brandLinks = driver.findElements(By.cssSelector(".brands-name ul li a"));
        if (brandLinks.size() > 1) {
            WebElement brand2 = brandLinks.get(1);
            String b2 = brand2.getText().trim();
            jsClick(brand2);
            mustVisible(By.cssSelector(".title.text-center"));
            System.out.println("Brand switch: " + b1 + " -> " + b2);
        }
    }

    // =========================================================
    // TC20: Search Products + Verify Cart After Login
    // =========================================================
    private static void tc20_searchProducts_verifyCartAfterLogin() {
        openHomeAndVerify();
        click(MENU_PRODUCTS);
        mustVisible(ALL_PRODUCTS_TITLE);

        type(By.id("search_product"), "dress");
        click(By.id("submit_search"));
        mustVisible(By.xpath("//h2[contains(@class,'title') and contains(.,'Searched Products')]"));

        // add first 2 results to cart
        List<WebElement> cards = driver.findElements(By.cssSelector(".features_items .product-image-wrapper"));
        expectTrue(!cards.isEmpty(), "No searched products to add");

        int addN = Math.min(2, cards.size());
        for (int i = 0; i < addN; i++) {
            WebElement card = cards.get(i);
            scrollIntoView(card);
            actions.moveToElement(card).perform();
            WebElement add = card.findElement(By.cssSelector("a.add-to-cart"));
            jsClick(add);
            mustVisible(CART_MODAL);
            if (i < addN - 1) clickContinueShoppingOnModal();
        }
        clickViewCartOnModal();
        mustVisible(CART_TABLE);
        int beforeLogin = driver.findElements(CART_ROWS).size();
        expectTrue(beforeLogin >= addN, "Cart rows before login < added items");

        // login
        click(MENU_SIGNUP_LOGIN);
        mustVisible(By.xpath("//h2[contains(.,'Login to your account')]"));
        type(By.cssSelector("input[data-qa='login-email']"), OK_EMAIL);
        type(By.cssSelector("input[data-qa='login-password']"), OK_PASS);
        click(By.cssSelector("button[data-qa='login-button']"));
        mustLoggedInAs(OK_NAME);

        // back to cart verify still there
        click(MENU_CART);
        mustVisible(CART_TABLE);
        int afterLogin = driver.findElements(CART_ROWS).size();
        expectTrue(afterLogin >= addN, "Cart rows after login < added items (actual=" + afterLogin + ")");
    }

    // =========================================================
    // TC21: Add review on product
    // =========================================================
    private static void tc21_addReviewOnProduct() {
        openHomeAndVerify();
        click(MENU_PRODUCTS);
        mustVisible(ALL_PRODUCTS_TITLE);

        WebElement view = mustVisible(By.cssSelector("a[href^='/product_details/']"));
        scrollIntoView(view);
        dismissAdsIfAny();
        jsClick(view);

        mustVisible(By.xpath("//*[contains(.,'Write Your Review')]"));

        type(By.id("name"), "Tien Dung");
        type(By.id("email"), "rv" + System.currentTimeMillis() + "@example.com");
        type(By.id("review"), "Nice product. Automated review.");

        click(By.id("button-review"));
        mustVisible(By.xpath("//*[contains(.,'Thank you for your review.')]"));
    }

    // =========================================================
    // TC22: Add to cart from Recommended items
    // =========================================================
    private static void tc22_addToCartFromRecommendedItems() {
        openHomeAndVerify();

        // scroll down to recommended
        scrollToBottom();
        mustVisible(By.xpath("//*[contains(.,'Recommended Items') or contains(.,'RECOMMENDED ITEMS')]"));

        WebElement add = mustVisible(By.xpath("//div[@id='recommended-item-carousel']//a[contains(@class,'add-to-cart')]"));
        scrollIntoView(add);
        dismissAdsIfAny();
        jsClick(add);

        mustVisible(CART_MODAL);
        clickViewCartOnModal();

        mustVisible(CART_TABLE);
        expectTrue(driver.findElements(CART_ROWS).size() > 0, "Cart must have at least 1 item");
    }

    // =========================================================
    // TC23: Verify address details in checkout page
    // =========================================================
    private static void tc23_verifyAddressDetailsCheckout() {
        openHomeAndVerify();
        click(MENU_SIGNUP_LOGIN);

        Account a = createAccountFromSignupScreen(); // fill known address

        // add product
        click(MENU_PRODUCTS);
        mustVisible(ALL_PRODUCTS_TITLE);
        addProductByIndex(0);
        clickViewCartOnModal();

        mustVisible(CART_TABLE);
        click(By.xpath("//a[contains(.,'Proceed To Checkout')]"));

        // verify address blocks exist
        mustVisible(By.id("address_delivery"));
        mustVisible(By.id("address_invoice"));

        // delete account
        click(MENU_DELETE_ACCOUNT);
        mustVisible(By.cssSelector("h2[data-qa='account-deleted']"));
        click(By.cssSelector("a[data-qa='continue-button']"));
    }

    // =========================================================
    // TC24: Download Invoice after purchase order
    // =========================================================
    private static void tc24_downloadInvoiceAfterPurchase() {
        openHomeAndVerify();

        // add product
        click(MENU_PRODUCTS);
        mustVisible(ALL_PRODUCTS_TITLE);
        addProductByIndex(0);
        clickViewCartOnModal();

        // checkout -> register/login -> create account
        click(By.xpath("//a[contains(.,'Proceed To Checkout')]"));
        click(By.xpath("//u[contains(.,'Register / Login') or contains(.,'Register')]/.. | //a[contains(.,'Register / Login')]"));
        Account a = createAccountFromSignupScreen();

        // back cart -> checkout -> place order
        click(MENU_CART);
        mustVisible(CART_TABLE);
        click(By.xpath("//a[contains(.,'Proceed To Checkout')]"));
        type(By.name("message"), "Invoice test");
        click(By.xpath("//a[contains(.,'Place Order')]"));

        fillPaymentAndConfirm();
        mustVisible(By.xpath("//*[contains(.,'Your order has been placed successfully!')]"));

        // Download Invoice (best-effort verify: button exists & clickable)
        WebElement download = mustVisible(By.xpath("//a[contains(.,'Download Invoice')]"));
        scrollIntoView(download);
        jsClick(download);

        // Continue
        WebElement cont = mustVisible(By.xpath("//a[contains(.,'Continue')]"));
        jsClick(cont);

        // delete account
        click(MENU_DELETE_ACCOUNT);
        mustVisible(By.cssSelector("h2[data-qa='account-deleted']"));
        click(By.cssSelector("a[data-qa='continue-button']"));
    }

    // =========================================================
    // TC25: Scroll Up using Arrow
    // =========================================================
    private static void tc25_scrollUpWithArrow() {
        openHomeAndVerify();

        scrollToBottom();
        mustVisible(FOOTER_SUBSCRIPTION);

        WebElement arrow = mustVisible(By.id("scrollUp"));
        jsClick(arrow);

        // verify top text visible
        mustVisible(By.xpath("//*[contains(.,'Full-Fledged practice website for Automation Engineers')]"));
    }

    // =========================================================
    // TC26: Scroll Up without Arrow
    // =========================================================
    private static void tc26_scrollUpWithoutArrow() {
        openHomeAndVerify();

        scrollToBottom();
        mustVisible(FOOTER_SUBSCRIPTION);

        // scroll top by JS
        ((JavascriptExecutor) driver).executeScript("window.scrollTo(0,0);");
        mustVisible(By.xpath("//*[contains(.,'Full-Fledged practice website for Automation Engineers')]"));
    }

    // ===================== Account helpers =====================
    private static void ensureReusableAccount() {
        // Create once; if already set, do nothing
        if (OK_EMAIL != null) return;

        Account a = createAccountViaUI();
        OK_NAME = a.name;
        OK_EMAIL = a.email;
        OK_PASS = a.pass;

        // keep logged in state? We'll logout for clean tests
        safeLogoutIfLoggedIn();
    }

    private static void loginWithReusableAccount() {
        if (OK_EMAIL == null) ensureReusableAccount();
        login(OK_EMAIL, OK_PASS, OK_NAME);
    }

    private static void login(String email, String pass, String expectedName) {
        openHomeAndVerify();
        click(MENU_SIGNUP_LOGIN);
        mustVisible(By.xpath("//h2[contains(.,'Login to your account')]"));

        type(By.cssSelector("input[data-qa='login-email']"), email);
        type(By.cssSelector("input[data-qa='login-password']"), pass);
        click(By.cssSelector("button[data-qa='login-button']"));

        mustLoggedInAs(expectedName);
    }

    private static Account createAccountViaUI() {
        openHomeAndVerify();
        click(MENU_SIGNUP_LOGIN);
        return createAccountFromSignupScreen();
    }

    // Creates account from /login screen (New User Signup) and returns creds; DOES NOT delete
    private static Account createAccountFromSignupScreen() {
        mustVisible(By.xpath("//h2[text()='New User Signup!']"));

        String name = "Tien Dung";
        String email = "t" + System.currentTimeMillis() + "@example.com";
        String pass = "P@ssw0rd123";

        type(By.name("name"), name);
        type(By.cssSelector("input[data-qa='signup-email']"), email);
        click(By.cssSelector("button[data-qa='signup-button']"));

        mustVisible(By.xpath("//b[normalize-space()='Enter Account Information']"));

        click(By.id("id_gender1"));
        type(By.id("password"), pass);
        selectByValue(By.id("days"), "19");
        selectByValue(By.id("months"), "5");
        selectByValue(By.id("years"), "1990");

        safeClick(By.id("newsletter"));
        safeClick(By.id("optin"));

        // Fill address (stable)
        type(By.id("first_name"), "Tien");
        type(By.id("last_name"), "Dung");
        type(By.id("company"), "ABC Corp");
        type(By.id("address1"), "123 Test Street");
        type(By.id("address2"), "Line 2");
        selectByText(By.id("country"), "Canada");
        type(By.id("state"), "ON");
        type(By.id("city"), "Toronto");
        type(By.id("zipcode"), "A1A1A1");
        type(By.id("mobile_number"), "0900000000");

        click(By.cssSelector("button[data-qa='create-account']"));
        mustVisible(By.cssSelector("h2[data-qa='account-created']"));

        click(By.cssSelector("a[data-qa='continue-button']"));
        mustLoggedInAs(name);

        return new Account(name, email, pass);
    }

    private static void safeLogoutIfLoggedIn() {
        try {
            List<WebElement> logged = driver.findElements(By.xpath("//a[contains(.,'Logged in as')]"));
            if (!logged.isEmpty()) {
                dismissAdsIfAny();
                WebElement lo = driver.findElement(MENU_LOGOUT);
                scrollIntoView(lo);
                jsClick(lo);
                wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//h2[contains(.,'Login to your account')]")));
            }
        } catch (Throwable ignored) {}
    }

    // ===================== Checkout payment helper =====================
    private static void fillPaymentAndConfirm() {
        // Payment page inputs
        type(By.name("name_on_card"), "Tien Dung");
        type(By.name("card_number"), "4111111111111111");
        type(By.name("cvc"), "123");
        type(By.name("expiry_month"), "12");
        type(By.name("expiry_year"), "2030");

        click(By.id("submit"));
    }

    // ===================== Cart reset/cleanup =====================
    private static void resetCartBeforeCartTC() {
        try {
            driver.get(BASE_URL + "view_cart");
            dismissAdsIfAny();

            // if empty message -> ok
            if (pageHasText("cart is empty")) return;

            // if table present, remove all
            if (!driver.findElements(CART_TABLE).isEmpty()) {
                List<WebElement> deletes = driver.findElements(CART_DELETE);
                while (!deletes.isEmpty()) {
                    int before = driver.findElements(CART_ROWS).size();
                    jsClick(deletes.get(0));
                    waitUntil(() -> pageHasText("cart is empty") || driver.findElements(CART_ROWS).size() < before, 25);
                    deletes = driver.findElements(CART_DELETE);
                }
            }
        } catch (Throwable ignored) {}
    }

    private static void clearCartIfAny() {
        try {
            driver.get(BASE_URL + "view_cart");
            dismissAdsIfAny();

            if (pageHasText("cart is empty")) return;

            List<WebElement> deletes = driver.findElements(CART_DELETE);
            while (!deletes.isEmpty()) {
                int before = driver.findElements(CART_ROWS).size();
                jsClick(deletes.get(0));
                waitUntil(() -> pageHasText("cart is empty") || driver.findElements(CART_ROWS).size() < before, 25);
                deletes = driver.findElements(CART_DELETE);
            }
        } catch (Throwable ignored) {}
    }

    // ===================== Cart actions =====================
    private static void addProductByIndex(int index) {
        dismissAdsIfAny();

        List<WebElement> cards = wait.until(d -> d.findElements(By.cssSelector(".features_items .product-image-wrapper")));
        expectTrue(cards.size() > index, "Not enough products. index=" + index + ", total=" + cards.size());

        WebElement card = cards.get(index);
        scrollIntoView(card);
        actions.moveToElement(card).perform();

        WebElement addBtn = card.findElement(By.cssSelector("a.add-to-cart"));
        dismissAdsIfAny();
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
        // fallback first row
        String qtyTxt = driver.findElement(By.cssSelector("td.cart_quantity button")).getText().trim();
        return Integer.parseInt(qtyTxt);
    }

    // ===================== Common helpers =====================
    private static void openHomeAndVerify() {
        driver.get(BASE_URL);
        dismissAdsIfAny();
        mustVisible(HOME_LOGO);
    }

    private static WebElement mustVisible(By by) {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(by));
    }

    private static void click(By by) {
        WebElement el = wait.until(ExpectedConditions.elementToBeClickable(by));
        scrollIntoView(el);
        dismissAdsIfAny();
        jsClick(el);
    }

    private static void safeClick(By by) {
        try {
            WebElement el = driver.findElement(by);
            if (el.isDisplayed() && el.isEnabled()) {
                scrollIntoView(el);
                dismissAdsIfAny();
                jsClick(el);
            }
        } catch (Throwable ignored) {}
    }

    private static void type(By by, String text) {
        WebElement el = mustVisible(by);
        scrollIntoView(el);
        el.clear();
        el.sendKeys(text);
    }

    private static void selectByValue(By selectBy, String value) {
        WebElement el = mustVisible(selectBy);
        new Select(el).selectByValue(value);
    }

    private static void selectByText(By selectBy, String text) {
        WebElement el = mustVisible(selectBy);
        new Select(el).selectByVisibleText(text);
    }

    private static void mustLoggedInAs(String name) {
        WebElement logged = mustVisible(By.xpath("//a[contains(.,'Logged in as')]"));
        expectTrue(logged.getText().contains(name), "Expected Logged in as " + name + ", actual=" + logged.getText());
    }

    private static void jsClick(WebElement el) {
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", el);
    }

    private static void scrollIntoView(WebElement el) {
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block:'center'});", el);
    }

    private static void scrollToBottom() {
        ((JavascriptExecutor) driver).executeScript("window.scrollTo(0, document.body.scrollHeight);");
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

    private static void waitUntil(BooleanSupplier condition, int seconds) {
        new WebDriverWait(driver, Duration.ofSeconds(seconds)).until(d -> condition.getAsBoolean());
    }

    private static boolean pageHasText(String lowerNeedle) {
        try {
            return driver.getPageSource().toLowerCase(Locale.ROOT).contains(lowerNeedle.toLowerCase(Locale.ROOT));
        } catch (Throwable t) {
            return false;
        }
    }

    private static void expectTrue(boolean condition, String messageIfFail) {
        if (!condition) throw new AssertionError(messageIfFail);
    }

    // ===================== Ads popup/iframe killer =====================
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

    // ===================== Temp upload file =====================
    private static Path createTempUploadFile() {
        try {
            Path p = Files.createTempFile("ae-upload-", ".txt");
            Files.writeString(p, "AutomationExercise upload file test", StandardOpenOption.TRUNCATE_EXISTING);
            p.toFile().deleteOnExit();
            return p;
        } catch (Exception e) {
            // fallback
            return Paths.get(System.getProperty("user.home"));
        }
    }

    // ===================== Simple DTO =====================
    private static class Account {
        final String name;
        final String email;
        final String pass;
        Account(String name, String email, String pass) {
            this.name = name;
            this.email = email;
            this.pass = pass;
        }
    }
}
