package Demo_Alert;

import java.util.Set;

import org.openqa.selenium.By;

import Initialization.Init;

public class Windows extends Init {

	public static void main(String[] args) throws InterruptedException {
		// TODO Auto-generated method stub
		SetUp("edge");
		driver.get("https://the-internet.herokuapp.com/windows");
		driver.findElement(By.xpath("//a[normalize-space()='Click Here']")).click();
		
		//Lam viec tren cac cua so Windows
		Set<String> allHandleWindows = driver.getWindowHandles();
//		for (String window:allHandleWindows) {
//			System.out.println(window);
//		}
		//De truy cap tung cua so Windows, chuyen tap hop Set sang mang
		Object [] windows = allHandleWindows.toArray();
		driver.switchTo().window(windows[1].toString());
		if (driver.getPageSource().contains("New Window")) {
			System.out.println("Cua so New Window");
			
			//Dong tab New Window
			System.out.println("Dong cua so New Window");
			driver.close();
		}
		
		System.out.println("Tro ve cua so Main Window");
		driver.switchTo().window(windows[0].toString());
//		Teardown();
	}

}