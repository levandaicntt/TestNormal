package Demo_xpart;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import Initialization.Init;

public class xpartList extends Init{

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		SetUp("edge");
		driver.get("https://www.tutorialspoint.com/selenium/practice/selenium_automation_practice.php");
	
		driver.findElement(By.xpath("//*[normalize-space()='Elements']")).click();
		
		WebElement elements = driver.findElement(By.xpath("//ul[@id='navMenus']"));
		
		WebElement textbox = elements.findElement(By.xpath("//li[1]/a"));
		
		System.out.println(textbox.getText());
	}

}