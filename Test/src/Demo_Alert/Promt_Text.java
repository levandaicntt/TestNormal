package Demo_Alert;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;

import Initialization.Init;

public class Promt_Text extends Init {

	public static void main(String[] args) throws InterruptedException {
		// TODO Auto-generated method stub
		SetUp("chrome");
		driver.get("https://the-internet.herokuapp.com/javascript_alerts");
		driver.findElement(By.xpath("//button[normalize-space()='Click for JS Prompt']")).click();
		
		//Chuyen sang alert
		Alert alert = driver.switchTo().alert();
		
		//Nhap noi dung
		alert.sendKeys("Anh Dũng non");
		
		//Nhan nut OK
		alert.accept();
		
		if (driver.getPageSource().contains("Anh Dũng non")){
			System.out.println("Alert thanh cong");
		} else {
			System.out.println("Alert that bai");
		}
		
		Teardown();
	}

}