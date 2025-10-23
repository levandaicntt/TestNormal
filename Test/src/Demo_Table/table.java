package Demo_Table;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import Initialization.Init;

public class table extends Init {

	public static void main(String[] args) throws InterruptedException {
		SetUp("edge");
        driver.get("https://practice.bpbonline.com/");
        driver.manage().window().maximize();
        
        WebElement Table_product = driver.findElement(By.tagName("table"));
        
        List<WebElement> rows = Table_product.findElements(By.xpath("//tbody/tr"));
        
        int count = 0;
        String content;
        String name = "";
        float price = 0.0f;
        
        for (WebElement row: rows) {
        	List<WebElement> products = row.findElements(By.xpath("td"));
        	
        	for (WebElement product: products) {
        		content = product.getText();
//        		System.out.println(content);
        		
        		String[] st = content.split("\n");
        		name = st[0];		
        		price = Float.parseFloat(st[1].substring(1));
        		System.out.println("Product: " + name + " Price: " + price);
        		count++;
        	}
        }
        System.out.println(count);
        Teardown();
	}

}
