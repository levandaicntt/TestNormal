package Test;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.edge.EdgeDriver;

public class RunWeb {
    public static void main(String[] args) throws InterruptedException {
        WebDriver driver = new EdgeDriver();

        driver.get("https://www.google.com/?zx=1757581060885&no_sw_cr=1");
        driver.navigate().to("https://practice.bpbonline.com/");
        driver.manage().window().maximize();
        
        String title = driver.getTitle();
        System.out.println(title);
        
        String url = driver.getCurrentUrl();
        System.out.println(url);
        
//        WebElement myAccount = driver.findElement(By.linkText("My Account"));
//        myAccount.click();
        
        List<WebElement> allLinks = driver.findElements(By.xpath("//a"));
        
        for (WebElement link : allLinks)
        	System.out.println(link.getText());
        
        Thread.sleep(10000);
        
        driver.quit()
;    }
}
