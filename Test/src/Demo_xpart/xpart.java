package Demo_xpart;

import org.openqa.selenium.By;

import Initialization.*;

public class xpart extends Init{

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		SetUp("edge");
		driver.get("https://www.youtube.com/");
//		driver.findElement(By.xpath("//input[@placeholder='Search']")).sendKeys("2323");
//		driver.findElement(By.xpath("//button[@title='Search']")).click();
		
		driver.findElement(By.xpath("//*[@placeholder='Search']")).sendKeys("2323");
		driver.findElement(By.xpath("//*[@title='Search']")).click();
	}

}
