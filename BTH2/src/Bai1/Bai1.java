package Bai1;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.edge.EdgeDriver;

public class Bai1 {

	public static void main(String[] args) throws InterruptedException {
		WebDriver driver = new EdgeDriver();
		driver.get("https://daotao.qnu.edu.vn");
		driver.manage().window().maximize();
		
		String title = driver.getTitle();
        System.out.println(title);
        
        Thread.sleep(10000);
        
        driver.quit();
	}

}
