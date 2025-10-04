package Demo_Elements;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.edge.EdgeDriver;

public class Element_Test {

	public static void main(String[] args) throws InterruptedException {
		WebDriver driver = new EdgeDriver();
        driver.get("https://demoqa.com/text-box");
        driver.manage().window().maximize();
        
        WebElement inputUserName = driver.findElement(By.id("userName"));
        inputUserName.clear();      
        inputUserName.sendKeys("123");
        
        WebElement inputUserEmail = driver.findElement(By.id("userEmail"));
        inputUserEmail.clear();      
        inputUserEmail.sendKeys("a@a.com");
        
        WebElement inputCurrentAddress = driver.findElement(By.id("currentAddress"));
        inputCurrentAddress.clear();      
        inputCurrentAddress.sendKeys("123");
        
        WebElement inputPermanentAddress = driver.findElement(By.id("permanentAddress"));
        inputPermanentAddress.clear();      
        inputPermanentAddress.sendKeys("123");
        
        driver.findElement(By.id("submit")).click();
        
        Thread.sleep(2000);
        
//        String outputStr = driver.findElement(By.xpath("//div[@class='border col-md-12 col-sm-12']")).getText();
//        System.out.print(outputStr);
	}

}
