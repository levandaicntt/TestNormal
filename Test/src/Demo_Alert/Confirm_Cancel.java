package Demo_Alert;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;

import Initialization.Init;

public class Confirm_Cancel extends Init {

	public static void main(String[] args) throws InterruptedException {
		// TODO Auto-generated method stub
		SetUp("chrome");
		driver.get("https://the-internet.herokuapp.com/javascript_alerts");
		driver.findElement(By.xpath("//button[normalize-space()='Click for JS Confirm']")).click();
		
		//Chuyen sang alert
		Alert alert = driver.switchTo().alert();
		//Nhan nut Cancel
		alert.dismiss();
		
		if (driver.getPageSource().contains("Cancel")){
			System.out.println("Alert thanh cong");
		} else {
			System.out.println("Alert that bai");
		}
		
		Teardown();
	}

}
