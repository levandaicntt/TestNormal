package Demo_Alert;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;

import Initialization.Init;

public class Alert_OK extends Init {

	public static void main(String[] args) throws InterruptedException {
		// TODO Auto-generated method stub
		SetUp("chrome");
		driver.get("https://the-internet.herokuapp.com/javascript_alerts");
		driver.findElement(By.xpath("//button[normalize-space()='Click for JS Alert']")).click();
		
		//Chuyen sang alert
		Alert alert = driver.switchTo().alert();
		//Nhan nut OK
		alert.accept();
		
		if (driver.getPageSource().contains("You successfully clicked an alert"));{
			System.out.println("Alert thanh cong");
		}
		
		Teardown();
	}

}
