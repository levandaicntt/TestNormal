package Test;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import Initialization.Init;
import java.time.Duration;

public class Viewer extends Init {
    public static void main(String[] args) {
        SetUp("edge");
        driver.get("https://lib.qnu.edu.vn");
        driver.manage().window().maximize();

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        By monthBy = By.xpath("(//select[@id='ctl00_phContent_ucPortalLeftStatistic_ddlMonth'])[1]");
        By lblBy = By.id("ctl00_phContent_ucPortalLeftStatistic_lblThongKeLuotTruyCapChiTiet");

        WebElement lbl = wait.until(ExpectedConditions.visibilityOfElementLocated(lblBy));
        String previousText = lbl.getText();

        long totalView = 0;
        for (int m = 1; m <= 10; m++) {
            WebElement monthEl = wait.until(ExpectedConditions.elementToBeClickable(monthBy));
            new Select(monthEl).selectByVisibleText("Tháng " + m);

            wait.until(ExpectedConditions.or(
                    ExpectedConditions.stalenessOf(lbl),
                    ExpectedConditions.not(ExpectedConditions.textToBe(lblBy, previousText))
            ));

            lbl = wait.until(ExpectedConditions.visibilityOfElementLocated(lblBy));
            String viewText = lbl.getText();
            previousText = viewText;

            String clean = viewText.replace(".", "").replace(",", "").replaceAll("[^0-9]", "").trim();
            if (!clean.isEmpty()) {
                long current = Long.parseLong(clean);
                totalView += current;
                System.out.printf("Tháng %d: %d%n", m, current);
            } else {
                System.out.printf("Tháng %d: không đọc được số từ: '%s'%n", m, viewText);
            }
        }

        System.out.println("Tổng lượt xem (Tháng 1 → 10): " + totalView);
        driver.quit();
    }
}
