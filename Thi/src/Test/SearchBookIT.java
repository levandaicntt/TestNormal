package Test;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import Initialization.Init;

import java.time.Duration;
import java.util.List;

public class SearchBookIT extends Init{

    public static void main(String[] args) {
        SetUp("edge");
        
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        
        System.out.println("Bắt đầu Test Case: Tìm kiếm tài liệu số CNTT...");
        
        driver.get("https://lib.qnu.edu.vn/");
        driver.manage().window().maximize();
        
        
        WebElement searchField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@placeholder='Nhập từ khóa tìm kiếm']")));
        searchField.sendKeys("Công nghệ thông tin");
        
        driver.findElement(By.xpath("//input[@value='optThuvienso']")).click();
        
        WebElement searchButton = driver.findElement(By.linkText("Tìm kiếm"));
        searchButton.click();
        System.out.println("Đã tìm kiếm 'Công nghệ thông tin'");
        List<WebElement> items = driver.findElements(By.xpath("//div[@class='col-md-6']"));
        for (int i = 0; i < items.size(); i++) {
        	System.out.println("Sách thứ :" + i);
        	System.out.println(items.get(i).getText());
        }

    }
    
}