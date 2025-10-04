package Bai2;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.edge.EdgeDriver;

public class Bai2 {

	public static void main(String[] args) throws InterruptedException {
		WebDriver driver = new EdgeDriver();
		driver.get("https://www.google.com");
		driver.manage().window().maximize();
		
		String title = driver.getTitle();
        System.out.println(title);

        String url = driver.getCurrentUrl();
        System.out.println(url);
        
        String pageSource = driver.getPageSource();
        System.out.println("HTML source" + pageSource);
        System.out.println("Length" + pageSource.length());
        
        Thread.sleep(10000);
        
        driver.quit();
	}

}
