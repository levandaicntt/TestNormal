package Demo_Elements;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.edge.EdgeDriver;

public class Elements_Method_1 {

	public static void main(String[] args) throws InterruptedException {
		WebDriver driver = new EdgeDriver();
        driver.get("https://practice.bpbonline.com/");
        driver.manage().window().maximize();
        
        WebElement inputSearch = driver.findElement(By.name("keywords"));
        inputSearch.clear();
        
        inputSearch.sendKeys("Mouse");
        
        WebElement search= driver.findElement(By.xpath("//input[@title=' Quick Find ']"));
        search.click();
        
        String st = driver.findElement(By.xpath("//span[contains(text(),'Displaying')]")).getText();
        String[] part = st.split("//s+");
        System.out.print(part[5]);
        
        Thread.sleep(3000);
	}

}
